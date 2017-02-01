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
package com.axiastudio.suite.deliberedetermine.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
@Entity
@Table(schema="deliberedetermine")
@SequenceGenerator(name="genspesaimpegnoesistente", sequenceName="deliberedetermine.spesaimpegnoesistente_id_seq", initialValue=1, allocationSize=1)
public class SpesaImpegnoEsistente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genspesaimpegnoesistente")
    private Long id;
    @JoinColumn(name = "determina", referencedColumnName = "id")
    @ManyToOne
    private Determina determina;
/*    @JoinColumn(name = "capitolo", referencedColumnName = "id")
    @ManyToOne
    private Capitolo capitolo;*/
    @Column(name="capitolo")
    private Integer capitolo;
    @Column(name="impegno")
    private String impegno;
    @Column(name="sottoimpegno")
    private String sottoImpegno;
    @Column(name="annoimpegno")
    private Integer annoImpegno;
    @Column(name="importo")
    private BigDecimal importo=BigDecimal.valueOf(0);
    @Column(name="annoesercizio")
    private Integer annoEsercizio = Calendar.getInstance().get(Calendar.YEAR);
    @Column(name="codicebeneficiario")
    private Long codiceBeneficiario;
    @Column(name="descrizionebeneficiario")
    private String descrizioneBeneficiario;
    @Column(name="codicecup")
    private String codiceCup;
    @Column(name="codicecig")
    private String codiceCig;
    @Column(name="cespite")
    private String cespite;
    @Column(name="descrizionecespite")
    private String descrizioneCespite;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Determina getDetermina() {
        return determina;
    }

    public void setDetermina(Determina determina) {
        this.determina = determina;
    }

    public Integer getCapitolo() {
        return capitolo;
    }

    public void setCapitolo(Integer capitolo) {
        this.capitolo = capitolo;
    }

    public String getImpegno() {
        return impegno;
    }

    public void setImpegno(String impegno) {
        this.impegno = impegno;
    }

    public String getSottoImpegno() {
        return sottoImpegno;
    }

    public void setSottoImpegno(String sottoImpegno) {
        this.sottoImpegno = sottoImpegno;
    }

    public Integer getAnnoImpegno() {
        return annoImpegno;
    }

    public void setAnnoImpegno(Integer annoImpegno) {
        this.annoImpegno = annoImpegno;
    }

    public BigDecimal getImporto() {
        return importo;
    }

    public void setImporto(BigDecimal importo) {
        this.importo = importo;
    }

    public Integer getAnnoEsercizio() {
        return annoEsercizio;
    }

    public void setAnnoEsercizio(Integer annoEsercizio) {
        this.annoEsercizio = annoEsercizio;
    }

    public Long getCodiceBeneficiario() {
        return codiceBeneficiario;
    }

    public void setCodiceBeneficiario(Long codiceBeneficiario) {
        this.codiceBeneficiario = codiceBeneficiario;
    }

    public String getDescrizioneBeneficiario() {
        return descrizioneBeneficiario;
    }

    public void setDescrizioneBeneficiario(String descrizioneBeneficiario) {
        this.descrizioneBeneficiario = descrizioneBeneficiario;
    }

    public String getCodiceCup() {
        return codiceCup;
    }

    public void setCodiceCup(String codiceCup) {
        this.codiceCup = codiceCup;
    }

    public String getCodiceCig() {
        return codiceCig;
    }

    public void setCodiceCig(String codiceCig) {
        this.codiceCig = codiceCig;
    }

    public String getCespite() {
        return cespite;
    }

    public void setCespite(String cespite) {
        this.cespite = cespite;
    }

    public String getDescrizioneCespite() {
        return descrizioneCespite;
    }

    public void setDescrizioneCespite(String descrizioneCespite) {
        this.descrizioneCespite = descrizioneCespite;
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
        if (!(object instanceof SpesaImpegnoEsistente)) {
            return false;
        }
        SpesaImpegnoEsistente other = (SpesaImpegnoEsistente) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.axiastudio.suite.deliberedetermine.entities.spesaimpegnoesistente[ id=" + id + " ]";
    }

}
