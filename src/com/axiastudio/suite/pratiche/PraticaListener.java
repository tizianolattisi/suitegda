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
import com.axiastudio.pypapi.ui.IForm;
import com.axiastudio.pypapi.ui.Util;
import com.axiastudio.suite.pratiche.entities.FasePratica;
import com.axiastudio.suite.pratiche.entities.Pratica;
import com.axiastudio.suite.procedimenti.entities.FaseProcedimento;

import javax.persistence.*;
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
        for( Integer i=0; i<fasiProcedimento.size(); i++ ){
            FaseProcedimento faseProcedimento = fasiProcedimento.get(i);
            FasePratica fasePratica = new FasePratica();
            fasePratica.setPratica(pratica);
            fasePratica.setFase(faseProcedimento.getFase());
            fasePratica.setTesto(faseProcedimento.getTesto());
            fasePratica.setCondizione(faseProcedimento.getCondizione());
            fasePratica.setConfermabile(faseProcedimento.getConfermabile());
            fasePratica.setTestoconfermata(faseProcedimento.getTestoconfermata());
            fasePratica.setRifiutabile(faseProcedimento.getRifiutabile());
            fasePratica.setTestorifiutata(faseProcedimento.getTestorifiutata());
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

        // creazione del dettaglio
        String className = pratica.getTipo().getProcedimento().getTipodettaglio();
        if( className != null && !className.equals("") ){
            try {
                Class<?> klass = Class.forName(className);
                IDettaglio dettaglio = (IDettaglio) klass.newInstance();
                dettaglio.setPratica(pratica);  // XXX: non va in persistenza...
                dettaglio.setCodiceinterno(pratica.getCodiceinterno());
                IForm form = Util.formFromEntity(dettaglio);
                form.show();
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
