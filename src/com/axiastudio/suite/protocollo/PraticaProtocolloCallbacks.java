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
            if( pratica.getRiservata() ){
                    /* TODO: riservato */
                if( !ufficiPrivato.contains(ufficioGestore) && !autenticato.getSupervisorepratiche() ){
                    msg += "Per poter inserire pratiche riservate è necessario appartenere al loro ufficio gestore\n";
                    msg += "con flag riservato, o essere un amministratore delle pratiche.\n";
                    res = false;
                }
            } else {
                if( !uffici.contains(ufficioGestore) && !autenticato.getSupervisorepratiche() ){
                    msg += "Per poter inserire pratiche è necessario appartenere al loro ufficio gestore,\n";
                    msg += "o essere un amministratore delle pratiche.\n";
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
