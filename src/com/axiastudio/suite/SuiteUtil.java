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
package com.axiastudio.suite;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.suite.base.entities.Giunta;
import com.axiastudio.suite.generale.entities.Costante;
import com.axiastudio.suite.modelli.entities.Modello;
import com.axiastudio.suite.pratiche.entities.Pratica;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class SuiteUtil {
    
    public static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    
    public static String digest(String s){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(s.getBytes());
            byte byteData[] = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte aByteData : byteData) {
                sb.append(Integer.toString((aByteData & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SuiteUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
    public static Pratica trovaPratica(String idpratica){
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Pratica> cq = cb.createQuery(Pratica.class);
        Root<Pratica> root = cq.from(Pratica.class);
        cq.select(root);
        cq.where(cb.equal(root.get("idpratica"), idpratica));
        TypedQuery<Pratica> tq = em.createQuery(cq);
        return tq.getSingleResult();
    }
    
    public static Costante trovaCostante(String name){
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Costante> cq = cb.createQuery(Costante.class);
        Root<Costante> root = cq.from(Costante.class);
        cq.select(root);
        cq.where(cb.equal(root.get("nome"), name));
        TypedQuery<Costante> tq = em.createQuery(cq);
//        Costante costante = tq.getSingleResult();   // errore se la costante non esiste;
        // return null se costante non esiste (in caso di gestione esterna...); eccezione se si ottiene più di 1 risultato
        List results = tq.getResultList();
        if ( results == null || results.isEmpty() ) {
            return null;
        } else if (results.size() == 1) {
            return (Costante) results.get(0);
        }
        throw new NonUniqueResultException();
    }

    public static Giunta trovaGiuntaCorrente(){
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Giunta> cq = cb.createQuery(Giunta.class);
        Root<Giunta> root = cq.from(Giunta.class);
        cq.select(root);
        cq.where(cb.isNull(root.get("datacessazione")));
        TypedQuery<Giunta> tq = em.createQuery(cq);
        return tq.getSingleResult();
    }

    public static List<Modello> elencoModelli(){
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Modello> cq = cb.createQuery(Modello.class);
        Root<Modello> root = cq.from(Modello.class);
        cq.select(root);
        TypedQuery<Modello> query = em.createQuery(cq);
        return query.getResultList();
    }

    public static Date getServerDate(){
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        String query = "SELECT current_timestamp;";
        return (Date) em.createNativeQuery(query).getSingleResult();
    }
}
