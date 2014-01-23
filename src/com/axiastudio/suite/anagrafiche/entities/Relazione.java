/*
 * Copyright (C) 2013 AXIA Studio (http://www.axiastudio.com)
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
 * You should have received a copy of the GNU Afffero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axiastudio.suite.anagrafiche.entities;

import java.io.Serializable;
import java.util.Comparator;
import javax.persistence.*;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
@Entity
@Table(schema="ANAGRAFICHE")
@SequenceGenerator(name="genrelazione", sequenceName="anagrafiche.relazione_id_seq", initialValue=1, allocationSize=1)
public class Relazione implements Serializable, Comparable<Relazione> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genrelazione")
    private Long id;
    @Column(name="descrizione")
    private String descrizione;
    @Column(name="inversa")
    private String inversa;
    @Column(name="asx")
    private Boolean asx=false;
    @Column(name="psx")
    private Boolean psx=false;
    @Column(name="esx")
    private Boolean esx=false;
    @Column(name="adx")
    private Boolean adx=false;
    @Column(name="pdx")
    private Boolean pdx=false;
    @Column(name="edx")
    private Boolean edx=false;

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

    public String getInversa() {
        return inversa;
    }

    public void setInversa(String inversa) {
        this.inversa = inversa;
    }

    public Boolean getAsx() {
        return asx;
    }

    public void setAsx(Boolean asx) {
        this.asx = asx;
    }

    public Boolean getPsx() {
        return psx;
    }

    public void setPsx(Boolean psx) {
        this.psx = psx;
    }

    public Boolean getEsx() {
        return esx;
    }

    public void setEsx(Boolean esx) {
        this.esx = esx;
    }

    public Boolean getAdx() {
        return adx;
    }

    public void setAdx(Boolean adx) {
        this.adx = adx;
    }

    public Boolean getPdx() {
        return pdx;
    }

    public void setPdx(Boolean pdx) {
        this.pdx = pdx;
    }

    public Boolean getEdx() {
        return edx;
    }

    public void setEdx(Boolean edx) {
        this.edx = edx;
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
        if (!(object instanceof Relazione)) {
            return false;
        }
        Relazione other = (Relazione) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descrizione + " / " + inversa;
    }

    @Override
    public int compareTo(Relazione o) {
        return Comparators.DESCRIZIONE.compare(this, o);
    }

    public static class Comparators {
        public static Comparator<Relazione> DESCRIZIONE = new Comparator<Relazione>() {
            @Override
            public int compare(Relazione o1, Relazione o2) {
                return o1.descrizione.compareTo(o2.descrizione);
            }
        };
    }
}
