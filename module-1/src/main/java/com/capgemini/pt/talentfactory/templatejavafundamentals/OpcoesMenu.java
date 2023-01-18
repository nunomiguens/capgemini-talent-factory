package com.capgemini.pt.talentfactory.templatejavafundamentals;

public class OpcoesMenu {
    private String prato;
    private String descricaoPrato;


    public OpcoesMenu(String prato, String descricaoPrato) {
        this.prato = prato;
        this.descricaoPrato = descricaoPrato;
    }

    public String getDescricaoPrato() {
        return descricaoPrato;
    }

    public void setDescricaoPrato(String descricao) {
        this.descricaoPrato = descricao;
    }

    public String getPrato() {
        return prato;
    }

    public void setPrato(String prato) {
        this.prato = prato;
    }

}
