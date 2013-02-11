/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.sedute.entities;

import com.axiastudio.suite.pratiche.entities.TipologiaPratica;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
@Entity
@Table(schema="SEDUTE")
@SequenceGenerator(name="gentipologiaseduta", sequenceName="sedute.tipologiaseduta_id_seq", initialValue=1, allocationSize=1)
public class TipologiaSeduta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="gentipologiaseduta")
    private Long id;
    @Column(name="descrizione", length=1024)
    private String descrizione;
    @JoinColumn(name = "commissione", referencedColumnName = "id")
    @ManyToOne
    private Commissione commissione;
    @JoinColumn(name = "tipologiapratica", referencedColumnName = "id")
    @ManyToOne
    private TipologiaPratica tipologiaPratica;

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

    public Commissione getCommissione() {
        return commissione;
    }

    public void setCommissione(Commissione commissione) {
        this.commissione = commissione;
    }

    public TipologiaPratica getTipologiaPratica() {
        return tipologiaPratica;
    }

    public void setTipologiaPratica(TipologiaPratica tipologiaPratica) {
        this.tipologiaPratica = tipologiaPratica;
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
        if (!(object instanceof TipologiaSeduta)) {
            return false;
        }
        TipologiaSeduta other = (TipologiaSeduta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getDescrizione();
    }
    
}
