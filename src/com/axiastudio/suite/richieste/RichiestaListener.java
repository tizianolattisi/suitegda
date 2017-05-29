package com.axiastudio.suite.richieste;

import com.axiastudio.pypapi.Application;
import com.axiastudio.pypapi.Register;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.plugins.cmis.DocerPlugin;
import com.axiastudio.suite.richieste.entities.Richiesta;
import it.tn.rivadelgarda.comune.gda.docer.DocerHelper;

import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Mickey
 * Date: 26/11/13
 * Time: 17.59
 * To change this template use File | Settings | File Templates.
 */
public class RichiestaListener {

    @PrePersist
    void prePersist(Richiesta richiesta) {
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        richiesta.setMittente(autenticato);
        if ( (richiesta.getRichiestaPraticaCollection()!=null && !richiesta.getRichiestaPraticaCollection().isEmpty()) ||
                (richiesta.getRichiestaProtocolloCollection()!=null && !richiesta.getRichiestaProtocolloCollection().isEmpty()) ) {
            richiesta.setCancellabile(Boolean.FALSE);
        }
    }

    @PostPersist
    void postPersist(Richiesta richiesta) {
        if ( richiesta.getVarPathDocumento()!=null && richiesta.getVarPathDocumento().length()>0 ) {
            // modifica del metadato EXTERNAL_ID
//            DocerPlugin docerPlugin = (DocerPlugin) Register.queryPlugin(Richiesta.class, "DocER");
//            DocerHelper docerHelper = docerPlugin.createDocerHelper(richiesta);
            Application app = Application.getApplicationInstance();
            DocerHelper docerHelper = new DocerHelper((String)app.getConfigItem("docer.url"), (String) app.getConfigItem("docer.username"),
                    (String) app.getConfigItem("docer.password"));
            String richiestaExternalId="richiesta_" + richiesta.getId();
            List<Map<String, String>> documents=new ArrayList<Map<String, String>>();
            try {
                documents = docerHelper.searchDocumentsByExternalIdFirstAndRelated(richiesta.getPathdocumento());
            } catch (Exception e) {
                e.printStackTrace();
            }
            for ( Map<String, String> doc:documents ) {
                try {
                    docerHelper.updateProfileDocumentExternalId(doc.get("DOCNUM"), richiestaExternalId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            richiesta.setPathdocumento(null);
        }
    }

}
