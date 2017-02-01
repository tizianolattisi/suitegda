package com.axiastudio.suite.procedimenti;

import com.axiastudio.pypapi.annotations.Callback;
import com.axiastudio.pypapi.annotations.CallbackType;
import com.axiastudio.pypapi.db.Validation;
import com.axiastudio.suite.procedimenti.entities.Delega;

public class DelegaCallbacks {
    @Callback(type= CallbackType.BEFORECOMMIT)
    public static Validation beforeCommit(Delega delega){

        Boolean res = true;
        String msg = "";

        if( !(delega.getTitolare() || delega.getDelegato() || delega.getImpedimento()) ){
            msg += "L'utente inserito deve essere titolare o delegato per la carica specificata oppure in sostituzione causa impedimento.";
            res = false;
        }
        if ( delega.getDelegato() && delega.getDelegante()==null ) {
            msg += "L'utente inserito è delegato. Indicare il titolare della carica.";
            res = false;
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
