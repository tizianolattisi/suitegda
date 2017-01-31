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
package com.axiastudio.suite.anagrafiche;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.annotations.Callback;
import com.axiastudio.pypapi.annotations.CallbackType;
import com.axiastudio.pypapi.db.Validation;
import com.axiastudio.suite.anagrafiche.entities.Soggetto;
import com.axiastudio.suite.anagrafiche.entities.TipoSoggetto;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Utente;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class SoggettoCallbacks {
    
    /*
     * Valida il protocollo e richiede il nuovo iddocumento
     */
    @Callback(type=CallbackType.BEFORECOMMIT)
    public static Validation beforeCommit(Soggetto soggetto){
        Boolean res = true;
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        String msg = "";

        /* inserimento nuove anagrafiche permesso solo agli utenti con flag "supervisore anagrafiche" */
        if ( soggetto.getId() == null && !autenticato.getSupervisoreanagrafiche() ) {
            msg += "Utente non abilitato per l'inserimento delle anagrafiche.\n";
            res = false;
        }

        /* denominazioni obbligatorie */
        if( soggetto.getTipo().equals(TipoSoggetto.PERSONA) && (soggetto.getCognome() == null || soggetto.getNome() == null ||
                soggetto.getCognome().trim().equals("") || soggetto.getNome().trim().equals("")) ){
            msg += "Deve essere indicato il cognome e nome della persona.\n";
            res = false;
        } else if ( soggetto.getTipo().equals(TipoSoggetto.AZIENDA) && (soggetto.getRagionesociale() == null ||
                soggetto.getRagionesociale().trim().equals("")) ) {
            msg += "Deve essere indicata la ragione sociale dell'azienda.\n";
            res = false;
        } else if ( soggetto.getTipo().equals(TipoSoggetto.ENTE) && (soggetto.getDenominazione() == null ||
                soggetto.getDenominazione().trim().equals("")) ){
            msg += "Deve essere indicata la denominazione principale dell'ente.\n";
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
