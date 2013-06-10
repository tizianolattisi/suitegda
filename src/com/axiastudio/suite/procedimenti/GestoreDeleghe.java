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
package com.axiastudio.suite.procedimenti;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Controller;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IController;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.finanziaria.entities.Servizio;
import com.axiastudio.suite.procedimenti.entities.Carica;
import com.axiastudio.suite.procedimenti.entities.CodiceCarica;
import com.axiastudio.suite.procedimenti.entities.Delega;
import com.axiastudio.suite.procedimenti.entities.Procedimento;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class GestoreDeleghe implements IGestoreDeleghe {
        
    private List<Delega> trovaTitoliEDeleghe(CodiceCarica codiceCarica, Servizio servizio, Procedimento procedimento, Ufficio ufficio, Utente utente, Date dataVerifica){

        Carica carica = this.findCarica(codiceCarica);
        
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Delega> cq = cb.createQuery(Delega.class);
        Root from = cq.from(Delega.class);
        
        List<Predicate> predicates = new ArrayList();

        // la carica richiesta
        predicates.add(cb.equal(from.get("carica"), carica));
        
        // servizio
        predicates.add(cb.equal(from.get("servizio"), servizio));

        // procedimento
        predicates.add(cb.equal(from.get("procedimento"), procedimento));

        // ufficio
        predicates.add(cb.equal(from.get("ufficio"), ufficio));
        
        // data verifica
        if( dataVerifica == null ){
            dataVerifica = new Date();
        }

        predicates.add(cb.lessThanOrEqualTo(from.get("inizio"), dataVerifica));
        predicates.add(cb.or(
                             cb.isNull(from.get("fine")),
                             cb.greaterThanOrEqualTo(from.get("fine"), dataVerifica)
                            )
                      );
        
        // l'utente
        if( utente == null ){
            utente = (Utente) Register.queryUtility(IUtente.class);
        }
        predicates.add(cb.equal(from.get("utente"), utente));
        
        // where
        cq = cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        
        TypedQuery<Delega> tq = em.createQuery(cq);
        List<Delega> deleghe = tq.getResultList();
        return deleghe;
    }
    
    private Carica findCarica(CodiceCarica codiceCarica){
        Controller ctrlCarica = (Controller) Register.queryUtility(IController.class, "com.axiastudio.suite.procedimenti.entities.Carica");
        for (Iterator it = ctrlCarica.createFullStore().iterator(); it.hasNext();) {
            Carica carica = (Carica) it.next();
            if( codiceCarica.equals(carica.getCodiceCarica()) ){
                return carica;
            }
        }
        return null;
    }

    /*
     * Ricerca secca della carica, non esiste una delega più ampia
     */
    @Override
    public Boolean checkTitoloODelega(CodiceCarica codiceCarica) {
        return this.checkTitoloODelega(codiceCarica, null, null, null, null, null);
    }

    @Override
    /*
     * Ricerca per servizio, da verificare il caso più ampio "tutti i servizi"
     */
    public Boolean checkTitoloODelega(CodiceCarica codiceCarica, Servizio servizio) {
        return this.checkTitoloODelega(codiceCarica, servizio, null, null, null, null);
    }

    @Override
    public Boolean checkTitoloODelega(CodiceCarica codiceCarica, Servizio servizio, Procedimento procedimento) {
        return this.checkTitoloODelega(codiceCarica, servizio, procedimento, null, null, null);
    }

    @Override
    public Boolean checkTitoloODelega(CodiceCarica codiceCarica, Servizio servizio, Procedimento procedimento, Ufficio ufficio) {
        return this.checkTitoloODelega(codiceCarica, servizio, procedimento, ufficio, null, null);
    }

    @Override
    public Boolean checkTitoloODelega(CodiceCarica codiceCarica, Servizio servizio, Procedimento procedimento, Ufficio ufficio, Utente utente) {
        return this.checkTitoloODelega(codiceCarica, servizio, procedimento, ufficio, utente, null);
    }

    @Override
    public Boolean checkTitoloODelega(CodiceCarica codiceCarica, Servizio servizio, Procedimento procedimento, Ufficio ufficio, Utente utente, Date dataVerifica){

        // prima cerchiamo un titolo o una delega esatta
        List<Delega> titoliEDeleghe = this.trovaTitoliEDeleghe(codiceCarica, servizio, procedimento, ufficio, utente, dataVerifica);
        // se è una delega verifico che il delegante abbia titolo per delegare
        for( Delega titoloODelega: titoliEDeleghe ){
            if( titoloODelega.getTitolare() ){
                return true;
            }
            if( this.checkTitoloODelega(codiceCarica, servizio, procedimento, ufficio, titoloODelega.getDelegante(), dataVerifica) ){
                return true;
            }
        }
        
        // Cerchiamo tutti i titoli e le deleghe più ampi della richiesta
        List<Delega> titoliEDelegheAmpie = new ArrayList();
        if( ufficio != null ){
            titoliEDelegheAmpie.addAll(this.trovaTitoliEDeleghe(codiceCarica, servizio, procedimento, null, utente, dataVerifica));
            if( procedimento != null ){
                titoliEDelegheAmpie.addAll(this.trovaTitoliEDeleghe(codiceCarica, servizio, null, null, utente, dataVerifica));
            }
            if( servizio != null ){
                titoliEDelegheAmpie.addAll(this.trovaTitoliEDeleghe(codiceCarica, null, procedimento, null, utente, dataVerifica));
            }
        }
        if( procedimento != null ){
            titoliEDelegheAmpie.addAll(this.trovaTitoliEDeleghe(codiceCarica, servizio, null, ufficio, utente, dataVerifica));
            if( servizio != null ){
                titoliEDelegheAmpie.addAll(this.trovaTitoliEDeleghe(codiceCarica, null, null, ufficio, utente, dataVerifica));
            }
        }
        if( servizio != null ){
            titoliEDelegheAmpie.addAll(this.trovaTitoliEDeleghe(codiceCarica, null, procedimento, ufficio, utente, dataVerifica));
        }
        // Prima andiamo alla ricerca delle titolarità
        for( Delega titoloODelega: titoliEDelegheAmpie ){
            if( titoloODelega.getTitolare() ){
                // L'utente ha una titolarità più ampia del richiesto
                return true;
            }
        }
        // Poi cerchiamo tra le deleghe
        for( Delega titoloODelega: titoliEDelegheAmpie ){
            if( titoloODelega.getDelegato() ){
                // L'utente è delegato, quindi devo verificare se il delegante ha titolo per delegare
                if( this.checkTitoloODelega(codiceCarica, servizio, procedimento, ufficio, titoloODelega.getDelegante(), dataVerifica) ){
                    return true;
                }
            }
        }        
        return false;
    }
    
    /*
     * L'utente autenticato può visualizzare le sue deleghe, sia come titolare che come delegato,
     * e le deleghe fatte da lui verso altri utenti.
     */
    public static Predicate filtroDelegheUtente(CriteriaBuilder cb, Root from) {
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        Predicate cariche = cb.equal(from.get("utente"), autenticato);
        Predicate deleghe = cb.equal(from.get("delegante"), autenticato);
        Predicate predicate = cb.or(cariche, deleghe);
        return predicate;
    }
    
}
