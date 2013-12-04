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
package com.axiastudio.suite.pratiche;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.suite.pratiche.entities.Pratica;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class PraticaListener {

    /*
     *  Generazione del corretto identificativo di pratica, codifica, e uffici
     */
    @PrePersist
    void prePersist(Pratica pratica) {
        Calendar calendar = Calendar.getInstance();
        Integer year = calendar.get(Calendar.YEAR);
        Date date = calendar.getTime();
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Pratica> cq = cb.createQuery(Pratica.class);
        Root<Pratica> root = cq.from(Pratica.class);
        cq.select(root);
        cq.where(cb.equal(root.get("anno"), year));
        cq.orderBy(cb.desc(root.get("idpratica")));
        TypedQuery<Pratica> tq = em.createQuery(cq).setMaxResults(1);
        Pratica max;
        pratica.setDatapratica(date);
        pratica.setAnno(year);
        try {
            max = tq.getSingleResult();
        } catch (NoResultException ex) {
            max=null;
        }
        String newIdpratica;
        if( max != null ){
            Integer i = Integer.parseInt(max.getIdpratica().substring(4));
            i++;
            newIdpratica = year+String.format("%05d", i);
        } else {
            newIdpratica = year+"00001";
        }
        pratica.setIdpratica(newIdpratica);

        // Codifica interna
        String codifica = PraticaUtil.creaCodificaInterna(pratica.getTipo());
        pratica.setCodiceinterno(codifica);

        // se mancano gestione e ubicazione, li fisso come l'attribuzione
        if( pratica.getGestione() == null ){
            pratica.setGestione(pratica.getAttribuzione());
        }
        if( pratica.getUbicazione() == null ){
            pratica.setUbicazione(pratica.getAttribuzione());
        }

        // creazione del dettaglio

    }

    @PostPersist
    void postPersist(Pratica pratica){
        Database db = (Database) Register.queryUtility(IDatabase.class);
        String className = pratica.getTipo().getProcedimento().getTipodettaglio();
        if( className != null ){
            try {
                Class<?> klass = Class.forName(className);
                IDettaglio dettaglio = (IDettaglio) klass.newInstance();
                dettaglio.setIdpratica(pratica.getIdpratica());
                dettaglio.setCodiceinterno(pratica.getCodiceinterno());
                EntityManager em = db.getEntityManagerFactory().createEntityManager();
                em.getTransaction().begin();
                em.persist(dettaglio);
                em.getTransaction().commit();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (InstantiationException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IllegalAccessException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }
}
