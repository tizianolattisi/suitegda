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

import com.axiastudio.suite.anagrafiche.entities.Soggetto;
import com.axiastudio.suite.generale.ITimeStamped;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
@Entity
@Table(schema="PROTOCOLLO")
@SequenceGenerator(name="gensoggettoprotocollo", sequenceName="protocollo.soggettoprotocollo_id_seq", initialValue=1, allocationSize=1)
public class SoggettoProtocollo implements Serializable, ITimeStamped {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="gensoggettoprotocollo")
    private Long id;
    @JoinColumn(name = "soggetto", referencedColumnName = "id")
    @ManyToOne
    private Soggetto soggetto;
    @JoinColumn(name = "protocollo", referencedColumnName = "iddocumento")
    @ManyToOne
    private Protocollo protocollo;
    @JoinColumn(name = "titolo", referencedColumnName = "id")
    @ManyToOne
    private Titolo titolo;
    @Column(name="primoinserimento")
    private Boolean primoinserimento=false;
    @Column(name="annullato")
    private Boolean annullato=false;
    @Column(name="conoscenza")
    private Boolean conoscenza=false;
    @Column(name="notifica")
    private Boolean notifica=false;
    @Column(name="corrispondenza")
    private Boolean corrispondenza=false;

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

    public Protocollo getProtocollo() {
        return protocollo;
    }

    public void setProtocollo(Protocollo protocollo) {
        this.protocollo = protocollo;
    }

    public Soggetto getSoggetto() {
        return soggetto;
    }

    public void setSoggetto(Soggetto soggetto) {
        this.soggetto = soggetto;
    }

    /*
     * SoggettoFormattato mette in bold i soggetti di primo inserimento
     */
    public String getSoggettoformattato() {
        String pre = "";
        String post = "";
        if( this.getAnnullato() ){
            pre = "- ";
            post = "";
        } else if( this.getPrimoinserimento() ){
            pre = "* ";
            post = "";
        }
        return pre+soggetto.toString()+post;
    }
    public void setSoggettoformattato( String s ) {
        
    }
    
    public Titolo getTitolo() {
        return titolo;
    }

    public void setTitolo(Titolo titolo) {
        this.titolo = titolo;
    }

    public Boolean getPrimoinserimento() {
        return primoinserimento;
    }

    public void setPrimoinserimento(Boolean primoinserimento) {
        this.primoinserimento = primoinserimento;
    }

    public Boolean getAnnullato() {
        return annullato;
    }

    public void setAnnullato(Boolean annullato) {
        this.annullato = annullato;
    }

    public Boolean getConoscenza() {
        return conoscenza;
    }

    public void setConoscenza(Boolean conoscenza) {
        this.conoscenza = conoscenza;
    }

    public Boolean getNotifica() {
        return notifica;
    }

    public void setNotifica(Boolean notifica) {
        this.notifica = notifica;
    }

    public Boolean getCorrispondenza() {
        return corrispondenza;
    }

    public void setCorrispondenza(Boolean corrispondenza) {
        this.corrispondenza = corrispondenza;
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
        if (!(object instanceof SoggettoProtocollo)) {
            return false;
        }
        SoggettoProtocollo other = (SoggettoProtocollo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.soggetto.toString();
        //return "com.axiastudio.suite.protocollo.entities.SoggettoProtocollo[ id=" + id + " ]";
    }
    
}
