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
package com.axiastudio.suite.base.entities;

import com.axiastudio.suite.generale.ITimeStamped;
import com.axiastudio.suite.generale.TimeStampedListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
@Entity
@Table(schema="BASE")
@EntityListeners({TimeStampedListener.class})
@SequenceGenerator(name="genufficioutente", sequenceName="base.ufficioutente_id_seq", initialValue=1, allocationSize=1)
public class UfficioUtente implements Serializable, ITimeStamped {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genufficioutente")
    private Long id;
    @JoinColumn(name = "ufficio", referencedColumnName = "id")
    @ManyToOne
    private Ufficio ufficio;
    @JoinColumn(name = "utente", referencedColumnName = "id")
    @ManyToOne
    private Utente utente;
    @Column(name="ospite")
    private Boolean ospite=false;
    @Column(name="ricerca")
    private Boolean ricerca=true;
    @Column(name="visualizza")
    private Boolean visualizza=true;
    @Column(name="riservato")
    private Boolean riservato=false;
    @Column(name="daiperletto")
    private Boolean daiperletto=false;
    @Column(name="modificapratica")
    private Boolean modificapratica=false;
    @Column(name="inseriscepratica")
    private Boolean inseriscepratica=false;
    @Column(name="consolida")
    private Boolean consolida=false;
    @Column(name="responsabile")
    private Boolean responsabile=false;
    @Column(name="procedimenti")
    private Boolean procedimenti=false;
    @Column(name="leggepec")
    private Boolean leggepec=false;

    /* timestamped */
    @Column(name="rec_creato", insertable=false, updatable=false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date recordcreato;
    @Column(name="rec_creato_da")
    private String recordcreatoda;
    @Column(name="rec_modificato")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date recordmodificato;
    @Column(name="rec_modificato_da")
    private String recordmodificatoda;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ufficio getUfficio() {
        return ufficio;
    }

    public void setUfficio(Ufficio ufficio) {
        this.ufficio = ufficio;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Boolean getRiservato() {
        return riservato;
    }

    public void setRiservato(Boolean riservato) {
        this.riservato = riservato;
    }

    public Boolean getRicerca() {
        return ricerca;
    }

    public void setRicerca(Boolean ricerca) {
        this.ricerca = ricerca;
    }

    public Boolean getVisualizza() {
        return visualizza;
    }

    public void setVisualizza(Boolean visualizza) {
        this.visualizza = visualizza;
    }

    public Boolean getOspite() {
        return ospite;
    }

    public void setOspite(Boolean ospite) {
        this.ospite = ospite;
    }

    public Boolean getDaiperletto() {
        return daiperletto;
    }

    public void setDaiperletto(Boolean daiperletto) {
        this.daiperletto = daiperletto;
    }

    public Boolean getModificapratica() {
        return modificapratica;
    }

    public void setModificapratica(Boolean modificapratica) {
        this.modificapratica = modificapratica;
    }

    public Boolean getInseriscepratica() {
        return inseriscepratica;
    }

    public void setInseriscepratica(Boolean inseriscepratica) {
        this.inseriscepratica = inseriscepratica;
    }

    public Boolean getConsolida() {
        return consolida;
    }

    public void setConsolida(Boolean consolida) {
        this.consolida = consolida;
    }

    public Boolean getResponsabile() {
        return responsabile;
    }

    public void setResponsabile(Boolean responsabile) {
        this.responsabile = responsabile;
    }

    public Boolean getProcedimenti() {
        return procedimenti;
    }

    public void setProcedimenti(Boolean procedimenti) {
        this.procedimenti = procedimenti;
    }

    public Boolean getLeggepec() {
        return leggepec;
    }

    public void setLeggepec(Boolean leggepec) {
        this.leggepec = leggepec;
    }

    public Date getRecordcreato() {
        return recordcreato;
    }

    public void setRecordcreato(Date recordcreato) {
        this.recordcreato = recordcreato;
    }

    public String getRecordcreatoda() {
        return recordcreatoda;
    }

    public void setRecordcreatoda(String recordcreatoda) {
        this.recordcreatoda = recordcreatoda;
    }

    public Date getRecordmodificato() {
        return recordmodificato;
    }

    public void setRecordmodificato(Date recordmodificato) {
        this.recordmodificato = recordmodificato;
    }

    public String getRecordmodificatoda() {
        return recordmodificatoda;
    }

    public void setRecordmodificatoda(String recordmodificatoda) {
        this.recordmodificatoda = recordmodificatoda;
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
        if (!(object instanceof UfficioUtente)) {
            return false;
        }
        UfficioUtente other = (UfficioUtente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        //return "com.axiastudio.suite.base.entities.UfficioUtente[ id=" + id + " ]";
        return getUfficio().getId() + " - " + getUtente();
    }
    
}
