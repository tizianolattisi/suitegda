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
package com.axiastudio.suite.protocollo.entities;

import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.generale.ITimeStamped;
import com.axiastudio.suite.generale.TimeStampedListener;
import org.apache.commons.lang3.SerializationUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
@Entity
@EntityListeners({TimeStampedListener.class})
@Table(schema="PROTOCOLLO")
@SequenceGenerator(name="genattribuzione", sequenceName="protocollo.attribuzione_id_seq", initialValue=1, allocationSize=1)
@NamedQuery(name="trovaAttribuzioniUtente",
            query = "SELECT a FROM Attribuzione a JOIN a.ufficio u "
                  + "JOIN u.ufficioUtenteCollection uu "
                  + "WHERE a.letto = FALSE AND uu.ricerca = TRUE "
                  + "AND uu.utente.id = :id ORDER BY a.protocollo.iddocumento DESC")
public class Attribuzione implements Serializable, ITimeStamped {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genattribuzione")
    private Long id;
    @JoinColumn(name = "ufficio", referencedColumnName = "id")
    @ManyToOne
    private Ufficio ufficio;
    @JoinColumn(name = "protocollo", referencedColumnName = "iddocumento")
    @ManyToOne
    private Protocollo protocollo;
    @Column(name="principale")
    private Boolean principale=false;
    @Column(name="letto")
    private Boolean letto=false;
    @Column(name="dataletto")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataletto;
    @Column(name="esecutoreletto", length=1)
    private String esecutoreletto;
    @Column(name="evidenza", length=1)
    private String evidenza;

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

    /* OLD STATE */
    @Transient
    private Attribuzione old;
    @PostLoad
    private void saveOld(){
        old = SerializationUtils.clone(this);
    }
    public Attribuzione getOldState() {
        return old;
    }


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

    public Ufficio getUfficio() {
        return ufficio;
    }

    public void setUfficio(Ufficio ufficio) {
        this.ufficio = ufficio;
    }

    public Boolean getPrincipale() {
        return principale;
    }

    public void setPrincipale(Boolean principale) {
        this.principale = principale;
    }
    
    public String getIddocumento(){
        return this.protocollo.getIddocumento();
    }
    
    public void setIddocumento(String iddocumento){
        
    }
    
    public TipoProtocollo getTipoprotocollo(){
        return this.protocollo.getTipo();
    }
    
    public void setTipoprotocollo(TipoProtocollo tipoProtocollo){
        
    }
    
    public Date getDataprotocollo(){
        return this.protocollo.getDataprotocollo();
    }
    
    public void setDataprotocollo(Date dataProtocollo){
        
    }
    
    public String getOggetto(){
        return this.protocollo.getOggetto();
    }
    
    public void setOggetto(String oggetto){

    }

    public Boolean getLetto() {
        return letto;
    }

    public void setLetto(Boolean letto) {
        this.letto = letto;
    }

    public String getEvidenza() {
        return evidenza;
    }

    public Date getDataletto() {
        return dataletto;
    }

    public void setDataletto(Date dataletto) {
        this.dataletto = dataletto;
    }

    public String getEsecutoreletto() {
        return esecutoreletto;
    }

    public void setEsecutoreletto(String esecutoreletto) {
        this.esecutoreletto = esecutoreletto;
    }

    public void setEvidenza(String evidenza) {
        this.evidenza = evidenza;
    }

    @Override
    public Date getRecordcreato() {
        return recordcreato;
    }

    public void setRecordcreato(Date recordcreato) {
        this.recordcreato = recordcreato;
    }

    @Override
    public String getRecordcreatoda() {
        return recordcreatoda;
    }

    public void setRecordcreatoda(String recordcreatoda) {
        this.recordcreatoda = recordcreatoda;
    }

    @Override
    public Date getRecordmodificato() {
        return recordmodificato;
    }

    public void setRecordmodificato(Date recordmodificato) {
        this.recordmodificato = recordmodificato;
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
        if (!(object instanceof Attribuzione)) {
            return false;
        }
        Attribuzione other = (Attribuzione) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.axiastudio.suite.protocollo.entities.Attribuzione[ id=" + id + " ]";
    }
    
}
