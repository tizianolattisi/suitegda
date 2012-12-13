/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.pratiche.entities;

import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.protocollo.entities.PraticaProtocollo;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
@Entity
@Table(schema="PRATICHE")
@SequenceGenerator(name="genpratica", sequenceName="pratiche.pratica_id_seq", initialValue=1, allocationSize=1)
public class Pratica implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genpratica")
    private Long id;
    @Column(name="idpratica", unique=true)
    private String idpratica;
    @Column(name="datapratica")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date datapratica;
    @Column(name="anno")
    private Integer anno;
    @Column(name="descrizione")
    private String descrizione;
    @Column(name="note")
    private String note;
    @OneToMany(mappedBy = "pratica", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<PraticaProtocollo> praticaProtocolloCollection;
    @JoinColumn(name = "attribuzione", referencedColumnName = "id")
    @ManyToOne
    private Ufficio attribuzione;
    @JoinColumn(name = "gestione", referencedColumnName = "id")
    @ManyToOne
    private Ufficio gestione;
    @JoinColumn(name = "ubicazione", referencedColumnName = "id")
    @ManyToOne
    private Ufficio ubicazione;
    @Column(name="dettaglioubicazione")
    private String dettaglioubicazione;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnno() {
        return anno;
    }

    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    public Date getDatapratica() {
        return datapratica;
    }

    public void setDatapratica(Date datapratica) {
        this.datapratica = datapratica;
    }

    public String getIdpratica() {
        return idpratica;
    }

    public void setIdpratica(String idpratica) {
        this.idpratica = idpratica;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String oggetto) {
        this.descrizione = oggetto;
    }

    public Collection<PraticaProtocollo> getPraticaProtocolloCollection() {
        return praticaProtocolloCollection;
    }

    public void setPraticaProtocolloCollection(Collection<PraticaProtocollo> praticaProtocolloCollection) {
        this.praticaProtocolloCollection = praticaProtocolloCollection;
    }

    public Ufficio getAttribuzione() {
        return attribuzione;
    }

    public void setAttribuzione(Ufficio attribuzione) {
        this.attribuzione = attribuzione;
    }

    public Ufficio getGestione() {
        return gestione;
    }

    public void setGestione(Ufficio gestione) {
        this.gestione = gestione;
    }

    public Ufficio getUbicazione() {
        return ubicazione;
    }

    public void setUbicazione(Ufficio ubicazione) {
        this.ubicazione = ubicazione;
    }

    public String getDettaglioubicazione() {
        return dettaglioubicazione;
    }

    public void setDettaglioubicazione(String dettaglioubicazione) {
        this.dettaglioubicazione = dettaglioubicazione;
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
        if (!(object instanceof Pratica)) {
            return false;
        }
        Pratica other = (Pratica) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.axiastudio.suite.pratiche.entities.Pratica[ id=" + id + " ]";
    }
    
}
