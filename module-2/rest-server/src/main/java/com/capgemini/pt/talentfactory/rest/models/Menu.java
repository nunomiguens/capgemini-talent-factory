package com.capgemini.pt.talentfactory.rest.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;

    @OneToMany(fetch = FetchType.EAGER)
    private List<OpcaoMenu> opcaoMenu = new ArrayList<OpcaoMenu>();

    public Menu() {
    }

    public Menu(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
        this.opcaoMenu = new ArrayList<OpcaoMenu>();
    }

    public void addOpcaoMenu(String carne, String peixe) {
        this.opcaoMenu.add(new OpcaoMenu(carne, peixe));
    }

    public void addOpcaoMenu(OpcaoMenu opcaoMenu) {
        this.opcaoMenu.add(opcaoMenu);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public OpcaoMenu[] getOpcaoMenu() {
        return this.opcaoMenu.toArray(new OpcaoMenu[opcaoMenu.size()]);
    }
}
