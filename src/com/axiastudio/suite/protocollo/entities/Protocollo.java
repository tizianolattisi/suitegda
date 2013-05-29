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

import com.axiastudio.pypapi.Register;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.generale.ITimeStamped;
import com.axiastudio.suite.SuiteUtil;
import com.axiastudio.suite.protocollo.ProfiloUtenteProtocollo; // XXX: brutto qui
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;


/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
@Entity
@Table(schema="PROTOCOLLO")
@SequenceGenerator(name="genprotocollo", sequenceName="protocollo.protocollo_id_seq", initialValue=1, allocationSize=1)
public class Protocollo implements Serializable, ITimeStamped {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genprotocollo")
    private Long id;
    @Column(name="iddocumento", length=12, unique=true)
    private String iddocumento;
    @Column(name="dataprotocollo")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataprotocollo;
    @Column(name="anno")
    private Integer anno;
    @Column(name="oggetto", length=1024)
    private String oggetto;
    @Column(name="note", length=1024)
    private String note;
    @Enumerated(EnumType.STRING)
    private TipoProtocollo tipo=TipoProtocollo.ENTRATA;
    @JoinColumn(name = "sportello", referencedColumnName = "id")
    @ManyToOne
    private Ufficio sportello;
    @OneToMany(mappedBy = "protocollo", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<SoggettoProtocollo> soggettoProtocolloCollection;
    @OneToMany(mappedBy = "protocollo", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<SoggettoRiservatoProtocollo> soggettoRiservatoProtocolloCollection;
    @OneToMany(mappedBy = "protocollo", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<UfficioProtocollo> ufficioProtocolloCollection;
    @OneToMany(mappedBy = "protocollo", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<Attribuzione> attribuzioneCollection;
    @OneToMany(mappedBy = "protocollo", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<PraticaProtocollo> praticaProtocolloCollection;
    @OneToMany(mappedBy = "protocollo", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<RiferimentoProtocollo> riferimentoProtocolloCollection;
    @OneToMany(mappedBy = "precedente", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<RiferimentoProtocollo> riferimentoProtocolloSuccessivoCollection;
    @OneToMany(mappedBy = "protocollo", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<AnnullamentoProtocollo> annullamentoProtocolloCollection;
    @Column(name="annullato")
    private Boolean annullato=false;
    @Column(name="annullamentorichiesto")
    private Boolean annullamentorichiesto=false;
    @Column(name="richiederisposta")
    private Boolean richiederisposta=false;
    @Column(name="spedito")
    private Boolean spedito=false;
    @Column(name="riservato")
    private Boolean riservato=false;
    @Column(name="corrispostoostornato")
    private Boolean corrispostoostornato=false;
    @Enumerated(EnumType.STRING)
    private TipoRiferimentoMittente tiporiferimentomittente;
    @Column(name="riferimentomittente")
    private String riferimentomittente;
    @Column(name="datariferimentomittente")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date datariferimentomittente;
    @JoinColumn(name = "fascicolo", referencedColumnName = "id")
    @ManyToOne
    private Fascicolo fascicolo;
    
    @Column(name="convalidaattribuzioni")
    private Boolean convalidaattribuzioni=false;
    @Column(name="dataconvalidaattribuzioni")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataconvalidaattribuzioni;
    @Column(name="esecutoreconvalidaattribuzioni", length=40)
    private String esecutoreconvalidaattribuzioni;

    @Column(name="convalidaprotocollo")
    private Boolean convalidaprotocollo=false;
    @Column(name="dataconvalidaprotocollo")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataconvalidaprotocollo;
    @Column(name="esecutoreconvalidaprotocollo", length=40)
    private String esecutoreconvalidaprotocollo;
    @Column(name="numeroconvalidaprotocollo", length=10)
    private String numeroconvalidaprotocollo;
    
    @Column(name="consolidadocumenti")
    private Boolean consolidadocumenti=false;
    @Column(name="dataconsolidadocumenti")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataconsolidadocumenti;
    @Column(name="esecutoreconsolidadocumenti", length=40)
    private String esecutoreconsolidadocumenti;
    
    /* timestamped */
    @Column(name="rec_creato")
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

    public String getIddocumento() {
        return iddocumento;
    }

    public void setIddocumento(String iddocumento) {
        this.iddocumento = iddocumento;
    }

    public Date getDataprotocollo() {
        return dataprotocollo;
    }

    public void setDataprotocollo(Date dataprotocollo) {
        this.dataprotocollo = dataprotocollo;
    }

    public Integer getAnno() {
        return anno;
    }

    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getOggetto() {
        return oggetto;
    }

    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }

    public String getOggettop() {
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        ProfiloUtenteProtocollo profilo = new ProfiloUtenteProtocollo(this, autenticato);
        if( !profilo.inSportelloOAttribuzione() ){
            return "RISERVATO";
        }
        return this.getOggetto();
    }

    public void setOggettop(String oggetto) {
        /* nulla da fare */
    }

    public String getNotep() {
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        ProfiloUtenteProtocollo profilo = new ProfiloUtenteProtocollo(this, autenticato);
        if( !profilo.inSportelloOAttribuzione() ){
            return "RISERVATO";
        }
        return this.getNote();
    }

    public void setNotep(String note) {
        /* nulla da fare */
    }
    
    public TipoProtocollo getTipo() {
        return tipo;
    }

    public void setTipo(TipoProtocollo tipo) {
        this.tipo = tipo;
    }

    public Ufficio getSportello() {
        return sportello;
    }

    public void setSportello(Ufficio sportello) {
        this.sportello = sportello;
    }

    public Collection<Attribuzione> getAttribuzioneCollection() {
        return attribuzioneCollection;
    }

    public void setAttribuzioneCollection(Collection<Attribuzione> attribuzioneprotocolloCollection) {
        this.attribuzioneCollection = attribuzioneprotocolloCollection;
    }

    public Boolean getAnnullamentorichiesto() {
        return annullamentorichiesto;
    }

    public void setAnnullamentorichiesto(Boolean annullamentorichiesto) {
        this.annullamentorichiesto = annullamentorichiesto;
    }

    public Boolean getAnnullato() {
        return annullato;
    }

    public void setAnnullato(Boolean annullato) {
        this.annullato = annullato;
    }

    public Boolean getCorrispostoostornato() {
        return corrispostoostornato;
    }

    public void setCorrispostoostornato(Boolean corrispostoostornato) {
        this.corrispostoostornato = corrispostoostornato;
    }

    public Boolean getRichiederisposta() {
        return richiederisposta;
    }

    public void setRichiederisposta(Boolean richiederisposta) {
        this.richiederisposta = richiederisposta;
    }

    public Boolean getRiservato() {
        return riservato;
    }

    public void setRiservato(Boolean riservato) {
        this.riservato = riservato;
    }

    public Boolean getSpedito() {
        return spedito;
    }

    public void setSpedito(Boolean spedito) {
        this.spedito = spedito;
    }

    public TipoRiferimentoMittente getTiporiferimentomittente() {
        return tiporiferimentomittente;
    }

    public void setTiporiferimentomittente(TipoRiferimentoMittente tiporiferimentomittente) {
        this.tiporiferimentomittente = tiporiferimentomittente;
    }

    public Collection<SoggettoProtocollo> getSoggettoProtocolloCollection() {
        return soggettoProtocolloCollection;
    }

    public void setSoggettoProtocolloCollection(Collection<SoggettoProtocollo> soggettoProtocolloCollection) {
        this.soggettoProtocolloCollection = soggettoProtocolloCollection;
    }

    public Collection<SoggettoRiservatoProtocollo> getSoggettoRiservatoProtocolloCollection() {
        return soggettoRiservatoProtocolloCollection;
    }

    public void setSoggettoRiservatoProtocolloCollection(Collection<SoggettoRiservatoProtocollo> soggettoRiservatoProtocolloCollection) {
        this.soggettoRiservatoProtocolloCollection = soggettoRiservatoProtocolloCollection;
    }

    public Collection<UfficioProtocollo> getUfficioProtocolloCollection() {
        return ufficioProtocolloCollection;
    }

    public void setUfficioProtocolloCollection(Collection<UfficioProtocollo> ufficioProtocolloCollection) {
        this.ufficioProtocolloCollection = ufficioProtocolloCollection;
    }

    public Collection<PraticaProtocollo> getPraticaProtocolloCollection() {
        return praticaProtocolloCollection;
    }

    public void setPraticaProtocolloCollection(Collection<PraticaProtocollo> praticaProtocolloCollection) {
        this.praticaProtocolloCollection = praticaProtocolloCollection;
    }

    public Collection<RiferimentoProtocollo> getRiferimentoProtocolloCollection() {
        return riferimentoProtocolloCollection;
    }

    public void setRiferimentoProtocolloCollection(Collection<RiferimentoProtocollo> riferimentoProtocolloCollection) {
        this.riferimentoProtocolloCollection = riferimentoProtocolloCollection;
    }

    public Collection<RiferimentoProtocollo> getRiferimentoProtocolloSuccessivoCollection() {
        return riferimentoProtocolloSuccessivoCollection;
    }

    public void setRiferimentoProtocolloSuccessivoCollection(Collection<RiferimentoProtocollo> riferimentoProtocolloSuccessivoCollection) {
        this.riferimentoProtocolloSuccessivoCollection = riferimentoProtocolloSuccessivoCollection;
    }

    public Collection<AnnullamentoProtocollo> getAnnullamentoProtocolloCollection() {
        return annullamentoProtocolloCollection;
    }

    public void setAnnullamentoProtocolloCollection(Collection<AnnullamentoProtocollo> annullamentoProtocolloCollection) {
        this.annullamentoProtocolloCollection = annullamentoProtocolloCollection;
    }

    public Date getDatariferimentomittente() {
        return datariferimentomittente;
    }

    public void setDatariferimentomittente(Date datariferimentomittente) {
        this.datariferimentomittente = datariferimentomittente;
    }

    public String getRiferimentomittente() {
        return riferimentomittente;
    }

    public void setRiferimentomittente(String riferimentomittente) {
        this.riferimentomittente = riferimentomittente;
    }

    public Boolean getConvalidaattribuzioni() {
        return convalidaattribuzioni;
    }

    public void setConvalidaattribuzioni(Boolean convalidaattribuzioni) {
        this.convalidaattribuzioni = convalidaattribuzioni;
    }

    public Boolean getConvalidaprotocollo() {
        return convalidaprotocollo;
    }

    public void setConvalidaprotocollo(Boolean convalidaprotocollo) {
        this.convalidaprotocollo = convalidaprotocollo;
    }

    public Date getDataconvalidaprotocollo() {
        return dataconvalidaprotocollo;
    }

    public void setDataconvalidaprotocollo(Date dataconvalida) {
        this.dataconvalidaprotocollo = dataconvalida;
    }

    public Boolean getConsolidadocumenti() {
        return consolidadocumenti;
    }

    public void setConsolidadocumenti(Boolean consolidadocumenti) {
        this.consolidadocumenti = consolidadocumenti;
    }

    public Fascicolo getFascicolo() {
        return fascicolo;
    }

    public void setFascicolo(Fascicolo fascicolo) {
        this.fascicolo = fascicolo;
    }

    public Date getDataconvalidaattribuzioni() {
        return dataconvalidaattribuzioni;
    }

    public void setDataconvalidaattribuzioni(Date dataconvalidaattribuzioni) {
        this.dataconvalidaattribuzioni = dataconvalidaattribuzioni;
    }

    public String getEsecutoreconvalidaattribuzioni() {
        return esecutoreconvalidaattribuzioni;
    }

    public void setEsecutoreconvalidaattribuzioni(String esecutoreconvalidaattribuzioni) {
        this.esecutoreconvalidaattribuzioni = esecutoreconvalidaattribuzioni;
    }

    public String getEsecutoreconvalidaprotocollo() {
        return esecutoreconvalidaprotocollo;
    }

    public void setEsecutoreconvalidaprotocollo(String esecutoreconvalidaprotocollo) {
        this.esecutoreconvalidaprotocollo = esecutoreconvalidaprotocollo;
    }

    public Date getDataconsolidadocumenti() {
        return dataconsolidadocumenti;
    }

    public void setDataconsolidadocumenti(Date dataconsolidadocumenti) {
        this.dataconsolidadocumenti = dataconsolidadocumenti;
    }

    public String getEsecutoreconsolidadocumenti() {
        return esecutoreconsolidadocumenti;
    }

    public void setEsecutoreconsolidadocumenti(String esecutoreconsolidadocumenti) {
        this.esecutoreconsolidadocumenti = esecutoreconsolidadocumenti;
    }

    public String getNumeroconvalidaprotocollo() {
        return numeroconvalidaprotocollo;
    }

    public void setNumeroconvalidaprotocollo(String numeroconvalidaprotocollo) {
        this.numeroconvalidaprotocollo = numeroconvalidaprotocollo;
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
        if (!(object instanceof Protocollo)) {
            return false;
        }
        Protocollo other = (Protocollo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.tipo.toString().substring(0, 1) + " " + this.iddocumento + " (" + SuiteUtil.DATETIME_FORMAT.format(this.dataprotocollo) + ") " + this.getOggettop();
    }
    
}
