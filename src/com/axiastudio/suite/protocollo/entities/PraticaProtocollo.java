/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.protocollo.entities;

import com.axiastudio.suite.pratiche.entities.Pratica;
import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
@Entity
@Table(schema="PROTOCOLLO")
public class PraticaProtocollo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JoinColumn(name = "pratica", referencedColumnName = "id")
    @ManyToOne
    private Pratica pratica;
    @JoinColumn(name = "protocollo", referencedColumnName = "id")
    @ManyToOne
    private Protocollo protocollo;
    @Enumerated(EnumType.STRING)
    private TitoloPraticaProtocollo titolo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pratica getPratica() {
        return pratica;
    }

    public void setPratica(Pratica pratica) {
        this.pratica = pratica;
    }

    public Protocollo getProtocollo() {
        return protocollo;
    }

    public void setProtocollo(Protocollo protocollo) {
        this.protocollo = protocollo;
    }

    public TitoloPraticaProtocollo getTitolo() {
        return titolo;
    }

    public void setTitolo(TitoloPraticaProtocollo titolo) {
        this.titolo = titolo;
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
        if (!(object instanceof PraticaProtocollo)) {
            return false;
        }
        PraticaProtocollo other = (PraticaProtocollo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.axiastudio.suite.protocollo.entities.PraticaProtocollo[ id=" + id + " ]";
    }
    
}
