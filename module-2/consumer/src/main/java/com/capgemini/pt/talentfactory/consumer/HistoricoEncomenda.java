package com.capgemini.pt.talentfactory.consumer;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Entity
public class HistoricoEncomenda {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private Long encomendaId;
    private String estadoEncomenda;
    private Calendar calendario;

    public HistoricoEncomenda() {
    }

    public HistoricoEncomenda(Long encomendaId, String estadoEncomenda) {
        this.encomendaId = encomendaId;
        this.estadoEncomenda = estadoEncomenda;
        this.calendario = Calendar.getInstance();
    }

    public Long getEncomendaId() {
        return encomendaId;
    }

    public void setEncomendaId(Long encomendaId) {
        this.encomendaId = encomendaId;
    }

    public String getEstadoEncomenda() {
        return estadoEncomenda;
    }

    public void setEstadoEncomenda(String estadoEncomenda) {
        this.estadoEncomenda = estadoEncomenda;
    }

    public String getCalendario() {
        DateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String data = formatoData.format(calendario.getTime());
        return data;
    }

    public void setCalendario(Calendar calendario) {
        this.calendario = calendario;
    }
}
