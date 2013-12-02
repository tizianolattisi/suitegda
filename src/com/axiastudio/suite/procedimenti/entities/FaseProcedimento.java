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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="gennormaprocedimento")
    private Long id;
    @JoinColumn(name = "procedimento", referencedColumnName = "id")
    @ManyToOne
    private Procedimento procedimento;
    @JoinColumn(name = "fase", referencedColumnName = "id")
    @ManyToOne
    private Fase fase;
    @Column(name="progressivo")
    private Integer progressivo;
    @JoinColumn(name = "confermata", referencedColumnName = "id")
    @ManyToOne
    private FaseProcedimento confermata;
    @JoinColumn(name = "rifiutata", referencedColumnName = "id")
    @ManyToOne
    private FaseProcedimento rifiutata;
    @Column(name="condizione")
    private String condizione;
    @Column(name="completata")
    private Boolean completata;

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

    public Boolean getCompletata() {
        return completata;
    }

    public void setCompletata(Boolean completata) {
        this.completata = completata;
    }
}
