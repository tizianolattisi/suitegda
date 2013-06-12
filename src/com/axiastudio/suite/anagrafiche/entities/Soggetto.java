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
package com.axiastudio.suite.anagrafiche.entities;

import com.axiastudio.suite.generale.TimeStampedListener;
import com.axiastudio.suite.generale.ITimeStamped;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
@Entity
@EntityListeners({TimeStampedListener.class})
@Table(schema="ANAGRAFICHE")
@SequenceGenerator(name="gensoggetto", sequenceName="anagrafiche.soggetto_id_seq", initialValue=1, allocationSize=1)
public class Soggetto implements Serializable, ITimeStamped {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="gensoggetto")
    private Long id;
    @Column(name="tipo", nullable=false)
    @Enumerated(EnumType.STRING)
    private TipoSoggetto tipo = TipoSoggetto.PERSONA;
    @Column(name="sessosoggetto")
    @Enumerated(EnumType.STRING)
    private SessoSoggetto sessosoggetto;
    @JoinColumn(name = "titolosoggetto", referencedColumnName = "id")
    @ManyToOne
    private TitoloSoggetto titolosoggetto;
    @Column(name="nick")
    private String nick;
    @Column(name="nome")
    private String nome;
    @Column(name="cognome")
    private String cognome;
    @Column(name="codicefiscale")
    private String codicefiscale;
    @Column(name="partitaiva")
    private String partitaiva;
    @Column(name="ragionesociale")
    private String ragionesociale;
    @Column(name="denominazione")
    private String denominazione;
    @Column(name="denominazione2")
    private String denominazione2;
    @Column(name="denominazione3")
    private String denominazione3;
    @Column(name="referente")
    private String referente;
    @Column(name="comunedinascita")
    private String comunedinascita;
    @Column(name="datanascita")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date datanascita;
    @Column(name="datacessazione")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date datacessazione;
    @Column(name="descrizionecessazione")
    private String descrizionecessazione;
    @JoinColumn(name = "alboprofessionale", referencedColumnName = "id")
    @ManyToOne
    private AlboProfessionale alboprofessionale;
    @Column(name="provinciaalbo", length=2)
    private String provinciaalbo;
    @Column(name="numeroiscrizionealbo")
    private String numeroiscrizionealbo;
    @Column(name="indicepao")
    private String indicepao;
    @Column(name="indicepaaoo")
    private String indicepaaoo;
    @Column(name="residente")
    private Boolean residente=false;
    @Column(name="codiceanagrafe")
    private String codiceanagrafe;
    @OneToMany(mappedBy = "soggetto", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<Indirizzo> indirizzoCollection;
    @OneToMany(mappedBy = "soggetto", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<GruppoSoggetto> gruppoSoggettoCollection;
    @OneToMany(mappedBy = "soggetto", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<Riferimento> riferimentoCollection;
    @OneToMany(mappedBy = "soggetto", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<RelazioneSoggetto> relazioneSoggettoCollection;
    @OneToMany(mappedBy = "soggetto", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<TitoloStudioSoggetto> titolostudioSoggettoCollection;

    /* timestamped */
    @Column(name="rec_creato", insertable=false, updatable=false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date recordcreato;
    @Column(name="rec_creato_da")
    private String recordcreatoda;
    @Column(name="rec_modificato")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date recordmodificato;
    @Column(name="rec_modificato_da")
    private String recordmodificatoda;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRagionesociale() {
        return ragionesociale;
    }

    public void setRagionesociale(String ragionesociale) {
        this.ragionesociale = ragionesociale;
    }

    public TipoSoggetto getTipo() {
        return tipo;
    }

    public void setTipo(TipoSoggetto tipo) {
        this.tipo = tipo;
    }

    public SessoSoggetto getSessosoggetto() {
        return sessosoggetto;
    }

    public void setSessosoggetto(SessoSoggetto sessoSoggetto) {
        this.sessosoggetto = sessoSoggetto;
    }

    public TitoloSoggetto getTitolosoggetto() {
        return titolosoggetto;
    }

    public void setTitolosoggetto(TitoloSoggetto titolosoggetto) {
        this.titolosoggetto = titolosoggetto;
    }

    public String getCodicefiscale() {
        return codicefiscale;
    }

    public void setCodicefiscale(String coficeFiscale) {
        this.codicefiscale = coficeFiscale;
    }

    public String getDenominazione() {
        return denominazione;
    }

    public void setDenominazione(String denominazione) {
        this.denominazione = denominazione;
    }

    public Collection<Indirizzo> getIndirizzoCollection() {
        return indirizzoCollection;
    }

    public void setIndirizzoCollection(Collection<Indirizzo> indirizzoCollection) {
        this.indirizzoCollection = indirizzoCollection;
    }

    public String getPartitaiva() {
        return partitaiva;
    }

    public void setPartitaiva(String partitaiva) {
        this.partitaiva = partitaiva;
    }

    public String getDenominazione2() {
        return denominazione2;
    }

    public void setDenominazione2(String denominazione2) {
        this.denominazione2 = denominazione2;
    }

    public String getDenominazione3() {
        return denominazione3;
    }

    public void setDenominazione3(String denominazione3) {
        this.denominazione3 = denominazione3;
    }

    public String getReferente() {
        return referente;
    }

    public void setReferente(String referente) {
        this.referente = referente;
    }

    public String getComunedinascita() {
        return comunedinascita;
    }

    public void setComunedinascita(String comunedinascita) {
        this.comunedinascita = comunedinascita;
    }

    public Date getDatanascita() {
        return datanascita;
    }

    public void setDatanascita(Date datanascita) {
        this.datanascita = datanascita;
    }

    public Date getDatacessazione() {
        return datacessazione;
    }

    public void setDatacessazione(Date datacessazione) {
        this.datacessazione = datacessazione;
    }

    public String getDescrizionecessazione() {
        return descrizionecessazione;
    }

    public void setDescrizionecessazione(String descrizionecessazione) {
        this.descrizionecessazione = descrizionecessazione;
    }

    public AlboProfessionale getAlboprofessionale() {
        return alboprofessionale;
    }

    public void setAlboprofessionale(AlboProfessionale alboprofessionale) {
        this.alboprofessionale = alboprofessionale;
    }

    public String getProvinciaalbo() {
        return provinciaalbo;
    }

    public void setProvinciaalbo(String provinciaalbo) {
        this.provinciaalbo = provinciaalbo;
    }

    public String getNumeroiscrizionealbo() {
        return numeroiscrizionealbo;
    }

    public void setNumeroiscrizionealbo(String numeroiscrizionealbo) {
        this.numeroiscrizionealbo = numeroiscrizionealbo;
    }

    public String getIndicepao() {
        return indicepao;
    }

    public void setIndicepao(String indicepao) {
        this.indicepao = indicepao;
    }

    public String getIndicepaaoo() {
        return indicepaaoo;
    }

    public void setIndicepaaoo(String indicepaaoo) {
        this.indicepaaoo = indicepaaoo;
    }

    public Boolean getResidente() {
        return residente;
    }

    public void setResidente(Boolean residente) {
        this.residente = residente;
    }

    public String getCodiceanagrafe() {
        return codiceanagrafe;
    }

    public void setCodiceanagrafe(String codiceanagrafe) {
        this.codiceanagrafe = codiceanagrafe;
    }

    public Collection<GruppoSoggetto> getGruppoSoggettoCollection() {
        return gruppoSoggettoCollection;
    }

    public void setGruppoSoggettoCollection(Collection<GruppoSoggetto> gruppoSoggettoCollection) {
        this.gruppoSoggettoCollection = gruppoSoggettoCollection;
    }

    public Collection<RelazioneSoggetto> getRelazioneSoggettoCollection() {
        return relazioneSoggettoCollection;
    }

    public void setRelazioneSoggettoCollection(Collection<RelazioneSoggetto> relazioneSoggettoCollection) {
        this.relazioneSoggettoCollection = relazioneSoggettoCollection;
    }

    public Collection<Riferimento> getRiferimentoCollection() {
        return riferimentoCollection;
    }

    public void setRiferimentoCollection(Collection<Riferimento> riferimentoCollection) {
        this.riferimentoCollection = riferimentoCollection;
    }

    public Collection<TitoloStudioSoggetto> getTitolostudioSoggettoCollection() {
        return titolostudioSoggettoCollection;
    }

    public void setTitolostudioSoggettoCollection(Collection<TitoloStudioSoggetto> titolostudioSoggettoCollection) {
        this.titolostudioSoggettoCollection = titolostudioSoggettoCollection;
    }

/* In ricerca, ritorna l'unione delle denominazioni dei soggetti */
    
    public String getDescrizione(){
        String out = "";
        if ( this.denominazione != null) {
            out += " " + this.denominazione;
        }
        if ( this.denominazione2 != null) {
            out += " " + this.denominazione2;
        }
        if ( this.denominazione3 != null) {
            out += " " + this.denominazione3;
        }
        if ( this.ragionesociale != null) {
            out += " " + this.ragionesociale;
        }
        if ( this.cognome != null) {
            out += " " + this.cognome;
        }
        if ( this.nome != null) {
            out += " " + this.nome;
        }
        return out.trim();
    }
    
    public void setDescrizione(String descrizione){
        // non deve fare nulla
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Date getRecordcreato() {
        return recordcreato;
    }

    public void setRecordcreato(Date recordcreato) {
        this.recordcreato = recordcreato;
    }

    public String getRecordcreatoda() {
        return recordcreatoda;
    }

    public void setRecordcreatoda(String recordcreatoda) {
        this.recordcreatoda = recordcreatoda;
    }

    public Date getRecordmodificato() {
        return recordmodificato;
    }

    public void setRecordmodificato(Date recordmodificato) {
        this.recordmodificato = recordmodificato;
    }

    public String getRecordmodificatoda() {
        return recordmodificatoda;
    }

    public void setRecordmodificatoda(String recordmodificatoda) {
        this.recordmodificatoda = recordmodificatoda;
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
        if (!(object instanceof Soggetto)) {
            return false;
        }
        Soggetto other = (Soggetto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if( this.tipo == null ){
            return "-";
        }
        String out = "(" + this.id + ") ";
        if( this.tipo.equals(TipoSoggetto.PERSONA) ){
            return out + this.nome+" "+this.cognome;
        } else if( this.tipo.equals(TipoSoggetto.AZIENDA) ){
            return out + this.ragionesociale;
        } else if ( this.tipo.equals(TipoSoggetto.ENTE) ){
            return out + this.denominazione;
        }
        return "-";
        //return "com.axiastudio.suite.anagrafiche.entities.Soggetto[ id=" + id + " ]";
    }
    
}
