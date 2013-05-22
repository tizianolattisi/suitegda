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
import com.axiastudio.suite.anagrafiche.AnagraficheAdapters;
import com.axiastudio.suite.anagrafiche.entities.AlboProfessionale;
import com.axiastudio.suite.anagrafiche.entities.Gruppo;
import com.axiastudio.suite.anagrafiche.entities.GruppoSoggetto;
import com.axiastudio.suite.anagrafiche.entities.Indirizzo;
import com.axiastudio.suite.anagrafiche.entities.Relazione;
import com.axiastudio.suite.anagrafiche.entities.RelazioneSoggetto;
import com.axiastudio.suite.anagrafiche.entities.Riferimento;
import com.axiastudio.suite.anagrafiche.entities.Soggetto;
import com.axiastudio.suite.anagrafiche.entities.Stato;
import com.axiastudio.suite.anagrafiche.entities.TitoloSoggetto;
import com.axiastudio.suite.anagrafiche.forms.FormIndirizzo;
import com.axiastudio.suite.anagrafiche.forms.FormQuickInsertSoggetto;
import com.axiastudio.suite.anagrafiche.forms.FormRelazioneSoggetto;
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
import com.axiastudio.suite.generale.entities.Costante;
import com.axiastudio.suite.pratiche.PraticaAdapters;
import com.axiastudio.suite.pratiche.PraticaCallbacks;
import com.axiastudio.suite.pratiche.PraticaPrivate;
import com.axiastudio.suite.pratiche.entities.Dipendenza;
import com.axiastudio.suite.pratiche.entities.DipendenzaPratica;
import com.axiastudio.suite.pratiche.entities.Pratica;
import com.axiastudio.suite.pratiche.entities.TipoPratica;
import com.axiastudio.suite.pratiche.forms.FormDipendenzaPratica;
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
import com.axiastudio.suite.protocollo.entities.Oggetto;
import com.axiastudio.suite.protocollo.entities.PraticaProtocollo;
import com.axiastudio.suite.protocollo.entities.Protocollo;
import com.axiastudio.suite.protocollo.entities.SoggettoProtocollo;
import com.axiastudio.suite.protocollo.entities.SoggettoRiservatoProtocollo;
import com.axiastudio.suite.protocollo.entities.Titolo;
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
        
        // dati demo
        DemoData.initSchema();
        DemoData.initData();
        
        // applicazione Qt
        Application app = new Application(args);
        app.setLanguage("it");
        
        // configurazione SuitePA
        Configure.configure(db);

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
        rules.put("BAR_COD_PRATICA", "return Determina.getIdpratica();");
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
            //sofficeUrl = "/Applications/OpenOffice.org.app/Contents/MacOS/soffice -accept=socket,host=localhost,port=8100;urp;";
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
        login.setWindowTitle("PyPaPi Suite PA (demo)");
        int res = login.exec();
        if( res == 1 ){
        
            Mdi mdi = new Mdi();
            mdi.setWindowTitle("PyPaPi Suite PA (demo)");
            mdi.showMaximized();
            //mdi.show();
            
            app.setCustomApplicationName("PyPaPi SuitePA - modalità dimostrazione");
            app.setCustomApplicationCredits("Copyright AXIA Studio 2013<br/>");
            app.exec();
        }
        
        System.exit(res);
    }
}
