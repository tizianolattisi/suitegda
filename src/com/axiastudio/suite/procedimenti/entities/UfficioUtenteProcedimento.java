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
import com.axiastudio.suite.base.entities.UfficioUtente;
import com.axiastudio.suite.base.entities.Utente;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
@Entity
@Table(schema="PROCEDIMENTI")
@SequenceGenerator(name="genutenteprocedimento", sequenceName="procedimenti.ufficioutenteprocedimento_id_seq", initialValue=1, allocationSize=1)
public class UfficioUtenteProcedimento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genutenteprocedimento")
    private Long id;
    @JoinColumn(name = "procedimento", referencedColumnName = "id")
    @ManyToOne
    private Procedimento procedimento;
    @JoinColumn(name = "ufficioutente", referencedColumnName = "id")
    @ManyToOne
    private UfficioUtente ufficioUtente;
    @Column(name="responsabile")
    private Boolean responsabile=false;
    @Column(name="abilitato")
    private Boolean abilitato=false;
    @Column(name="abituale")
    private Boolean abituale=false;

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

    public UfficioUtente getUfficioUtente() {
        return ufficioUtente;
    }

    public void setUfficioUtente(UfficioUtente ufficioUtente) {
        this.ufficioUtente = ufficioUtente;
    }

    public Boolean getResponsabile() {
        return responsabile;
    }

    public void setResponsabile(Boolean responsabile) {
        this.responsabile = responsabile;
    }

    public Boolean getAbilitato() {
        return abilitato;
    }

    public void setAbilitato(Boolean abilitato) {
        this.abilitato = abilitato;
    }

    public Boolean getAbituale() {
        return abituale;
    }

    public void setAbituale(Boolean abituale) {
        this.abituale = abituale;
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
        if (!(object instanceof UfficioUtenteProcedimento)) {
            return false;
        }
        UfficioUtenteProcedimento other = (UfficioUtenteProcedimento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.axiastudio.suite.procedimenti.entities.UtenteProcedimento[ id=" + id + " ]";
    }
    
}
