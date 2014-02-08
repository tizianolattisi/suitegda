package com.axiastudio.suite.pubblicazioni.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * User: tiziano
 * Date: 16/01/14
 * Time: 11:35
 */

@Entity
@Table(schema="PUBBLICAZIONI")
@SequenceGenerator(name="gentipoattopubblicazione", sequenceName="pubblicazioni.tipoattopubblicazione_id_seq", initialValue=1, allocationSize=1)
public class TipoAttoPubblicazione implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genpubblicazione")
    private Long id;
    @Column(name="chiave")
    private String chiave;
    @Column(name="descrizione")
    private String descrizione;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChiave() {
        return chiave;
    }

    public void setChiave(String chiave) {
        this.chiave = chiave;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
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
        if (!(object instanceof TipoAttoPubblicazione)) {
            return false;
        }
        TipoAttoPubblicazione other = (TipoAttoPubblicazione) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getDescrizione();
    }
}
