package com.axiastudio.suite.urp.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * User: tiziano
 * Date: 16/11/14
 * Time: 21:06
 */
@Entity
@Table(schema="urp")
@SequenceGenerator(name="genservizioalcittadino", sequenceName="urp.servizioalcittadino_id_seq", initialValue=1, allocationSize=1)
public class ServizioAlCittadino implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genservizioalcittadino")
    private Long id;

    @Column(name="codiceservizio")
    private String codiceservizio;

    @Column(name="descrizione")
    private String descrizione;

    @OneToMany(fetch= FetchType.EAGER)
    @JoinTable(name = "servizioalcittadinosportello", schema = "urp",
            joinColumns = {@JoinColumn(name = "servizioalcittadino", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "sportello", referencedColumnName = "id")})
    private Collection<Sportello> sportelli;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodiceservizio() {
        return codiceservizio;
    }

    public void setCodiceservizio(String codiceservizio) {
        this.codiceservizio = codiceservizio;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Collection<Sportello> getSportelli() {
        return sportelli;
    }

    public void setSportelli(Collection<Sportello> sportelli) {
        this.sportelli = sportelli;
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
        if (!(object instanceof ServizioAlCittadino)) {
            return false;
        }
        ServizioAlCittadino other = (ServizioAlCittadino) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return this.getDescrizione();
    }

}