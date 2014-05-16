package com.axiastudio.suite.pubblicazioni;

import com.axiastudio.menjazo.AlfrescoHelper;
import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Controller;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.suite.plugins.cmis.CmisPlugin;
import com.axiastudio.suite.protocollo.entities.Protocollo;
import com.axiastudio.suite.pubblicazioni.entities.Pubblicazione;
import com.axiastudio.suite.pubblicazioni.entities.TipoAttoPubblicazione;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * User: tiziano
 * Date: 27/01/14
 * Time: 11:15
 */
public class PubblicazioneUtil {

    public static Pubblicazione pubblicaProtocollo(Protocollo protocollo) {

        Database db = (Database) Register.queryUtility(IDatabase.class);
        Controller controllerPubblicazione = db.createController(Pubblicazione.class);
        Controller controllerTAP = db.createController(TipoAttoPubblicazione.class);

        TipoAttoPubblicazione delibere = (TipoAttoPubblicazione) controllerTAP.get(1L);

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();

        Pubblicazione pubblicazione = new Pubblicazione();
        pubblicazione.setDescrizione(protocollo.getOggetto());
        if( protocollo.getDataatto() != null ) {
            pubblicazione.setDataatto(protocollo.getDataatto());
        } else {
            pubblicazione.setDataatto(protocollo.getDataprotocollo());
        }
        if( protocollo.getNumeroatto() != null ){
            pubblicazione.setNumeroatto(protocollo.getNumeroatto());
        }

        // defaults
        pubblicazione.setDurataconsultazione(10);
        pubblicazione.setTipoattopubblicazione(delibere);
        pubblicazione.setOrgano("");

        controllerPubblicazione.commit(pubblicazione);

        CmisPlugin cmisPluginPubblicazione = (CmisPlugin) Register.queryPlugin(Pubblicazione.class, "CMIS");
        AlfrescoHelper helperPubblicazione = cmisPluginPubblicazione.createAlfrescoHelper(pubblicazione);
        helperPubblicazione.createFolder();
        String path = helperPubblicazione.getPath();

        CmisPlugin cmisPluginProtocollo = (CmisPlugin) Register.queryPlugin(Protocollo.class, "CMIS");
        AlfrescoHelper helperProtocollo = cmisPluginProtocollo.createAlfrescoHelper(protocollo);



        for( Map map: helperProtocollo.children() ){
            helperProtocollo.copyDocument((String) map.get("objectId"), path);
        }

        pubblicazione.setProtocollo(protocollo);

        return pubblicazione;

    }
}
