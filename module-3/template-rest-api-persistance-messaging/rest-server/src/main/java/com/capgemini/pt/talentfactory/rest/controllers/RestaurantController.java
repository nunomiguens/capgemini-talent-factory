package com.capgemini.pt.talentfactory.rest.controllers;

import com.capgemini.pt.talentfactory.rest.KafkaTopicConfig;
import com.capgemini.pt.talentfactory.rest.models.*;
import com.capgemini.pt.talentfactory.rest.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.regex.Pattern;

@Controller
public class RestaurantController {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    private JmsTemplate jmsTemplate;
    private RepositoryRestaurante repositoryRestaurante;
    private RepositoryMenu repositoryMenu;
    private RepositoryOpcaoMenu repositoryOpcaoMenu;
    private RepositoryEncomenda repositoryEncomenda;
    private RepositoryHistoricoEncomenda repositoryHistoricoEncomenda;
    private final Pattern codigoPostalPattern;

    public RestaurantController(JmsTemplate jmsTemplate, RepositoryRestaurante repositoryRestaurante, RepositoryMenu repositoryMenu,
                                RepositoryOpcaoMenu repositoryOpcaoMenu, RepositoryEncomenda repositoryEncomenda, RepositoryHistoricoEncomenda repositoryHistoricoEncomenda) {
        this.jmsTemplate = jmsTemplate;
        this.repositoryRestaurante = repositoryRestaurante;
        this.repositoryMenu = repositoryMenu;
        this.repositoryOpcaoMenu = repositoryOpcaoMenu;
        this.repositoryEncomenda = repositoryEncomenda;
        this.repositoryHistoricoEncomenda = repositoryHistoricoEncomenda;
        this.codigoPostalPattern = Pattern.compile("^[0-9]{4}-[0-9]{3}");
    }

    @GetMapping("/olamundo")
    @ResponseBody
    public String olaMundo() {
        return "Olá mundo";
    }

    @PostMapping("/criaRestaurante")
    @ResponseBody
    public String criaRestaurante(@RequestBody Restaurante restaurante) {
        try {
            if (!restaurante.getCodigoPostal().matches(String.valueOf(codigoPostalPattern)))
                return "O Código Postal não é válido. " +
                        "Inserir no seguinte formato (1234-123).";
            Restaurante restaurante1 = new Restaurante(restaurante.getNome(), restaurante.getMorada(), restaurante.getCodigoPostal());
            if (this.repositoryRestaurante.findByNome(restaurante1.getNome()).isEmpty()) {
                this.repositoryRestaurante.save(restaurante1);
                this.repositoryRestaurante.flush();
                return "Restaurante criado!\n";
            } else
                return "Este restaurante já está registado.\n";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @PostMapping("/criaMenu/{nomeRestaurante}")
    @ResponseBody
    public String criaMenu(@RequestBody Menu menu, @PathVariable String nomeRestaurante) {
        try {
            if (this.repositoryRestaurante.findByNome(nomeRestaurante).isEmpty())
                return "Não há Restaurantes com esse nome. Por favor selecione um Restaurante existente! ";
            Menu menu1 = new Menu(menu.getNome(), menu.getDescricao());
            for (OpcaoMenu om : menu.getOpcaoMenu()) {
                menu1.addOpcaoMenu(om.getCarne(), om.getPeixe());
            }
            for (OpcaoMenu op : menu1.getOpcaoMenu()) {
                this.repositoryOpcaoMenu.save(op);
            }

            this.repositoryRestaurante.findByNome(nomeRestaurante).get(0).addMenu(menu1);
            this.repositoryMenu.save(menu1);
            this.repositoryMenu.flush();
            this.repositoryRestaurante.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Menu adicionado ao Restaurante \n";
    }

    @GetMapping("/listaRestaurantes")
    @ResponseBody
    public String listaRestaurantes() {
        String listaRestaurante = "";
        try {
            List<Restaurante> listaRestaurantes = this.repositoryRestaurante.findAll();
            if (listaRestaurantes.isEmpty())
                return "Não há Restaurantes para mostrar! Por favor crie um novo Restaurante!\n";
            else {
                for (Restaurante r : this.repositoryRestaurante.findAll()) {
                    listaRestaurante = listaRestaurante + "\n> Restaurante: " + r.getNome() + "\n> Morada: " + r.getMorada() + "\n> Código Postal: " + r.getCodigoPostal() + "\n";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaRestaurante;
    }

    @GetMapping("/listaMenus/{nomeRestaurante}")
    @ResponseBody
    public String listaMenus(@PathVariable String nomeRestaurante) {
        String listaMenu = "";
        try {
            for (Menu menu : this.repositoryRestaurante.findByNome(nomeRestaurante).get(0).getMenus()) {
                listaMenu = listaMenu + "## " + menu.getNome() + " ##" + "\nDescrição: " + menu.getDescricao() + "\n";
                for (OpcaoMenu opcaoMenu : menu.getOpcaoMenu()) {
                    listaMenu = listaMenu + "> Carne: " + opcaoMenu.getCarne() + " \n> Peixe: " + opcaoMenu.getPeixe() + "\n\n";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaMenu;
    }

    @PostMapping("/criaEncomenda/{nomeRestaurante}")
    @ResponseBody
    public String criaEncomenda(@PathVariable String nomeRestaurante, @RequestBody String[] nomeMenu) {
        try {
            Encomenda encomenda = new Encomenda(this.repositoryRestaurante.findByNome(nomeRestaurante).get(0));
            for (String oMenu : nomeMenu) {
                if (this.repositoryRestaurante.findByNome(nomeRestaurante).get(0).findMenu(oMenu) == null)
                    return "Não existe esse menu registado no restaurante inserido!\n";
                Menu menu = this.repositoryRestaurante.findByNome(nomeRestaurante).get(0).findMenu(oMenu);
                encomenda.addMenu(menu);
            }
            this.repositoryEncomenda.save(encomenda);
            this.repositoryEncomenda.flush();
            HistoricoEncomenda historicoEncomenda = new HistoricoEncomenda(encomenda.getId(), encomenda.getEstado());
            this.repositoryHistoricoEncomenda.save(historicoEncomenda);
            this.repositoryHistoricoEncomenda.flush();
            /* String message = String.format("ENCOMENDA", encomenda.getId());
            jmsTemplate.convertAndSend("ActualizarEncomenda", message); */
            return "Encomenda registada com sucesso! Foi atribuido o número: " + encomenda.getId() + "\n";
        } catch (Exception e) {
            return "Por favor insira um Restaurante existente.\n";
        }
    }

    @GetMapping("listarEncomenda/{idEncomenda}")
    @ResponseBody
    public String listarEncomenda(@PathVariable String idEncomenda) {
        try {
            Long encomendaId = Long.parseLong(idEncomenda);
            String nomeMenu = "";
            HistoricoEncomenda historicoEncomenda = this.repositoryHistoricoEncomenda.findEncomendaById(encomendaId).get();
            Encomenda encomenda = this.repositoryEncomenda.findById(encomendaId).get();
            for (Menu menu : this.repositoryEncomenda.findById(encomendaId).get().getMenus()) {
                nomeMenu = nomeMenu + "> " + menu.getNome();
            }
            return nomeMenu + ": registado na encomenda com o nº " + historicoEncomenda.getEncomendaId() + "\nEstado : " + encomenda.getEstado() + "\n" + "Submissão a: " + historicoEncomenda.getCalendario() + "\n";
        } catch (Exception e) {
            return "Não existe uma encomenda com esse id. Introduza um id correcto.\n";
        }
    }

    @PostMapping("processaEncomenda/{encomendaIdN}")
    @ResponseBody
    public String processaEncomenda(@PathVariable String encomendaIdN) {
        Long encomendaId;
        encomendaId = Long.parseLong(encomendaIdN);
        if (this.repositoryEncomenda.findById(encomendaId).isEmpty())
            return "A encomenda com esse numero não está registada! \n";
        Encomenda encomenda = this.repositoryEncomenda.findById(encomendaId).get();
        String message = String.format("ENCOMENDA >%s", encomenda.getId());
        kafkaTemplate.send(KafkaTopicConfig.TOPIC_ESTADO_ENCOMENDA, message);
        return "Entrega da encomenda feita com sucesso!\n";
    }

    @GetMapping("/historicoEncomenda")
    @ResponseBody
    public String historicoEncomenda() {
        String listaEncomenda = "## Histórico ##\n";
        List<HistoricoEncomenda> historicoEncomendaList = this.repositoryHistoricoEncomenda.findAll();
        if (historicoEncomendaList.isEmpty())
            return "Não existe registo de encomendas!\n";
        for (HistoricoEncomenda historicoEncomenda : this.repositoryHistoricoEncomenda.findAll()) {
            listaEncomenda = listaEncomenda + "\nEncomenda com o n.º: " + historicoEncomenda.getEncomendaId() + "\nEncontra-se no Estado: " + historicoEncomenda.getEstadoEncomenda() + "\n" + "Submissão efetuada a: " + historicoEncomenda.getCalendario() + "\n";
        }
        return listaEncomenda;
    }
}
