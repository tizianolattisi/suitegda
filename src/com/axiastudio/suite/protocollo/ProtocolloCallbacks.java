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
import com.axiastudio.suite.plugins.docer.DocerUtil;
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

            /* Oggetto non nullo */
            Costante costante = SuiteUtil.trovaCostante("LMIN_OGGETTO_PROTOCOLLO");
            Integer len = Integer.parseInt(costante.getValore());
            if( protocollo.getOggetto() == null || protocollo.getOggetto().trim().length()<len ){
                msg += "Devi compilare l'oggetto oppure oggetto troppo corto.";
                res = false;
            }

            /* Consolida del documento -> protocollazione in Doc/ER */
            if ( protocollo.getConsolidadocumenti() ) {
                if (!DocerUtil.protocollaDocumentiDocer(protocollo)) {
                    msg += "Errore nell'aggiornamento dello stato in Doc/ER. Consolida dei documenti annullata.";
                    res = false;
                }
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
            if(!res){
                return new Validation(false, msg);
            }
            /* almeno un ufficio */
            if( protocollo.getUfficioProtocolloCollection() == null || protocollo.getUfficioProtocolloCollection().isEmpty() ){
                msg += "Deve essere dichiarato almeno un ufficio (mittente o destinatario).";
                res = false;
            }

            /* Oggetto non nullo */
            Costante costante = SuiteUtil.trovaCostante("LMIN_OGGETTO_PROTOCOLLO");
            Integer len = Integer.parseInt(costante.getValore());
            if( protocollo.getOggetto() == null || protocollo.getOggetto().trim().length()<len ){
                msg += "Devi compilare l'oggetto oppure oggetto troppo corto.";
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
                if( pratica.getRiservata() ){
                    if( !ufficiPrivato.contains(ufficioGestore) && !autenticato.getSupervisorepratiche() ){
                        msg += "Per poter inserire pratiche riservate è necessario appartenere al loro ufficio gestore\n";
                        msg += "con flag riservato.\n";
                        res = false;
                    }
                } else {
                    if( !uffici.contains(ufficioGestore) && !autenticato.getSupervisorepratiche() && !autenticato.getAttributorepratiche()){
                        msg += "Per poter inserire pratiche è necessario appartenere al loro ufficio gestore,\n";
                        msg += "o essere un amministratore delle pratiche.\n";
                        res = false;
                    }
                }
            } else {
                if( praticaProtocollo.getOggettoModificato() || praticaProtocollo.getOriginaleModificato() ) {
                /* Modifica oggetto o originale */
                    Pratica pratica = praticaProtocollo.getPratica();
                    Ufficio ufficioGestore = pratica.getGestione();
                    Ufficio ufficioAttribuzione = pratica.getAttribuzione();
                    Ufficio ufficioUbicazione = pratica.getUbicazione();
                    if (praticaProtocollo.getOggettoModificato()) {
                        if( !uffici.contains(ufficioGestore) && !uffici.contains(ufficioAttribuzione) && !uffici.contains(ufficioUbicazione) &&
                                !autenticato.getSupervisorepratiche() && !autenticato.getAttributorepratiche()){
                            msg += "Per poter modificare l'oggetto di una pratica è necessario appartenere al suo ufficio gestore,\n";
                            msg += "attribuzione, o ubicazione, oppure avere permessi speciali sulle pratiche.\n";
                            res = false;
                        }
                    }
                    if (praticaProtocollo.getOriginaleModificato()) {
                        if( !uffici.contains(ufficioGestore) && !uffici.contains(ufficioAttribuzione) && !uffici.contains(ufficioUbicazione) &&
                                !autenticato.getSupervisorepratiche() && !autenticato.getAttributorepratiche() ){
                            msg += "Per poter modificare l'attributo 'originale' di una pratica pratiche è necessario appartenere al suo ufficio gestore,\n";
                            msg += "attribuzione, ubicazione, oppure oppure avere permessi speciali sulle pratiche.\n";
                            res = false;
                        }
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
            if( nrPraticheOriginali < 1 ){
                msg += "Il protocollo deve essere inserito come originale in una pratica.\n";
                res = false;
            } else if( nrPraticheOriginali > 1 ){
                msg += "Il protocollo può essere inserito come originale in una sola pratica.\n";
                res = false;
            }
        }

        /*
         * I riferimenti precedenti devono essere realmente precedenti
         */
        if( protocollo.getRiferimentoProtocolloCollection() != null ){
            for( RiferimentoProtocollo rp: protocollo.getRiferimentoProtocolloCollection() ){
                if( protocollo.getDataprotocollo()!=null && !rp.getPrecedente().getDataprotocollo().before(protocollo.getDataprotocollo()) ){
                    msg += "I protocolli precedenti riferiti non possono avere data uguale o successiva al protocollo.\n";
                    res = false;
                    break;
                }
            }
        }

        /*
         * Inserimento del testo di default della PEC, se mancante
         */
        if ( protocollo.getTiporiferimentomittente()!= null &&
                "PEC".equals(protocollo.getTiporiferimentomittente().getDescrizione()) ) {
            if (protocollo.getPecProtocollo() == null) {
                PecProtocollo pecProtocollo = new PecProtocollo();
                pecProtocollo.setProtocollo(protocollo);
                protocollo.setPecProtocollo(pecProtocollo);
            }
            if ( protocollo.getPecProtocollo().getBody()==null || protocollo.getPecProtocollo().getBody().isEmpty() ) {
                protocollo.getPecProtocollo().setBody("Buongiorno,\n" +
                        "in allegato si trasmette quanto in oggetto.\n" +
                        "Cordiali saluti.");
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

    @Callback(type=CallbackType.AFTERCOMMIT)
    public static Validation afterCommit(Protocollo protocollo) {
        if ( protocollo.hasChanged() ) {
            protocollo.reset();
            return ProtocolloUtil.modificaAttributoDocER(protocollo);
        }
        return new Validation(true);
    }

}
