package com.axiastudio.suite.richieste;

import com.axiastudio.pypapi.Register;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.menjazo.AlfrescoHelper;
import com.axiastudio.suite.plugins.cmis.CmisPlugin;
import com.axiastudio.suite.richieste.entities.Richiesta;

import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import java.text.SimpleDateFormat;
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
            // copia dei documenti contenuti nella cartella Alfresco definitiva
            String path = "/Siti/richieste/documentLibrary/" +
                    (new SimpleDateFormat("yyyy/MM/")).format(richiesta.getData()) + richiesta.getId().toString() + "/";
            CmisPlugin cmisPlugin = (CmisPlugin) Register.queryPlugin(Richiesta.class, "CMIS");
            AlfrescoHelper alfrescoHelper = cmisPlugin.createAlfrescoHelper(richiesta);
            alfrescoHelper.createFolder(path);
            for (Map map : alfrescoHelper.children()) {
                if ( alfrescoHelper.copyDocument((String) map.get("objectId"), path) != null ) {
                    alfrescoHelper.deleteDocument((String) map.get("objectId"));
                } else {
                    return;
                }
            }
            alfrescoHelper.deleteFolder(alfrescoHelper.folderFromPath(alfrescoHelper.getPath()).getId());
            richiesta.setPathdocumento(null);
        }
    }

}
