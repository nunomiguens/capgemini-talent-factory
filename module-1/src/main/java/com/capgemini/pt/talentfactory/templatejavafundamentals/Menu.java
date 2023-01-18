package com.capgemini.pt.talentfactory.templatejavafundamentals;

import java.util.ArrayList;

public class Menu {

    private String tipoMenu;
    private ArrayList<OpcoesMenu> opcoesMenu;

    public Menu(String tipoMenu) {
        this.tipoMenu = tipoMenu;
        this.opcoesMenu = new ArrayList<OpcoesMenu>();
    }

    public void addPrato(String prato, String descricaoPrato) {
        this.opcoesMenu.add(new OpcoesMenu(prato, descricaoPrato));
    }

    public String getTipoMenu() {
        return tipoMenu;
    }

    public void setTipoMenu(String tipoMenu) {
        this.tipoMenu = tipoMenu;
    }

    public OpcoesMenu[] getPratos() {
        return this.opcoesMenu.toArray(new OpcoesMenu[opcoesMenu.size()]);
    }
}
