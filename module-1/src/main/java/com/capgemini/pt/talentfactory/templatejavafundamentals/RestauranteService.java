package com.capgemini.pt.talentfactory.templatejavafundamentals;


import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service
public class RestauranteService {
    private LinkedList<Restaurante> restauranteList;

    public RestauranteService() {
        this.restauranteList = new LinkedList<Restaurante>();
    }

    public void add(Restaurante restaurante) {
        this.restauranteList.add(restaurante);
    }

    public void addMenu(Menu menu, int indexRestaurante){
        Restaurante restaurante =  restauranteList.get(indexRestaurante);
        restaurante.addMenu(menu);
    }

    public Restaurante[] list() {
        return this.restauranteList.toArray(new Restaurante[this.restauranteList.size()]);
    }
}

