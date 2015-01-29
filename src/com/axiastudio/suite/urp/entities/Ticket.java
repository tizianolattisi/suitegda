package com.axiastudio.suite.urp.entities;

import com.axiastudio.suite.base.entities.Utente;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * User: tiziano
 * Date: 16/11/14
 * Time: 21:05
 */
@Entity
@Table(schema="urp")
@SequenceGenerator(name="genticket", sequenceName="urp.ticket_id_seq", initialValue=1, allocationSize=1)
public class Ticket implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genticket")
    private Long id;

    @JoinColumn(name = "sportello", referencedColumnName = "id")
    @ManyToOne
    private Sportello sportello;

    @JoinColumn(name = "servizioalcittadino", referencedColumnName = "id")
    @ManyToOne
    private ServizioAlCittadino servizioalcittadino;

    @JoinColumn(name = "utente", referencedColumnName = "id")
    @ManyToOne
    private Utente utente;

    @Column(name="numero")
    private Integer numero;

    @Column(name="tsemesso")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date tsemesso;

    @Column(name="chiamato")
    private Boolean chiamato=false;

    @Column(name="tschiamato")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date tschiamato;

    @Column(name="servito")
    private Boolean servito=false;

    @Column(name="annullato")
    private Boolean annullato=false;

    @Column(name="tschiuso")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date tschiuso;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sportello getSportello() {
        return sportello;
    }

    public void setSportello(Sportello sportello) {
        this.sportello = sportello;
    }

    public ServizioAlCittadino getServizioalcittadino() {
        return servizioalcittadino;
    }

    public void setServizioalcittadino(ServizioAlCittadino servizioAlCittadino) {
        this.servizioalcittadino = servizioAlCittadino;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Date getTsemesso() {
        return tsemesso;
    }

    public void setTsemesso(Date tsemesso) {
        this.tsemesso = tsemesso;
    }

    public Boolean getChiamato() {
        return chiamato;
    }

    public void setChiamato(Boolean chiamato) {
        this.chiamato = chiamato;
    }

    public Date getTschiamato() {
        return tschiamato;
    }

    public void setTschiamato(Date tschiamato) {
        this.tschiamato = tschiamato;
    }

    public Boolean getServito() {
        return servito;
    }

    public void setServito(Boolean servito) {
        this.servito = servito;
    }

    public Boolean getAnnullato() {
        return annullato;
    }

    public void setAnnullato(Boolean annullato) {
        this.annullato = annullato;
    }

    public Date getTschiuso() {
        return tschiuso;
    }

    public void setTschiuso(Date tschiuso) {
        this.tschiuso = tschiuso;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ticket)) {
            return false;
        }
        Ticket other = (Ticket) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}
