/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.protocollo.entities;

import com.axiastudio.suite.base.entities.Ufficio;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
@Entity
public class Attribuzione implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JoinColumn(name = "ufficio", referencedColumnName = "id")
    @ManyToOne
    private Ufficio ufficio;
    @JoinColumn(name = "protocollo", referencedColumnName = "id")
    @ManyToOne
    private Protocollo protocollo;
    @Column(name="principale")
    private Boolean principale;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Protocollo getProtocollo() {
        return protocollo;
    }

    public void setProtocollo(Protocollo protocollo) {
        this.protocollo = protocollo;
    }

    public Ufficio getUfficio() {
        return ufficio;
    }

    public void setUfficio(Ufficio ufficio) {
        this.ufficio = ufficio;
    }

    public Boolean getPrincipale() {
        return principale;
    }

    public void setPrincipale(Boolean principale) {
        this.principale = principale;
    }
    
    public String getIddocumento(){
        return this.protocollo.getIddocumento();
    }
    
    public void setIddocumento(String iddocumento){
        
    }
    
    public TipoProtocollo getTipoprotocollo(){
        return this.protocollo.getTipo();
    }
    
    public void setTipoprotocollo(TipoProtocollo tipoProtocollo){
        
    }
    
    public Date getDataprotocollo(){
        return this.protocollo.getDataprotocollo();
    }
    
    public void setDataprotocollo(Date dataProtocollo){
        
    }
    
    public String getOggetto(){
        return this.protocollo.getOggetto();
    }
    
    public void setOggetto(String oggetto){

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
        if (!(object instanceof Attribuzione)) {
            return false;
        }
        Attribuzione other = (Attribuzione) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.axiastudio.suite.protocollo.entities.Attribuzione[ id=" + id + " ]";
    }
    
}
