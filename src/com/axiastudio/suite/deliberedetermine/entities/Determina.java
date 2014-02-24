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

import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.deliberedetermine.DeterminaListener;
import com.axiastudio.suite.finanziaria.entities.Servizio;
import com.axiastudio.suite.pratiche.IDettaglio;
import com.axiastudio.suite.pratiche.entities.Pratica;
import com.axiastudio.suite.protocollo.IProtocollabile;
import com.axiastudio.suite.protocollo.entities.Protocollo;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
@Entity
@EntityListeners({DeterminaListener.class})
@Table(schema="deliberedetermine")
@SequenceGenerator(name="gendetermina", sequenceName="deliberedetermine.determina_id_seq", initialValue=1, allocationSize=1)
public class Determina implements Serializable, IDettaglio, IProtocollabile {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="gendetermina")
    private Long id;
    @JoinColumn(name = "idpratica", referencedColumnName = "idpratica")
    @ManyToOne(cascade=CascadeType.MERGE)
    private Pratica pratica;
    @Column(name="codiceinterno", unique=true)
    private String codiceinterno;
    @Column(name="oggetto", length=2048)
    private String oggetto;
    @OneToMany(mappedBy = "determina", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<ServizioDetermina> servizioDeterminaCollection;
    @OneToMany(mappedBy = "determina", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<MovimentoDetermina> movimentoDeterminaCollection;
    @OneToMany(mappedBy = "determina", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<UfficioDetermina> ufficioDeterminaCollection;
    @Column(name="dispesa")
    private Boolean dispesa=Boolean.FALSE;
    @Column(name="dientrata")
    private Boolean diEntrata=Boolean.FALSE;
    @Column(name="diregolarizzazione")
    private Boolean diRegolarizzazione=Boolean.FALSE;
    @Column(name="referentepolitico")
    private String referentePolitico;
    @Column(name="ufficioresponsabile")
    private String ufficioResponsabile;
    @Column(name="nomeresponsabile")
    private String nomeResponsabile;
    @Column(name="anno")
    private Integer anno;
    @Column(name="numero")
    private Integer numero;
    @Column(name="data")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date data;

    /* protocollo */
    @JoinColumn(name = "protocollo", referencedColumnName = "iddocumento")
    @ManyToOne
    private Protocollo protocollo;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Pratica getPratica() {
        return pratica;
    }

    @Override
    public void setPratica(Pratica pratica) {
        this.pratica = pratica;
    }

    @Override
    public String getIdpratica() {
        if( this.pratica != null ){
            return pratica.getIdpratica();
        }
        return null;
    }

    @Override
    public void setIdpratica(String idpratica) {
        // NOP
    }

    @Override
    public String getCodiceinterno() {
        return codiceinterno;
    }

    @Override
    public void setCodiceinterno(String codiceinterno) {
        this.codiceinterno = codiceinterno;
    }

    @Override
    public Servizio getServizio() {
        Collection<ServizioDetermina> serviziDetermina = getServizioDeterminaCollection();
        for( ServizioDetermina servizioDetermina: getServizioDeterminaCollection() ){
            if( servizioDetermina.getPrincipale() ){
                return servizioDetermina.getServizio();
            }
        }
        return null;
    }

    @Override
    public Ufficio getUfficio() {
        Servizio servizio = getServizio();
        if( servizio != null ){
            return servizio.getUfficio();
        }
        return null;
    }

    @Override
    public String getOggetto() {
        return oggetto;
    }

    @Override
    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }

    public Date getDatapratica() {
        if( this.pratica != null ){
            return pratica.getDatapratica();
        }
        return null;
    }

    public void setDatapratica(Date datapratica) {
        // NOP
    }

    public Collection<ServizioDetermina> getServizioDeterminaCollection() {
        return servizioDeterminaCollection;
    }

    public void setServizioDeterminaCollection(Collection<ServizioDetermina> servizioDeterminaCollection) {
        this.servizioDeterminaCollection = servizioDeterminaCollection;
    }

    public Collection<MovimentoDetermina> getMovimentoDeterminaCollection() {
        return movimentoDeterminaCollection;
    }

    public void setMovimentoDeterminaCollection(Collection<MovimentoDetermina> movimentoDeterminaCollection) {
        this.movimentoDeterminaCollection = movimentoDeterminaCollection;
    }

    public Collection<UfficioDetermina> getUfficioDeterminaCollection() {
        return ufficioDeterminaCollection;
    }

    public void setUfficioDeterminaCollection(Collection<UfficioDetermina> ufficioDeterminaCollection) {
        this.ufficioDeterminaCollection = ufficioDeterminaCollection;
    }

    public Boolean getDispesa() {
        return dispesa;
    }

    public void setDispesa(Boolean dispesa) {
        this.dispesa = dispesa;
    }

    public Boolean getDiEntrata() {
        return diEntrata;
    }

    public void setDiEntrata(Boolean diEntrata) {
        this.diEntrata = diEntrata;
    }

    public Boolean getDiRegolarizzazione() {
        return diRegolarizzazione;
    }

    public void setDiRegolarizzazione(Boolean diRegolarizzazione) {
        this.diRegolarizzazione = diRegolarizzazione;
    }

    public String getReferentePolitico() {
        return referentePolitico;
    }

    public void setReferentePolitico(String referentePolitico) {
        this.referentePolitico = referentePolitico;
    }

    public String getUfficioResponsabile() {
        return ufficioResponsabile;
    }

    public void setUfficioResponsabile(String ufficioResponsabile) {
        this.ufficioResponsabile = ufficioResponsabile;
    }

    public String getNomeResponsabile() {
        return nomeResponsabile;
    }

    public void setNomeResponsabile(String nomeResponsabile) {
        this.nomeResponsabile = nomeResponsabile;
    }

    public Integer getAnno() {
        return anno;
    }

    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Protocollo getProtocollo() {
        return protocollo;
    }

    public void setProtocollo(Protocollo protocollo) {
        this.protocollo = protocollo;
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
        if (!(object instanceof Determina)) {
            return false;
        }
        Determina other = (Determina) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.axiastudio.suite.deliberedetermine.entities.Determina[ id=" + id + " ]";
    }
    
}
