package com.axiastudio.suite.pubblicazioni;

import com.axiastudio.pypapi.Application;
import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Controller;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.suite.plugins.cmis.DocerPlugin;
import com.axiastudio.suite.protocollo.entities.Protocollo;
import com.axiastudio.suite.pubblicazioni.entities.Pubblicazione;
import com.axiastudio.suite.pubblicazioni.entities.TipoAttoPubblicazione;
import it.tn.rivadelgarda.comune.gda.docer.DocerHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import it.tn.rivadelgarda.comune.gda.docer.keys.MetadatiDocumento.TIPO_COMPONENTE_VALUES;

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
//        if ( protocollo.getNumeroatto()==null ) {
//            pubblicazione.setNumeroatto(Integer.parseInt(protocollo.getIddocumento().substring(5)));
//        } else {
//            pubblicazione.setNumeroatto(protocollo.getNumeroatto());
//        }
        pubblicazione.setNumeroatto(Integer.parseInt(protocollo.getIddocumento().substring(5)));

        // defaults
        pubblicazione.setDurataconsultazione(10);
        pubblicazione.setTipoattopubblicazione(delibere);
        pubblicazione.setOrgano("");
        pubblicazione.setProtocollo(protocollo);

        controllerPubblicazione.commit(pubblicazione);
        String pubblicazioneExternalId = "pubblicazione_" + pubblicazione.getId();

//        DocerPlugin docerPlugin = (DocerPlugin) Register.queryPlugin(Pubblicazione.class, "DocER");
//        DocerHelper docerHelper = docerPlugin.createDocerHelper(pubblicazione);
        Application app = Application.getApplicationInstance();
        DocerHelper docerHelper = new DocerHelper((String)app.getConfigItem("docer.url"), (String) app.getConfigItem("docer.username"),
                (String) app.getConfigItem("docer.password"));

        String protocolloExternalId = "protocollo_" + protocollo.getId();
        try {
            List<Map<String, String>> documents = docerHelper.searchDocumentsByExternalIdFirstAndRelated(protocolloExternalId);
            for( Map<String, String> doc: documents){
                byte[] bytes = docerHelper.getDocument(doc.get("DOCNUM"), "1");
                TIPO_COMPONENTE_VALUES tipoComponente;
                if( "PRINCIPALE".equals(doc.get("TIPO_COMONENTE")) ){
                    tipoComponente = TIPO_COMPONENTE_VALUES.PRINCIPALE;
                } else if( "ALLEGATO".equals(doc.get("TIPO_COMONENTE")) ){
                    tipoComponente = TIPO_COMPONENTE_VALUES.ALLEGATO;
                } else {
                    continue;
                }
                docerHelper.createDocumentTypeDocumentoAndRelateToExternalId(
                        doc.get("DOCNAME"),
                        bytes,
                        tipoComponente,
                        doc.get("ABSTRACT"),
                        pubblicazioneExternalId
                        );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pubblicazione;
    }
}
