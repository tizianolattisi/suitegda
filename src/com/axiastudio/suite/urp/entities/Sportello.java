package com.axiastudio.suite.urp.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * User: tiziano
 * Date: 17/11/14
 * Time: 09:38
 */
@Entity
@Table(schema="urp")
@SequenceGenerator(name="gensportello", sequenceName="urp.sportello_id_seq", initialValue=1, allocationSize=1)
public class Sportello implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="gensportello")
    private Long id;

    @Column(name="descrizione")
    private String descrizione;


    @Column(name="attivo")
    private Boolean attivo=false;

    /*@JoinColumn(name = "utente", referencedColumnName = "id")
    @ManyToOne
    private Utente utente;*/

    @OneToMany(fetch= FetchType.EAGER)
    @JoinTable(name = "servizioalcittadinosportello", schema = "urp",
                joinColumns = {@JoinColumn(name = "sportello", referencedColumnName = "id")},
                inverseJoinColumns = {@JoinColumn(name = "servizioalcittadino", referencedColumnName = "id")})
    private Collection<ServizioAlCittadino> servizialcittadino;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Boolean getAttivo() {
        return attivo;
    }

    public void setAttivo(Boolean attivo) {
        this.attivo = attivo;
    }

    public Collection<ServizioAlCittadino> getServizialcittadino() {
        return servizialcittadino;
    }

    public void setServizialcittadino(Collection<ServizioAlCittadino> servizialcittadino) {
        this.servizialcittadino = servizialcittadino;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sportello)) {
            return false;
        }
        Sportello other = (Sportello) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}
