package com.axiastudio.suite.urp.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * User: tiziano
 * Date: 20/01/15
 * Time: 08:38
 */
@Entity
@Table(schema="urp")
@SequenceGenerator(name="gennotiziaaurp", sequenceName="urp.notiziaurp_id_seq", initialValue=1, allocationSize=1)
public class NotiziaURP {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="gennotiziaaurp")
    private Long id;

    @Column(name="titolo")
    private String titolo;

    @Column(name="testo")
    private String testo;

    @Column(name="iniziopubblicazione")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date iniziopubblicazione;

    @Column(name="finepubblicazione")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date finepubblicazione;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public Date getIniziopubblicazione() {
        return iniziopubblicazione;
    }

    public void setIniziopubblicazione(Date iniziopubblicazione) {
        this.iniziopubblicazione = iniziopubblicazione;
    }

    public Date getFinepubblicazione() {
        return finepubblicazione;
    }

    public void setFinepubblicazione(Date finepubblicazione) {
        this.finepubblicazione = finepubblicazione;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotiziaURP)) {
            return false;
        }
        NotiziaURP other = (NotiziaURP) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}
