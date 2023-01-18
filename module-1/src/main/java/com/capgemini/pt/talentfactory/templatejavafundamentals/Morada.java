package com.capgemini.pt.talentfactory.templatejavafundamentals;

public class Morada {
    private String codPostal;
    private String morada;


    public Morada(String morada, String codPostal) {
        this.morada = morada;
        if(codPostalValido(codPostal)){
            this.codPostal = codPostal;
        }else{
            throw new IllegalArgumentException("O Código Postal não é válido. " +
                    "Inserir no seguinte formato (1234-123).");
        }
    }

    public boolean codPostalValido(String codPostal){

        return codPostal.matches("^[0-9]{4}-[0-9]{3}");
    }
}