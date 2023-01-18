package com.capgemini.pt.talentfactory.templatejavafundamentals;

import java.util.ArrayList;

public class Restaurante {
    private String nomeRestaurante;
    private Morada moradaRestaurante;


    private ArrayList<Menu> menus;

    public Restaurante(String nomeRestaurante, Morada moradaRestaurante) {
        this.nomeRestaurante = nomeRestaurante;
        this.moradaRestaurante = moradaRestaurante;
        this.menus = new ArrayList<>();
    }

    public String getNomeRestaurante() {
        return nomeRestaurante;
    }

    public Menu[] getMenu() {
        return this.menus.toArray(new Menu[menus.size()]);
    }

    public void addMenu(Menu menu){
        this.menus.add(menu);
    }
}

