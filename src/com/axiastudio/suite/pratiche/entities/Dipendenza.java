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
package com.axiastudio.suite.pratiche.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Comparator;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
@Entity
@Table(schema="PRATICHE")
@SequenceGenerator(name="gendipendenza", sequenceName="pratiche.dipendenza_id_seq", initialValue=1, allocationSize=1)
public class Dipendenza implements Serializable, Comparable<Dipendenza> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genrelazione")
    private Long id;
    @Column(name="descrizionedominante")
    private String descrizionedominante;
    @Column(name="descrizionedipendente")
    private String descrizionedipendente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescrizionedominante() {
        return descrizionedominante;
    }

    public void setDescrizionedominante(String descrizionedominante) {
        this.descrizionedominante = descrizionedominante;
    }

    public String getDescrizionedipendente() {
        return descrizionedipendente;
    }

    public void setDescrizionedipendente(String descrizionedipendente) {
        this.descrizionedipendente = descrizionedipendente;
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
        if (!(object instanceof Dipendenza)) {
            return false;
        }
        Dipendenza other = (Dipendenza) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return getDescrizionedominante() + " - " + getDescrizionedipendente();
    }

    @Override
    public int compareTo(Dipendenza o) {
        return Comparators.DESCRIZIONE.compare(this, o);
    }

    public static class Comparators {
        public static Comparator<Dipendenza> DESCRIZIONE = new Comparator<Dipendenza>() {
            @Override
            public int compare(Dipendenza o1, Dipendenza o2) {
                return o1.descrizionedominante.compareTo(o2.descrizionedominante);
            }
        };
    }
}
