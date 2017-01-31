package com.axiastudio.suite.base;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.suite.base.entities.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

/**
 * User: tiziano
 * Date: 18/03/14
 * Time: 10:13
 */
public class BaseUtil {

    public static Ufficio trovaUfficioDaPec(String pec){

        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Ufficio> cq = cb.createQuery(Ufficio.class);
        Root from = cq.from(Ufficio.class);
        Predicate predicate = cb.equal(from.get("pec"), pec);
        cq.select(from);
        cq.where(predicate);
        TypedQuery<Ufficio> tq = em.createQuery(cq);
        Ufficio ufficio = tq.getSingleResult();
        return ufficio;
    }

    public static Boolean utenteInUfficio(Utente utente, Integer idUfficio, Boolean nonOspite){

        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UfficioUtente> cq = cb.createQuery(UfficioUtente.class);
        Root from = cq.from(UfficioUtente.class);
        Join<UfficioUtente, Ufficio> itemUfficio = from.join(UfficioUtente_.ufficio);
        Predicate predicate = cb.and(cb.equal(from.get(UfficioUtente_.utente), utente),
                cb.equal(itemUfficio.get(Ufficio_.id), idUfficio));
        if ( nonOspite ) {
            predicate = cb.and(predicate, cb.isFalse(from.get(UfficioUtente_.ospite)));
        }
        cq.select(from);
        cq.where(predicate);
        TypedQuery<UfficioUtente> tq = em.createQuery(cq);
        return !tq.getResultList().isEmpty();
    }

}
