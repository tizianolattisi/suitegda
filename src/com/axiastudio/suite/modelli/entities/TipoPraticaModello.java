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

import com.axiastudio.suite.pratiche.entities.TipoPratica;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
@Entity
@Table(schema="modelli")
@SequenceGenerator(name="gentipopraticamodello", sequenceName="modelli.tipopraticamodello_id_seq", initialValue=1, allocationSize=1)
public class TipoPraticaModello implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="gentipopraticamodello")
    private Long id;
    @JoinColumn(name = "tipopratica", referencedColumnName = "id")
    @ManyToOne
    private TipoPratica tipopratica;
    @JoinColumn(name = "modello", referencedColumnName = "id")
    @ManyToOne
    private Modello modello;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoPratica getTipopratica() {
        return tipopratica;
    }

    public void setTipopratica(TipoPratica tipopratica) {
        this.tipopratica = tipopratica;
    }

    public Modello getModello() {
        return modello;
    }

    public void setModello(Modello modello) {
        this.modello = modello;
    }
}
