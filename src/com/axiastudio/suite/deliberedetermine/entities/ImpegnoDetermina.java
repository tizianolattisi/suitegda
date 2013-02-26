/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.deliberedetermine.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
@Entity
@Table(schema="deliberedetermine")
@SequenceGenerator(name="genimpegnodetermina", sequenceName="deliberedetermine.impegnodetermina_id_seq", initialValue=1, allocationSize=1)
public class ImpegnoDetermina implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genimpegnodetermina")
    private Long id;
    @JoinColumn(name = "determina", referencedColumnName = "id")
    @ManyToOne
    private Determina determina;
    @Column(name="importo")
    private BigDecimal importo;
            


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Determina getDetermina() {
        return determina;
    }

    public void setDetermina(Determina determina) {
        this.determina = determina;
    }

    public BigDecimal getImporto() {
        return importo;
    }

    public void setImporto(BigDecimal importo) {
        this.importo = importo;
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
        if (!(object instanceof ImpegnoDetermina)) {
            return false;
        }
        ImpegnoDetermina other = (ImpegnoDetermina) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.axiastudio.suite.deliberedetermine.entities.ImpegnoDetermina[ id=" + id + " ]";
    }
    
}
