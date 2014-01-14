package com.axiastudio.suite.deliberedetermine;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.annotations.Callback;
import com.axiastudio.pypapi.annotations.CallbackType;
import com.axiastudio.pypapi.db.Validation;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.UfficioUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.deliberedetermine.entities.Determina;

/**
 * User: tiziano
 * Date: 14/01/14
 * Time: 10:36
 */
public class DeterminaCallbacks {

    @Callback(type= CallbackType.BEFORECOMMIT)
    public static Validation validaDetermina(Determina determina){
        String msg = "";
        Boolean res = true;
        Boolean inUfficioGestore = false;
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        // se l'utente non è istruttore non può inserire o modificare pratiche,
        if( !autenticato.getIstruttorepratiche() ){
            msg = "Devi avere come ruolo \"istruttore\" per poter inserire\n";
            msg += "o modificare una determina.";
            return new Validation(false, msg);
        }
        for( UfficioUtente uu: autenticato.getUfficioUtenteCollection() ){
            if( uu.getUfficio().equals(determina.getPratica().getGestione()) && uu.getModificapratica() ){
                // se la pratica è riservata, mi serve anche il flag
                if( !determina.getPratica().getRiservata() || uu.getRiservato() ){
                    inUfficioGestore = true;
                    break;
                }
            }
        }
        // se l'utente non è inserito nell'ufficio gestore con flag modificapratiche non può modificare
        if( !inUfficioGestore ){
            msg = "Per modificare la determina devi appartenere all'ufficio gestore della pratica con i permessi di modifica.";
            return new Validation(false, msg);
        }

        return new Validation(true);
    }

}