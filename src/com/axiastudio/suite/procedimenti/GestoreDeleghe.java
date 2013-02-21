/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.procedimenti;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Controller;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IController;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.pypapi.db.Store;
import com.axiastudio.pypapi.ui.Column;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.procedimenti.entities.Carica;
import com.axiastudio.suite.procedimenti.entities.CodiceCarica;
import com.axiastudio.suite.procedimenti.entities.Delega;
import com.axiastudio.suite.procedimenti.entities.Delega_;
import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class GestoreDeleghe implements IGestoreDeleghe {
    
    @Override
    public Boolean checkDelega(CodiceCarica codiceCarica){

        Carica carica = this.findCarica(codiceCarica);
        
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Delega> cq = cb.createQuery(Delega.class);
        Root from = cq.from(Delega.class);
        
        // l'utente
        Utente utente = (Utente) Register.queryUtility(IUtente.class);
        Predicate pUtente = cb.equal(from.get(Delega_.utente), utente);
        // la carica richiesta
        Predicate pCarica = cb.equal(from.get(Delega_.carica), carica);
        
        // where
        cq = cq.where(cb.and(pUtente, pCarica));
        
        TypedQuery<Delega> tq = em.createQuery(cq);
        List<Delega> deleghe = tq.getResultList();
        if( deleghe.size()>0 ){
            return true;
        }
        return false;
    }
    
    private Carica findCarica(CodiceCarica codiceCarica){
        Controller ctrlCarica = (Controller) Register.queryUtility(IController.class, "com.axiastudio.suite.procedimenti.entities.Carica");
        Integer i = codiceCarica.ordinal()+1;
        Carica carica = (Carica) ctrlCarica.get(i.longValue());
        return carica;        
    }


    
}
