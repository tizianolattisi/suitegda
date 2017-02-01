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
package com.axiastudio.suite.protocollo.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
@Entity
@Table(schema="PROTOCOLLO")
@SequenceGenerator(name="genfascicolo", sequenceName="protocollo.fascicolo_id_seq", initialValue=1, allocationSize=1)
public class Fascicolo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genfascicolo")
    private Long id;
    @Column(name="categoria")
    private Integer categoria;
    @Column(name="classe")
    private Integer classe;
    @Column(name="fascicolo")
    private Integer fascicolo;
    @Column(name="descrizione")
    private String descrizione;
    @Column(name="note")
    private String note;
    @Column(name="dal")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dal;
    @Column(name="al")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date al;

    @Transient
    private String codicefascicolo;
    @Transient
    private String validita;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCategoria() {
        return categoria;
    }

    public void setCategoria(Integer categoria) {
        this.categoria = categoria;
    }

    public Integer getClasse() {
        return classe;
    }

    public void setClasse(Integer classe) {
        this.classe = classe;
    }

    public Integer getFascicolo() {
        return fascicolo;
    }

    public void setFascicolo(Integer fascicolo) {
        this.fascicolo = fascicolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCodicefascicolo() {
        return this.getCategoria() + "-" + this.getClasse() + "-" + this.getFascicolo();
    }

    public void setCodicefascicolo(String codicefascicolo) {
    }

    public Date getDal() {
        return dal;
    }

    public void setDal(Date dal) {
        this.dal = dal;
    }

    public Date getAl() {
        return al;
    }

    public void setAl(Date al) {
        this.al = al;
    }

    public String getDescTitolario () {
         return "Titolario valido " + getValidita();
    }

    public String getValidita() {
        String desc="dal ";
        if ( this.getDal() != null ){
            desc += (new SimpleDateFormat("dd/MM/yyyy")).format(this.getDal());
        }
        if ( this.getAl() != null ){
            desc += " al "+ (new SimpleDateFormat("dd/MM/yyyy")).format(this.getAl());
        }
        return desc;
    }

    public void setValidita(String validita) {
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
        if (!(object instanceof Fascicolo)) {
            return false;
        }
        Fascicolo other = (Fascicolo) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return " (" + categoria + "-" + classe + "-" + fascicolo + ") " + this.getDescrizione();
    }
    
}
