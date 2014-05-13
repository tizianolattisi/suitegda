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
package com.axiastudio.suite.protocollo.entities;

import com.axiastudio.suite.SuiteUtil;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
@Entity
@Table(schema="PROTOCOLLO")
@SequenceGenerator(name="genannullamentoprotocollo", sequenceName="protocollo.annullamentoprotocollo_id_seq", initialValue=1, allocationSize=1)
@NamedQuery(name="annullamentiRichiesti",
        query = "SELECT a FROM AnnullamentoPrototollo a WHERE a.autorizzato = FALSE AND uu.respinto = FALSE "
                + "ORDER BY a.datarichiesta DESC")
public class AnnullamentoProtocollo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genannullamentoprotocollo")
    private Long id;
    @JoinColumn(name = "protocollo", referencedColumnName = "iddocumento")
    @ManyToOne
    private Protocollo protocollo;
    @Column(name="datarichiesta")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date datarichiesta;
    @Column(name="esecutorerichiesta", length=40)
    private String esecutorerichiesta;
    @JoinColumn(name = "motivazioneannullamento", referencedColumnName = "id")
    @ManyToOne
    private MotivazioneAnnullamento motivazioneannullamento;
    @Column(name="dataautorizzazione")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataautorizzazione;
    @Column(name="esecutoreautorizzazione", length=40)
    private String esecutoreautorizzazione;
    @Column(name="respinto")
    private Boolean respinto=false;
    @Column(name="autorizzato")
    private Boolean autorizzato=false;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Protocollo getProtocollo() {
        return protocollo;
    }

    public void setProtocollo(Protocollo protocollo) {
        this.protocollo = protocollo;
    }

    public Date getDatarichiesta() {
        return datarichiesta;
    }

    public void setDatarichiesta(Date datarichiesta) {
        this.datarichiesta = datarichiesta;
    }

    public String getEsecutorerichiesta() {
        return esecutorerichiesta;
    }

    public void setEsecutorerichiesta(String esecutorerichiesta) {
        this.esecutorerichiesta = esecutorerichiesta;
    }

    public MotivazioneAnnullamento getMotivazioneannullamento() {
        return motivazioneannullamento;
    }

    public void setMotivazioneannullamento(MotivazioneAnnullamento motivazioneannullamento) {
        this.motivazioneannullamento = motivazioneannullamento;
    }

    public Date getDataautorizzazione() {
        return dataautorizzazione;
    }

    public void setDataautorizzazione(Date dataautorizzazione) {
        this.dataautorizzazione = dataautorizzazione;
    }

    public String getEsecutoreautorizzazione() {
        return esecutoreautorizzazione;
    }

    public void setEsecutoreautorizzazione(String esecutoreautorizzazione) {
        this.esecutoreautorizzazione = esecutoreautorizzazione;
    }

    public Boolean getRespinto() {
        return respinto;
    }

    public void setRespinto(Boolean respinta) {
        this.respinto = respinta;
    }

    public Boolean getAutorizzato() {
        return autorizzato;
    }

    public void setAutorizzato(Boolean autorizzato) {
        this.autorizzato = autorizzato;
    }

    public String getStatorichiesta() {
        if( this.getRespinto() ){
            return "respinta";
        } else if( this.getAutorizzato() ){
            return "autorizzata da " + this.getEsecutoreautorizzazione() + " il " + SuiteUtil.DATE_FORMAT.format(this.getDataautorizzazione());
        }
        return "in attesa";
    }
    
    public void setStatorichiesta(String stato) {
        
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
        if (!(object instanceof AnnullamentoProtocollo)) {
            return false;
        }
        AnnullamentoProtocollo other = (AnnullamentoProtocollo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.axiastudio.suite.protocollo.entities.AnnullamentoProtocollo[ id=" + id + " ]";
    }
    
}
