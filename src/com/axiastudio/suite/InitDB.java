/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite;

import com.axiastudio.pypapi.db.Database;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class InitDB {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        /*
         * 1. dropdb -h 127.0.0.1 suite && createdb -h 127.0.0.1 suite
         * 
         * 2. esecuzione di questo main
         * 
         * 3. nice -n 10 time python import.py | /Library/PostgreSQL/9.1/bin/psql -h 127.0.0.1 suite
         * 
         */
        
        String jdbcUrl = System.getProperty("jdbc.url");
        String jdbcUser = System.getProperty("jdbc.user");
        String jdbcPassword = System.getProperty("jdbc.password");
        String jdbcDriver = System.getProperty("jdbc.driver");
        Map properties = new HashMap();
        
        if( jdbcUrl != null ){
            properties.put("javax.persistence.jdbc.url", jdbcUrl);
        }
        if( jdbcUser != null ){
            properties.put("javax.persistence.jdbc.user", jdbcUser);
        }
        if( jdbcPassword != null ){
            properties.put("javax.persistence.jdbc.password", jdbcPassword);
        }
        if( jdbcDriver != null ){
            properties.put("javax.persistence.jdbc.driver", jdbcDriver);
        }
        properties.put("eclipselink.ddl-generation", "create-tables");

        Database db = new Database();
        db.open("SuitePU", properties);
        EntityManagerFactory emf = db.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        
        System.out.println("Done.");
    }
}
