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
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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
    @Column(name="tipomovimento")
    @Enumerated(EnumType.STRING)
    private TipoMovimento tipoMovimento;
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
    @Column(name="annoesercizio")
    private Long annoEsercizio;

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

    public TipoMovimento getTipoMovimento() {
        return tipoMovimento;
    }

    public void setTipoMovimento(TipoMovimento tipoMovimento) {
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

    public Long getAnnoEsercizio() {
        return annoEsercizio;
    }

    public void setAnnoEsercizio(Long annoEsercizio) {
        this.annoEsercizio = annoEsercizio;
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
