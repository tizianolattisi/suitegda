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
package com.axiastudio.suite.pratiche.entities;

import com.axiastudio.suite.procedimenti.entities.Procedimento;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
@Entity
@Table(schema="PRATICHE")
@SequenceGenerator(name="gentipopratica", sequenceName="pratiche.tipopratica_id_seq", initialValue=1, allocationSize=1)
public class TipoPratica implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="gentipopratica")
    private Long id;
    @Column(name="codice")
    private String codice;
    @Column(name="descrizione")
    private String descrizione;
    @JoinColumn(name = "tipopadre", referencedColumnName = "id")
    @ManyToOne
    private TipoPratica tipopadre;
    @JoinColumn(name="procedimento", referencedColumnName = "id")
    @ManyToOne
    private Procedimento procedimento;
    @Column(name="formulacodifica")
    private String formulacodifica;
    @Column(name="porzionenumeroda")
    private Integer porzionenumeroda;
    @Column(name="porzionenumeroa")
    private Integer porzionenumeroa;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public TipoPratica getTipopadre() {
        return tipopadre;
    }

    public void setTipopadre(TipoPratica tipopadre) {
        this.tipopadre = tipopadre;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Procedimento getProcedimento() {
        return procedimento;
    }

    public void setProcedimento(Procedimento procedimento) {
        this.procedimento = procedimento;
    }

    public String getFormulacodifica() {
        return formulacodifica;
    }

    public void setFormulacodifica(String formulacodifica) {
        this.formulacodifica = formulacodifica;
    }

    public Integer getPorzionenumeroda() {
        return porzionenumeroda;
    }

    public void setPorzionenumeroda(Integer porzionenumeroda) {
        this.porzionenumeroda = porzionenumeroda;
    }

    public Integer getPorzionenumeroa() {
        return porzionenumeroa;
    }

    public void setPorzionenumeroa(Integer porzionenumeroa) {
        this.porzionenumeroa = porzionenumeroa;
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
        if (!(object instanceof TipoPratica)) {
            return false;
        }
        TipoPratica other = (TipoPratica) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getCodice()+" - "+this.getDescrizione();
    }
    
}
