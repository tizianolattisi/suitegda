/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite;

import com.axiastudio.pypapi.Application;
import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.Resolver;
import com.axiastudio.pypapi.db.*;
import com.axiastudio.pypapi.ui.Form;
import com.axiastudio.pypapi.ui.IForm;
import com.axiastudio.suite.anagrafiche.entities.Soggetto;
import com.axiastudio.suite.anagrafiche.entities.TipologiaSoggetto;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.pratiche.PraticheValidators;
import com.axiastudio.suite.pratiche.entities.Pratica;
import com.axiastudio.suite.protocollo.ProtocolloAdapters;
import com.axiastudio.suite.protocollo.ProtocolloValidators;
import com.axiastudio.suite.protocollo.entities.Protocollo;
import com.axiastudio.suite.protocollo.entities.TipoProtocollo;
import com.axiastudio.suite.protocollo.forms.FormProtocollo;
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
        Register.registerUtility(db, IDatabase.class);
        EntityManagerFactory emf = db.getEntityManagerFactory();        
        //EntityManager em = emf.createEntityManager();

        // registro adapter e validatori
        Register.registerAdapters(Resolver.adaptersFromClass(ProtocolloAdapters.class));
        Register.registerValidators(Resolver.validatorsFromClass(ProtocolloValidators.class));
        Register.registerValidators(Resolver.validatorsFromClass(PraticheValidators.class));

        // qualche dato di base
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
        p2.setSportello(u1);
        p2.setTipo(TipoProtocollo.ENTRATA);

        Protocollo p3 = new Protocollo();
        p3.setOggetto("Oggetto del protocollo3");
        p3.setNote("Note del protocollo3");
        p3.setTipo(TipoProtocollo.INTERNO);

        Pratica pr = new Pratica();
        pr.setDescrizione("Pratica demo");
        
        // commit
        Controller controller = new Controller(emf, Protocollo.class);
        controller.commit(p);
        controller.commit(p2);
        controller.commit(p3);
        controller.commit(pr);
        
        Application app = new Application(args);
        

        Form formUfficio = Register.registerForm(db.getEntityManagerFactory(),
                            "classpath:com/axiastudio/suite/base/forms/ufficio.ui",
                            Ufficio.class);

        Form formSoggetto = Register.registerForm(db.getEntityManagerFactory(),
                            "classpath:com/axiastudio/suite/anagrafiche/forms/soggetto.ui",
                            Soggetto.class);

        Form formPratica = Register.registerForm(db.getEntityManagerFactory(),
                           "classpath:com/axiastudio/suite/pratiche/forms/pratica.ui",
                           Pratica.class);


        /* 
         * Registrazione a mano (form personalizzato), in alternativa a:
         * 
         * Form formProtocollo = Register.registerForm(db.getEntityManagerFactory(),
         *                     "classpath:com/axiastudio/suite/protocollo/forms/protocollo.ui",
         *                     Protocollo.class);
         * 
         */
        Register.registerUtility(new Controller(db.getEntityManagerFactory(), Protocollo.class), IController.class, Protocollo.class.getName());
        Form formProtocollo = new FormProtocollo("classpath:com/axiastudio/suite/protocollo/forms/protocollo.ui", Protocollo.class, "Protocolli");
        Register.registerUtility(formProtocollo, IForm.class, Protocollo.class.getName());
        Register.registerUtility(Protocollo.class, IFactory.class, Protocollo.class.getName());
        formProtocollo.init();

        
        Mdi mdi = new Mdi();
        mdi.showMaximized();
        //mdi.show();
        
        app.setCustomApplicationName("PyPaPi Suite");
        app.setCustomApplicationCredits("Copyright AXIA Studio 2012<br/>");
        app.exec();
    
    }
}
