package com.axiastudio.suite.richieste;

import com.axiastudio.pypapi.annotations.Callback;
import com.axiastudio.pypapi.annotations.CallbackType;
import com.axiastudio.pypapi.db.Validation;
import com.axiastudio.suite.richieste.entities.Richiesta;

/**
 * Created with IntelliJ IDEA.
 * User: Mickey
 * Date: 27/11/13
 * Time: 9.01
 * To change this template use File | Settings | File Templates.
 */
public class RichiestaCallbacks {

    @Callback(type= CallbackType.BEFORECOMMIT)
    public static Validation beforeCommit(Richiesta richiesta){

        if( richiesta.getTesto() == null || richiesta.getTesto().isEmpty() ){
            return new Validation(false, "Devi inserire un testo.");
        }
        if ( richiesta.getDestinatarioUfficioCollection().size() + richiesta.getDestinatarioUtenteCollection().size() <= 0) {
            return new Validation(false, "Devi inserire almeno un destinatario.");
        }

        return new Validation(true);

    }
}
