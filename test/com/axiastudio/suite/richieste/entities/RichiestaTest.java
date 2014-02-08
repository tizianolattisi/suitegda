package com.axiastudio.suite.richieste.entities;

import com.axiastudio.suite.EntityBaseTest;
import com.axiastudio.suite.base.entities.Utente;
import org.junit.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Mickey
 * Date: 18/11/13
 * Time: 18.19
 * To change this template use File | Settings | File Templates.
 */
public class RichiestaTest extends EntityBaseTest {

    @Test
    public void testInsert() {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Utente> cq = cb.createQuery(Utente.class);
        TypedQuery<Utente> tq = em.createQuery(cq);
        Utente utente = tq.getResultList().get(0);

        Richiesta msg = new Richiesta();
        String testo = "Bene, ecco tutto.";
        //msg.setData(Calendar.getInstance().getTime());
        msg.setTesto(testo);
        msg.setMittente(utente);
        //msg.setCancellabile(Boolean.TRUE);
        //msg.setRichiestaautomatica(Boolean.FALSE);

        em.getTransaction().begin();
        em.persist(msg);
        em.getTransaction().commit();

        assert msg.getTesto().equals(testo);

        em.getTransaction().begin();
        em.remove(msg);
        em.getTransaction().commit();

    }
}
