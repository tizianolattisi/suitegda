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

import com.axiastudio.suite.anagrafiche.entities.RelazioneSoggetto;
import com.axiastudio.suite.anagrafiche.entities.Soggetto;
import com.axiastudio.suite.generale.ITimeStamped;
import com.axiastudio.suite.generale.TimeStampedListener;
import com.axiastudio.suite.protocollo.ISoggettoProtocollo;

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
@SequenceGenerator(name="gensoggettoprotocollo", sequenceName="protocollo.soggettoprotocollo_id_seq", initialValue=1, allocationSize=1)
public class SoggettoProtocollo implements Serializable, ISoggettoProtocollo, ITimeStamped {
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
    @Column(name="datainizio", columnDefinition="DATE DEFAULT CURRENT_DATE")
    @Temporal(TemporalType.DATE)
    private Date datainizio;
    @Column(name="datafine")
    @Temporal(TemporalType.DATE)
    private Date datafine;
    @Column(name="principale")
    private Boolean principale=false;
    @Column(name="pec")
    private Boolean pec=false;
    @JoinColumn(name = "soggettoreferente", referencedColumnName = "id")
    @ManyToOne
    private Soggetto soggettoReferente;
    @Column(name = "indirizzo")
    private String indirizzo;
    @Column(name="messaggiopec")
    private Long messaggiopec;

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

    public Protocollo getProtocollo() {
        return protocollo;
    }

    public void setProtocollo(Protocollo protocollo) {
        this.protocollo = protocollo;
    }

    @Override
    public Soggetto getSoggetto() {
        return soggetto;
    }

    public void setSoggetto(Soggetto soggetto) {
        this.soggetto = soggetto;
    }

    /*
     * SoggettoFormattato mette in bold i soggetti di primo inserimento
     */
    @Override
    public String getSoggettoformattato() {
        String pre = "";
        String post = "";
        if( this.getAnnullato() ){
            pre = "<del>";
            post = "</del>";
        } else if( this.getPrimoinserimento() ){
            pre = "'''";
            post = "'''";
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

    @Override
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
    public Date getDatainizio() {
        return datainizio;
    }

    @Override
    public void setDatainizio(Date datainizio) {
        this.datainizio = datainizio;
    }

    public Date getDatafine() {
        return datafine;
    }

    public void setDatafine(Date datafine) {
        this.datafine = datafine;
    }

    public Boolean getPrincipale() {
        return principale;
    }

    public void setPrincipale(Boolean principale) {
        this.principale = principale;
    }

    @Override
    public Boolean getPec() {
        return pec;
    }

    public void setPec(Boolean pec) {
        this.pec = pec;
    }

    @Override
    public Soggetto getSoggettoReferente() {
        return soggettoReferente;
    }

    @Override
    public void setSoggettoReferente(Soggetto soggettoReferente) {
        this.soggettoReferente = soggettoReferente;
    }

    @Override
    public Long getMessaggiopec() {
        return messaggiopec;
    }

    @Override
    public void setMessaggiopec(Long messaggiopec) {
        this.messaggiopec = messaggiopec;
    }

    public RelazioneSoggetto getReferenteRelazione() {
        return null;
    }

    public void setReferenteRelazione(RelazioneSoggetto referenteRelazione) {
        if (referenteRelazione ==  null ) {
            this.soggettoReferente = null;
        } else {
            this.soggettoReferente = referenteRelazione.getRelazionato();
        }
    }

    @Override
    public String getPredicato(){
        if ( this.getSoggettoReferente()==null ) {
            return "-";
        }
        return this.getSoggettoReferente().toString();
    }

    public void setPredicato(String predicato){
        // non deve fare nulla
    }

    @Override
    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
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
        if (!(object instanceof SoggettoProtocollo)) {
            return false;
        }
        SoggettoProtocollo other = (SoggettoProtocollo) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return this.soggetto.toString();
        //return "com.axiastudio.suite.protocollo.entities.SoggettoProtocollo[ id=" + id + " ]";
    }
    
}
