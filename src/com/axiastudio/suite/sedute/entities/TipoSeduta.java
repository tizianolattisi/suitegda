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
package com.axiastudio.suite.sedute.entities;

import com.axiastudio.suite.pratiche.entities.TipoPratica;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 * 
 * I tipi di seduta caratterizzano gli incontri di una commissione, specificando
 * la commissione pertinente e il genere di pratica che viene trattato.
 * Questi possono essere di vario tipo, ad esempio la giunta comunale o il
 * consiglo comunale.
 * E' possibile che una commissione partecipi a diversi tipi di seduta.
 * 
 */
@Entity
@Table(schema="SEDUTE")
@SequenceGenerator(name="gentiposeduta", sequenceName="sedute.tiposeduta_id_seq", initialValue=1, allocationSize=1)
public class TipoSeduta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="gentiposeduta")
    private Long id;
    @Column(name="descrizione", length=1024)
    private String descrizione;
    @JoinColumn(name = "commissione", referencedColumnName = "id")
    @ManyToOne
    private Commissione commissione;
    @JoinColumn(name = "tipopratica", referencedColumnName = "id")
    @ManyToOne
    private TipoPratica tipopratica;

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

    public TipoPratica getTipopratica() {
        return tipopratica;
    }

    public void setTipopratica(TipoPratica tipopratica) {
        this.tipopratica = tipopratica;
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
        if (!(object instanceof TipoSeduta)) {
            return false;
        }
        TipoSeduta other = (TipoSeduta) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return this.getDescrizione();
    }
    
}
