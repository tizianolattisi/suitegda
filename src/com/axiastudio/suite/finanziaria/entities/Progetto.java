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
package com.axiastudio.suite.finanziaria.entities;

import com.axiastudio.suite.base.entities.Ufficio;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
@Entity
@Table(schema="FINANZIARIA")
@SequenceGenerator(name="genprogetto", sequenceName="finanziaria.progetto_id_seq", initialValue=1, allocationSize=1)
public class Progetto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genprogetto")
    private Long id;
    @Column(name="anno")
    private Integer anno;
    @Column(name="codiceprogetto")
    private String codiceProgetto;
    @Column(name="progetto")
    private String progetto;
    @JoinColumn(name = "ufficioresponsabile", referencedColumnName = "id")
    @ManyToOne
    private Ufficio ufficioResponsabile;
    @Column(name="investimento")
    private String investimento;
    @Column(name="servizio")
    private String servizio;
    @Column(name="motivazione")
    private String motivazione;
    @Column(name="descrizione")
    private String descrizione;
    @Column(name="referentepolitico")
    private String referentePolitico;
    @Column(name="approvazioneconsiglio")
    private Boolean approvazioneConsiglio=Boolean.FALSE;
    @Column(name="dataconsiglio")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataConsiglio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Integer getAnno() {
        return anno;
    }

    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    public String getCodiceProgetto() {
        return codiceProgetto;
    }

    public void setCodiceProgetto(String codiceProgetto) {
        this.codiceProgetto = codiceProgetto;
    }

    public String getProgetto() {
        return progetto;
    }

    public void setProgetto(String progetto) {
        this.progetto = progetto;
    }

    public Ufficio getUfficioResponsabile() {
        return ufficioResponsabile;
    }

    public void setUfficioResponsabile(Ufficio ufficioResponsabile) {
        this.ufficioResponsabile = ufficioResponsabile;
    }

    public String getInvestimento() {
        return investimento;
    }

    public void setInvestimento(String investimento) {
        this.investimento = investimento;
    }

    public String getServizio() {
        return servizio;
    }

    public void setServizio(String servizio) {
        this.servizio = servizio;
    }

    public String getMotivazione() {
        return motivazione;
    }

    public void setMotivazione(String motivazione) {
        this.motivazione = motivazione;
    }

    public String getReferentePolitico() {
        return referentePolitico;
    }

    public void setReferentePolitico(String referentePolitico) {
        this.referentePolitico = referentePolitico;
    }

    public Boolean getApprovazioneConsiglio() {
        return approvazioneConsiglio;
    }

    public void setApprovazioneConsiglio(Boolean approvazioneConsiglio) {
        this.approvazioneConsiglio = approvazioneConsiglio;
    }

    public Date getDataConsiglio() {
        return dataConsiglio;
    }

    public void setDataConsiglio(Date dataConsiglio) {
        this.dataConsiglio = dataConsiglio;
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
        if (!(object instanceof Progetto)) {
            return false;
        }
        Progetto other = (Progetto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descrizione;
    }
    
}
