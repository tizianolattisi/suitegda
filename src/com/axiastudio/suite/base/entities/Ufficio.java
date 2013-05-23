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
package com.axiastudio.suite.base.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
@Entity
@Table(schema="BASE")
@SequenceGenerator(name="genufficio", sequenceName="base.ufficio_id_seq", initialValue=1, allocationSize=1)
public class Ufficio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genufficio")
    private Long id;
    @Column(name="descrizione")
    private String descrizione;
    @Column(name="sportello")
    private Boolean sportello=false;
    @Column(name="mittenteodestinatario")
    private Boolean mittenteodestinatario=false;
    @Column(name="attribuzione")
    private Boolean attribuzione=false;
    @OneToMany(mappedBy = "ufficio", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<UfficioUtente> ufficioUtenteCollection;


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

    public Boolean getSportello() {
        return sportello;
    }

    public void setSportello(Boolean sportello) {
        this.sportello = sportello;
    }

    public Boolean getMittenteodestinatario() {
        return mittenteodestinatario;
    }

    public void setMittenteodestinatario(Boolean mittenteodestinatario) {
        this.mittenteodestinatario = mittenteodestinatario;
    }

    public Boolean getAttribuzione() {
        return attribuzione;
    }

    public void setAttribuzione(Boolean attribuzione) {
        this.attribuzione = attribuzione;
    }

    public Collection<UfficioUtente> getUfficioUtenteCollection() {
        return ufficioUtenteCollection;
    }

    public void setUfficioUtenteCollection(Collection<UfficioUtente> ufficioUtenteCollection) {
        this.ufficioUtenteCollection = ufficioUtenteCollection;
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
        if (!(object instanceof Ufficio)) {
            return false;
        }
        Ufficio other = (Ufficio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return " (" + id + ") " + this.getDescrizione();
    }
    
}
