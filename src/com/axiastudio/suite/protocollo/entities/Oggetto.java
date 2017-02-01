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
package com.axiastudio.suite.protocollo.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Comparator;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
@Entity
@Table(schema="PROTOCOLLO")
@SequenceGenerator(name="genoggetto", sequenceName="protocollo.oggetto_id_seq", initialValue=1, allocationSize=1)
public class Oggetto implements Serializable, Comparable<Oggetto> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genoggetto")
    private Long id;
    @Column(name="descrizione")
    private String descrizione;

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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Oggetto)) {
            return false;
        }
        Oggetto other = (Oggetto) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
//        return this.getDescrizione();
          return "-" + this.getDescrizione() + " (."+ this.getId() + ".)";

    }

    @Override
    public int compareTo(Oggetto o) {
        return Comparators.DESCRIZIONE.compare(this, o);
    }

    public static class Comparators {
        public static Comparator<Oggetto> DESCRIZIONE = new Comparator<Oggetto>() {
            @Override
            public int compare(Oggetto o1, Oggetto o2) {
                return o1.descrizione.compareTo(o2.descrizione);
            }
        };
    }
}
