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
package com.axiastudio.suite.pratiche.entities;

import com.axiastudio.suite.procedimenti.entities.Procedimento;
import com.axiastudio.suite.procedimenti.entities.TipoPraticaProcedimento;
import com.axiastudio.suite.protocollo.entities.Fascicolo;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import javax.persistence.*;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
@Entity
@Table(schema="PRATICHE")
@SequenceGenerator(name="gentipopratica", sequenceName="pratiche.tipopratica_id_seq", initialValue=1, allocationSize=1)
public class TipoPratica implements Serializable, Comparable<TipoPratica> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="gentipopratica")
    private Long id;
    @Column(name="codice")
    private String codice;
    @Column(name="descrizione")
    private String descrizione;
    @JoinColumn(name = "tipopadre", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private TipoPratica tipopadre;
    @JoinColumn(name="procedimento", referencedColumnName = "id")
    @Column(name="formulacodifica")
    private String formulacodifica;
    @Column(name="lunghezzaprogressivo")
    private Integer lunghezzaprogressivo;
    @Column(name="progressivoanno")
    private Boolean progressivoanno=false;
    @Column(name="progressivogiunta")
    private Boolean progressivogiunta=false;
    @JoinColumn(name = "fascicolo", referencedColumnName = "id")
    @ManyToOne
    private Fascicolo fascicolo;
    @Column(name="foglia")
    private Boolean foglia=false;
    @Column(name="approvata")
    private Boolean approvata=false;
    @Column(name="obsoleta")
    private Boolean obsoleta=false;
    @OneToMany(mappedBy = "tipopratica", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<TipoPraticaProcedimento> tipopraticaProcedimentoCollection;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public TipoPratica getTipopadre() {
        return tipopadre;
    }

    public void setTipopadre(TipoPratica tipopadre) {
        this.tipopadre = tipopadre;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Procedimento getProcedimento() {
        Collection<TipoPraticaProcedimento> c = getTipopraticaProcedimentoCollection();
        if( c.size() != 1 ){
            return null;
        }
        TipoPraticaProcedimento tpp = (TipoPraticaProcedimento) c.toArray()[0];
        return tpp.getProcedimento();
    }

    public String getFormulacodifica() {
        return formulacodifica;
    }

    public void setFormulacodifica(String formulacodifica) {
        this.formulacodifica = formulacodifica;
    }

    public Integer getLunghezzaprogressivo() {
        return lunghezzaprogressivo;
    }

    public void setLunghezzaprogressivo(Integer lunghezzaprogressivo) {
        this.lunghezzaprogressivo = lunghezzaprogressivo;
    }

    public Boolean getProgressivoanno() {
        return progressivoanno;
    }

    public void setProgressivoanno(Boolean progressivoanno) {
        this.progressivoanno = progressivoanno;
    }

    public Boolean getProgressivogiunta() {
        return progressivogiunta;
    }

    public void setProgressivogiunta(Boolean progressivogiunta) {
        this.progressivogiunta = progressivogiunta;
    }

    public Fascicolo getFascicolo() {
        return fascicolo;
    }

    public void setFascicolo(Fascicolo fascicolo) {
        this.fascicolo = fascicolo;
    }

    public Boolean getFoglia() {
        return foglia;
    }

    public void setFoglia(Boolean foglia) {
        this.foglia = foglia;
    }

    public Boolean getApprovata() {
        return approvata;
    }

    public void setApprovata(Boolean approvata) {
        this.approvata = approvata;
    }

    public Boolean getObsoleta() {
        return obsoleta;
    }

    public void setObsoleta(Boolean obsoleta) {
        this.obsoleta = obsoleta;
    }

    public Collection<TipoPraticaProcedimento> getTipopraticaProcedimentoCollection() {
        return tipopraticaProcedimentoCollection;
    }

    public void setTipopraticaProcedimentoCollection(Collection<TipoPraticaProcedimento> tipopraticaProcedimentoCollection) {
        this.tipopraticaProcedimentoCollection = tipopraticaProcedimentoCollection;
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
        if (!(object instanceof TipoPratica)) {
            return false;
        }
        TipoPratica other = (TipoPratica) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return " "+this.getCodice()+" - "+this.getDescrizione();
    }

    @Override
    public int compareTo(TipoPratica o) {
        return Comparators.CODICE.compare(this, o);
    }

    public static class Comparators {
        public static Comparator<TipoPratica> CODICE = new Comparator<TipoPratica>() {
            @Override
            public int compare(TipoPratica o1, TipoPratica o2) {
                return o1.codice.compareTo(o2.codice);
            }
        };
    }

    
}
