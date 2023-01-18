package com.capgemini.pt.talentfactory.consumer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OpcaoMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String carne;
    private String peixe;

    public OpcaoMenu () {
        this.carne = "";
        this.peixe = "";
    }

    public OpcaoMenu (String carne, String peixe) {
        this.carne = carne;
        this.peixe = peixe;
    }

    public String getCarne() {
        return carne;
    }

    public void setCarne(String carne) {
        this.carne = carne;
    }

    public String getPeixe() {
        return peixe;
    }

    public void setPeixe(String peixe) {
        this.peixe = peixe;
    }

}
