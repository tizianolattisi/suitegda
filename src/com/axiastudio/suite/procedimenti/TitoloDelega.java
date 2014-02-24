package com.axiastudio.suite.procedimenti;

import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.procedimenti.entities.Carica;

/**
 * User: tiziano
 * Date: 24/02/14
 * Time: 11:05
 */
public class TitoloDelega {

    private Boolean titolo=false;
    private Boolean delega=false;
    private Carica carica=null;
    private Utente utente=null;
    private Utente delegante=null;

    public TitoloDelega(Boolean titolo, Boolean delega, Carica carica, Utente utente, Utente delegante) {
        this.titolo = titolo;
        this.delega = delega;
        this.carica = carica;
        this.utente = utente;
        this.delegante = delegante;
    }

    public Boolean getTitolo() {
        return titolo;
    }

    public void setTitolo(Boolean titolo) {
        this.titolo = titolo;
    }

    public Boolean getDelega() {
        return delega;
    }

    public void setDelega(Boolean delega) {
        this.delega = delega;
    }

    public Carica getCarica() {
        return carica;
    }

    public void setCarica(Carica carica) {
        this.carica = carica;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Utente getDelegante() {
        return delegante;
    }

    public void setDelegante(Utente delegante) {
        this.delegante = delegante;
    }
}
