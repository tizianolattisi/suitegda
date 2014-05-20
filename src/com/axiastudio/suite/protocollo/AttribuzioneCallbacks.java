package com.axiastudio.suite.protocollo;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.annotations.Callback;
import com.axiastudio.pypapi.annotations.CallbackType;
import com.axiastudio.pypapi.db.Validation;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.protocollo.entities.Attribuzione;

/**
 * User: tiziano
 * Date: 20/05/14
 * Time: 10:55
 */
public class AttribuzioneCallbacks {

    @Callback(type= CallbackType.BEFORECOMMIT)
    public static Validation beforeCommit(Attribuzione attribuzione){

        // Nota: FormProtocollo già non permette di "provare" a salvare le singole attribuzioni
        //       se !autenticato.getAttributoreprotocollo()

        Boolean res = true;
        String msg = "";

        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        if( !autenticato.getAttributoreprotocollo() ){
            res = false;
            msg = "Devi essere un utente attributore di protocollo per poter modificare direttamente le attribuzioni.";
        }

        /*
         * Restituzione della validazione
         */
        if( res == false ){
            return new Validation(false, msg);
        } else {
            return new Validation(true);
        }

    }

}
