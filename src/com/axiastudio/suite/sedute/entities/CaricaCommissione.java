/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.sedute.entities;

import com.axiastudio.suite.procedimenti.entities.Carica;
import java.io.Serializable;
import javax.persistence.Entity;
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
@SequenceGenerator(name="gencaricacommissione", sequenceName="sedute.caricaommissione_id_seq", initialValue=1, allocationSize=1)

public class CaricaCommissione implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="gencaricacommissione")
    private Long id;
    @JoinColumn(name = "commissione", referencedColumnName = "id")
    @ManyToOne
    private Commissione commissione;
    @JoinColumn(name = "carica", referencedColumnName = "id")
    @ManyToOne
    private Carica carica;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Commissione getCommissione() {
        return commissione;
    }

    public void setCommissione(Commissione commissione) {
        this.commissione = commissione;
    }

    public Carica getCarica() {
        return carica;
    }

    public void setCarica(Carica carica) {
        this.carica = carica;
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
        if (!(object instanceof CaricaCommissione)) {
            return false;
        }
        CaricaCommissione other = (CaricaCommissione) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.axiastudio.suite.sedute.entities.CaricaCommissione[ id=" + id + " ]";
    }
    
}
