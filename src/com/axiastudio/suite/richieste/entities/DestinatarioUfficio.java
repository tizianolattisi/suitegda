package com.axiastudio.suite.richieste.entities;

import com.axiastudio.suite.base.entities.Ufficio;
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
@SequenceGenerator(name="gendestinatarioufficio", sequenceName="richieste.destinatarioufficio_id_seq", initialValue=1, allocationSize=1)
@NamedQuery(name="trovaDestinatarioUfficio",
        query = "SELECT d FROM DestinatarioUfficio d JOIN d.destinatario u "
                + "JOIN u.ufficioUtenteCollection uu "
                + "WHERE d.letto = FALSE AND uu.ospite = FALSE "
                + "AND uu.utente.id = :id ORDER BY d.richiesta.data desc ")
public class DestinatarioUfficio extends DestinatarioRichiesta implements Serializable, IDestinatarioRichiesta {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="gendestinatarioufficio")
    private Long id;

    @JoinColumn(name="destinatario", referencedColumnName = "id")
    @ManyToOne
    private Ufficio destinatario;

    @JoinColumn(name="richiesta", referencedColumnName = "id")
    @ManyToOne
    private Richiesta richiesta;

    private Boolean conoscenza=Boolean.FALSE;
    private Boolean letto=Boolean.FALSE;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ufficio getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Ufficio destinatario) {
        this.destinatario = destinatario;
    }

    public Richiesta getRichiesta() {
        return richiesta;
    }

    public void setRichiesta(Richiesta richiesta) {
        this.richiesta = richiesta;
    }

    public Boolean getConoscenza() {
        return conoscenza;
    }

    public void setConoscenza(Boolean conoscenza) {
        this.conoscenza = conoscenza;
    }

    @Override
    public Boolean getLetto() {
        return letto;
    }

    public void setData(Date date){

    }

    public void setLetto(Boolean letto) {
        this.letto = letto;
    }

    @Override
    public Date getData(){
        return richiesta.getData();
    }

    @Override
    public String getTesto(){
        return richiesta.getTesto();
    }

    public void setTesto(String testo){
    }

    @Override
    public String getMittente(){
        return richiesta.getMittente().getNome();
    }

    public void setMittente(String mittente){
    }

    public Date getDatascadenza() {
        return richiesta.getDatascadenza();
    }

    public void setDatascadenza(Date data) {

    }

    @Override
    public String getNomedestinatario() {
        return destinatario.getDescrizione();
    }

    public void setNomedestinatario(String nome) {

    }

}
