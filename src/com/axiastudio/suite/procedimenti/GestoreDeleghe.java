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
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.finanziaria.entities.Servizio;
import com.axiastudio.suite.procedimenti.entities.Carica;
import com.axiastudio.suite.procedimenti.entities.Delega;
import com.axiastudio.suite.procedimenti.entities.Procedimento;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class GestoreDeleghe implements IGestoreDeleghe {

    private static Map<String, Carica> cariche = new HashMap<String, Carica>();

    public GestoreDeleghe() {
        Database db = (Database) Register.queryUtility(IDatabase.class);
        Controller controller = db.createController(Carica.class);
        for (Object o : controller.createFullStore()) {
            Carica carica = (Carica) o;
            cariche.put(carica.getCodiceCarica(), carica);
        }
    }

    public Utente trovaTitolare(String codiceCarica, Servizio servizio) {
        Carica carica = findCarica(codiceCarica);
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Delega> cq = cb.createQuery(Delega.class);
        Root from = cq.from(Delega.class);

        List<Predicate> predicates = new ArrayList();
        predicates.add(cb.equal(from.get("carica"), carica));
        predicates.add(cb.equal(from.get("servizio"), servizio));
        predicates.add(cb.equal(from.get("titolare"), true));
        predicates.add(from.get("fine").isNull());
        predicates.add(cb.lessThanOrEqualTo(from.get("inizio"), new Date()));

        cq = cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        TypedQuery<Delega> tq = em.createQuery(cq);
        List<Delega> deleghe = tq.getResultList();
        if( deleghe.size()==1 ){
            Utente utente = deleghe.get(0).getUtente();
            em.detach(utente);
            return utente;
        }
        return null;
    }
        
    private List<Delega> trovaTitoliEDeleghe(String codiceCarica, Servizio servizio, Procedimento procedimento, Ufficio ufficio, Utente utente, Date dataVerifica,
                                             Boolean delegaSuAssenza){

        Carica carica = findCarica(codiceCarica);
        
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

        // delega solo su assenza
        if (delegaSuAssenza) {
            predicates.add(cb.or(cb.equal(from.get("delegato"), Boolean.FALSE), cb.equal(from.get("suassenza"), Boolean.TRUE)));
        }

        // where
        cq = cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        
        TypedQuery<Delega> tq = em.createQuery(cq);
        return tq.getResultList();
    }

    public static Carica findCarica(String codiceCarica){
        if( cariche.keySet().contains(codiceCarica) ) {
            return cariche.get(codiceCarica);
        }
        return null;
    }

    public static List<String> codiciCarica() {
        List<String> codiciCarica = new ArrayList<String>();
        for( String key: cariche.keySet() ){
            codiciCarica.add(key);
        }
        return codiciCarica;
    }

    /*
     * Ricerca secca della carica, non esiste una delega più ampia
     */
    @Override
    public TitoloDelega checkTitoloODelega(String codiceCarica) {
        return this.checkTitoloODelega(codiceCarica, null, null, null, null, null);
    }

    @Override
    /*
     * Ricerca per servizio, da verificare il caso più ampio "tutti i servizi"
     */
    public TitoloDelega checkTitoloODelega(String codiceCarica, Servizio servizio) {
        return this.checkTitoloODelega(codiceCarica, servizio, null, null, null, null);
    }

    @Override
    public TitoloDelega checkTitoloODelega(String codiceCarica, Servizio servizio, Procedimento procedimento) {
        return this.checkTitoloODelega(codiceCarica, servizio, procedimento, null, null, null);
    }

    @Override
    public TitoloDelega checkTitoloODelega(String codiceCarica, Servizio servizio, Procedimento procedimento, Ufficio ufficio) {
        return this.checkTitoloODelega(codiceCarica, servizio, procedimento, ufficio, null, null);
    }

    @Override
    public TitoloDelega checkTitoloODelega(String codiceCarica, Servizio servizio, Procedimento procedimento, Ufficio ufficio, Utente utente) {
        return this.checkTitoloODelega(codiceCarica, servizio, procedimento, ufficio, utente, null);
    }

    @Override
    public TitoloDelega checkTitoloODelega(String codiceCarica, Servizio servizio, Procedimento procedimento, Ufficio ufficio, Utente utente, Date dataVerifica){
        return this.checkTitoloODelega(codiceCarica, servizio, procedimento, ufficio, utente, dataVerifica, Boolean.TRUE);
    }

    @Override
    public TitoloDelega checkTitoloODelega(String codiceCarica, Servizio servizio, Procedimento procedimento, Ufficio ufficio, Utente utente, Date dataVerifica,
                                           Boolean delegaSuAssenza) {
        return this.checkTitoloODelega(codiceCarica, servizio, procedimento, ufficio, utente, dataVerifica, delegaSuAssenza, null);
    }

    @Override
    public TitoloDelega checkTitoloODelega(String codiceCarica, Servizio servizio, Procedimento procedimento, Ufficio ufficio, Utente utente, Date dataVerifica,
                                           Boolean delegaSuAssenza, Utente firmatario) {
        // prima cerchiamo un titolo o una delega esatta
        List<Delega> titoliEDeleghe = this.trovaTitoliEDeleghe(codiceCarica, servizio, procedimento, ufficio, utente, dataVerifica, delegaSuAssenza);
        // se è una delega verifico che il delegante abbia titolo per delegare
        for( Delega titoloODelega: titoliEDeleghe ){
            if( titoloODelega.getTitolare() ){
                return new TitoloDelega(true, false, titoloODelega.getCarica(), utente, null);
            }
            if( this.checkTitoloODelega(codiceCarica, servizio, procedimento, ufficio, titoloODelega.getDelegante(), dataVerifica) != null ){
                return new TitoloDelega(false, true, titoloODelega.getCarica(), utente, titoloODelega.getDelegante());
            }
        }
        
        // Cerchiamo tutti i titoli e le deleghe più ampi della richiesta
        List<Delega> titoliEDelegheAmpie = new ArrayList();
//        titoliEDelegheAmpie.addAll(this.trovaTitoliEDeleghe(codiceCarica, servizio, procedimento, ufficio, utente, dataVerifica, delegaSuAssenza));
        if( ufficio != null && procedimento != null && servizio != null ){
            titoliEDelegheAmpie.addAll(this.trovaTitoliEDeleghe(codiceCarica, servizio, procedimento, null, utente, dataVerifica, delegaSuAssenza));
            titoliEDelegheAmpie.addAll(this.trovaTitoliEDeleghe(codiceCarica, servizio, null, ufficio, utente, dataVerifica, delegaSuAssenza));
            titoliEDelegheAmpie.addAll(this.trovaTitoliEDeleghe(codiceCarica, null, procedimento, ufficio, utente, dataVerifica, delegaSuAssenza));
            titoliEDelegheAmpie.addAll(this.trovaTitoliEDeleghe(codiceCarica, servizio, null, null, utente, dataVerifica, delegaSuAssenza));
            titoliEDelegheAmpie.addAll(this.trovaTitoliEDeleghe(codiceCarica, null, procedimento, null, utente, dataVerifica, delegaSuAssenza));
            titoliEDelegheAmpie.addAll(this.trovaTitoliEDeleghe(codiceCarica, null, null, ufficio, utente, dataVerifica, delegaSuAssenza));
            titoliEDelegheAmpie.addAll(this.trovaTitoliEDeleghe(codiceCarica, null, null, null, utente, dataVerifica, delegaSuAssenza));
        } else if( ufficio == null && procedimento != null && servizio != null ) {
            titoliEDelegheAmpie.addAll(this.trovaTitoliEDeleghe(codiceCarica, servizio, procedimento, null, utente, dataVerifica, delegaSuAssenza));
            titoliEDelegheAmpie.addAll(this.trovaTitoliEDeleghe(codiceCarica, servizio, null, null, utente, dataVerifica, delegaSuAssenza));
            titoliEDelegheAmpie.addAll(this.trovaTitoliEDeleghe(codiceCarica, null, procedimento, null, utente, dataVerifica, delegaSuAssenza));
            titoliEDelegheAmpie.addAll(this.trovaTitoliEDeleghe(codiceCarica, null, null, null, utente, dataVerifica, delegaSuAssenza));
        } else if( ufficio != null && procedimento == null && servizio != null ) {
            titoliEDelegheAmpie.addAll(this.trovaTitoliEDeleghe(codiceCarica, servizio, null, ufficio, utente, dataVerifica, delegaSuAssenza));
            titoliEDelegheAmpie.addAll(this.trovaTitoliEDeleghe(codiceCarica, null, null, ufficio, utente, dataVerifica, delegaSuAssenza));
            titoliEDelegheAmpie.addAll(this.trovaTitoliEDeleghe(codiceCarica, servizio, null, null, utente, dataVerifica, delegaSuAssenza));
            titoliEDelegheAmpie.addAll(this.trovaTitoliEDeleghe(codiceCarica, null, null, null, utente, dataVerifica, delegaSuAssenza));
        } else if( ufficio != null && procedimento != null && servizio == null) {
            titoliEDelegheAmpie.addAll(this.trovaTitoliEDeleghe(codiceCarica, null, procedimento, ufficio, utente, dataVerifica, delegaSuAssenza));
            titoliEDelegheAmpie.addAll(this.trovaTitoliEDeleghe(codiceCarica, null, null, ufficio, utente, dataVerifica, delegaSuAssenza));
            titoliEDelegheAmpie.addAll(this.trovaTitoliEDeleghe(codiceCarica, null, procedimento, null, utente, dataVerifica, delegaSuAssenza));
            titoliEDelegheAmpie.addAll(this.trovaTitoliEDeleghe(codiceCarica, null, null, null, utente, dataVerifica, delegaSuAssenza));
        } else if( ufficio != null && procedimento == null && servizio == null){
            titoliEDelegheAmpie.addAll(this.trovaTitoliEDeleghe(codiceCarica, null, null, ufficio, utente, dataVerifica, delegaSuAssenza));
            titoliEDelegheAmpie.addAll(this.trovaTitoliEDeleghe(codiceCarica, null, null, null, utente, dataVerifica, delegaSuAssenza));
        } else if( ufficio == null && procedimento != null && servizio == null ){
            titoliEDelegheAmpie.addAll(this.trovaTitoliEDeleghe(codiceCarica, null, procedimento, null, utente, dataVerifica, delegaSuAssenza));
            titoliEDelegheAmpie.addAll(this.trovaTitoliEDeleghe(codiceCarica, null, null, null, utente, dataVerifica, delegaSuAssenza));
        } else if( ufficio == null && procedimento == null && servizio != null ){
            titoliEDelegheAmpie.addAll(this.trovaTitoliEDeleghe(codiceCarica, servizio, null, null, utente, dataVerifica, delegaSuAssenza));
            titoliEDelegheAmpie.addAll(this.trovaTitoliEDeleghe(codiceCarica, null, null, null, utente, dataVerifica, delegaSuAssenza));
        }
        // Prima andiamo alla ricerca delle titolarità
        for( Delega titoloODelega: titoliEDelegheAmpie ){
            if( titoloODelega.getTitolare() ){
                // L'utente ha una titolarità più ampia del richiesto
                return new TitoloDelega(true, false, titoloODelega.getCarica(), utente, null);
            }
        }
        // Poi cerchiamo tra le deleghe
        for( Delega titoloODelega: titoliEDelegheAmpie ){
            if( titoloODelega.getDelegato() ){
                // L'utente è delegato, quindi devo verificare se il delegante ha titolo per delegare
                if( titoloODelega.getDelegante() != null &&
                        this.checkTitoloODelega(codiceCarica, servizio, procedimento, ufficio, titoloODelega.getDelegante(), dataVerifica) != null ){
                    return new TitoloDelega(false, true, titoloODelega.getCarica(), utente, titoloODelega.getDelegante());
                }
            }
        }        
        return null;
    }
    
    /*
     * L'utente autenticato può visualizzare le sue deleghe, sia come titolare che come delegato,
     * e le deleghe fatte da lui verso altri utenti.
     */
    public static Predicate filtroDelegheUtente(CriteriaBuilder cb, Root from) {
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        Predicate cariche = cb.equal(from.get("utente"), autenticato);
        Predicate deleghe = cb.equal(from.get("delegante"), autenticato);
        return cb.or(cariche, deleghe);
    }

    public List<Delega> checkCarica(String codiceCarica){
        return checkCarica(codiceCarica, null, null);
    }

    public List<Delega> checkCarica(String codiceCarica, Utente utente, Date dataVerifica) {
        Carica carica = findCarica(codiceCarica);

        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Delega> cq = cb.createQuery(Delega.class);
        Root from = cq.from(Delega.class);

        List<Predicate> predicates = new ArrayList();

        // la carica richiesta
        predicates.add(cb.equal(from.get("carica"), carica));

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
        return tq.getResultList();
    }

}
