/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.procedimenti.entities;

import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.finanziaria.entities.Servizio;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(schema="PROCEDIMENTI")
@SequenceGenerator(name="gendelega", sequenceName="procedimenti.delega_id_seq", initialValue=1, allocationSize=1)
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
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.axiastudio.suite.procedimenti.entities.Delega[ id=" + id + " ]";
    }
    
}
