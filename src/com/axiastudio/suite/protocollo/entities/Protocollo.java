/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.protocollo.entities;

import com.axiastudio.suite.base.entities.Ufficio;
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
public class Protocollo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genprotocollo")
    private Long id;
    @Column(name="iddocumento", unique=true)
    private String iddocumento;
    @Column(name="dataprotocollo")
    @Temporal(javax.persistence.TemporalType.DATE)
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
    private Collection<UfficioProtocollo> ufficioProtocolloCollection;
    @OneToMany(mappedBy = "protocollo", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<Attribuzione> attribuzioneCollection;
    @OneToMany(mappedBy = "protocollo", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<PraticaProtocollo> praticaProtocolloCollection;
    @OneToMany(mappedBy = "protocollo", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<RiferimentoProtocollo> riferimentoProtocolloCollection;
    @OneToMany(mappedBy = "precedente", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<RiferimentoProtocollo> riferimentoProtocolloSuccessivoCollection;
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
    @Column(name="convalidaattribuzioni")
    private Boolean ConvalidaAttribuzioni=false;
    @Column(name="convalidaprotocollo")
    private Boolean ConvalidaProtocollo=false;
    @JoinColumn(name = "fascicolo", referencedColumnName = "id")
    @ManyToOne
    private Fascicolo fascicolo;



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

    public Boolean getConvalidaAttribuzioni() {
        return ConvalidaAttribuzioni;
    }

    public void setConvalidaAttribuzioni(Boolean ConvalidaAttribuzioni) {
        this.ConvalidaAttribuzioni = ConvalidaAttribuzioni;
    }

    public Boolean getConvalidaProtocollo() {
        return ConvalidaProtocollo;
    }

    public void setConvalidaProtocollo(Boolean ConvalidaProtocollo) {
        this.ConvalidaProtocollo = ConvalidaProtocollo;
    }

    public Fascicolo getFascicolo() {
        return fascicolo;
    }

    public void setFascicolo(Fascicolo fascicolo) {
        this.fascicolo = fascicolo;
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
        //return "com.axiastudio.suite.protocollo.entities.Protocollo[ id=" + id + " ]";
        return this.tipo.toString().substring(0, 1) + " " + this.iddocumento + "(" + this.dataprotocollo + ")";
    }
    
}
