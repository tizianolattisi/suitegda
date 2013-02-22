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
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.finanziaria.entities.Servizio;
import com.axiastudio.suite.procedimenti.entities.Carica;
import com.axiastudio.suite.procedimenti.entities.CodiceCarica;
import com.axiastudio.suite.procedimenti.entities.Delega;
import com.axiastudio.suite.procedimenti.entities.Delega_;
import com.axiastudio.suite.procedimenti.entities.Procedimento;
import java.util.ArrayList;
import java.util.Date;
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
    public Boolean checkDelega(CodiceCarica codiceCarica, Servizio servizio, Procedimento procedimento, Ufficio ufficio, Utente utente, Date dataVerifica){

        Carica carica = this.findCarica(codiceCarica);
        
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Delega> cq = cb.createQuery(Delega.class);
        Root from = cq.from(Delega.class);
        
        List<Predicate> predicates = new ArrayList();

        // la carica richiesta
        predicates.add(cb.equal(from.get(Delega_.carica), carica));
        
        // servizio
        if( servizio != null ){
            predicates.add(cb.equal(from.get(Delega_.servizio), servizio));
        }

        // procedimento
        if( procedimento != null ){
            predicates.add(cb.equal(from.get(Delega_.procedimento), procedimento));
        }

        // ufficio
        if( servizio != null ){
            predicates.add(cb.equal(from.get(Delega_.ufficio), ufficio));
        }
        
        // data verifica
        if( dataVerifica == null ){
            dataVerifica = new Date();
        }

        predicates.add(cb.lessThanOrEqualTo(from.get(Delega_.inizio), dataVerifica));
        predicates.add(cb.or(
                             cb.isNull(from.get(Delega_.fine)),
                             cb.greaterThanOrEqualTo(from.get(Delega_.fine), dataVerifica)
                            )
                      );
        
        // l'utente
        if( utente == null ){
            utente = (Utente) Register.queryUtility(IUtente.class);
        }
        predicates.add(cb.equal(from.get(Delega_.utente), utente));
        
        // where
        cq = cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        
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

    @Override
    public Boolean checkDelega(CodiceCarica codiceCarica) {
        return this.checkDelega(codiceCarica, null, null, null, null, null);
    }

    @Override
    public Boolean checkDelega(CodiceCarica codiceCarica, Servizio servizio) {
        return this.checkDelega(codiceCarica, servizio, null, null, null, null);
    }

    @Override
    public Boolean checkDelega(CodiceCarica codiceCarica, Servizio servizio, Procedimento procedimento) {
        return this.checkDelega(codiceCarica, servizio, procedimento, null, null, null);
    }

    @Override
    public Boolean checkDelega(CodiceCarica codiceCarica, Servizio servizio, Procedimento procedimento, Ufficio ufficio) {
        return this.checkDelega(codiceCarica, servizio, procedimento, ufficio, null, null);
    }

    @Override
    public Boolean checkDelega(CodiceCarica codiceCarica, Servizio servizio, Procedimento procedimento, Ufficio ufficio, Utente utente) {
        return this.checkDelega(codiceCarica, servizio, procedimento, ufficio, utente, null);
    }

    
}
