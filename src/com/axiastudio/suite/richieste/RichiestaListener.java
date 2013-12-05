package com.axiastudio.suite.richieste;

import com.axiastudio.pypapi.Register;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.richieste.entities.Richiesta;

import javax.persistence.PrePersist;

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
    }

}
