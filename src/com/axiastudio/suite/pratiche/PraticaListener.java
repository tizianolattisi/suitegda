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
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.pratiche.entities.FasePratica;
import com.axiastudio.suite.pratiche.entities.Pratica;
import com.axiastudio.suite.pratiche.entities.UtentePratica;
import com.axiastudio.suite.procedimenti.entities.FaseProcedimento;
import com.axiastudio.suite.procedimenti.entities.UfficioUtenteProcedimento;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PrePersist;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;

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

        // eredito classificazione dalla codifica interna
        if (pratica.getFascicolo() == null) {
            pratica.setFascicolo(pratica.getTipo().getFascicolo());
        }

        // mi copio nella pratica le fasi di procedimento
        List<FaseProcedimento> fasiProcedimento = pratica.getTipo().getProcedimento().getFaseProcedimentoCollection();
        List<FasePratica> fasiPratica = new ArrayList<FasePratica>();
        Boolean prima=true;
        for( Integer i=0; i<fasiProcedimento.size(); i++ ){
            FaseProcedimento faseProcedimento = fasiProcedimento.get(i);
            FasePratica fasePratica = new FasePratica();
            fasePratica.setPratica(pratica);
            fasePratica.setFase(faseProcedimento.getFase());
            fasePratica.setTesto(faseProcedimento.getTesto());
            fasePratica.setDascartare(faseProcedimento.getDascartare());
            fasePratica.setCondizione(faseProcedimento.getCondizione());
            fasePratica.setAzione(faseProcedimento.getAzione());
            fasePratica.setConfermabile(faseProcedimento.getConfermabile());
            fasePratica.setTestoconfermata(faseProcedimento.getTestoconfermata());
            fasePratica.setRifiutabile(faseProcedimento.getRifiutabile());
            fasePratica.setTestorifiutata(faseProcedimento.getTestorifiutata());
            fasePratica.setUsoresponsabile(faseProcedimento.getUsoresponsabile());
            fasePratica.setCariche(faseProcedimento.getCariche());
            if( prima ){
                fasePratica.setAttiva(true);
                prima=false;
            }
            fasiPratica.add(fasePratica);
        }
        for( Integer i=0; i<fasiPratica.size(); i++ ){
            if( fasiProcedimento.get(i).getConfermata() != null ){
                fasiPratica.get(i).setConfermata(fasiPratica.get(fasiProcedimento.get(i).getConfermata().getProgressivo()));
            }
            if( fasiProcedimento.get(i).getRifiutata() != null ){
                fasiPratica.get(i).setRifiutata(fasiPratica.get(fasiProcedimento.get(i).getRifiutata().getProgressivo()));
            }
        }
        pratica.setFasePraticaCollection(fasiPratica);

        // mi copio responsabile e istruttore dal procedimento
        Utente istruttore=null;
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        List<UfficioUtenteProcedimento> utentiProcedimento = new ArrayList<UfficioUtenteProcedimento>(pratica.getTipo().getProcedimento().getUfficioUtenteProcedimentoCollection());
        List<UtentePratica> utentiPratica = new ArrayList<UtentePratica>();
        for( Integer i=0; i<utentiProcedimento.size(); i++ ){
            UfficioUtenteProcedimento ufficioUtenteProcedimento = utentiProcedimento.get(i);
            Utente utente = ufficioUtenteProcedimento.getUfficioUtente().getUtente();
            Ufficio ufficio = ufficioUtenteProcedimento.getUfficioUtente().getUfficio();
            // mi interessa solo l'ufficio gestore
            if( ufficio.equals(pratica.getGestione()) ) {
                // se l'utente autenticato è nella lista come ufficio gestore della pratica e abilitato
                if (utente.equals(autenticato) && ufficioUtenteProcedimento.getAbilitato()) {
                    istruttore = utente;
                    break; // sicuramente è lui
                } else if( ufficioUtenteProcedimento.getAbituale() ){
                    istruttore = utente;
                }
            }
        }
        UtentePratica utentePratica = new UtentePratica();
        utentePratica.setUtente(istruttore);
        utentePratica.setIstruttore(true);
        utentePratica.setPratica(pratica);
        utentiPratica.add(utentePratica);
        pratica.setUtentePraticaCollection(utentiPratica);

    }

}
