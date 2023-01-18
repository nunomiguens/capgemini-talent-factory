package com.capgemini.pt.talentfactory.consumer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(name = "Encomenda.startLetter", query = "Select e From Encomenda e where e.id = :id")
})
public class Encomenda {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne (fetch = FetchType.LAZY)
    private Restaurante restaurante;
    @ManyToMany (fetch = FetchType.LAZY)
    private List<Menu> menus = new ArrayList<Menu>();
    private String estado;

    public Encomenda() {}

    public Encomenda(Restaurante restaurante) {
        this.restaurante = restaurante;
        this.estado = "Encomenda registada com sucesso!";
        this.menus = new ArrayList<Menu>();
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    public void addMenu(Menu menu) {
        this.menus.add(menu);
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    public Long getId() {
        return id;
    }

    public List<Menu> getMenus() {
        return menus;
    }
}
