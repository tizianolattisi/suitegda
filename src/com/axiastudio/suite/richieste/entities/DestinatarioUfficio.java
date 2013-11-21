package com.axiastudio.suite.richieste.entities;

import com.axiastudio.suite.base.entities.Ufficio;

import javax.persistence.*;
import java.io.Serializable;

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
public class DestinatarioUfficio implements Serializable{
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

}
