package com.axiastudio.suite.base;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.suite.base.entities.Ufficio;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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

}
