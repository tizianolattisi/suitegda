/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Controller;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.suite.anagrafiche.entities.Soggetto;
import com.axiastudio.suite.anagrafiche.entities.TipologiaSoggetto;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.protocollo.entities.Protocollo;
import com.axiastudio.suite.protocollo.entities.TipoProtocollo;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class DemoData {
    
    private static Controller createController(){
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManagerFactory emf = db.getEntityManagerFactory();   
        Controller controller = new Controller(emf);
        return controller;
    }
    
    public static void initBase(){
        Controller ctrl = DemoData.createController();

        Ufficio u1 = new Ufficio();
        u1.setDescrizione("Ufficio informativo");
        Ufficio u2 = new Ufficio();
        u2.setDescrizione("Ufficio protocollo");
        
        ctrl.commit(u1);
        ctrl.commit(u2);
    }
    
    public static void initAnagrafiche(){
        Controller ctrl = DemoData.createController();
        
        Soggetto s = new Soggetto();
        s.setNome("Tiziano");
        s.setCognome("Lattisi");
        s.setTipologiaSoggetto(TipologiaSoggetto.PERSONA);
        
        ctrl.commit(s);
    }
    
    public static void initProtocollo(){
        Controller ctrl = DemoData.createController();
        
        Protocollo p1 = new Protocollo();
        p1.setOggetto("Oggetto del protocollo");
        p1.setNote("Note del protocollo");
        p1.setTipo(TipoProtocollo.USCITA);
        //p.setSportello(u2);
        p1.setRichiederisposta(Boolean.TRUE);

        Protocollo p2 = new Protocollo();
        p2.setOggetto("Oggetto del protocollo2");
        p2.setNote("Note del protocollo2");
        //p2.setSportello(u1);
        p2.setTipo(TipoProtocollo.ENTRATA);

        Protocollo p3 = new Protocollo();
        p3.setOggetto("Oggetto del protocollo3");
        p3.setNote("Note del protocollo3");
        p3.setTipo(TipoProtocollo.INTERNO);
        
        ctrl.commit(p1);
        ctrl.commit(p2);
        ctrl.commit(p3);
    }
}
