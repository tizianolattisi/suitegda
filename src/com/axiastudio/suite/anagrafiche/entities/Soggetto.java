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

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
@Entity
@Table(schema="ANAGRAFICHE")
@SequenceGenerator(name="gensoggetto", sequenceName="anagrafiche.soggetto_id_seq", initialValue=1, allocationSize=1)
public class Soggetto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="gensoggetto")
    private Long id;
    @Column(name="tipo", nullable=false)
    @Enumerated(EnumType.STRING)
    private TipoSoggetto tipo = TipoSoggetto.PERSONA;
    @Column(name="sessosoggetto")
    @Enumerated(EnumType.STRING)
    private SessoSoggetto sessoSoggetto;
    @Column(name="titolosoggetto")
    @Enumerated(EnumType.STRING)
    private TitoloSoggetto titoloSoggetto;
    @Column(name="nick")
    private String nick;
    @Column(name="nome")
    private String nome;
    @Column(name="cognome")
    private String cognome;
    @Column(name="codicefiscale")
    private String codiceFiscale;
    @Column(name="ragionesociale")
    private String ragionesociale;
    @Column(name="denominazione")
    private String denominazione;
    @OneToMany(mappedBy = "soggetto", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<Indirizzo> indirizzoCollection;

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

    public SessoSoggetto getSessoSoggetto() {
        return sessoSoggetto;
    }

    public void setSessoSoggetto(SessoSoggetto sessoSoggetto) {
        this.sessoSoggetto = sessoSoggetto;
    }

    public TitoloSoggetto getTitoloSoggetto() {
        return titoloSoggetto;
    }

    public void setTitoloSoggetto(TitoloSoggetto titoloSoggetto) {
        this.titoloSoggetto = titoloSoggetto;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String coficeFiscale) {
        this.codiceFiscale = coficeFiscale;
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
        if( this.tipo.equals(TipoSoggetto.PERSONA) ){
            return this.nome+" "+this.cognome;
        } else if( this.tipo.equals(TipoSoggetto.AZIENDA) ){
            return this.ragionesociale;
        } else if ( this.tipo.equals(TipoSoggetto.ENTE) ){
            return this.denominazione;
        }
        return "-";
        //return "com.axiastudio.suite.anagrafiche.entities.Soggetto[ id=" + id + " ]";
    }
    
}
