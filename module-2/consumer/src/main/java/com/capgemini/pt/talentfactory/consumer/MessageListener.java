package com.capgemini.pt.talentfactory.consumer;

import com.capgemini.pt.talentfactory.consumer.Encomenda;
import com.capgemini.pt.talentfactory.consumer.HistoricoEncomenda;
import com.capgemini.pt.talentfactory.consumer.Menu;
import com.capgemini.pt.talentfactory.consumer.Restaurante;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class MessageListener {
    /*@PersistenceContext(unitName = "default")
    EntityManager em;

    private Logger logger;

    public MessageListener() {
        this.logger = LoggerFactory.getLogger(MessageListener.class);
    }

    //////// JMS
    @JmsListener(destination = "ActualizarEncomenda")
    public void processActualizarEncomenda(String content) {
        System.out.println(content);
    }
    ////////

    @JmsListener(destination = "ToLowerCaseQueue")
    @Transactional
    public void processMessage(String content) {
        this.logger.info("ActiveMQ message: " + content);

        String[] splitted = content.split(" ");

        List<Invoice> invoices = em.createNamedQuery("Invoice.startLetter", Invoice.class)
                .setParameter("name", splitted[1])
                .getResultList();
        invoices.forEach((invoice) -> {
            System.out.println(invoice.getName());

            for (Item item : invoice.getItems()) {
                item.setName(item.getName().toLowerCase());
                em.persist(item);
            }
            em.flush();

        });
    }*/
}
