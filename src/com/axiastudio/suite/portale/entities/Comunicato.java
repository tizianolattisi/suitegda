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
package com.axiastudio.suite.portale.entities;

import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.finanziaria.entities.Servizio;
import com.axiastudio.suite.generale.ITimeStamped;
import com.axiastudio.suite.generale.TimeStampedListener;
import com.axiastudio.suite.pratiche.IDettaglio;
import com.axiastudio.suite.pratiche.entities.Pratica;
import com.axiastudio.suite.protocollo.entities.PraticaProtocollo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Comune di Riva del Garda
 */
@Entity
@Table(schema="PORTALE")
@EntityListeners(TimeStampedListener.class)
@SequenceGenerator(name="gencomunicato", sequenceName="portale.comunicato_id_seq", initialValue=1, allocationSize=1)
public class Comunicato implements Serializable, ITimeStamped, IDettaglio {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="gencomunicato")
    private Long id;
    @JoinColumn(name = "pratica", referencedColumnName = "idpratica")
    @OneToOne
    private Pratica pratica;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dal;
    @Temporal(TemporalType.TIMESTAMP)
    private Date al;

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

    public Pratica getPratica() {
        return pratica;
    }

    public void setPratica(Pratica pratica) {
        this.pratica = pratica;
    }

    public Date getDal() {
        return dal;
    }

    public void setDal(Date dal) {
        this.dal = dal;
    }

    public Date getAl() {
        return al;
    }

    public void setAl(Date al) {
        this.al = al;
    }

    public String getProtocollo() {
        for (PraticaProtocollo pp: getPratica().getPraticaProtocolloCollection()){
            if (pp.getOggetto().getId().equals(269L)) {
                return pp.getProtocollo().getIddocumento();
            }
        }
        return "";
    }

    public void setProtocollo(String protocollo) {}

    public String getDescpratica() {
        return pratica.toString();
    }

    public void setDescpratica(String pratica) {
    }


    @Override
    public String getOggetto(){
        return getPratica().getDescrizioner();
    }
    @Override
    public void setOggetto(String oggetto){}

    // campi proxy (setter in NOP)
    @Override
    public String getIdpratica(){
        return getPratica().getIdpratica();
    }
    @Override
    public void setIdpratica(String idpratica){}
    @Override
    public String getCodiceinterno(){
        return getPratica().getCodiceinterno();
    }
    @Override
    public void setCodiceinterno(String codiceinterno){}

    // per la gestione delle deleghe nei procedimenti
    @Override
    public Servizio getServizio(){
        return null;
    }
    @Override
    public Ufficio getUfficio(){
        return null;
    }

    @Override
    public Date getRecordcreato() {
        return recordcreato;
    }

    public void setRecordcreato(Date recordcreato) {
        this.recordcreato = recordcreato;
    }

    @Override
    public Date getRecordmodificato() {
        return recordmodificato;
    }

    public void setRecordmodificato(Date recordmodificato) {
        this.recordmodificato = recordmodificato;
    }

    @Override
    public String getRecordcreatoda() {
        return recordcreatoda;
    }

    public void setRecordcreatoda(String recordcreatoda) {
        this.recordcreatoda = recordcreatoda;
    }

    @Override
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
        if (!(object instanceof com.axiastudio.suite.portale.entities.Comunicato)) {
            return false;
        }
        com.axiastudio.suite.portale.entities.Comunicato other = (com.axiastudio.suite.portale.entities.Comunicato) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return this.getPratica().toString();
    }


}
