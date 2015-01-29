package com.axiastudio.suite.urp.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * User: tiziano
 * Date: 18/11/14
 * Time: 15:07
 */
@Entity
@Table(schema="urp", name = "servizioalcittadinosportello")
@SequenceGenerator(name="genservizioalcittadinosportello", sequenceName="urp.servizioalcittadinosportello_id_seq", initialValue=1, allocationSize=1)
public class ServizioAlCittadinoSportello implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genservizioalcittadinosportello")
    private Long id;

    @JoinColumn(name = "sportello", referencedColumnName = "id")
    @ManyToOne
    private Sportello sportello;

    @JoinColumn(name = "servizioalcittadino", referencedColumnName = "id")
    @ManyToOne
    private ServizioAlCittadino servizioalcittadino;

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

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ServizioAlCittadinoSportello)) {
            return false;
        }
        ServizioAlCittadinoSportello other = (ServizioAlCittadinoSportello) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}
