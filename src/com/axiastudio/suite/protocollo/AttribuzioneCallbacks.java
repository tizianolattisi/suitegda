package com.axiastudio.suite.protocollo;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.annotations.Callback;
import com.axiastudio.pypapi.annotations.CallbackType;
import com.axiastudio.pypapi.db.Validation;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.UfficioUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.protocollo.entities.Attribuzione;
import com.axiastudio.suite.protocollo.entities.Protocollo;

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
            // non sono attributore protocollo, quindi posso solo committare la singola attribuzione per dare per letto
            if( autenticato.getId() == null ){
                // attribuzione nuova
                res = false;
            } else {
                Protocollo protocollo = attribuzione.getProtocollo();
                if( !attribuzione.getSnapshot().getPrincipale().equals(attribuzione.getPrincipale()) ){
                    // cambio attribuzione principale
                    res = false;
                }
                if( !attribuzione.getSnapshot().getLetto() && attribuzione.getLetto() ){
                    // sto dando per letto
                    for( UfficioUtente uu: autenticato.getUfficioUtenteCollection() ){
                        if( uu.getUfficio().equals(attribuzione.getUfficio()) && uu.getDaiperletto() ){
                            // e hai il permesso per farlo
                            res = true;
                        }
                    }
                }
            }
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
