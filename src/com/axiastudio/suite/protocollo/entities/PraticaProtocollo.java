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

import com.axiastudio.suite.generale.ITimeStamped;
import com.axiastudio.suite.pratiche.entities.Pratica;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
@Entity
@Table(schema="PROTOCOLLO")
@SequenceGenerator(name="genpraticaprotocollo", sequenceName="protocollo.praticaprotocollo_id_seq", initialValue=1, allocationSize=1)
public class PraticaProtocollo implements Serializable, ITimeStamped {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genpraticaprotocollo")
    private Long id;
    @JoinColumn(name = "pratica", referencedColumnName = "id")
    @ManyToOne
    private Pratica pratica;
    @JoinColumn(name = "protocollo", referencedColumnName = "id")
    @ManyToOne
    private Protocollo protocollo;
    @Enumerated(EnumType.STRING)
    private TitoloPraticaProtocollo titolo;
    @Column(name="originale")
    private Boolean originale=false;

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

    public Boolean getOriginale() {
        return originale;
    }

    public void setOriginale(Boolean originale) {
        this.originale = originale;
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
