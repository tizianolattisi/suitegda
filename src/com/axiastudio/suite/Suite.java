/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite;

import com.axiastudio.pypapi.Application;
import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.Resolver;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.ui.Form;
import com.axiastudio.suite.anagrafiche.entities.Soggetto;
import com.axiastudio.suite.anagrafiche.entities.TipologiaSoggetto;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.protocollo.Adapters;
import com.axiastudio.suite.protocollo.entities.Protocollo;
import com.axiastudio.suite.protocollo.entities.TipoProtocollo;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class Suite {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Database db = new Database();
        db.open("SuitePU");
        EntityManagerFactory emf = db.getEntityManagerFactory();
        
        //*
        EntityManager em = emf.createEntityManager();
        
        Ufficio u1 = new Ufficio();
        u1.setDescrizione("Ufficio informativo");

        Ufficio u2 = new Ufficio();
        u2.setDescrizione("Ufficio protocollo");

        Soggetto s = new Soggetto();
        s.setNome("Tiziano");
        s.setCognome("Lattisi");
        s.setTipologiaSoggetto(TipologiaSoggetto.PERSONA);
        
        Protocollo p = new Protocollo();
        p.setOggetto("Oggetto del protocollo");
        p.setNote("Note del protocollo");
        p.setTipo(TipoProtocollo.USCITA);
        p.setSportello(u2);
        p.setRichiederisposta(Boolean.TRUE);

        Protocollo p2 = new Protocollo();
        p2.setOggetto("Oggetto del protocollo2");
        p2.setNote("Note del protocollo2");
        p2.setTipo(TipoProtocollo.ENTRATA);

        Protocollo p3 = new Protocollo();
        p3.setOggetto("Oggetto del protocollo3");
        p3.setNote("Note del protocollo3");
        p3.setTipo(TipoProtocollo.INTERNO);

        
        em.getTransaction().begin();
        em.persist(p);
        em.persist(p2);
        em.persist(p3);
        em.persist(u1);
        em.persist(u2);
        em.persist(s);
        em.getTransaction().commit();
        em.close();
        //*/
        
        Application.initialize(args);
        
        Form formProtocollo = Register.registerForm(db.getEntityManagerFactory(),
                            "classpath:com/axiastudio/suite/protocollo/forms/protocollo.ui",
                            Protocollo.class);

        Form formUfficio = Register.registerForm(db.getEntityManagerFactory(),
                            "classpath:com/axiastudio/suite/base/forms/ufficio.ui",
                            Ufficio.class);

        Form formSoggetto = Register.registerForm(db.getEntityManagerFactory(),
                            "classpath:com/axiastudio/suite/anagrafiche/forms/soggetto.ui",
                            Soggetto.class);

        Register.registerAdapters(Resolver.adaptersFromEntityClass(Adapters.class));
        
        formProtocollo.show();
        formSoggetto.show();
        
        Application.exec();
    
    }
}
