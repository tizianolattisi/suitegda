package com.axiastudio.suite.protocollo;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.annotations.Callback;
import com.axiastudio.pypapi.annotations.CallbackType;
import com.axiastudio.pypapi.db.Validation;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.base.entities.UfficioUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.pratiche.entities.Pratica;
import com.axiastudio.suite.protocollo.entities.Attribuzione;
import com.axiastudio.suite.protocollo.entities.PraticaProtocollo;

import java.util.ArrayList;
import java.util.List;

/**
 * User: tiziano
 * Date: 20/05/14
 * Time: 10:55
 */
public class PraticaProtocolloCallbacks {

    @Callback(type= CallbackType.BEFORECOMMIT)
    public static Validation beforeCommit(PraticaProtocollo praticaProtocollo){

        // Nota: FormProtocollo già non permette di "provare" a salvare le singole attribuzioni
        //       se !autenticato.getAttributoreprotocollo()

        Boolean res = true;
        String msg = "";

        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        List<Ufficio> uffici = new ArrayList();
        List<Ufficio> ufficiRicerca = new ArrayList();
        List<Ufficio> ufficiPrivato = new ArrayList();
        for(UfficioUtente uu: autenticato.getUfficioUtenteCollection()){
            uffici.add(uu.getUfficio());
            if( uu.getRicerca() ){
                ufficiRicerca.add(uu.getUfficio());
            }
            if( uu.getRiservato() ){
                ufficiPrivato.add(uu.getUfficio());
            }
        }
        if( praticaProtocollo.getProtocollo() == null ){
                /* Nuovo inserimento */
            Pratica pratica = praticaProtocollo.getPratica();
            Ufficio ufficioGestore = pratica.getGestione();
            boolean permessiSpeciali=false;
            boolean permessiSpecialiR=false;
            String msg1="";
            if (pratica.getDatachiusura()!=null) {
                msg="archiviate ";
            } else {
                if (pratica.getMultiufficio()) {
                    permessiSpeciali=true;
                } else {
                    permessiSpeciali=autenticato.getSupervisorepratiche() || autenticato.getAttributorepratiche();
                }
                permessiSpecialiR=autenticato.getSupervisorepraticheriservate();
                msg1=", o essere un amministratore delle pratiche";
            }
            if( pratica.getRiservata() ){
                    /* TODO: riservato */
                if( !ufficiPrivato.contains(ufficioGestore) && !permessiSpecialiR ){
                    msg = "Per poter inserire pratiche " + msg + "riservate è necessario appartenere al loro ufficio gestore ";
                    msg += "con flag riservato" + msg1 + ".\n";
                    res = false;
                }
            } else {
                if( !uffici.contains(ufficioGestore) && !permessiSpeciali){
                    msg = "Per poter inserire pratiche " + msg + "è necessario appartenere al loro ufficio gestore" + msg1 + ".\n";
                    res = false;
                }
            }
        }
        else {
            if ( praticaProtocollo.getPratica()!=null && praticaProtocollo.getOriginale()) {
                Ufficio principale=null;
                for (Attribuzione attr : praticaProtocollo.getProtocollo().getAttribuzioneCollection()) {
                    if (attr.getPrincipale()) {
                        principale = attr.getUfficio();
                        break;
                    }
                }
                if ( !praticaProtocollo.getPratica().getGestione().equals(principale) ) {
                    msg = "Per poter inserire il protocollo in originale, l'attribuzione principale deve coincidere con l'ufficio gestore della pratica.\n";
                    res = false;
                }
            }
        }

        /*
         * Restituzione della validazione
         */
        if(!res){
            return new Validation(false, msg);
        } else {
            return new Validation(true);
        }

    }

}
