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
package com.axiastudio.suite.protocollo;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.annotations.Callback;
import com.axiastudio.pypapi.annotations.CallbackType;
import com.axiastudio.pypapi.db.Controller;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.pypapi.db.Validation;
import com.axiastudio.suite.SuiteUtil;
import com.axiastudio.suite.anagrafiche.entities.Soggetto;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.base.entities.UfficioUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.generale.entities.Costante;
import com.axiastudio.suite.pratiche.entities.Pratica;
import com.axiastudio.suite.protocollo.entities.*;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class ProtocolloCallbacks {
    
    /*
     * Valida il protocollo e richiede il nuovo iddocumento
     */
    @Callback(type=CallbackType.BEFORECOMMIT)
    public static Validation beforeCommit(Protocollo protocollo){
        Boolean res = true;
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        Boolean eNuovo = protocollo.getId() == null;
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
        Ufficio attribuzionePrincipale = null;
        int nrAttribuzioniPrincipali = 0;
        for( Attribuzione attribuzione: protocollo.getAttribuzioneCollection() ){
            if( attribuzione.getPrincipale() ){
                nrAttribuzioniPrincipali += 1;
                attribuzionePrincipale = attribuzione.getUfficio();
            }
        }
        int nrPraticheOriginali = 0;
        for( PraticaProtocollo praticaProtocollo: protocollo.getPraticaProtocolloCollection()){
            if( praticaProtocollo.getOriginale()){
                nrPraticheOriginali += 1;
            }
        }
        Ufficio sportello = protocollo.getSportello();

        String msg = "";

        if( !eNuovo ){
            /*
             * Modifica permessa solo allo sportello e all'attribuzione principale
             * con flag ricerca
             */
            if( !(uffici.contains(sportello) || ufficiRicerca.contains(attribuzionePrincipale)) ){
                msg += "Devi appartenere allo sportello o all'attribuzione principale\n";
                msg += "con diritti di ricerca, per poter modificare il protocollo.\n";
                res = false;
            }

        } else {
            /*
             * Nuovo inserimento
             */


            /* sportello obbligatorio */
            if( protocollo.getSportello() == null ){
                msg += "Deve essere dichiarato uno sportello ricevente.\n";
                res = false;
            }

            /* sportello tra quelli dell'utente */
            if( !uffici.contains(protocollo.getSportello()) ){
                msg += "Lo sportello deve essere scelto tra gli uffici dell'utente.\n";
                res = false;
            }

            /* almeno un soggetto */
            if( protocollo.getSoggettoProtocolloCollection() == null || protocollo.getSoggettoProtocolloCollection().isEmpty() ){
                if( TipoProtocollo.INTERNO.equals(protocollo.getTipo()) ){
                    Costante costante = SuiteUtil.trovaCostante("SOGGETTO_INTERNI");
                    Long id = Long.parseLong(costante.getValore());
                    Database db = (Database) Register.queryUtility(IDatabase.class);
                    Controller controller = db.createController(Soggetto.class);
                    Soggetto soggetto = (Soggetto) controller.get(id);
                    SoggettoProtocollo sp = new SoggettoProtocollo();
                    sp.setSoggetto(soggetto);
                    List<SoggettoProtocollo> spList = new ArrayList<SoggettoProtocollo>();
                    spList.add(sp);
                    protocollo.setSoggettoProtocolloCollection(spList);
                } else {
                    msg += "Deve essere dichiarato almeno un soggetto esterno (mittente o destinatario).";
                    res = false;
                }
            }
            if( res == false ){
                return new Validation(false, msg);
            }
            /* almeno un ufficio */
            if( protocollo.getUfficioProtocolloCollection() == null || protocollo.getUfficioProtocolloCollection().isEmpty() ){
                msg += "Deve essere dichiarato almeno un ufficio (mittente o destinatario).";
                res = false;
            }

            /* Oggetto non nullo */
            if( protocollo.getOggetto() == null || protocollo.getOggetto().isEmpty() ){
                msg += "Devi compilare l'oggetto.";
                res = false;
            }
        }

        /*
         * Verifica inserimento pratiche: permesso solo se ufficio gestore, eventualmente
         * con flag riservato.
         */
        for( PraticaProtocollo praticaProtocollo: protocollo.getPraticaProtocolloCollection() ){
            if( praticaProtocollo.getProtocollo() == null ){
                /* Nuovo inserimento */
                Pratica pratica = praticaProtocollo.getPratica();
                Ufficio ufficioGestore = pratica.getGestione();
                if( false ){
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
        }

        /*
         * Una sola attribuzione in via principale
         */
        if( nrAttribuzioniPrincipali != 1 ){
            msg += "E' possibile e necessario impostare una sola attribuzione principale.\n";
            res = false;
        }

        /*
         * Una sola pratica in originale
         */
        if( protocollo.getPraticaProtocolloCollection().size() > 0 ){
            if( nrPraticheOriginali != 1 ){
                msg += "Il protocollo può essere inserito come originale in una sola pratica.\n";
                res = false;
            }
        }

        /*
         * I riferimenti precedenti devono essere realmente precedenti
         */
        if( protocollo.getRiferimentoProtocolloCollection() != null ){
            for( RiferimentoProtocollo rp: protocollo.getRiferimentoProtocolloCollection() ){
                if( rp.getPrecedente().getDataprotocollo().after(protocollo.getDataprotocollo()) ){
                    msg += "I protocolli precedenti riferiti non possono avere data successiva al protocollo.\n";
                    res = false;
                    break;
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
