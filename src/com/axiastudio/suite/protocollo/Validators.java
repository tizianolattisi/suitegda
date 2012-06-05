/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.protocollo;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.pypapi.db.Validator;
import com.axiastudio.suite.protocollo.entities.Protocollo;
import com.axiastudio.suite.protocollo.entities.Protocollo_;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class Validators {
    /*
     * Valida il protocollo e richiede il nuovo iddocumento
     */
    @Validator
    public static Boolean validaProtocollo(Protocollo protocollo){
        if( protocollo.getId() == null ){
            Calendar calendar = Calendar.getInstance();
            Integer year = calendar.get(Calendar.YEAR);
            Date date = calendar.getTime();
            Database db = (Database) Register.queryUtility(IDatabase.class);
            EntityManager em = db.getEntityManagerFactory().createEntityManager();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Protocollo> cq = cb.createQuery(Protocollo.class);
            Root<Protocollo> root = cq.from(Protocollo.class);
            cq.select(root);
            cq.where(cb.equal(root.get(Protocollo_.anno), year));
            cq.orderBy(cb.desc(root.get("iddocumento")));
            TypedQuery<Protocollo> tq = em.createQuery(cq).setMaxResults(1);
            Protocollo max;
            protocollo.setDataprotocollo(date);
            protocollo.setAnno(year);
            try {
                max = tq.getSingleResult();
            } catch (NoResultException ex) {
                max=null;
            }
            String newIddocumento;
            if( max != null ){
                Integer i = Integer.parseInt(max.getIddocumento().substring(4));
                i++;
                newIddocumento = year+String.format("%08d", i);
            } else {
                newIddocumento = year+"00000001";
            }
            protocollo.setIddocumento(newIddocumento);
        }
        return true;
    }
    
}
