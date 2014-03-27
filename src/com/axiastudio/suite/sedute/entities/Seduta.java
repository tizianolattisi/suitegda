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

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
@Entity
@Table(schema="SEDUTE")
@SequenceGenerator(name="genseduta", sequenceName="sedute.seduta_id_seq", initialValue=1, allocationSize=1)
public class Seduta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genseduta")
    private Long id;
    @Column(name="datacreazione")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date datacreazione;
    @JoinColumn(name = "tiposeduta", referencedColumnName = "id")
    @ManyToOne
    private TipoSeduta tipoSeduta;
    @Column(name="dataoraconvocazione")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataoraconvocazione;
    @Enumerated(EnumType.STRING)
    private FaseSeduta faseseduta=FaseSeduta.IN_GESTIONE;
    @Enumerated(EnumType.STRING)
    private StatoSeduta statoseduta=StatoSeduta.A;
    @Column(name="inizioseduta")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date inizioseduta;
    @Column(name="cambiostatoseduta")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date cambiostatoseduta;
    @Column(name="fineseduta")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fineseduta;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDatacreazione() {
        return datacreazione;
    }

    public void setDatacreazione(Date datacreazione) {
        this.datacreazione = datacreazione;
    }

    public TipoSeduta getTipoSeduta() {
        return tipoSeduta;
    }

    public void setTipoSeduta(TipoSeduta tipo) {
        this.tipoSeduta = tipo;
    }

    public Date getDataoraconvocazione() {
        return dataoraconvocazione;
    }

    public void setDataoraconvocazione(Date dataoraconvocazione) {
        this.dataoraconvocazione = dataoraconvocazione;
    }

    public FaseSeduta getFaseseduta() {
        return faseseduta;
    }

    public void setFaseseduta(FaseSeduta faseseduta) {
        this.faseseduta = faseseduta;
    }

    public StatoSeduta getStatoseduta() {
        return statoseduta;
    }

    public void setStatoseduta(StatoSeduta statoseduta) {
        this.statoseduta = statoseduta;
    }

    public Date getInizioseduta() {
        return inizioseduta;
    }

    public void setInizioseduta(Date inizioseduta) {
        this.inizioseduta = inizioseduta;
    }

    public Date getCambiostatoseduta() {
        return cambiostatoseduta;
    }

    public void setCambiostatoseduta(Date cambiostatoseduta) {
        this.cambiostatoseduta = cambiostatoseduta;
    }

    public Date getFineseduta() {
        return fineseduta;
    }

    public void setFineseduta(Date fineseduta) {
        this.fineseduta = fineseduta;
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
        if (!(object instanceof Seduta)) {
            return false;
        }
        Seduta other = (Seduta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.axiastudio.suite.sedute.entities.Seduta[ id=" + id + " ]";
    }
    
}
