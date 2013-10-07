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
package com.axiastudio.suite.modelli.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
@Entity
@Table(schema="modelli")
@SequenceGenerator(name="genmodello", sequenceName="modelli.modello_id_seq", initialValue=1, allocationSize=1)
public class Modello implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genmodello")
    private Long id;
    @Column(name="titolo", length=255)
    private String titolo;
    @Column(name="descrizione", length=1024)
    private String descrizione;
    @Column(name="uri", length=2048)
    private String uri;
    @JoinColumn(name = "modellopadre", referencedColumnName = "id")
    @ManyToOne
    private Modello modellopadre;
    @OneToMany(mappedBy = "modello", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<TipoPraticaModello> tipoPraticaModelloCollection;
    @OneToMany(mappedBy = "modello", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<ProcedimentoModello> procedimentoModelloCollection;
    @OneToMany(mappedBy = "modello", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<Segnalibro> segnalibroCollection;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Modello getModellopadre() {
        return modellopadre;
    }

    public void setModellopadre(Modello modellopadre) {
        this.modellopadre = modellopadre;
    }

    public Collection<TipoPraticaModello> getTipoPraticaModelloCollection() {
        return tipoPraticaModelloCollection;
    }

    public void setTipoPraticaModelloCollection(Collection<TipoPraticaModello> tipoPraticaModelloCollection) {
        this.tipoPraticaModelloCollection = tipoPraticaModelloCollection;
    }

    public Collection<ProcedimentoModello> getProcedimentoModelloCollection() {
        return procedimentoModelloCollection;
    }

    public void setProcedimentoModelloCollection(Collection<ProcedimentoModello> procedimentoModelloCollection) {
        this.procedimentoModelloCollection = procedimentoModelloCollection;
    }

    public Collection<Segnalibro> getSegnalibroCollection() {
        return segnalibroCollection;
    }

    public void setSegnalibroCollection(Collection<Segnalibro> segnalibroCollection) {
        this.segnalibroCollection = segnalibroCollection;
    }

    @Override
    public String toString() {
        return this.descrizione;
    }

}
