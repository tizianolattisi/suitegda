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
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
@Entity
@Table(schema="BASE")
@EntityListeners({TimeStampedListener.class})
@SequenceGenerator(name="genufficio", sequenceName="base.ufficio_id_seq", initialValue=1, allocationSize=1)
public class Ufficio implements Serializable, ITimeStamped {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genufficio")
    private Long id;
    @Column(name="descrizione")
    private String descrizione;
    @Column(name="denominazione")
    private String denominazione;
    @Column(name="sportello")
    private Boolean sportello=false;
    @Column(name="mittenteodestinatario")
    private Boolean mittenteodestinatario=false;
    @Column(name="attribuzione")
    private Boolean attribuzione=false;
    @Column(name="assessorato")
    private Boolean assessorato=false;
    @OneToMany(mappedBy = "ufficio", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<UfficioUtente> ufficioUtenteCollection;
    @Column(name="pec")
    private String pec;
    @Column(name="richieste")
    private Boolean richieste=false;

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

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getDenominazione() {
        return denominazione;
    }

    public void setDenominazione(String denominazione) {
        this.denominazione = denominazione;
    }

    public Boolean getSportello() {
        return sportello;
    }

    public void setSportello(Boolean sportello) {
        this.sportello = sportello;
    }

    public Boolean getMittenteodestinatario() {
        return mittenteodestinatario;
    }

    public void setMittenteodestinatario(Boolean mittenteodestinatario) {
        this.mittenteodestinatario = mittenteodestinatario;
    }

    public Boolean getAttribuzione() {
        return attribuzione;
    }

    public void setAttribuzione(Boolean attribuzione) {
        this.attribuzione = attribuzione;
    }

    public Collection<UfficioUtente> getUfficioUtenteCollection() {
        return ufficioUtenteCollection;
    }

    public void setUfficioUtenteCollection(Collection<UfficioUtente> ufficioUtenteCollection) {
        this.ufficioUtenteCollection = ufficioUtenteCollection;
    }

    public Boolean getAssessorato() {
        return assessorato;
    }

    public void setAssessorato(Boolean assessorato) {
        this.assessorato = assessorato;
    }

    public String getPec() {
        return pec;
    }

    public void setPec(String pec) {
        this.pec = pec;
    }

    public Boolean getRichieste() {
        return richieste;
    }

    public void setRichieste(Boolean richieste) {
        this.richieste = richieste;
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
        if (!(object instanceof Ufficio)) {
            return false;
        }
        Ufficio other = (Ufficio) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return " (" + id + ") " + this.getDescrizione();
    }
    
}
