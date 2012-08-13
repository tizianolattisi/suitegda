/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.suite.anagrafiche.entities.SessoSoggetto;
import com.axiastudio.suite.anagrafiche.entities.Soggetto;
import com.axiastudio.suite.anagrafiche.entities.TipologiaSoggetto;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.base.entities.UfficioUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.pratiche.entities.Pratica;
import com.axiastudio.suite.protocollo.ProtocolloValidators;
import com.axiastudio.suite.protocollo.entities.*;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class DemoData {
    
    public static void main(String[] args) {
        DemoData.initData();
    }
    
    public DemoData() {
    }
    
    public static void initData(){
        // inizializzo e apro la transazione
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManagerFactory emf = db.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        

        // amministratore
        Utente admin = new Utente();
        admin.setLogin("admin");
        admin.setPassword(SuiteUtil.digest("pypapi"));
        admin.setAmministratore(Boolean.TRUE);
        em.merge(admin);
        
        // uffici
        Ufficio uffInf = new Ufficio();
        uffInf.setDescrizione("Ufficio informativo");
        Ufficio uffPro = new Ufficio();
        uffPro.setDescrizione("Ufficio protocollo");
        Ufficio uffEdi = new Ufficio();
        uffEdi.setDescrizione("Ufficio edilizia");
        Ufficio uffCom = new Ufficio();
        uffCom.setDescrizione("Ufficio commercio");
        em.merge(uffInf);
        em.merge(uffPro);
        em.merge(uffEdi);
        em.merge(uffCom);

        
        // utenti
        Utente mario = new Utente();
        mario.setLogin("mario");
        mario.setPassword(SuiteUtil.digest("super"));
        mario.setOperatoreprotocollo(Boolean.TRUE);
        mario.setOperatoreanagrafiche(Boolean.TRUE);
        mario.setOperatorepratiche(Boolean.TRUE);
        Utente luigi = new Utente();
        luigi.setLogin("luigi");
        luigi.setPassword(SuiteUtil.digest("bros"));
        List<UfficioUtente> ufficiUtente = new ArrayList();
        UfficioUtente uu = new UfficioUtente();
        uu.setUfficio(uffInf);
        uu.setRicerca(Boolean.TRUE);
        uu.setVisualizza(Boolean.TRUE);
        ufficiUtente.add(uu);
        UfficioUtente uu2 = new UfficioUtente();
        uu2.setUfficio(uffPro);
        uu2.setRicerca(Boolean.TRUE);
        ufficiUtente.add(uu2);
        mario.setUfficioUtenteCollection(ufficiUtente);
        em.merge(mario);
        em.merge(luigi);

        // soggetti
        Soggetto tiziano = new Soggetto();
        tiziano.setNome("Tiziano");
        tiziano.setCognome("Lattisi");
        tiziano.setSessoSoggetto(SessoSoggetto.M);
        tiziano.setTipologiaSoggetto(TipologiaSoggetto.PERSONA);
        em.merge(tiziano);
        
        // pratiche
        Pratica pratica = new Pratica();
        pratica.setDescrizione("Pratica demo");
        em.merge(pratica);
        
        // protocolli
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
        sp.setTitolo(TitoloSoggettoProtocollo.TECNICO);
        List<SoggettoProtocollo> soggettiprotocollo = new ArrayList<SoggettoProtocollo>();
        soggettiprotocollo.add(sp);
        pro1.setSoggettoProtocolloCollection(soggettiprotocollo);
        PraticaProtocollo pp = new PraticaProtocollo();
        pp.setPratica(pratica);
        List<PraticaProtocollo> praticheprotocollo = new ArrayList<PraticaProtocollo>();
        praticheprotocollo.add(pp);
        pro1.setPraticaProtocolloCollection(praticheprotocollo);            
        Protocollo pro2 = new Protocollo();
        pro2.setOggetto("Oggetto del protocollo2");
        pro2.setNote("Note del protocollo2");
        pro2.setSportello(uffPro);
        pro2.setTipo(TipoProtocollo.USCITA);
        List<SoggettoProtocollo> soggettiprotocollo2 = new ArrayList<SoggettoProtocollo>();
        soggettiprotocollo2.add(sp);
        pro2.setSoggettoProtocolloCollection(soggettiprotocollo2);
        ProtocolloValidators.validaProtocollo(pro1);
        //ProtocolloValidators.validaProtocollo(pro2);
        em.merge(pro1);
        //em.merge(pro2);
      
       // committo
        em.getTransaction().commit();

    }
}
