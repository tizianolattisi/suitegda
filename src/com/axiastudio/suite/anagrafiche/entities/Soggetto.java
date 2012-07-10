/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.anagrafiche.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
@Entity
public class Soggetto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name="tipologiasoggetto")
    @Enumerated(EnumType.STRING)
    private TipologiaSoggetto tipologiaSoggetto;
    @Column(name="sessosoggetto")
    @Enumerated(EnumType.STRING)
    private SessoSoggetto sessoSoggetto;
    @Column(name="titolosoggetto")
    @Enumerated(EnumType.STRING)
    private TitoloSoggetto titoloSoggetto;
    @Column(name="nick")
    private String nick;
    @Column(name="nome")
    private String nome;
    @Column(name="cognome")
    private String cognome;
    @Column(name="codicefiscale")
    private String coficeFiscale;
    @Column(name="ragionesociale")
    private String ragionesociale;
    @OneToMany(mappedBy = "soggetto", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<Indirizzo> indirizzoCollection;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRagionesociale() {
        return ragionesociale;
    }

    public void setRagionesociale(String ragionesociale) {
        this.ragionesociale = ragionesociale;
    }

    public TipologiaSoggetto getTipologiaSoggetto() {
        return tipologiaSoggetto;
    }

    public void setTipologiaSoggetto(TipologiaSoggetto tipologiaSoggetto) {
        this.tipologiaSoggetto = tipologiaSoggetto;
    }

    public SessoSoggetto getSessoSoggetto() {
        return sessoSoggetto;
    }

    public void setSessoSoggetto(SessoSoggetto sessoSoggetto) {
        this.sessoSoggetto = sessoSoggetto;
    }

    public TitoloSoggetto getTitoloSoggetto() {
        return titoloSoggetto;
    }

    public void setTitoloSoggetto(TitoloSoggetto titoloSoggetto) {
        this.titoloSoggetto = titoloSoggetto;
    }

    public String getCoficeFiscale() {
        return coficeFiscale;
    }

    public void setCoficeFiscale(String coficeFiscale) {
        this.coficeFiscale = coficeFiscale;
    }

    public Collection<Indirizzo> getIndirizzoCollection() {
        return indirizzoCollection;
    }

    public void setIndirizzoCollection(Collection<Indirizzo> indirizzoCollection) {
        this.indirizzoCollection = indirizzoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Soggetto)) {
            return false;
        }
        Soggetto other = (Soggetto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if( this.tipologiaSoggetto.equals(TipologiaSoggetto.PERSONA) ){
            return this.nome+" "+this.cognome;
        } else if( this.tipologiaSoggetto.equals(TipologiaSoggetto.AZIENDA) ){
            return this.ragionesociale;
        } else if ( this.tipologiaSoggetto.equals(TipologiaSoggetto.ENTE) ){
            return "ente (da completare)";
        }
        return "-";
        //return "com.axiastudio.suite.anagrafiche.entities.Soggetto[ id=" + id + " ]";
    }
    
}
