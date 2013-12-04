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
import com.axiastudio.suite.pratiche.IDettaglio;
import com.axiastudio.suite.pratiche.entities.Pratica;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
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
        Pratica pratica = tq.getSingleResult();
        return pratica;
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
        Costante costante = tq.getSingleResult();
        return costante;
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
        Giunta giunta = tq.getSingleResult();
        return giunta;
    }

    public static List<Modello> elencoModelli(){
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Modello> cq = cb.createQuery(Modello.class);
        Root<Modello> root = cq.from(Modello.class);
        cq.select(root);
        TypedQuery<Modello> query = em.createQuery(cq);
        List<Modello> modelli = query.getResultList();
        return modelli;
    }
    
}
