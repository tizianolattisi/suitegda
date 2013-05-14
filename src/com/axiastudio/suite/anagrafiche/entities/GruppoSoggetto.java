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
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
@Entity
@Table(schema="ANAGRAFICHE")
@SequenceGenerator(name="gengrupposoggetto", sequenceName="anagrafiche.grupposoggetto_id_seq", initialValue=1, allocationSize=1)
public class GruppoSoggetto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="gengrupposoggetto")
    private Long id;
    @JoinColumn(name = "soggetto", referencedColumnName = "id")
    @ManyToOne
    private Soggetto soggetto;
    @JoinColumn(name = "gruppo", referencedColumnName = "id")
    @ManyToOne
    private Gruppo gruppo;
    @Column(name="datanascita")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date datanascita;
    @Column(name="datacessazione")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date datacessazione;

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

    public Gruppo getGruppo() {
        return gruppo;
    }

    public void setGruppo(Gruppo gruppo) {
        this.gruppo = gruppo;
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
    
    /*
     * Il predicato esprime la relazione nel corretto verso
     */
    public String getPredicato(){
        String out = "";
        out += this.getGruppo();
        if( this.getDatanascita() != null ){
            out += " dal " + SuiteUtil.DATE_FORMAT.format(this.getDatanascita());
        }
        if( this.getDatacessazione()!= null ){
            out += " fino al " + SuiteUtil.DATE_FORMAT.format(this.getDatacessazione());
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
        if (!(object instanceof GruppoSoggetto)) {
            return false;
        }
        GruppoSoggetto other = (GruppoSoggetto) object;
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
