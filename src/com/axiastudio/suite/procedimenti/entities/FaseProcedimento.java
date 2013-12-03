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

import com.axiastudio.suite.pratiche.entities.Fase;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
@Entity
@Table(schema="PROCEDIMENTI")
@SequenceGenerator(name="genfaseprocedimento", sequenceName="procedimenti.faseprocedimento_id_seq", initialValue=1, allocationSize=1)
public class FaseProcedimento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genfaseprocedimento")
    private Long id;
    @JoinColumn(name = "procedimento", referencedColumnName = "id")
    @ManyToOne
    private Procedimento procedimento;
    @JoinColumn(name = "fase", referencedColumnName = "id")
    @ManyToOne
    private Fase fase;
    @Column(name="progressivo")
    private Integer progressivo;
    @Column(name="testo")
    private String testo;
    @Column(name="confermabile")
    private Boolean confermabile=false;
    @JoinColumn(name = "confermata", referencedColumnName = "id")
    @ManyToOne
    private FaseProcedimento confermata;
    @Column(name="testoconfermata")
    private String testoconfermata;
    @Column(name="rifiutabile")
    private Boolean rifiutabile=false;
    @JoinColumn(name = "rifiutata", referencedColumnName = "id")
    @ManyToOne
    private FaseProcedimento rifiutata;
    @Column(name="testorifiutata")
    private String testorifiutata;
    @Column(name="condizione")
    private String condizione;

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

    public Fase getFase() {
        return fase;
    }

    public void setFase(Fase fase) {
        this.fase = fase;
    }

    public Integer getProgressivo() {
        return progressivo;
    }

    public void setProgressivo(Integer progressivo) {
        this.progressivo = progressivo;
    }

    public String getCondizione() {
        return condizione;
    }

    public void setCondizione(String condizione) {
        this.condizione = condizione;
    }

    public FaseProcedimento getConfermata() {
        return confermata;
    }

    public void setConfermata(FaseProcedimento confermata) {
        this.confermata = confermata;
    }

    public FaseProcedimento getRifiutata() {
        return rifiutata;
    }

    public void setRifiutata(FaseProcedimento rifiutata) {
        this.rifiutata = rifiutata;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public String getTestoconfermata() {
        return testoconfermata;
    }

    public void setTestoconfermata(String testoconfermata) {
        this.testoconfermata = testoconfermata;
    }

    public String getTestorifiutata() {
        return testorifiutata;
    }

    public void setTestorifiutata(String testorifiutata) {
        this.testorifiutata = testorifiutata;
    }

    public Boolean getConfermabile() {
        return confermabile;
    }

    public void setConfermabile(Boolean confermabile) {
        this.confermabile = confermabile;
    }

    public Boolean getRifiutabile() {
        return rifiutabile;
    }

    public void setRifiutabile(Boolean rifiutabile) {
        this.rifiutabile = rifiutabile;
    }

    @Override
    public String toString() {
        return getFase().getDescrizione();
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FaseProcedimento)) {
            return false;
        }
        FaseProcedimento other = (FaseProcedimento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;

    }

}
