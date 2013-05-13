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

import com.axiastudio.suite.pratiche.entities.TipoPratica;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
@Entity
@Table(schema="PROCEDIMENTI")
@SequenceGenerator(name="gentipopraticaprocedimento", sequenceName="procedimenti.tipopraticaprocedimento_id_seq", initialValue=1, allocationSize=1)
@NamedQuery(name="trovaTipiPraticaPermessiDaAttribuzioni",
            query = "SELECT tpp.tipopratica.id FROM TipoPraticaProcedimento tpp "
                  + "JOIN tpp.procedimento p JOIN p.ufficioProcedimentoCollection up "
                  + "WHERE up.ufficio.id = :id")
public class TipoPraticaProcedimento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="gentipopraticaprocedimento")
    private Long id;
    @JoinColumn(name = "procedimento", referencedColumnName = "id")
    @ManyToOne
    private Procedimento procedimento;
    @JoinColumn(name = "tipopratica", referencedColumnName = "id")
    @ManyToOne
    private TipoPratica tipopratica;



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

    public TipoPratica getTipopratica() {
        return tipopratica;
    }

    public void setTipopratica(TipoPratica tipoPratica) {
        this.tipopratica = tipoPratica;
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
        if (!(object instanceof TipoPraticaProcedimento)) {
            return false;
        }
        TipoPraticaProcedimento other = (TipoPraticaProcedimento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.axiastudio.suite.procedimenti.entities.TipoPraticaProcedimento[ id=" + id + " ]";
    }
    
}
