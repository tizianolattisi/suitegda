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
package com.axiastudio.suite.procedimenti.entities;

import com.axiastudio.suite.base.entities.Ufficio;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
@Entity
@Table(schema="PROCEDIMENTI")
@SequenceGenerator(name="genufficioprocedimento", sequenceName="procedimenti.ufficioprocedimento_id_seq", initialValue=1, allocationSize=1)
public class UfficioProcedimento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genufficioprocedimento")
    private Long id;
    @JoinColumn(name = "procedimento", referencedColumnName = "id")
    @ManyToOne
    private Procedimento procedimento;
    @JoinColumn(name = "ufficio", referencedColumnName = "id")
    @ManyToOne
    private Ufficio ufficio;
    @Column(name="principale")
    private Boolean principale=false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Procedimento getProcedimento() {
        return procedimento;
    }

    public void setProcedimento(Procedimento procedimento) {
        this.procedimento = procedimento;
    }

    public Ufficio getUfficio() {
        return ufficio;
    }

    public void setUfficio(Ufficio ufficio) {
        this.ufficio = ufficio;
    }

    public Boolean getPrincipale() {
        return principale;
    }

    public void setPrincipale(Boolean principale) {
        this.principale = principale;
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
        if (!(object instanceof UfficioProcedimento)) {
            return false;
        }
        UfficioProcedimento other = (UfficioProcedimento) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.axiastudio.suite.procedimenti.entities.UfficioProcedimento[ id=" + id + " ]";
    }
    
}
