/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.protocollo;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.annotations.Callback;
import com.axiastudio.pypapi.annotations.CallbackType;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.pypapi.db.Validation;
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
public class ProtocolloCallbacks {
    /*
     * Valida il protocollo e richiede il nuovo iddocumento
     */
    @Callback(type=CallbackType.BEFORECOMMIT)
    public static Validation validaProtocollo(Protocollo protocollo){
        String msg = "";
        Boolean res = true;
        /* sportello obbligatorio */
        if( protocollo.getSportello() == null ){
            msg += "Deve essere dichiarato uno sportello ricevente";
            res = false;
        }
        /* almeno un soggetto */
        if( protocollo.getSoggettoProtocolloCollection().isEmpty() ){
            msg += "Deve essere dichiarato almeno un soggetto esterno (mittente o destinatario).";
            res = false;
        }
        if( res == false ){
            return new Validation(false, msg);
        }
        
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
            /*
            if( protocollo.getTipo() == null ) protocollo.setTipo(TipoProtocollo.ENTRATA);
            if( protocollo.getFascicolo() == null ){
                Controller controller = (Controller) Register.queryUtility(IController.class, Fascicolo.class.getName());
                HashMap hm = new HashMap();
                hm.put("categoria", 0);
                hm.put("classe", 0);
                hm.put("fascicolo", 0);
                Store store = controller.createCriteriaStore(hm);
                System.out.println(store);
            }*/
        }
        return new Validation(true);
    }
    
}
