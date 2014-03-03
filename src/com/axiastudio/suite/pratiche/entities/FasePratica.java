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

import com.axiastudio.suite.procedimenti.entities.Procedimento;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
@Entity
@Table(schema="PRATICHE")
@SequenceGenerator(name="genfasepratica", sequenceName="pratiche.fasepratica_id_seq", initialValue=1, allocationSize=1)
public class FasePratica implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genfasepratica")
    private Long id;
    @JoinColumn(name = "pratica", referencedColumnName = "id")
    @ManyToOne
    private Pratica pratica;
    @JoinColumn(name = "fase", referencedColumnName = "id")
    @ManyToOne
    private Fase fase;
    @Column(name="progressivo")
    private Integer progressivo;
    @Column(name="testo")
    private String testo;
    @Column(name="dascartare")
    private String dascartare;
    @Column(name="confermabile")
    private Boolean confermabile=false;
    @JoinColumn(name = "confermata", referencedColumnName = "id")
    @ManyToOne
    private FasePratica confermata;
    @Column(name="testoconfermata")
    private String testoconfermata;
    @Column(name="rifiutabile")
    private Boolean rifiutabile=false;
    @JoinColumn(name = "rifiutata", referencedColumnName = "id")
    @ManyToOne
    private FasePratica rifiutata;
    @Column(name="testorifiutata")
    private String testorifiutata;
    @Column(name="condizione")
    private String condizione;
    @Column(name="azione")
    private String azione;
    @Column(name="completata")
    private Boolean completata=Boolean.FALSE;
    @Column(name="attiva")
    private Boolean attiva=Boolean.FALSE;
    @Column(name="usoresponsabile")
    private Boolean usoresponsabile=false;
    @Column(name="cariche")
    private String cariche;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pratica getPratica() {
        return pratica;
    }

    public void setPratica(Pratica pratica) {
        this.pratica = pratica;
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

    public String getAzione() {
        return azione;
    }

    public void setAzione(String azione) {
        this.azione = azione;
    }

    public FasePratica getConfermata() {
        return confermata;
    }

    public void setConfermata(FasePratica confermata) {
        this.confermata = confermata;
    }

    public FasePratica getRifiutata() {
        return rifiutata;
    }

    public void setRifiutata(FasePratica rifiutata) {
        this.rifiutata = rifiutata;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public String getDascartare() {
        return dascartare;
    }

    public void setDascartare(String dascartare) {
        this.dascartare = dascartare;
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

    public Boolean getCompletata() {
        return completata;
    }

    public void setCompletata(Boolean completata) {
        this.completata = completata;
    }

    public Boolean getAttiva() {
        return attiva;
    }

    public void setAttiva(Boolean attiva) {
        this.attiva = attiva;
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

    public Boolean getUsoresponsabile() {
        return usoresponsabile;
    }

    public void setUsoresponsabile(Boolean usoresponsabile) {
        this.usoresponsabile = usoresponsabile;
    }

    public String getCariche() {
        return cariche;
    }

    public void setCariche(String cariche) {
        this.cariche = cariche;
    }
}
