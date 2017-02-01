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

import com.axiastudio.suite.base.entities.Utente;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
@Entity
@Table(schema="PRATICHE")
@SequenceGenerator(name="genvisto", sequenceName="pratiche.visto_id_seq", initialValue=1, allocationSize=1)
public class Visto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genvisto")
    private Long id;
    private String codiceCarica;
    @JoinColumn(name = "pratica", referencedColumnName = "idpratica")
    @ManyToOne
    private Pratica pratica;
    @JoinColumn(name = "fase", referencedColumnName = "id")
    @ManyToOne
    private Fase fase;
    @JoinColumn(name = "utente", referencedColumnName = "id")
    @ManyToOne
    private Utente utente;
    @JoinColumn(name = "responsabile", referencedColumnName = "id")
    @ManyToOne
    private Utente responsabile;
    @Column(name="data", insertable=false, updatable=false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;
    @Column(name="negato")
    Boolean negato=false;
    @Column(name="commento")
    String commento;
    @Column(name="completato")
    Boolean completato=false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodiceCarica() {
        return codiceCarica;
    }

    public void setCodiceCarica(String codiceCarica) {
        this.codiceCarica = codiceCarica;
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

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Utente getResponsabile() {
        return responsabile;
    }

    public void setResponsabile(Utente responsabile) {
        this.responsabile = responsabile;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Boolean getNegato() {
        return negato;
    }

    public void setNegato(Boolean negato) {
        this.negato = negato;
    }

    public String getCommento() {
        return commento;
    }

    public void setCommento(String commento) {
        this.commento = commento;
    }

    public Boolean getCompletato() {
        return completato;
    }

    public void setCompletato(Boolean completato) {
        this.completato = completato;
    }
}
