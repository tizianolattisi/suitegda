/*
 * Copyright (C) 2013 AXIA Studio (http://www.axiastudio.com)
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

import com.axiastudio.pypapi.Application;
import com.axiastudio.pypapi.IStreamProvider;
import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.Resolver;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.ICriteriaFactory;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.pypapi.plugins.ooops.FileStreamProvider;
import com.axiastudio.pypapi.plugins.ooops.OoopsPlugin;
import com.axiastudio.pypapi.plugins.ooops.RuleSet;
import com.axiastudio.pypapi.plugins.ooops.Template;
import com.axiastudio.pypapi.ui.Dialog;
import com.axiastudio.pypapi.ui.IQuickInsertDialog;
import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.suite.anagrafiche.entities.Indirizzo;
import com.axiastudio.suite.anagrafiche.entities.Soggetto;
import com.axiastudio.suite.anagrafiche.forms.FormIndirizzo;
import com.axiastudio.suite.anagrafiche.forms.FormQuickInsertSoggetto;
import com.axiastudio.suite.anagrafiche.forms.FormSoggetto;
import com.axiastudio.suite.base.Login;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.deliberedetermine.entities.Determina;
import com.axiastudio.suite.deliberedetermine.entities.MovimentoDetermina;
import com.axiastudio.suite.deliberedetermine.forms.FormDetermina;
import com.axiastudio.suite.demo.DemoData;
import com.axiastudio.suite.finanziaria.entities.Capitolo;
import com.axiastudio.suite.finanziaria.entities.Servizio;
import com.axiastudio.suite.pratiche.PraticaCallbacks;
import com.axiastudio.suite.pratiche.entities.Pratica;
import com.axiastudio.suite.pratiche.entities.TipoPratica;
import com.axiastudio.suite.pratiche.forms.FormPratica;
import com.axiastudio.suite.procedimenti.GestoreDeleghe;
import com.axiastudio.suite.procedimenti.IGestoreDeleghe;
import com.axiastudio.suite.procedimenti.entities.Carica;
import com.axiastudio.suite.procedimenti.entities.Delega;
import com.axiastudio.suite.procedimenti.entities.Norma;
import com.axiastudio.suite.procedimenti.entities.Procedimento;
import com.axiastudio.suite.procedimenti.forms.FormDelega;
import com.axiastudio.suite.protocollo.ProtocolloAdapters;
import com.axiastudio.suite.protocollo.ProtocolloCallbacks;
import com.axiastudio.suite.protocollo.ProtocolloPrivate;
import com.axiastudio.suite.protocollo.entities.Fascicolo;
import com.axiastudio.suite.protocollo.entities.PraticaProtocollo;
import com.axiastudio.suite.protocollo.entities.Protocollo;
import com.axiastudio.suite.protocollo.entities.SoggettoProtocollo;
import com.axiastudio.suite.protocollo.forms.FormProtocollo;
import com.axiastudio.suite.protocollo.forms.FormSoggettoProtocollo;
import com.axiastudio.suite.pubblicazioni.entities.Pubblicazione;
import com.axiastudio.suite.pubblicazioni.forms.FormPubblicazione;
import com.axiastudio.suite.sedute.entities.CaricaCommissione;
import com.axiastudio.suite.sedute.entities.Commissione;
import com.axiastudio.suite.sedute.entities.Seduta;
import com.axiastudio.suite.sedute.entities.TipoSeduta;
import com.axiastudio.suite.sedute.forms.FormTipoSeduta;
import com.trolltech.qt.gui.QFileDialog;
import com.trolltech.qt.gui.QMessageBox;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class SuiteDemo {
    
    public static void setAndStartDemo(String[] args){
        
        Database db = new Database();
        db.open("SuitePU");
        Register.registerUtility(db, IDatabase.class);
        
        Register.registerAdapters(Resolver.adaptersFromClass(ProtocolloAdapters.class));
        Register.registerCallbacks(Resolver.callbacksFromClass(ProtocolloCallbacks.class));
        Register.registerCallbacks(Resolver.callbacksFromClass(PraticaCallbacks.class));
        Register.registerPrivates(Resolver.privatesFromClass(ProtocolloPrivate.class));
        
        DemoData.initSchema();
        DemoData.initData();
        
        Application app = new Application(args);
        
        app.setLanguage("it");
        
        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/base/forms/ufficio.ui",
                              Ufficio.class,
                              Window.class,
                              "Uffici");

        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/base/forms/utente.ui",
                              Utente.class,
                              Window.class,
                              "Utenti");
        
        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/anagrafiche/forms/soggetto.ui",
                              Soggetto.class,
                              FormSoggetto.class,
                              "Soggetti anagrafici");

        Register.registerUtility(FormQuickInsertSoggetto.class, IQuickInsertDialog.class, Soggetto.class.getName());
        
        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/anagrafiche/forms/indirizzo.ui",
                              Indirizzo.class,
                              FormIndirizzo.class,
                              "Indirizzo");

        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/pratiche/forms/pratica.ui",
                              Pratica.class,
                              FormPratica.class,
                              "Pratiche");

        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/pratiche/forms/tipopratica.ui",
                              TipoPratica.class,
                              Window.class,
                              "Tipo Pratica");
        
        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/protocollo/forms/soggettoprotocollo.ui",
                              SoggettoProtocollo.class,
                              FormSoggettoProtocollo.class,
                              "Soggetto del protocollo");
      
        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/protocollo/forms/praticaprotocollo.ui",
                              PraticaProtocollo.class,
                              Window.class,
                              "Pratica contenente il protocollo");

        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/protocollo/forms/protocollo.ui",
                              Protocollo.class,
                              FormProtocollo.class, // custom form
                              "Protocolli");

        Register.registerForm(db.getEntityManagerFactory(),
                              null,
                              Fascicolo.class,
                              Window.class,
                              "Finestra fascicolo");

        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/pubblicazioni/forms/pubblicazione.ui",
                              Pubblicazione.class,
                              FormPubblicazione.class,
                              "Pubblicazione all'albo");

        Register.registerForm(db.getEntityManagerFactory(),
                              null,
                              Carica.class,
                              Window.class,
                              "Carica");

        Register.registerForm(db.getEntityManagerFactory(),
                              null,
                              Commissione.class,
                              Window.class,
                              "Commissione");

        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/sedute/forms/caricacommissione.ui",
                              CaricaCommissione.class,
                              Window.class,
                              "Carica-commissione");
        
        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/sedute/forms/tiposeduta.ui",
                              TipoSeduta.class,
                              FormTipoSeduta.class,
                              "Tipo seduta");

        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/sedute/forms/seduta.ui",
                              Seduta.class,
                              Window.class,
                              "Seduta");

        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/finanziaria/forms/servizio.ui",
                              Servizio.class,
                              Window.class,
                              "Servizi");

        Register.registerForm(db.getEntityManagerFactory(),
                              null,
                              Capitolo.class,
                              Window.class,
                              "Capitoli");
        
        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/deliberedetermine/forms/determina.ui",
                              Determina.class,
                              FormDetermina.class,
                              "Determine");

        Register.registerForm(db.getEntityManagerFactory(),
                               "classpath:com/axiastudio/suite/deliberedetermine/forms/movimentodetermina.ui",
                               MovimentoDetermina.class,
                               Dialog.class,
                               "Movimento determina");

        Register.registerForm(db.getEntityManagerFactory(),
                              null,
                              Norma.class,
                              Window.class,
                              "Norma");
       
        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/procedimenti/forms/procedimento.ui",
                              Procedimento.class,
                              Window.class,
                              "Procedimenti");
        
        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/procedimenti/forms/delega.ui",
                              Delega.class,
                              FormDelega.class,
                              "Incarichi e deleghe");
        // TODO: filtro sulle deleghe
       try {
            Method provider = GestoreDeleghe.class.getMethod("filtroDelegheUtente", CriteriaBuilder.class, Root.class);
            Register.registerUtility(provider, ICriteriaFactory.class, Delega.class.getName());
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(SuiteDemo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(SuiteDemo.class.getName()).log(Level.SEVERE, null, ex);
        }


        // gestore deleghe
        GestoreDeleghe gestoreDeleghe = new GestoreDeleghe();
        Register.registerUtility(gestoreDeleghe, IGestoreDeleghe.class);        
        
        // Plugin OoopsPlugin per interazione con OpenOffice
        OoopsPlugin ooopsPlugin = new OoopsPlugin();
        ooopsPlugin.setup("uno:socket,host=localhost,port=8100;urp;StarOffice.ServiceManager", false);
        //ooopsPlugin.setup("uno:socket,host=192.168.64.59,port=2002;urp;StarOffice.ServiceManager");
        
        // template demo Determina
        HashMap<String,String> rules = new HashMap();
        rules.put("CODICE", "return Determina.getCodiceInterno();");
        rules.put("CODICE1", "return Determina.getCodiceInterno();");
        rules.put("DIC_IMP_PROVV", "if( Determina.getDiSpesa() ){ return \"Imegno come da allegato per totali euro\"; } else return \"\"");
        rules.put("SPESA", "if( Determina.getDiSpesa() ){ return \"Somma degli importi\"; } else return \"\""); // TODO: completare
        rules.put("PEG_CORRENTE", "return \"PEG approvato con bilancio...\""); // TODO: da strutturare
        rules.put("INDIRIZZO", ""); // TODO: cerca Michela
        rules.put("bar_code", "return Determina.getIdDocumento();");
        rules.put("BAR_COD_PRATICA", "return Determina.getIdPratica();");
        rules.put("OGGETTO", "return Determina.getOggetto();");
        rules.put("FRASE_IMPEGNO", "if( Determina.getDiSpesa() ){ return \"con impegno di spesa\"; } else return \"\"");
        rules.put("OBIETTIVO", ""); // per ora vuoro
        rules.put("ISTRUTTORE", ""); // XXX: istruttore... vedi appunti. :-)
        rules.put("SERVIZIO", "Determina.getServizioDeterminaCollection().toArray()[0].getServizio().getDescrizione()");
        rules.put("REGOLARIZZAZIONE", "if( Determina.getDiRegolarizzazione() ){ return \"frasetta REGOLARIZZAZIONE\"; } else return \"\""); // TODO: recuperare testo
        rules.put("CASO_SPESA", "if( Determina.getDiSpesa() ){ return \"frasetta CASO_SPESA\"; } else return \"\""); // TODO: recuperare testo
        rules.put("DISPOSIZIONI_SPESE", "if( Determina.getDiSpesa() ){ return \"frasetta DISPOSIZIONI_SPESE\"; } else return \"\""); // TODO: recuperare testo
        rules.put("UFFICI_DETER", ""); // TODO: uffici allegati alla determina, tipo attribuzioni con principale (manca nel modello)
        rules.put("ASSESSORE", "return Determina.getReferentePolitico();");
        rules.put("allegati", ""); // TODO: gli allegati sulla determina (titolo), in ordine di data
        rules.put("SIGLA", ""); // "Documento redatto da "+sigla utente autenticato
        rules.put("DELEGA", ""); // "IL" oppure "PER ASSENZA DEL" nel caso chi firmi (resp. servizio) sia un delegato
        rules.put("ufficio", ""); // l'ufficio collegato al servizio di bilancio o il servizio (Michela chiede)?
        rules.put("DELEGA1", ""); // "frasetta" come per delega
        rules.put("FIRMA", ""); // cognome e nome di chi ha firmato come responsabile del servizio
        rules.put("PRATICA", ""); // la pratica o le pratiche da cui dipende la determina: cod_interno+ubicazione
        rules.put("data", ""); // data della determina (e del protocollo)
        rules.put("numero", "return Determina.getAnno()+\" - \"+Determina.getNumero();");
        rules.put("protocollo", "return Determina.getIdDocumento();");
        RuleSet ruleSet = new RuleSet(rules);
        IStreamProvider streamProviderDetermina = new FileStreamProvider("demo/determina.ott");
        Map<String, Object> objectsMap = new HashMap();
        objectsMap.put("gestoreDeleghe", gestoreDeleghe);
        Template template = new Template(streamProviderDetermina, "Determina", "Template determina", ruleSet, objectsMap);
        ooopsPlugin.addTemplate(template);
        
        Register.registerPlugin(ooopsPlugin, FormDetermina.class);
        
        String msg = "";
        msg += "Avvio in modalità dimostrativa.";
        msg += "Login e password:\n";
        msg += "\n";
        msg += "mario / super (utente normale)\n";
        msg += "admin / pypapi (utente amministratore)\n";
        QMessageBox.warning(null, "Modalità demo", msg, QMessageBox.StandardButton.Ok, QMessageBox.StandardButton.Ok);
        /* Selezione openoffice */
        String sofficeUrl = null;
        if( "Mac OS X".equals(System.getProperty("os.name")) ){
            sofficeUrl = "/Applications/OpenOffice.org.app/Contents/MacOS/soffice -accept=socket,host=localhost,port=8100;urp;";
        } else {
            // XXX: da testare!
            String oooDir = QFileDialog.getExistingDirectory(null, "Seleziona la cartella contenente OpenOffice");
            sofficeUrl = oooDir + "/soffice -accept=socket,host=localhost,port=8100;urp;";
        }
        if( sofficeUrl != null ){
        Runtime runtime = Runtime.getRuntime();
            try {
                Process proc = runtime.exec(sofficeUrl);
            } catch (IOException ex) {
                Logger.getLogger(SuiteDemo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        Login login = new Login();
        int res = login.exec();
        if( res == 1 ){
        
            Mdi mdi = new Mdi();
            //mdi.showMaximized();
            mdi.show();
            
            app.setCustomApplicationName("PyPaPi SuitePA - modalità dimostrazione");
            app.setCustomApplicationCredits("Copyright AXIA Studio 2012<br/>");
            app.exec();
        }
        
        System.exit(res);
    }
}
