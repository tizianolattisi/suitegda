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
package com.axiastudio.suite.anagrafiche.entities;

import com.axiastudio.suite.SuiteUtil;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
@Entity
@Table(schema="ANAGRAFICHE")
@SequenceGenerator(name="gentitolostudiosoggetto", sequenceName="anagrafiche.titolostudiosoggetto_id_seq", initialValue=1, allocationSize=1)
public class TitoloStudioSoggetto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="gentitolostudiosoggetto")
    private Long id;
    @JoinColumn(name = "soggetto", referencedColumnName = "id")
    @ManyToOne
    private Soggetto soggetto;
    @JoinColumn(name = "titolostudio", referencedColumnName = "id")
    @ManyToOne
    private TitoloStudio titolostudio;
    @Column(name="dettaglio")
    private String dettaglio;
    @Column(name="datatitolo")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date datatitolo;
    @Column(name="secondario")
    private Boolean secondario=false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Soggetto getSoggetto() {
        return soggetto;
    }

    public void setSoggetto(Soggetto soggetto) {
        this.soggetto = soggetto;
    }

    public TitoloStudio getTitolostudio() {
        return titolostudio;
    }

    public void setTitolostudio(TitoloStudio titolostudio) {
        this.titolostudio = titolostudio;
    }

    public String getDettaglio() {
        return dettaglio;
    }

    public void setDettaglio(String dettaglio) {
        this.dettaglio = dettaglio;
    }

    public Date getDatatitolo() {
        return datatitolo;
    }

    public void setDatatitolo(Date datatitolo) {
        this.datatitolo = datatitolo;
    }

    public Boolean getSecondario() {
        return secondario;
    }

    public void setSecondario(Boolean secondario) {
        this.secondario = secondario;
    }
    
    /*
     * Il predicato esprime la relazione nel corretto verso
     */
    public String getPredicato(){
        String out = "";
        out += this.getTitolostudio();
        if( this.getDatatitolo() != null ){
            out += " in data " + SuiteUtil.DATE_FORMAT.format(this.getDatatitolo());
        }
        return out;
    }
    
    public void setPredicato(String predicato){
        // non deve fare nulla
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
        if (!(object instanceof TitoloStudioSoggetto)) {
            return false;
        }
        TitoloStudioSoggetto other = (TitoloStudioSoggetto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.axiastudio.suite.anagrafiche.entities.GruppoSoggetto[ id=" + id + " ]";
    }
    
}
