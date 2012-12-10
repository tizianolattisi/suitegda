/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.protocollo.entities;

import com.axiastudio.suite.anagrafiche.entities.Soggetto;
import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
@Entity
@Table(schema="PROTOCOLLO")
@SequenceGenerator(name="gensoggettoprotocollo", sequenceName="soggettoprotocollo_id_seq", initialValue=1, allocationSize=1)
public class SoggettoProtocollo implements Serializable {
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
    @Enumerated(EnumType.STRING)
    private TitoloSoggettoProtocollo titolo;
    @Column(name="conoscenza")
    private Boolean conoscenza=false;
    @Column(name="notifica")
    private Boolean notifica=false;
    @Column(name="corrispondenza")
    private Boolean corrispondenza=false;

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

    public TitoloSoggettoProtocollo getTitolo() {
        return titolo;
    }

    public void setTitolo(TitoloSoggettoProtocollo titolo) {
        this.titolo = titolo;
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
