/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.base.entities;

import java.util.Collection;
import javax.persistence.*;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
@Entity
@Table(schema="BASE")
@SequenceGenerator(name="genutente", sequenceName="utente_id_seq", initialValue=1, allocationSize=1)
public class Utente implements IUtente {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genutente")
    private Long id;
    @Column(name="nome")
    private String nome;
    @Column(name="email")
    private String email;
    @Column(name="login")
    private String login;
    @Column(name="password")
    private String password;
    @OneToMany(mappedBy = "utente", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<UfficioUtente> ufficioUtenteCollection;
    @Column(name="amministratore")
    private Boolean amministratore=false;
    @Column(name="superutente")
    private Boolean superutente=false;
    @Column(name="operatoreanagrafiche")
    private Boolean operatoreanagrafiche=false;
    @Column(name="supervisoreanagrafiche")
    private Boolean supervisoreanagrafiche=false;
    @Column(name="operatoreprotocollo")
    private Boolean operatoreprotocollo=false;
    @Column(name="attributoreprotocollo")
    private Boolean attributoreprotocollo=false;
    @Column(name="supervisoreprotocollo")
    private Boolean supervisoreprotocollo=false;
    @Column(name="operatorepratiche")
    private Boolean operatorepratiche=false;
    @Column(name="supervisorepratiche")
    private Boolean supervisorepratiche=false;
    @Column(name="modellatorepratiche")
    private Boolean modellatorepratiche=false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<UfficioUtente> getUfficioUtenteCollection() {
        return ufficioUtenteCollection;
    }

    public void setUfficioUtenteCollection(Collection<UfficioUtente> ufficioUtenteCollection) {
        this.ufficioUtenteCollection = ufficioUtenteCollection;
    }

    public Boolean getAmministratore() {
        return amministratore;
    }

    public void setAmministratore(Boolean amministratore) {
        this.amministratore = amministratore;
    }

    public Boolean getOperatoreanagrafiche() {
        return operatoreanagrafiche;
    }

    public void setOperatoreanagrafiche(Boolean operatoreanagrafiche) {
        this.operatoreanagrafiche = operatoreanagrafiche;
    }

    public Boolean getSupervisoreanagrafiche() {
        return supervisoreanagrafiche;
    }

    public void setSupervisoreanagrafiche(Boolean supervisoreanagrafiche) {
        this.supervisoreanagrafiche = supervisoreanagrafiche;
    }

    public Boolean getOperatorepratiche() {
        return operatorepratiche;
    }

    public void setOperatorepratiche(Boolean operatorepratiche) {
        this.operatorepratiche = operatorepratiche;
    }

    public Boolean getOperatoreprotocollo() {
        return operatoreprotocollo;
    }

    public void setOperatoreprotocollo(Boolean operatoreprotocollo) {
        this.operatoreprotocollo = operatoreprotocollo;
    }

    public Boolean getAttributoreprotocollo() {
        return attributoreprotocollo;
    }

    public void setAttributoreprotocollo(Boolean attributoreprotocollo) {
        this.attributoreprotocollo = attributoreprotocollo;
    }

    public Boolean getSuperutente() {
        return superutente;
    }

    public void setSuperutente(Boolean superutente) {
        this.superutente = superutente;
    }

    public Boolean getSupervisorepratiche() {
        return supervisorepratiche;
    }

    public void setSupervisorepratiche(Boolean supervisorepratiche) {
        this.supervisorepratiche = supervisorepratiche;
    }

    public Boolean getModellatorepratiche() {
        return modellatorepratiche;
    }

    public void setModellatorepratiche(Boolean modellatorepratiche) {
        this.modellatorepratiche = modellatorepratiche;
    }

    public Boolean getSupervisoreprotocollo() {
        return supervisoreprotocollo;
    }

    public void setSupervisoreprotocollo(Boolean supervisoreprotocollo) {
        this.supervisoreprotocollo = supervisoreprotocollo;
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
        if (!(object instanceof Utente)) {
            return false;
        }
        Utente other = (Utente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.axiastudio.suite.base.entities.Utente[ id=" + id + " ]";
    }
    
}
