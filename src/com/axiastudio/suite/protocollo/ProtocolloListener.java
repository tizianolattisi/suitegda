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
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.base.entities.UfficioUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.protocollo.entities.Protocollo;
import com.axiastudio.suite.protocollo.entities.SoggettoProtocollo;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class ProtocolloListener {

    @PrePersist
    void prePersist(Object object) {

        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        Protocollo protocollo = (Protocollo) object;
        Calendar calendar = Calendar.getInstance();
        Integer year = calendar.get(Calendar.YEAR);
        Date today = calendar.getTime();

        // Generazione dell'iddocumento
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

        // Soggetti di primo inserimento
        for( SoggettoProtocollo sp: protocollo.getSoggettoProtocolloCollection() ){
            sp.setPrimoinserimento(Boolean.TRUE);
        }

        /* convalide */
        this.convalide(protocollo, autenticato, today);
    }

    @PreUpdate
    void preUpdate(Object object) {
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        Protocollo protocollo = (Protocollo) object;
        Calendar calendar = Calendar.getInstance();
        Integer year = calendar.get(Calendar.YEAR);
        Date today = calendar.getTime();

        /* convalide */
        this.convalide(protocollo, autenticato, today);
    }

    private void convalide(Protocollo protocollo, Utente autenticato, Date today){
        if( protocollo.getConvalidaattribuzioni() &&
                (protocollo.getEsecutoreconvalidaattribuzioni() == null || protocollo.getEsecutoreconvalidaattribuzioni().length() == 0)){
            protocollo.setEsecutoreconvalidaattribuzioni(autenticato.getLogin());
            protocollo.setDataconvalidaattribuzioni(today);
        }
        if( protocollo.getConvalidaprotocollo()&&
                (protocollo.getEsecutoreconvalidaprotocollo() == null || protocollo.getEsecutoreconvalidaprotocollo().length() == 0)){
            protocollo.setEsecutoreconvalidaprotocollo(autenticato.getLogin());
            protocollo.setDataconvalidaprotocollo(today);
            // numero protocollo
        }
        if( protocollo.getConsolidadocumenti()&&
                (protocollo.getEsecutoreconsolidadocumenti() == null || protocollo.getEsecutoreconsolidadocumenti().length() == 0)){
            protocollo.setEsecutoreconsolidadocumenti(autenticato.getLogin());
            protocollo.setDataconsolidadocumenti(today);
        }
        if( protocollo.getSpedito() &&
                (protocollo.getEsecutorespedizione() == null || protocollo.getEsecutorespedizione().length() == 0)){
            protocollo.setEsecutorespedizione(autenticato.getLogin());
            protocollo.setDataspedizione(today);
        }
    }

}
