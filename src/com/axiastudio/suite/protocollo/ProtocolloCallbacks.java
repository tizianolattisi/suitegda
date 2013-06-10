/*
 * Copyright (C) 2012 AXIA Studio (http://www.axiastudio.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axiastudio.suite.protocollo;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.annotations.Callback;
import com.axiastudio.pypapi.annotations.CallbackType;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.pypapi.db.Validation;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.base.entities.UfficioUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.protocollo.entities.Protocollo;
import com.axiastudio.suite.protocollo.entities.SoggettoProtocollo;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
    public static Validation beforeCommit(Protocollo protocollo){
        String msg = "";
        Boolean res = true;
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        ProfiloUtenteProtocollo profilo = new ProfiloUtenteProtocollo(protocollo, autenticato);
        
        // La modifica di un protocollo è permessa solo a sportello e attribuzione principale
        if( protocollo.getId() != null && !profilo.inSportelloOAttribuzionePrincipale() ){
            msg += "Devi appartenere allo sportello o all'attribuzione principale\n";
            msg += "per poter modificare il protocollo.";
            return new Validation(false, msg);
        }
        
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
        
        Calendar calendar = Calendar.getInstance();
        Integer year = calendar.get(Calendar.YEAR);
        Date today = calendar.getTime();
        if( protocollo.getId() == null ){
            
            /* primo inserimento */
            for( SoggettoProtocollo sp: protocollo.getSoggettoProtocolloCollection() ){
                sp.setPrimoinserimento(Boolean.TRUE);
            }

            /* sportello tra quelli dell'utente */
            List<Ufficio> uffici = new ArrayList();
            for(UfficioUtente uu: autenticato.getUfficioUtenteCollection()){
                uffici.add(uu.getUfficio());
            }
            if( !uffici.contains(protocollo.getSportello()) ){
                msg += "Lo sportello deve essere scelto tra gli uffici dell'utente";
                res = false;
            }

            Database db = (Database) Register.queryUtility(IDatabase.class);
            EntityManager em = db.getEntityManagerFactory().createEntityManager();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Protocollo> cq = cb.createQuery(Protocollo.class);
            Root<Protocollo> root = cq.from(Protocollo.class);
            cq.select(root);
            cq.where(cb.equal(root.get("anno"), year));
            cq.orderBy(cb.desc(root.get("iddocumento")));
            TypedQuery<Protocollo> tq = em.createQuery(cq).setMaxResults(1);
            Protocollo max;
            protocollo.setDataprotocollo(today);
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
        
        /*
         * Se ci sono convalide inserisco l'esecutore
         */
        if( protocollo.getConvalidaattribuzioni() && protocollo.getEsecutoreconvalidaattribuzioni() == null ){
            protocollo.setEsecutoreconvalidaattribuzioni(autenticato.getLogin());
            protocollo.setDataconvalidaattribuzioni(today);
        }
        if( protocollo.getConvalidaprotocollo()&& protocollo.getEsecutoreconvalidaprotocollo() == null ){
            protocollo.setEsecutoreconvalidaprotocollo(autenticato.getLogin());
            protocollo.setDataconvalidaprotocollo(today);
            // numero protocollo
        }
        if( protocollo.getConsolidadocumenti()&& protocollo.getEsecutoreconsolidadocumenti() == null ){
            protocollo.setEsecutoreconsolidadocumenti(autenticato.getLogin());
            protocollo.setDataconsolidadocumenti(today);
        }

        return new Validation(true);
    }
    
    /*
     * CallbackType.AFTERCOMMIT
     */
    @Callback(type=CallbackType.AFTERCOMMIT)
    public static Validation afterCommit(Protocollo protocollo){
        // placeholder
        return new Validation(true);
    }

}
