package com.axiastudio.suite.protocollo;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Controller;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.pypapi.db.Validation;
import com.axiastudio.suite.anagrafiche.AnagraficheUtil;
import com.axiastudio.suite.anagrafiche.entities.Soggetto;
import com.axiastudio.suite.base.BaseUtil;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.email.EMail;
import com.axiastudio.suite.protocollo.entities.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User: tiziano
 * Date: 18/03/14
 * Time: 09:00
 */
public class ProtocolloUtil {

    public static Protocollo protocollaEmail(EMail email){

        Protocollo protocollo = new Protocollo();
        protocollo.setTipo(TipoProtocollo.ENTRATA);
        protocollo.setOggetto(email.getSubject());

        // soggetti
        List<SoggettoProtocollo> soggettoProtocolloList = new ArrayList<SoggettoProtocollo>();
        for( String from: email.getFroms() ){
            List<Soggetto> mittenti = AnagraficheUtil.trovaSoggettoDaPec(from);
            for( Soggetto mittente: mittenti ){
                SoggettoProtocollo soggettoProtocollo = new SoggettoProtocollo();
                soggettoProtocollo.setSoggetto(mittente);
                soggettoProtocolloList.add(soggettoProtocollo);
            }
        }
        protocollo.setSoggettoProtocolloCollection(soggettoProtocolloList);

        // sportello
        Ufficio sportello=null;

        // uffici e attribuzioni
        List<UfficioProtocollo> ufficioProtocolloList = new ArrayList<UfficioProtocollo>();
        List<Attribuzione> attribuzioneList = new ArrayList<Attribuzione>();
        Boolean principale = Boolean.FALSE;
        for( String to: email.getTos() ){
            Ufficio ufficio = BaseUtil.trovaUfficioDaPec(to);
            UfficioProtocollo ufficioProtocollo = new UfficioProtocollo();
            ufficioProtocollo.setUfficio(ufficio);
            ufficioProtocolloList.add(ufficioProtocollo);
            Attribuzione attribuzione = new Attribuzione();
            attribuzione.setUfficio(ufficio);
            if( !principale ){
                attribuzione.setPrincipale(true);
                principale = Boolean.TRUE;
            }
            attribuzioneList.add(attribuzione);
            if( sportello==null ){
                protocollo.setSportello(ufficio);
            }
        }
        protocollo.setUfficioProtocolloCollection(ufficioProtocolloList);
        protocollo.setAttribuzioneCollection(attribuzioneList);

        // pratiche
        protocollo.setPraticaProtocolloCollection(new ArrayList<PraticaProtocollo>());


        Database db = (Database) Register.queryUtility(IDatabase.class);
        Controller controller = db.createController(Protocollo.class);
        Validation validation = controller.commit(protocollo);
        if( validation.getResponse() ){
            return protocollo;
        }

        return null;
    }
}
