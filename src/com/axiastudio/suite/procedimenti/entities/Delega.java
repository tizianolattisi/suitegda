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
package com.axiastudio.suite.procedimenti.entities;

import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.finanziaria.entities.Servizio;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 * 
 * L'entità delega esprime la carica che un determinato soggetto ha per
 * titolarità o per delega.
 * 
 */
@Entity
@Table(schema="PROCEDIMENTI")
@SequenceGenerator(name="gendelega", sequenceName="procedimenti.delega_id_seq", initialValue=1, allocationSize=1)
@NamedQuery(name="trovaIncaricatiODelegati",
        query = "SELECT d FROM Delega d JOIN d.utente u "
                + "WHERE d.carica = :carica "
                // TODO: intervallo di validità
                + " ORDER BY d.titolare DESC")
public class Delega implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="gendelega")
    private Long id;    
    @JoinColumn(name = "carica", referencedColumnName = "id")
    @ManyToOne
    private Carica carica;
    @JoinColumn(name = "utente", referencedColumnName = "id")
    @ManyToOne
    private Utente utente;
    @JoinColumn(name = "ufficio", referencedColumnName = "id")
    @ManyToOne
    private Ufficio ufficio;
    @JoinColumn(name = "servizio", referencedColumnName = "id")
    @ManyToOne
    private Servizio servizio;
    @JoinColumn(name = "procedimento", referencedColumnName = "id")
    @ManyToOne
    private Procedimento procedimento;
    @Column(name="inizio")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date inizio;
    @Column(name="fine")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fine;
    @Column(name="titolare")
    private Boolean titolare=false;
    @Column(name="segretario")
    private Boolean segretario=false;
    @Column(name="delegato")
    private Boolean delegato=false;
    @Column(name="suassenza")
    private Boolean suassenza=false;
    @JoinColumn(name = "delegante", referencedColumnName = "id")
    @ManyToOne
    private Utente delegante;
    @Column(name="impedimento")
    private Boolean impedimento =false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Carica getCarica() {
        return carica;
    }

    public void setCarica(Carica carica) {
        this.carica = carica;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Ufficio getUfficio() {
        return ufficio;
    }

    public void setUfficio(Ufficio ufficio) {
        this.ufficio = ufficio;
    }

    public Servizio getServizio() {
        return servizio;
    }

    public void setServizio(Servizio servizio) {
        this.servizio = servizio;
    }

    public Procedimento getProcedimento() {
        return procedimento;
    }

    public void setProcedimento(Procedimento procedimento) {
        this.procedimento = procedimento;
    }

    public Date getInizio() {
        return inizio;
    }

    public void setInizio(Date inizio) {
        this.inizio = inizio;
    }

    public Date getFine() {
        return fine;
    }

    public void setFine(Date fine) {
        this.fine = fine;
    }

    public Boolean getTitolare() {
        return titolare;
    }

    public void setTitolare(Boolean titolare) {
        this.titolare = titolare;
    }

    public Boolean getSegretario() {
        return segretario;
    }

    public void setSegretario(Boolean segretario) {
        this.segretario = segretario;
    }

    public Boolean getDelegato() {
        return delegato;
    }

    public void setDelegato(Boolean delegato) {
        this.delegato = delegato;
    }

    public Boolean getSuassenza() {
        return suassenza;
    }

    public void setSuassenza(Boolean suassenza) {
        this.suassenza = suassenza;
    }

    public Utente getDelegante() {
        return delegante;
    }

    public void setDelegante(Utente delegante) {
        this.delegante = delegante;
    }

    public Boolean getImpedimento() {
        return impedimento;
    }

    public void setImpedimento(Boolean impedimento) {
        this.impedimento = impedimento;
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
        if (!(object instanceof Delega)) {
            return false;
        }
        Delega other = (Delega) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.axiastudio.suite.procedimenti.entities.Delega[ id=" + id + " ]";
    }
    
}
