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
package com.axiastudio.suite.pratiche;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.annotations.Callback;
import com.axiastudio.pypapi.annotations.CallbackType;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.pypapi.db.Validation;
import com.axiastudio.suite.base.BaseUtil;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.pratiche.entities.DipendenzaPratica;
import com.axiastudio.suite.pratiche.entities.Pratica;
import com.axiastudio.suite.procedimenti.entities.TipoPraticaProcedimento;
import com.axiastudio.suite.protocollo.ProfiloUtenteProtocollo;
import com.axiastudio.suite.protocollo.entities.Attribuzione;
import com.axiastudio.suite.protocollo.entities.PraticaProtocollo;
import com.axiastudio.suite.protocollo.entities.Protocollo;

import javax.persistence.EntityManager;
import java.util.List;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class PraticaCallbacks {
    
    @Callback(type=CallbackType.BEFORECOMMIT)
    public static Validation validaPratica(Pratica pratica){
        String msg = "";
        Boolean res = true;
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        Boolean inUfficioGestore = PraticaUtil.utenteInGestorePraticaMod(pratica, autenticato);

        // se l'utente non è istruttore non può inserire o modificare pratiche,
        if( !autenticato.getIstruttorepratiche() ){
            msg = "Devi avere come ruolo \"istruttore\" per poter inserire\n";
            msg += "o modificare una pratica.";
            return new Validation(false, msg);
        }
                        
        // devono essese definite attribuzione e tipologia
        if( pratica.getAttribuzione() == null ){
            msg = "Devi selezionare un'attribuzione.";
            return new Validation(false, msg);
        } else if( pratica.getTipo() == null ){
            msg = "Devi selezionare un tipo di pratica.";
            return new Validation(false, msg);
        }

        
        if( pratica.getId() == null ){
            Database db = (Database) Register.queryUtility(IDatabase.class);
            EntityManager em = db.getEntityManagerFactory().createEntityManager();
            /* controllo attribuzione - tipo pratica */
            List ids = em.createNamedQuery("trovaTipiPraticaPermessiDaAttribuzioni", TipoPraticaProcedimento.class)
                                      .setParameter("id", pratica.getAttribuzione().getId())
                                      .getResultList();
            if( !ids.contains(pratica.getTipo().getId()) ){
                msg = "Manca corrispondenza tra l'attribuzione e la tipologia di pratica.";
                return new Validation(false, msg);
            }

            // se codifica pratica non ha progressivo, si controlla se la pratica non esiste già
            if (!PraticaUtil.codificaInternaUnivoca(pratica.getTipo())) {
                msg = "Esiste già una pratica con la codifica specificata.";
                return new Validation(false, msg);
            }

            // TODO: da eliminare quando si inserirà il controllo in nella finestra di inserimento/modifica delle codifiche
            String codifica = PraticaUtil.creaCodificaInterna(pratica.getTipo());
            if (codifica == null) {
                msg = "Errore nella creazione della codifica della pratica.";
                return new Validation(false, msg);
            } else if (codifica.equals("Codifica errata")) {
                msg = "È stata trovata una codifica anomala per la tipologia di pratica selezionata. Calcolo del progressivo non riuscito." +
                        " Contattare l'assistenza.";
                return new Validation(false, msg);
            }

            // verifica esistenza di 1! procedimento collegato alla tipologia di pratica
            if ( pratica.getTipo().getTipopraticaProcedimentoCollection() == null ||
                        pratica.getTipo().getTipopraticaProcedimentoCollection().size() == 0 ) {
                msg = "Nessun procedimento collegato alla codifica selezionata. Inserimento annullato.";
                return new Validation(false, msg);
            } else if ( pratica.getTipo().getTipopraticaProcedimentoCollection().size() > 1 ) {
                msg = "Esiste più di un procedimento collegato alla codifica selezionata. Inserimento annullato.";
                return new Validation(false, msg);
            }

        } else {
            // l'amministratore pratiche modifica anche se non appartenente all'ufficio gestore e
            // anche se la pratica è archiviata.
            if( !autenticato.getSupervisorepratiche() ){
                // se l'utente non è inserito nell'ufficio gestore con flag modificapratiche non può modificare
                if( !inUfficioGestore ){
                    msg = "Per modificare la pratica devi appartenere all'ufficio gestore con i permessi di modifica, ed eventuali privilegi sulle pratiche riservate.";
                    return new Validation(false, msg);
                }
                // impossibile togliere gli uffici
                if( pratica.getGestione() == null || pratica.getAttribuzione() == null || pratica.getUbicazione() == null){
                    msg = "Non è permesso rimuovere attribuzione, gestione o ubicazione.";
                    return new Validation(false, msg);
                }
                // Se la pratica è archiviata, non posso modificarla, ma ciò viene implementato con il cambio di ufficio gestore
            }
        }

        /*
         * Verifica inserimento protocollo in pratica: permesso solo all'ufficio gestore (già sopra),
         * e solo se l'utente ha piena visibilità del protocollo (sportello o attribuzione o superutente protocollo)
         */
        for( PraticaProtocollo praticaProtocollo: pratica.getPraticaProtocolloCollection() ){
            // nuovo inserimento
            if( praticaProtocollo.getPratica() == null ){
                // il supervisore inserisce pratiche non riservate
                if( !(!pratica.getRiservata() && autenticato.getSupervisorepratiche()) ){
                    Protocollo protocollo = praticaProtocollo.getProtocollo();
                    ProfiloUtenteProtocollo profilo = new ProfiloUtenteProtocollo(protocollo, autenticato);
                    if( !pratica.getRiservata() && !profilo.inSportelloOAttribuzione() && !autenticato.getRicercatoreprotocollo() &&
                            !autenticato.getSupervisoreprotocollo() ){
                        msg = "Devi avere completa visibilità del protocollo per poterlo inserire nella pratica.";
                        return new Validation(false, msg);
                    } else if( pratica.getRiservata() && !profilo.inSportelloOAttribuzioneR() && !autenticato.getSupervisoreprotocollo()){
                        msg = "Devi avere completa visibilità del protocollo e permesso sui dati riservati per poterlo inserire nella pratica riservata.";
                        return new Validation(false, msg);
                    }
                }
                // inserire in originale protocolli
                if ( praticaProtocollo.getOriginale() ) {
                    // esiste già un protocollo collegato come originale -> msg box
                    // con attribuzione principale!=ufficio gestore -> vietato
                    Ufficio principale=null;
                    for (Attribuzione attr : praticaProtocollo.getProtocollo().getAttribuzioneCollection()) {
                        if (attr.getPrincipale()) {
                            principale = attr.getUfficio();
                            break;
                        }
                    }
                    if ( !(pratica.getGestione().equals(principale) || autenticato.getSupervisorepratiche()) ) {
                        msg = "Per poter inserire il protocollo in originale, l'attribuzione principale deve coincidere con l'ufficio gestore della pratica.\n";
                        return new Validation(false, msg);
                    }
                }
            }
        }

        /*
         * Verifica collegamenti tra pratiche: permesso se pratica collegata non è archiviata oppure
         * all'ufficio gestore della pratica collegata
         */
        for( DipendenzaPratica dipendenzaPratica: pratica.getDipendenzaPraticaCollection() ){
            // nuovo inserimento
            if( dipendenzaPratica.getPraticadominante() == null ){
                // l'altra pratica è archiviata / ufficio gestore?
                Pratica dipende = dipendenzaPratica.getPraticadipendente();
                if( dipende.getArchiviata() &&
                        !BaseUtil.utenteInUfficio(autenticato, dipende.getGestione().getId().intValue(),Boolean.FALSE)) {
                    msg = "Non hai i permessi per collegare questa pratica ad una pratica archiviata.";
                    return new Validation(false, msg);
                }
            }
        }

        return new Validation(true);
    }
}
