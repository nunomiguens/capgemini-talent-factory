package com.capgemini.pt.talentfactory.templatejavafundamentals;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Scanner;
import java.util.regex.Pattern;

@Component
public class Runner implements CommandLineRunner {

    private RestauranteService restauranteService;

    private Pattern integerPattern;

    public Runner(RestauranteService restauranteService) {
        this.restauranteService = restauranteService;
        this.integerPattern = Pattern.compile("[0-9]{4}-[0-9]{3}");
    }

    public int getTotalRestaurantes() {
        return this.restauranteService.list().length;
    }

    public void showMainMenu(Scanner scanner) {
        System.out.println("##-- Menu principal --##");
        System.out.println("1) Criar Restaurante");
        System.out.println("2) Ver Restaurantes");
        System.out.println("3) Criar Menu em Restaurante");
        System.out.println("4) Ver Menu do Restaurante");
        System.out.println("5) Exit");
        System.out.print("> ");
        String option = scanner.nextLine();

        switch (option) {
            case "1":
                this.createRestaurante(scanner);
                break;
            case "2":
                this.showRestaurantes();
                break;
            case "3":
                this.createMenu(scanner);
                break;
            case "4":
                this.showMenus(scanner);
                break;
            case "5":
                return;
            default:
                this.showMainMenu(scanner);
        }
        this.showMainMenu(scanner);
    }

    private void createRestaurante(Scanner scanner) {
        System.out.println("##-- Criar Restaurante --##");
        String resposta;
        do {
            System.out.print("#Inserir Nome do Restaurante: \n");
            String nomeRestaurante = scanner.nextLine();
            System.out.print("#Inserir morada do Restaurante: \n");
            String morada = scanner.nextLine();
            System.out.print("#Código Postal do Restaurante: \n ");
            String codPostal = scanner.nextLine();
            Morada moradaRestaurante = addMorada(scanner, morada, codPostal);
            Restaurante restaurante = new Restaurante(nomeRestaurante, moradaRestaurante);
            this.restauranteService.add(restaurante);
            System.out.println("Restaurante criado!\n");
            System.out.println("##--> Adicionar novo restaurante? (S/N)");
            resposta = scanner.nextLine();
        } while (resposta.equalsIgnoreCase("S"));
    }

    public Morada addMorada(Scanner scanner, String morada, String codPostal) {
        try {
            return new Morada(morada, codPostal);
        } catch (IllegalArgumentException exceptionPostal) {
            System.out.print(exceptionPostal.getMessage());
            String novoCodPostal = scanner.nextLine();
            return addMorada(scanner, morada, novoCodPostal);
        }
    }

    private void showRestaurantes() {
        int index = 1;
        System.out.println("");
        if (restauranteService.list().length == 0) {
            System.out.println("Não há Restaurantes para mostrar! Por favor crie um novo Restaurante.");
        } else if (restauranteService.list().length != 0) {
            for (Restaurante restaurante : this.restauranteService.list()) {
                System.out.println(String.format("%d - Restaurante %s", index++, restaurante.getNomeRestaurante()));
            }
        }
    }

    private void validarIndex(int index) throws ArrayIndexOutOfBoundsException {

        if (index >= restauranteService.list().length || index < 0) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    private void createMenu(Scanner scanner) {

        System.out.println("##-- Criar Menu --##");
        System.out.println("#>> Escolha o Restaurante <<#");
        showRestaurantes();
        int opcoes;
        try {
            opcoes = Integer.parseInt(scanner.nextLine());
            validarIndex(opcoes - 1);
        } catch (NumberFormatException exception) {
            System.out.println("Escolha um numero correcto!");
            return;
        } catch (ArrayIndexOutOfBoundsException exception2) {
            System.out.println("Escolha um restaurante existente!");
            return;
        }

        String resposta;
        do {
            System.out.print("\nTipo de Menu " + "\n #Carne - C" + "\n #Peixe - P");

            String escolhaMenu = scanner.nextLine();

            switch (escolhaMenu.toUpperCase()) {
                case "C":
                    escolhaMenu = "Carne";
                    break;
                case "P":
                    escolhaMenu = "Peixe";
                    break;
                default:
                    System.out.println("Escolha uma das Opções.");
                    this.createMenu(scanner);
            }

            System.out.print("Adicione ao menu " + escolhaMenu + ":");
            String prato = "\n start";
            String descricaoPrato = "";

            Menu menu = new Menu(escolhaMenu);

            do {
                System.out.println("\nPrato: ");
                prato = scanner.nextLine();
                System.out.println("Descrição: ");
                descricaoPrato = scanner.nextLine();
                menu.addPrato(prato, descricaoPrato);
                System.out.println("Enter - Para adicionar mais.\n S - Para sair.");
                resposta = scanner.nextLine();

            } while (!resposta.equalsIgnoreCase("S"));

            this.restauranteService.addMenu(menu, opcoes - 1);
            System.out.println("Menu adicionado ao Restaurante " + restauranteService.list()[opcoes - 1].getNomeRestaurante());
            System.out.println("Adicionar outro Tipo de Menu? (S/N)");
            resposta = scanner.nextLine();
        } while (resposta.equalsIgnoreCase("s"));
    }


    private void showMenus(Scanner scanner) {
        System.out.println("##-- Ver Menu --##" +
                "\n #>> Escolha o restaurante <<#");
        showRestaurantes();

        int opcoes = Integer.parseInt(scanner.nextLine());

        Restaurante restaurante = this.restauranteService.list()[opcoes - 1];

        System.out.printf("\n ##Restaurante %s", restaurante.getNomeRestaurante());
        for (Menu menu : restaurante.getMenu()) {
            System.out.println("\n #" + menu.getTipoMenu());
            for (OpcoesMenu dish : menu.getPratos()) {
                System.out.println(">>>-" + dish.getPrato());
            }
        }
    }

    @Override
    public void run(String... args) throws Exception {
        try (Scanner scanner = new Scanner(System.in)) {
            this.showMainMenu(scanner);
        }
        System.out.println("\nBye bye!");
    }
}