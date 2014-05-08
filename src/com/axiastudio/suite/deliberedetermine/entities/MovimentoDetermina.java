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

import com.axiastudio.suite.finanziaria.entities.Capitolo;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
@Entity
@Table(schema="deliberedetermine")
@SequenceGenerator(name="genmovimentodetermina", sequenceName="deliberedetermine.movimentodetermina_id_seq", initialValue=1, allocationSize=1)
public class MovimentoDetermina implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genmovimentodetermina")
    private Long id;
    @JoinColumn(name = "determina", referencedColumnName = "id")
    @ManyToOne
    private Determina determina;
    @Column(name="archivio")
    private String archivio;
    @Column(name="eu")
    private String eu;
    @Column(name="tipomovimento")
 //   @Enumerated(EnumType.STRING)
 //   private TipoMovimento tipoMovimento;
    private String tipoMovimento;
    @JoinColumn(name = "capitolo", referencedColumnName = "id")
    @ManyToOne
    private Capitolo capitolo;
    @Column(name="articolo")
    private String articolo;
    @Column(name="codicemeccanografico")
    private String codiceMeccanografico;
    @Column(name="impegno")
    private String impegno;
    @Column(name="sottoimpegno")
    private String sottoImpegno;
    @Column(name="descrizioneimpegno")
    private String descrizioneImpegno;
    @Column(name="annoimpegno")
    private Long annoImpegno;
    @Column(name="importo")
    private BigDecimal importo;
    @Column(name="importoimpegnoaccertamento")
    private BigDecimal importoImpegnoAccertamento;
    @Column(name="annoesercizio")
    private Long annoEsercizio;
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

    public String getArchivio() {
        return archivio;
    }

    public void setArchivio(String archivio) {
        this.archivio = archivio;
    }

    public Determina getDetermina() {
        return determina;
    }

    public void setDetermina(Determina determina) {
        this.determina = determina;
    }

    public String getEu() {
        return eu;
    }

    public void setEu(String eu) {
        this.eu = eu;
    }

    public String getTipoMovimento() {
        return tipoMovimento;
    }

    public void setTipoMovimento(String tipoMovimento) {
        this.tipoMovimento = tipoMovimento;
    }

    public Capitolo getCapitolo() {
        return capitolo;
    }

    public void setCapitolo(Capitolo capitolo) {
        this.capitolo = capitolo;
    }

    public String getArticolo() {
        return articolo;
    }

    public void setArticolo(String articolo) {
        this.articolo = articolo;
    }

    public String getCodiceMeccanografico() {
        return codiceMeccanografico;
    }

    public void setCodiceMeccanografico(String codiceMeccanografico) {
        this.codiceMeccanografico = codiceMeccanografico;
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

    public String getDescrizioneImpegno() {
        return descrizioneImpegno;
    }

    public void setDescrizioneImpegno(String descrizioneImpegno) {
        this.descrizioneImpegno = descrizioneImpegno;
    }

    public Long getAnnoImpegno() {
        return annoImpegno;
    }

    public void setAnnoImpegno(Long annoImpegno) {
        this.annoImpegno = annoImpegno;
    }

    public BigDecimal getImporto() {
        return importo;
    }

    public void setImporto(BigDecimal importo) {
        this.importo = importo;
    }

    public BigDecimal getImportoImpegnoAccertamento() {
        return importoImpegnoAccertamento;
    }

    public void setImportoImpegnoAccertamento(BigDecimal importoImpegnoAccertamento) {
        this.importoImpegnoAccertamento = importoImpegnoAccertamento;
    }

    public Long getAnnoEsercizio() {
        return annoEsercizio;
    }

    public void setAnnoEsercizio(Long annoEsercizio) {
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
        if (!(object instanceof MovimentoDetermina)) {
            return false;
        }
        MovimentoDetermina other = (MovimentoDetermina) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.axiastudio.suite.deliberedetermine.entities.MovimentoDetermina[ id=" + id + " ]";
    }

}
