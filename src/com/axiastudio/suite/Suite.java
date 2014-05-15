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
import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.suite.base.Login;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class Suite {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Application app = new Application(args);
        String propertiesFile = Suite.class.getResource("suite.properties").getPath();

        configure(app, propertiesFile);
        Database db = (Database) Register.queryUtility(IDatabase.class);

        /* login */
        Login login = new Login();
        login.setWindowTitle("PyPaPi Suite PA");
        int res = login.exec();
        if( res == 1 ){
        
            Mdi mdi = new Mdi();
            mdi.setWindowTitle("PyPaPi Suite PA");
            //mdi.showMaximized();
            mdi.show();

            //Window window = Util.formFromName(Richiesta.class.getName());
            //window.show();

            app.setCustomApplicationName("PyPaPi Suite");
            app.setCustomApplicationCredits("Copyright AXIA Studio 2013<br/>");
            res = app.exec();
        }
        
        System.exit(res);
    
    }

    protected static void configure(Application app, String propertiesFile) {

        String jdbcUrl = null;
        String jdbcUser = null;
        String jdbcPassword = null;
        String jdbcDriver = null;

        String logLevel = null;

        String cmisUrl = null;
        String cmisUser = null;
        String cmisPassword = null;

        String alfrescopathProtocollo = null;
        String alfrescopathPratica = null;
        String alfrescopathPubblicazione = null;

        String barcodeDevice = null;
        String barcodeLanguage = null;

        String oooConnection = null;

        // file di Properties
        Properties properties = new Properties();
        try {
            InputStream inputStream = new FileInputStream(propertiesFile);
            if( inputStream != null ) {
                properties.load(inputStream);

                jdbcUrl = properties.getProperty("jdbc.url");
                jdbcUser = properties.getProperty("jdbc.user");
                jdbcPassword = properties.getProperty("jdbc.password");
                jdbcDriver = properties.getProperty("jdbc.driver");

                logLevel = properties.getProperty("suite.loglevel");

                cmisUrl = properties.getProperty("cmis.url");
                cmisUser = properties.getProperty("cmis.user");
                cmisPassword = properties.getProperty("cmis.password");

                alfrescopathProtocollo = properties.getProperty("alfrescopath.protocollo");
                alfrescopathPratica = properties.getProperty("alfrescopath.pratica");
                alfrescopathPubblicazione = properties.getProperty("alfrescopath.pubblicazione");

                barcodeDevice = properties.getProperty("barcode.device"); // es. Zebra_Technologies_ZTC_GK420t
                barcodeLanguage = properties.getProperty("barcode.language"); // es. ZPL

                oooConnection = properties.getProperty("ooo.connection");

            }
        } catch (IOException e) {

        }

        /*
         *  OVerriding della configurazione
         */

        // jdbc
        if( System.getProperty("jdbc.url") != null ) {
            jdbcUrl = System.getProperty("jdbc.url");
        }
        if( System.getProperty("jdbc.user") != null ) {
            jdbcUser = System.getProperty("jdbc.user");
        }
        if( System.getProperty("jdbc.password") != null ) {
            jdbcPassword = System.getProperty("jdbc.password");
        }
        if( System.getProperty("jdbc.driver") != null ) {
            jdbcDriver = System.getProperty("jdbc.driver");
        }
        // log
        if( System.getProperty("suite.loglevel") != null ) {
            logLevel = System.getProperty("suite.loglevel");
        }
        // Alfresco
        if( System.getProperty("cmis.url") != null ) {
            cmisUrl = System.getProperty("cmis.url");
        }
        if( System.getProperty("cmis.user") != null ) {
            cmisUser = System.getProperty("cmis.user");
        }
        if( System.getProperty("cmis.password") != null ) {
            cmisPassword = System.getProperty("cmis.password");
        }
        if( System.getProperty("alfrescopath.protocollo") != null ) {
            alfrescopathProtocollo = System.getProperty("alfrescopath.protocollo");
        }
        if( System.getProperty("alfrescopath.pratica") != null ) {
            alfrescopathPratica = System.getProperty("alfrescopath.pratica");
        }
        if( System.getProperty("alfrescopath.pubblicazione") != null ) {
            alfrescopathPubblicazione = System.getProperty("alfrescopath.pubblicazione");
        }
        // OpenOffice
        if( System.getProperty("ooo.connection") != null ) {
            oooConnection = System.getProperty("ooo.connection");
        }
        // i parametri ATM non possono essere passati come parametro
        // Stampante etichette
        if( System.getProperty("barcode.device") != null ) {
            barcodeDevice = System.getProperty("barcode.device");
        }
        if( System.getProperty("barcode.language") != null ) {
            barcodeLanguage = System.getProperty("barcode.language");
        }

        Map mapProperties = new HashMap();
        mapProperties.put("javax.persistence.jdbc.url", jdbcUrl);
        if( jdbcUser != null ){
            mapProperties.put("javax.persistence.jdbc.user", jdbcUser);
        }
        if( jdbcPassword != null ){
            mapProperties.put("javax.persistence.jdbc.password", jdbcPassword);
        }
        if( jdbcDriver != null ){
            mapProperties.put("javax.persistence.jdbc.driver", jdbcDriver);
        }
        if( logLevel != null ){
            mapProperties.put("eclipselink.logging.level", logLevel);
            mapProperties.put("eclipselink.logging.parameters", "true");
        }

        Database db = new Database();
        mapProperties.put("eclipselink.ddl-generation", "");
        db.open("SuitePU", mapProperties);
        Register.registerUtility(db, IDatabase.class);

        // applicazione Qt
        app.addQmFile("classpath:com/axiastudio/menjazo/lang/menjazo_{0}.qm"); // menjazo
        app.setLanguage("it");

        // jdbc
        app.setConfigItem("jdbc.url", jdbcUrl);

        // barcode
        app.setConfigItem("barcode.device", barcodeDevice);
        app.setConfigItem("barcode.language", barcodeLanguage);

        // Alfresco
        app.setConfigItem("cmis.url", cmisUrl);
        app.setConfigItem("cmis.user", cmisUser);
        app.setConfigItem("cmis.password", cmisPassword);
        app.setConfigItem("alfrescopath.protocollo", alfrescopathProtocollo);
        app.setConfigItem("alfrescopath.pratica", alfrescopathPratica);
        app.setConfigItem("alfrescopath.pubblicazione", alfrescopathPubblicazione);

        // scringa di connessione per OpenOffice
        app.setConfigItem("ooops.connection", oooConnection);
        //app.setConfigItem("ooops.connection", "uno:socket,host=192.168.64.56,port=2002;urp;StarOffice.ServiceManager");

        // configurazione originale SuitePA
        Configure.configure(db);

    }
    
}
