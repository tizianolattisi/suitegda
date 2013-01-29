/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.pubblicazioni.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author tiziano
 */
@Entity
@Table(schema="PUBBLICAZIONI")
@SequenceGenerator(name="genpubblicazione", sequenceName="pubblicazioni.pubblicazione_id_seq", initialValue=1, allocationSize=1)
public class Pubblicazione implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genpubblicazione")
    private Long id;
    @Column(name="titolo", length=255)
    private String titolo;
    @Column(name="descrizione", length=2048)
    private String descrizione;
    @Column(name="richiedente", length=255)
    private String richiedente;
    @Column(name="inizioconsultazione")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date inizioconsultazione;
    @Column(name="fineconsultazione")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fineconsultazione;
    @Column(name="pubblicato")
    private Boolean pubblicato=false;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getRichiedente() {
        return richiedente;
    }

    public void setRichiedente(String richiedente) {
        this.richiedente = richiedente;
    }

    public Date getInizioconsultazione() {
        return inizioconsultazione;
    }

    public void setInizioconsultazione(Date inizioconsultazione) {
        this.inizioconsultazione = inizioconsultazione;
    }

    public Date getFineconsultazione() {
        return fineconsultazione;
    }

    public void setFineconsultazione(Date fineconsultazione) {
        this.fineconsultazione = fineconsultazione;
    }

    public Boolean getPubblicato() {
        return pubblicato;
    }

    public void setPubblicato(Boolean pubblicato) {
        this.pubblicato = pubblicato;
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
        if (!(object instanceof Pubblicazione)) {
            return false;
        }
        Pubblicazione other = (Pubblicazione) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.axiastudio.suite.pubblicazioni.entities.Pubblicazione[ id=" + id + " ]";
    }
    
}
