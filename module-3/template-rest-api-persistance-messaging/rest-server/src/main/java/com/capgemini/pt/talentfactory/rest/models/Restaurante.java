package com.capgemini.pt.talentfactory.rest.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Restaurante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String morada;
    private String codigoPostal;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Menu> menus = new ArrayList<Menu>();

    public Restaurante() {
    }

    public Restaurante(String nome, String morada, String codigoPostal) {
        this.nome = nome;
        this.morada = morada;
        this.codigoPostal = codigoPostal;
        this.menus = new ArrayList<Menu>();
    }

    public void addMenu(Menu menu) {
        this.menus.add(menu);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public Menu[] getMenus() {
        return this.menus.toArray(new Menu[menus.size()]);
    }

    public Menu findMenu(String nomeMenu) {
        for (Menu menu : getMenus()) {
            if (nomeMenu.equals(menu.getNome())) {
                return menu;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Restaurante ## " +
                nome + "\n" + morada +
                "\n" + codigoPostal;
    }
}
