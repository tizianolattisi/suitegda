/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.protocollo.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
@Entity
@Table(schema="PROTOCOLLO")
@SequenceGenerator(name="genfascicolo", sequenceName="fascicolo_id_seq", initialValue=1, allocationSize=1)
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
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "(" + categoria + "-" + classe + "-" + fascicolo + ") " + this.getDescrizione();
    }
    
}
