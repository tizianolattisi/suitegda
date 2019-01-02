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
package com.axiastudio.suite.richieste.entities;

import com.axiastudio.suite.base.entities.Utente;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
@Entity
@Table(schema="RICHIESTE")
@SequenceGenerator(name="genvistoindividuale", sequenceName="richieste.vistoindividuale_id_seq", initialValue=1, allocationSize=1)
public class VistoIndividuale implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genvistoindividuale")
    private Long id;
    @JoinColumn(name = "richiesta", referencedColumnName = "id")
    @ManyToOne
    private Richiesta richiesta;
    @JoinColumn(name = "esecutoreletto", referencedColumnName = "id")
    @ManyToOne
    private Utente esecutoreletto;
    @Column(name="dataletto")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataletto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Richiesta getRichiesta() {
        return richiesta;
    }

    public void setRichiesta(Richiesta richiesta) {
        this.richiesta = richiesta;
    }

    public Utente getEsecutoreletto() {
        return esecutoreletto;
    }

    public void setEsecutoreletto(Utente esecutoreletto) {
        this.esecutoreletto = esecutoreletto;
    }

    public Date getDataletto() {
        return dataletto;
    }

    public void setDataletto(Date dataletto) {
        this.dataletto = dataletto;
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
        if (!(object instanceof VistoIndividuale)) {
            return false;
        }
        VistoIndividuale other = (VistoIndividuale) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "Visto da " + this.getEsecutoreletto().getLogin() + " il " + this.getDataletto().toString();
    }
}
