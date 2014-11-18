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
package com.axiastudio.suite.anagrafiche.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
@Entity
@Table(schema="ANAGRAFICHE")
@SequenceGenerator(name="genindirizzo", sequenceName="anagrafiche.indirizzo_id_seq", initialValue=1, allocationSize=1)
public class Indirizzo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genindirizzo")
    private Long id;
    @JoinColumn(name = "soggetto", referencedColumnName = "id")
    @ManyToOne
    private Soggetto soggetto;
    @Column(name="descrizione")
    private String descrizione;
    @Column(name="principale")
    private Boolean principale=false;
    @Column(name="tipo")
    @Enumerated(EnumType.STRING)
    private TipoIndirizzo tipo;
    @Column(name="via")
    private String via;
    @Column(name="civico", length=6)
    private String civico;
    @Column(name="cap", length=5)
    private String cap;
    @Column(name="frazione")
    private String frazione;
    @Column(name="comune")
    private String comune;
    @JoinColumn(name = "provincia", referencedColumnName = "codice")
    @ManyToOne
    private Provincia provincia;
    @JoinColumn(name = "stato", referencedColumnName = "codice")
    @ManyToOne
    private Stato stato;
    @Column(name="datanascita")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date datanascita;
    @Column(name="datacessazione")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date datacessazione;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCivico() {
        return civico;
    }

    public void setCivico(String civico) {
        this.civico = civico;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public Soggetto getSoggetto() {
        return soggetto;
    }

    public void setSoggetto(Soggetto soggetto) {
        this.soggetto = soggetto;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Boolean getPrincipale() {
        return principale;
    }

    public void setPrincipale(Boolean principale) {
        this.principale = principale;
    }

    public TipoIndirizzo getTipo() {
        return tipo;
    }

    public void setTipo(TipoIndirizzo tipo) {
        this.tipo = tipo;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getFrazione() {
        return frazione;
    }

    public void setFrazione(String frazione) {
        this.frazione = frazione;
    }  

    public String getComune() {
        return comune;
    }

    public void setComune(String comune) {
        this.comune = comune;
    }

    public Stato getStato() {
        return stato;
    }

    public void setStato(Stato stato) {
        this.stato = stato;
    }

    public Date getDatanascita() {
        return datanascita;
    }

    public void setDatanascita(Date datanascita) {
        this.datanascita = datanascita;
    }

    public Date getDatacessazione() {
        return datacessazione;
    }

    public void setDatacessazione(Date datacessazione) {
        this.datacessazione = datacessazione;
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
        if (!(object instanceof Indirizzo)) {
            return false;
        }
        Indirizzo other = (Indirizzo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.axiastudio.suite.anagrafiche.entities.Indirizzo[ id=" + id + " ]";
    }
    
}
