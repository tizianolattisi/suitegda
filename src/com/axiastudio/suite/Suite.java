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

import com.axiastudio.pypapi.Application;
import com.axiastudio.pypapi.IStreamProvider;
import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.pypapi.plugins.cmis.CmisPlugin;
import com.axiastudio.pypapi.plugins.cmis.CmisStreamProvider;
import com.axiastudio.pypapi.plugins.ooops.FileStreamProvider;
import com.axiastudio.pypapi.plugins.ooops.OoopsPlugin;
import com.axiastudio.pypapi.plugins.ooops.RuleSet;
import com.axiastudio.pypapi.plugins.ooops.Template;
import com.axiastudio.suite.base.Login;
import com.axiastudio.suite.pratiche.forms.FormPratica;
import com.axiastudio.suite.procedimenti.GestoreDeleghe;
import com.axiastudio.suite.procedimenti.IGestoreDeleghe;
import com.axiastudio.suite.protocollo.forms.FormProtocollo;
import com.axiastudio.suite.pubblicazioni.forms.FormPubblicazione;
import com.axiastudio.suite.protocollo.forms.FormScrivania;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class Suite {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        String jdbcUrl = System.getProperty("jdbc.url");
        if( jdbcUrl == null ){
            /* Avvio in modalità demo */
            SuiteDemo.setAndStartDemo(args);        
        }
        String jdbcUser = System.getProperty("jdbc.user");
        String jdbcPassword = System.getProperty("jdbc.password");
        String jdbcDriver = System.getProperty("jdbc.driver");
        String logLevel = System.getProperty("suite.loglevel");
        String cmisUrl = System.getProperty("cmis.url");
        String cmisUser = System.getProperty("cmis.user");
        String cmisPassword = System.getProperty("cmis.password");
        String barcodeDevice = System.getProperty("barcode.device"); // es. Zebra_Technologies_ZTC_GK420t
        String barcodeLanguage = System.getProperty("barcode.language"); // es. ZPL

        Map properties = new HashMap();
        properties.put("javax.persistence.jdbc.url", jdbcUrl);
        if( jdbcUser != null ){
            properties.put("javax.persistence.jdbc.user", jdbcUser);
        }
        if( jdbcPassword != null ){
            properties.put("javax.persistence.jdbc.password", jdbcPassword);
        }
        if( jdbcDriver != null ){
            properties.put("javax.persistence.jdbc.driver", jdbcDriver);
        }
        if( logLevel != null ){
            properties.put("eclipselink.logging.level", logLevel);
            properties.put("eclipselink.logging.parameters", "true");    
        }

        Database db = new Database();
        properties.put("eclipselink.ddl-generation", "");
        db.open("SuitePU", properties);
        Register.registerUtility(db, IDatabase.class);
        
        Application app = new Application(args);
        // aggiungo la localizzazione di Menjazo e imposto a it
        app.addQmFile("classpath:com/axiastudio/menjazo/lang/menjazo_{0}.qm");
        app.setLanguage("it");
        
        app.setConfigItem("barcode.device", barcodeDevice);
        app.setConfigItem("barcode.language", barcodeLanguage);

        Configure.configure(db);
        
        
           
        // Plugin CmisPlugin per accedere ad Alfresco
        CmisPlugin cmisPlugin = new CmisPlugin();
        cmisPlugin.setup("http://localhost:8080/alfresco/service/cmis", "admin", "admin", 
                "/Protocollo/${dataprotocollo,date,yyyy}/${dataprotocollo,date,MM}/${dataprotocollo,date,dd}/${iddocumento}/");
        Register.registerPlugin(cmisPlugin, FormProtocollo.class);
        Register.registerPlugin(cmisPlugin, FormScrivania.class);

        CmisPlugin cmisPluginPubblicazioni = new CmisPlugin();
        cmisPluginPubblicazioni.setup("http://localhost:8080/alfresco/service/cmis", "admin", "admin", 
                "/Pubblicazioni/${inizioconsultazione,date,yyyy}/${inizioconsultazione,date,MM}/${inizioconsultazione,date,dd}/${id}/");
        Register.registerPlugin(cmisPluginPubblicazioni, FormPubblicazione.class);
        
        // Plugin OoopsPlugin per interazione con OpenOffice
        OoopsPlugin ooopsPlugin = new OoopsPlugin();
        ooopsPlugin.setup("uno:socket,host=localhost,port=8100;urp;StarOffice.ServiceManager");
        
        // template da file system
        HashMap<String,String> rules = new HashMap();
        rules.put("oggetto", "return obj.getDescrizione()");
        RuleSet ruleSet = new RuleSet(rules);
        IStreamProvider streamProvider1 = new FileStreamProvider("/Users/tiziano/NetBeansProjects/PyPaPi/plugins/PyPaPiOoops/template/test.ott");
        Template template = new Template(streamProvider1, "Prova", "Template di prova", ruleSet);
        ooopsPlugin.addTemplate(template);
        
        // template da Cmis
        
        HashMap<String,String> rules2 = new HashMap();        
        rules2.put("oggetto", "return obj.getDescrizione()+\", da Alfresco!!\"");
        RuleSet ruleSet2 = new RuleSet(rules2);
        IStreamProvider streamProvider2 = new CmisStreamProvider("http://localhost:8080/alfresco/service/cmis", "admin", "admin", 
                                                                 "workspace://SpacesStore/7b3a2895-51e7-4f2c-9e3d-cf67f7043257");
        Template template2 = new Template(streamProvider2, "Prova 2", "(template proveniente da Alfresco)", ruleSet2);
        ooopsPlugin.addTemplate(template2);
        
                
        Register.registerPlugin(ooopsPlugin, FormPratica.class);

        // gestore deleghe
        GestoreDeleghe gestoreDeleghe = new GestoreDeleghe();
        Register.registerUtility(gestoreDeleghe, IGestoreDeleghe.class);
        
        /* login */
        Login login = new Login();
        login.setWindowTitle("PyPaPi Suite PA");
        int res = login.exec();
        if( res == 1 ){
        
            Mdi mdi = new Mdi();
            mdi.setWindowTitle("PyPaPi Suite PA");
            mdi.showMaximized();
            //mdi.show();
            
            app.setCustomApplicationName("PyPaPi Suite");
            app.setCustomApplicationCredits("Copyright AXIA Studio 2013<br/>");
            app.exec();
        }
        
        System.exit(res);
    
    }
    
}
