package com.capgemini.pt.talentfactory.rest;

import com.capgemini.pt.talentfactory.rest.models.Encomenda;
import com.capgemini.pt.talentfactory.rest.models.HistoricoEncomenda;
import com.capgemini.pt.talentfactory.rest.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {
    private Logger logger;

    private RepositoryEncomenda repositoryEncomenda;
    private RepositoryHistoricoEncomenda repositoryHistoricoEncomenda;
    private RepositoryRestaurante repositoryRestaurante;
    private RepositoryMenu repositoryMenu;
    private RepositoryOpcaoMenu repositoryOpcaoMenu;

    public MessageListener(RepositoryRestaurante repositoryRestaurante, RepositoryMenu repositoryMenu, RepositoryOpcaoMenu repositoryOpcaoMenu,
                           RepositoryEncomenda repositoryEncomenda, RepositoryHistoricoEncomenda repositoryHistoricoEncomenda) {
        this.logger = LoggerFactory.getLogger(MessageListener.class);
        this.logger.info("Created rest MessageListener");
        this.repositoryRestaurante = repositoryRestaurante;
        this.repositoryMenu = repositoryMenu;
        this.repositoryOpcaoMenu = repositoryOpcaoMenu;
        this.repositoryEncomenda = repositoryEncomenda;
        this.repositoryHistoricoEncomenda = repositoryHistoricoEncomenda;
    }

    @KafkaListener(topics = KafkaTopicConfig.TOPIC_ESTADO_ENCOMENDA)
    public void listen(String message) {
        System.out.println("Mensagem : " + message);
        String[] splitted = message.split(" >");
        Long encomendaId = Long.parseLong(splitted[1]);
        Encomenda encomenda = this.repositoryEncomenda.findById(encomendaId).get();
        encomenda.setEstado("Entrega com SUCESSO");
        this.repositoryEncomenda.save(encomenda);
        this.repositoryEncomenda.flush();
        HistoricoEncomenda historicoEncomenda = new HistoricoEncomenda(encomenda.getId(), encomenda.getEstado());
        this.repositoryHistoricoEncomenda.save(historicoEncomenda);
        this.repositoryHistoricoEncomenda.flush();
    }
}
