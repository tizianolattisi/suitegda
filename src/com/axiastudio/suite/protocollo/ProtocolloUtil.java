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
package com.axiastudio.suite.protocollo;

import com.axiastudio.pypapi.Application;
import com.axiastudio.pypapi.db.Validation;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.plugins.docer.DocerUtil;
import com.axiastudio.suite.protocollo.entities.*;
import it.tn.rivadelgarda.comune.gda.docer.DocerHelper;
import it.tn.rivadelgarda.comune.gda.docer.MetadatiHelper;
import it.tn.rivadelgarda.comune.gda.docer.keys.MetadatiDocumento;
import org.apache.chemistry.opencmis.commons.impl.json.JSONArray;
import org.apache.chemistry.opencmis.commons.impl.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class ProtocolloUtil {

    public static String urlDocumentiDocER(Protocollo protocollo, Utente autenticato){
        ProfiloUtenteProtocollo pup = new ProfiloUtenteProtocollo(protocollo, autenticato);

        // creazione dei flag con i permessi di accesso alla folder
        Boolean view = false;
        Boolean delete = false;
        Boolean download = false;
        Boolean parent = false;
        Boolean upload = false;
        Boolean version = false;
// gli utenti 'supervisore protocollo' possono vedere tutti i documenti; gli utenti 'ricercatore protocollo'
//    solo i documenti non riservati
        if( protocollo.getRiservato() ){
            view = download = (pup.inSportelloOAttribuzioneV() && pup.inSportelloOAttribuzioneR()) || autenticato.getSupervisoreprotocollo();
        } else {
            view = download = autenticato.getRicercatoreprotocollo() || autenticato.getSupervisoreprotocollo() || pup.inSportelloOAttribuzioneV();
        }
        // gli utenti che non sono operatori protocollo non possono aggiungere/versionare/cancellare file
        if( !autenticato.getOperatoreprotocollo() ) {
            upload = delete = version = false;
        } else {
            if( protocollo.getConsolidadocumenti() ){
                delete = upload = version = false;
                if (protocollo.getTiporiferimentomittente() != null && "PEC".equals(protocollo.getTiporiferimentomittente().getDescrizione()) &&
                        (protocollo.getPecProtocollo() == null || protocollo.getPecProtocollo().getStato() == null ||
                                protocollo.getPecProtocollo().getStato().compareTo(StatoPec.DAINVIARE) > 0)) {
                    version = false;
                } else {
                    version = pup.inAttribuzionePrincipaleC();
                }
                upload = pup.inAttribuzionePrincipaleC() && autenticato.getNuovodocsuconsolidato();
            } else {
                upload = pup.inSportelloOAttribuzionePrincipale();
                delete = version = upload;
            }
        }
        // apertura folder
        if( view ) {
            // watermark json
            JSONArray watermarkJson=DocerUtil.openWithStamp(protocollo, new ArrayList<String>(Arrays.asList("PROTOCOLLO", "COPIA_CONFORME")));

            // acl json
            JSONObject aclJson = new JSONObject();
            String riservato="";
            if ( protocollo.getRiservato() ) {
                riservato="R";
            }
            for (Attribuzione attribuzione : protocollo.getAttribuzioneCollection()) {
                Integer access;
                if (attribuzione.getPrincipale()) {
                    access = 0; // Full (read+write+delete)
                } else {
                    access = 2; // Read only
                }
                aclJson.put(attribuzione.getUfficio().getId().toString()+riservato, access);
            }
            aclJson.put(protocollo.getSportello().getId().toString()+riservato, 0);
            aclJson.put("0"+riservato, 2);  // gruppo utenti che possono vedere tutto

            String url = "#?externalId=protocollo_" + protocollo.getId();
            url += "&iddocumento=" + protocollo.getIddocumento();
            url += "&dataprotocollo=" + protocollo.getDataprotocollo();
            String codiceinterno = "";
            for (PraticaProtocollo pratica : protocollo.getPraticaProtocolloCollection()) {
                if (pratica.getOriginale()) {
                    codiceinterno = pratica.getPratica().getCodiceinterno();
                }
            }
            if ( codiceinterno!="" ) {
                url += "&codiceinterno=" + codiceinterno;
            }
            url += "&utente=" + autenticato.getLogin();
            String flags = "";
            for (Boolean flag : Arrays.asList(view, delete, download, parent, upload, version)) {
                flags += flag ? "1" : "0";
            }
            url += "&profile=" + flags;
            if (protocollo.getTipoarchivio() != null && !protocollo.getTipoarchivio().equals("")) {
                url += "&archiveType=" + protocollo.getTipoarchivio();
            } else {
                url += "&archiveType=" + TipoArchivio.PAPER;
            }
            url += "&stamp=" + watermarkJson.toJSONString();
            url += "&acls=" + aclJson.toJSONString();
            return url;
        } else {
            return "";
        }
    }

    public static Validation modificaAttributoDocER(Protocollo protocollo) {
        Validation val=new Validation(true);

        Application app = Application.getApplicationInstance();
        DocerHelper docerHelper = new DocerHelper((String)app.getConfigItem("docer.url"), (String) app.getConfigItem("docer.username"),
                (String) app.getConfigItem("docer.password"));
        List<Map<String, String>> documents=new ArrayList<Map<String, String>>();
        try {
            documents = docerHelper.searchDocumentsByExternalIdFirstAndRelated("protocollo_"+protocollo.getId());
        } catch (Exception e) {
            val.setResponse(false);
            val.setMessage(e.getMessage());
            e.printStackTrace();
        }
        MetadatiHelper<MetadatiDocumento> metadata = MetadatiHelper.build(MetadatiDocumento.ARCHIVE_TYPE,
                protocollo.getTipoarchivio().toString());
        for ( Map<String, String> doc:documents ) {
            try {
                if (!docerHelper.updateProfileDocument(doc.get("DOCNUM"), metadata)) {
                    val.setResponse(false);
                    val.setMessage("Errore nella modifica del tipo di documento (cartaceo/digitale).");
                    return val;
                }
            } catch (Exception e) {
                val.setResponse(false);
                val.setMessage(e.getMessage());
                e.printStackTrace();
            }
        }
        return val;
    }
}


