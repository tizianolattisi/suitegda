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
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.UfficioUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.pratiche.entities.Pratica;
import com.axiastudio.suite.procedimenti.entities.TipoPraticaProcedimento;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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
        Boolean inUfficioGestore = false;
        for( UfficioUtente uu: autenticato.getUfficioUtenteCollection() ){
            if( uu.getUfficio().equals(pratica.getGestione()) && uu.getModificapratica() ){
                // se la pratica è riservata, mi serve anche il flag
                if( !pratica.getRiservata() || uu.getRiservato() ){
                    inUfficioGestore = true;
                    break;
                }
            }
        }
        
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

            // se mancano gestione e ubicazione, li fisso come l'attribuzione
            if( pratica.getGestione() == null ){
                pratica.setGestione(pratica.getAttribuzione());
            }
            if( pratica.getUbicazione() == null ){
                pratica.setUbicazione(pratica.getAttribuzione());
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
        return new Validation(true);
    }
}
