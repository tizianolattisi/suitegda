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
package com.axiastudio.suite.pubblicazioni.entities;

import com.axiastudio.suite.protocollo.entities.Protocollo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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
    @JoinColumn(name = "protocollo", referencedColumnName = "iddocumento")
    @ManyToOne
    private Protocollo protocollo;
    @Column(name="titolo", length=255)
    private String titolo;
    @Column(name="descrizione", length=2048)
    private String descrizione;
    @Column(name="richiedente", length=255)
    private String richiedente;
    @Column(name="datapubblicazione")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date datapubblicazione;
    @Column(name="durataconsultazione")
    private Integer durataconsultazione;
    @Column(name="pubblicato")
    private Boolean pubblicato=false;
    @JoinColumn(name = "tipoattopubblicazione", referencedColumnName = "id")
    @ManyToOne
    private TipoAttoPubblicazione tipoattopubblicazione;
    @Column(name="numeroatto")
    private Integer numeroatto;
    @Column(name="dataatto")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataatto;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Protocollo getProtocollo() {
        return protocollo;
    }

    public void setProtocollo(Protocollo protocollo) {
        this.protocollo = protocollo;
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

    public Date getDatapubblicazione() {
        return datapubblicazione;
    }

    public void setDatapubblicazione(Date datapubblicazione) {
        this.datapubblicazione = datapubblicazione;
    }

    public Integer getDurataconsultazione() {
        return durataconsultazione;
    }

    public void setDurataconsultazione(Integer durataconsultazione) {
        this.durataconsultazione = durataconsultazione;
    }

    public Boolean getPubblicato() {
        return pubblicato;
    }

    public void setPubblicato(Boolean pubblicato) {
        this.pubblicato = pubblicato;
    }

    public TipoAttoPubblicazione getTipoattopubblicazione() {
        return tipoattopubblicazione;
    }

    public void setTipoattopubblicazione(TipoAttoPubblicazione tipoAttoPubblicazione) {
        this.tipoattopubblicazione = tipoAttoPubblicazione;
    }

    public Integer getNumeroatto() {
        return numeroatto;
    }

    public void setNumeroatto(Integer numeroatto) {
        this.numeroatto = numeroatto;
    }

    public Date getDataatto() {
        return dataatto;
    }

    public void setDataatto(Date dataatto) {
        this.dataatto = dataatto;
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
