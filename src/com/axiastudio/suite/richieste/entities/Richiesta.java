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
package com.axiastudio.suite.richieste.entities;


import com.axiastudio.suite.base.entities.Utente;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(schema="RICHIESTE")
@SequenceGenerator(name="genrichiesta", sequenceName="richieste.richiesta_id_seq", initialValue=1, allocationSize=1)
public class Richiesta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genrichiesta")
    private Long id;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date data =  Calendar.getInstance().getTime();
    private String testo;
    @JoinColumn(name = "mittente", referencedColumnName = "id")
    @ManyToOne
    private Utente mittente;
    private Boolean cancellabile = Boolean.TRUE;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date datascadenza;
    private Integer giornipreavviso;
    @JoinColumn(name = "richiestaprecedente", referencedColumnName = "id")
    @ManyToOne
    private Richiesta richiestaprecedente;
    private Integer relazione;
    private Boolean richiestaautomatica = Boolean.FALSE;
    private Integer fase;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public Utente getMittente() {
        return mittente;
    }

    public void setMittente(Utente mittente) {
        this.mittente = mittente;
    }

    public Boolean getCancellabile() {
        return cancellabile;
    }

    public void setCancellabile(Boolean cancellabile) {
        this.cancellabile = cancellabile;
    }

    public Date getDatascadenza() {
        return datascadenza;
    }

    public void setDatascadenza(Date datascadenza) {
        this.datascadenza = datascadenza;
    }

    public Integer getGiornipreavviso() {
        return giornipreavviso;
    }

    public void setGiornipreavviso(Integer giornipreavviso) {
        this.giornipreavviso = giornipreavviso;
    }

    public Richiesta getRichiestaprecedente() {
        return richiestaprecedente;
    }

    public void setRichiestaprecedente(Richiesta richiestaprecedente) {
        this.richiestaprecedente = richiestaprecedente;
    }

    public Integer getRelazione() {
        return relazione;
    }

    public void setRelazione(Integer relazione) {
        this.relazione = relazione;
    }

    public Boolean getRichiestaautomatica() {
        return richiestaautomatica;
    }

    public void setRichiestaautomatica(Boolean richiestaautomatica) {
        this.richiestaautomatica = richiestaautomatica;
    }

    public Integer getFase() {
        return fase;
    }

    public void setFase(Integer fase) {
        this.fase = fase;
    }
}
