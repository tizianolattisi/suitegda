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
import com.axiastudio.suite.pratiche.entities.Pratica;
import com.axiastudio.suite.protocollo.entities.*;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class DemoData {
    
    public DemoData() {
    }
    
    private static Controller createController(){
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManagerFactory emf = db.getEntityManagerFactory();   
        Controller controller = new Controller(emf);
        return controller;
    }
    
    public static void initData(){
        Controller ctrl = DemoData.createController();

        /* uffici */
        Ufficio uffInf = new Ufficio();
        uffInf.setDescrizione("Ufficio informativo");
        Ufficio uffPro = new Ufficio();
        uffPro.setDescrizione("Ufficio protocollo");
        
        /* soggetti */
        Soggetto tiziano = new Soggetto();
        tiziano.setNome("Tiziano");
        tiziano.setCognome("Lattisi");
        tiziano.setTipologiaSoggetto(TipologiaSoggetto.PERSONA);
        
        /* pratiche */
        //Pratica pratica = new Pratica();
        //pratica.setDescrizione("Pratica demo");
        
        /* protocolli */
        Protocollo pro1 = new Protocollo();
        pro1.setOggetto("Oggetto del protocollo");
        pro1.setNote("Note del protocollo");
        pro1.setTipo(TipoProtocollo.ENTRATA);
        pro1.setSportello(uffInf);
        pro1.setRichiederisposta(Boolean.TRUE);
        UfficioProtocollo up = new UfficioProtocollo();
        up.setUfficio(uffInf);
        List<UfficioProtocollo> ufficiprotocollo = new ArrayList<UfficioProtocollo>();
        ufficiprotocollo.add(up);
        pro1.setUfficioProtocolloCollection(ufficiprotocollo);
        List<Attribuzione> attribuzioni = new ArrayList<Attribuzione>();
        Attribuzione a1 = new Attribuzione();
        a1.setUfficio(uffInf);
        Attribuzione a2 = new Attribuzione();
        a2.setUfficio(uffPro);
        attribuzioni.add(a1);
        attribuzioni.add(a2);
        pro1.setAttribuzioneCollection(attribuzioni);
        SoggettoProtocollo sp = new SoggettoProtocollo();
        sp.setSoggetto(tiziano);
        List<SoggettoProtocollo> soggettiprotocollo = new ArrayList<SoggettoProtocollo>();
        soggettiprotocollo.add(sp);
        pro1.setSoggettoProtocolloCollection(soggettiprotocollo);
        /*
        PraticaProtocollo pp = new PraticaProtocollo();
        pp.setPratica(pratica);
        List<PraticaProtocollo> praticheprotocollo = new ArrayList<PraticaProtocollo>();
        praticheprotocollo.add(pp);
        pro1.setPraticaProtocolloCollection(praticheprotocollo);
        * 
        */

        Protocollo pro2 = new Protocollo();
        pro2.setOggetto("Oggetto del protocollo2");
        pro2.setNote("Note del protocollo2");
        pro2.setSportello(uffPro);
        pro2.setTipo(TipoProtocollo.USCITA);
        List<SoggettoProtocollo> soggettiprotocollo2 = new ArrayList<SoggettoProtocollo>();
        soggettiprotocollo2.add(sp);
        pro2.setSoggettoProtocolloCollection(soggettiprotocollo2);

        ctrl.commit(pro1);
        ctrl.commit(pro2);
    }
}
