package com.axiastudio.suite.richieste.entities;

import com.axiastudio.suite.base.entities.Utente;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Mickey
 * Date: 21/11/13
 * Time: 14.30
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema="RICHIESTE")
@SequenceGenerator(name="gendestinatarioutente", sequenceName="richieste.destinatarioutente_id_seq", initialValue=1, allocationSize=1)
@NamedQuery(name="trovaDestinatarioUtente",
        query = "SELECT d FROM DestinatarioUtente d JOIN d.destinatario u "
                + "WHERE d.letto = FALSE "
                + "AND u.id = :id ORDER BY d.richiesta.data desc ")
public class DestinatarioUtente extends DestinatarioRichiesta implements Serializable, IDestinatarioRichiesta {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="gendestinatarioutente")
    private Long id;

    @JoinColumn(name="destinatario", referencedColumnName = "id")
    @ManyToOne
    private Utente destinatario;

    @JoinColumn(name="richiesta", referencedColumnName = "id")
    @ManyToOne
    private Richiesta richiesta;

    private Boolean letto=Boolean.FALSE;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Utente getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Utente destinatario) {
        this.destinatario = destinatario;
    }

    @Override
    public Richiesta getRichiesta() {
        return richiesta;
    }

    public void setRichiesta(Richiesta richiesta) {
        this.richiesta = richiesta;
    }

    @Override
    public Boolean getLetto() {
        return letto;
    }

    public void setLetto(Boolean letto) {
        this.letto = letto;
    }

    public void setData(Date date){

    }

    @Override
    public Date getData(){
        return richiesta.getData();
    }

    public void setTesto(String testo){

    }

    @Override
    public String getTesto(){
        return richiesta.getTesto();
    }

    public void setMittente(){

    }

    @Override
    public String getMittente(){
        return richiesta.getMittente().getNome();
    }
}
