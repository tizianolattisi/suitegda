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
package com.axiastudio.suite.pratiche.entities;

import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.generale.ITimeStamped;
import com.axiastudio.suite.protocollo.entities.Fascicolo;
import com.axiastudio.suite.protocollo.entities.PraticaProtocollo;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
@Entity
@Table(schema="PRATICHE")
@SequenceGenerator(name="genpratica", sequenceName="pratiche.pratica_id_seq", initialValue=1, allocationSize=1)
public class Pratica implements Serializable, ITimeStamped {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genpratica")
    private Long id;
    @Column(name="idpratica", unique=true)
    private String idpratica;
    @Column(name="codiceinterno", unique=true)
    private String codiceInterno;
    @Column(name="datapratica")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date datapratica;
    @Column(name="anno")
    private Integer anno;
    @Column(name="descrizione")
    private String descrizione;
    @Column(name="note")
    private String note;
    @OneToMany(mappedBy = "pratica", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<PraticaProtocollo> praticaProtocolloCollection;
    @JoinColumn(name = "attribuzione", referencedColumnName = "id")
    @ManyToOne
    private Ufficio attribuzione;
    @JoinColumn(name = "gestione", referencedColumnName = "id")
    @ManyToOne
    private Ufficio gestione;
    @JoinColumn(name = "ubicazione", referencedColumnName = "id")
    @ManyToOne
    private Ufficio ubicazione;
    @Column(name="dettaglioubicazione")
    private String dettaglioubicazione;
    @JoinColumn(name = "tipo", referencedColumnName = "id")
    @ManyToOne
    private TipoPratica tipo;
    @Column(name="riservata")
    private Boolean riservata=false;
    @JoinColumn(name = "fascicolo", referencedColumnName = "id")
    @ManyToOne
    private Fascicolo fascicolo;

    /* timestamped */
    @Column(name="rec_creato")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date recordcreato;
    @Column(name="rec_modificato")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date recordmodificato;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnno() {
        return anno;
    }

    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    public Date getDatapratica() {
        return datapratica;
    }

    public void setDatapratica(Date datapratica) {
        this.datapratica = datapratica;
    }

    public String getIdpratica() {
        return idpratica;
    }

    public void setIdpratica(String idpratica) {
        this.idpratica = idpratica;
    }

    public String getCodiceInterno() {
        return codiceInterno;
    }

    public void setCodiceInterno(String codiceInterno) {
        this.codiceInterno = codiceInterno;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String oggetto) {
        this.descrizione = oggetto;
    }
    
    public String getDescrizioner() {
        if( this.getRiservata() != null && this.getRiservata() == true ){
            return "RISERVATA";
        }
        return this.getDescrizione();
    }

    public void setDescrizioner(String descrizione) {
        /* nulla da fare */
    }
    
    public Collection<PraticaProtocollo> getPraticaProtocolloCollection() {
        return praticaProtocolloCollection;
    }

    public void setPraticaProtocolloCollection(Collection<PraticaProtocollo> praticaProtocolloCollection) {
        this.praticaProtocolloCollection = praticaProtocolloCollection;
    }

    public Ufficio getAttribuzione() {
        return attribuzione;
    }

    public void setAttribuzione(Ufficio attribuzione) {
        this.attribuzione = attribuzione;
    }

    public Ufficio getGestione() {
        return gestione;
    }

    public void setGestione(Ufficio gestione) {
        this.gestione = gestione;
    }

    public Ufficio getUbicazione() {
        return ubicazione;
    }

    public void setUbicazione(Ufficio ubicazione) {
        this.ubicazione = ubicazione;
    }

    public String getDettaglioubicazione() {
        return dettaglioubicazione;
    }

    public void setDettaglioubicazione(String dettaglioubicazione) {
        this.dettaglioubicazione = dettaglioubicazione;
    }

    public TipoPratica getTipo() {
        return tipo;
    }

    public void setTipo(TipoPratica tipo) {
        this.tipo = tipo;
    }

    public Boolean getRiservata() {
        return riservata;
    }

    public void setRiservata(Boolean riservata) {
        this.riservata = riservata;
    }

    public Fascicolo getFascicolo() {
        return fascicolo;
    }

    public void setFascicolo(Fascicolo fascicolo) {
        this.fascicolo = fascicolo;
    }

    @Override
    public Date getRecordcreato() {
        return recordcreato;
    }

    public void setRecordcreato(Date recordcreato) {
        
    }

    @Override
    public Date getRecordmodificato() {
        return recordmodificato;
    }

    public void setRecordmodificato(Date recordmodificato) {
        
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
        if (!(object instanceof Pratica)) {
            return false;
        }
        Pratica other = (Pratica) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    /*
     * Le pratiche riservate non riportano l'oggetto
     */
    @Override
    public String toString() {
        String out = this.getIdpratica() + " - " + this.getDescrizioner();
        return out;
    }
    
}
