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
import com.axiastudio.pypapi.db.Store;
import com.axiastudio.suite.base.entities.UfficioUtente;
import com.axiastudio.suite.base.entities.Ufficio_;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.pratiche.entities.Pratica;
import com.axiastudio.suite.pratiche.entities.Pratica_;
import com.axiastudio.suite.protocollo.entities.Attribuzione;
import com.axiastudio.suite.protocollo.entities.Attribuzione_;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class SuiteUtil {
    
    public static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("dd/MM/yyyy h:mm");
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
    
    public static Store attribuzioni(Utente autenticato){
        Store store;
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Attribuzione> cq = cb.createQuery(Attribuzione.class);
        Root<Attribuzione> root = cq.from(Attribuzione.class);
        cq.select(root);
        CriteriaBuilder.In<Long> in = cb.in(root.get(Attribuzione_.ufficio).get(Ufficio_.id));
        for( UfficioUtente uu: autenticato.getUfficioUtenteCollection() ){
            if( uu.getRicerca() ){
                in = in.value(uu.getUfficio().getId());
            }
        }
        cq.where(cb.and(cb.equal(root.get(Attribuzione_.letto), Boolean.FALSE), in));
        TypedQuery<Attribuzione> tq = em.createQuery(cq);
        List<Attribuzione> resultList = tq.getResultList();
        store = new Store(resultList);
        return store;
    }
    
    public static Pratica findPratica(String idpratica){
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Pratica> cq = cb.createQuery(Pratica.class);
        Root<Pratica> root = cq.from(Pratica.class);
        cq.select(root);
        cq.where(cb.equal(root.get(Pratica_.idpratica), idpratica));
        TypedQuery<Pratica> tq = em.createQuery(cq);
        Pratica pratica = tq.getSingleResult();
        return pratica;
    }
    
}
