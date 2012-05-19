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
public class Protocollo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name="dataprotocollo")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataprotocollo;
    @Column(name="oggetto")
    private String oggetto;
    @Column(name="note")
    private String note;
    @Enumerated(EnumType.STRING)
    private TipoProtocollo tipo;
    @JoinColumn(name = "sportello", referencedColumnName = "id")
    @ManyToOne
    private Ufficio sportello;
    @OneToMany(mappedBy = "protocollo", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<SoggettoProtocollo> soggettoProtocolloCollection;
    @OneToMany(mappedBy = "protocollo", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<UfficioProtocollo> ufficioProtocolloCollection;
    @OneToMany(mappedBy = "protocollo", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<Attribuzione> attribuzioneCollection;
    @Column(name="annullato")
    private Boolean annullato;
    @Column(name="annullamentorichiesto")
    private Boolean annullamentorichiesto;
    @Column(name="richiederisposta")
    private Boolean richiederisposta;
    @Column(name="spedito")
    private Boolean spedito;
    @Column(name="riservato")
    private Boolean riservato;
    @Column(name="corrispostoostornato")
    private Boolean corrispostoostornato;
    @Enumerated(EnumType.STRING)
    private TipoRiferimentoMittente tiporiferimentomittente;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataprotocollo() {
        return dataprotocollo;
    }

    public void setDataprotocollo(Date dataprotocollo) {
        this.dataprotocollo = dataprotocollo;
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
        return "com.axiastudio.suite.protocollo.entities.Protocollo[ id=" + id + " ]";
    }
    
}
