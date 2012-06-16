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
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.pratiche.PraticheValidators;
import com.axiastudio.suite.pratiche.entities.Pratica;
import com.axiastudio.suite.protocollo.ProtocolloAdapters;
import com.axiastudio.suite.protocollo.ProtocolloValidators;
import com.axiastudio.suite.protocollo.entities.PraticaProtocollo;
import com.axiastudio.suite.protocollo.entities.Protocollo;
import com.axiastudio.suite.protocollo.entities.SoggettoProtocollo;
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

        // registro adapter e validatori
        Register.registerAdapters(Resolver.adaptersFromClass(ProtocolloAdapters.class));
        Register.registerValidators(Resolver.validatorsFromClass(ProtocolloValidators.class));
        Register.registerValidators(Resolver.validatorsFromClass(PraticheValidators.class));
        
        // dati di base
        DemoData.initData();
        
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
                          
        Form formSoggettoProtocollo = Register.registerForm(db.getEntityManagerFactory(),
                           "classpath:com/axiastudio/suite/protocollo/forms/soggettoprotocollo.ui",
                           SoggettoProtocollo.class);
      
        Form formPraticaProtocollo = Register.registerForm(db.getEntityManagerFactory(),
                           "classpath:com/axiastudio/suite/protocollo/forms/praticaprotocollo.ui",
                           PraticaProtocollo.class);


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

        
        Mdi mdi = new Mdi();
        //mdi.showMaximized();
        mdi.show();
        
        app.setCustomApplicationName("PyPaPi Suite");
        app.setCustomApplicationCredits("Copyright AXIA Studio 2012<br/>");
        app.exec();
    
    }
}
