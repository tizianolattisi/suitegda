package com.axiastudio.suite.richieste.entities;

import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.generale.ITimeStamped;
import com.axiastudio.suite.generale.TimeStampedListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Comparator;
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
@EntityListeners({TimeStampedListener.class})
@SequenceGenerator(name="gendestinatarioufficio", sequenceName="richieste.destinatarioufficio_id_seq", initialValue=1, allocationSize=1)
@NamedQuery(name="trovaDestinatarioUfficio",
        query = "SELECT d FROM DestinatarioUfficio d JOIN d.destinatario u "
                + "JOIN u.ufficioUtenteCollection uu "
                + "WHERE d.letto = FALSE AND uu.ospite = FALSE "
                + "AND uu.utente.id = :id ORDER BY d.richiesta.data desc ")
public class DestinatarioUfficio extends DestinatarioRichiesta
        implements Serializable, IDestinatarioRichiesta, ITimeStamped, Comparable<IDestinatarioRichiesta> {
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
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataletto;
    private String esecutoreletto;
    private Boolean richiestacancellabile=Boolean.TRUE;

    /* timestamped */
    @Column(name="rec_creato", insertable=false, updatable=false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date recordcreato;
    @Column(name="rec_creato_da")
    private String recordcreatoda;
    @Column(name="rec_modificato")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date recordmodificato;
    @Column(name="rec_modificato_da")
    private String recordmodificatoda;


    public Long getId() {
        return id;
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

    @Override
    public void setLetto(Boolean letto) {
        this.letto = letto;
    }

    @Override
    public Date getData(){
        return richiesta.getData();
    }

    public void setData(Date date){
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

    @Override
    public Date getDatascadenza() {
        return richiesta.getDatascadenza();
    }

    public void setDatascadenza(Date data) {
    }

    @Override
    public StatoRichiesta getStatorichiesta() {
        return richiesta.getStatorichiesta();
    }

    public void setStatorichiesta(StatoRichiesta stato) {
    }

    @Override
    public String getNomedestinatario() {
        return destinatario.getDescrizione();
    }

    public void setNomedestinatario(String nome) {
    }

    @Override
    public Date getDataletto() {
        return dataletto;
    }

    @Override
    public void setDataletto(Date dataletto) {
        this.dataletto = dataletto;
    }

    @Override
    public String getEsecutoreletto() {
        return esecutoreletto;
    }

    @Override
    public void setEsecutoreletto(String esecutoreletto) {
        this.esecutoreletto = esecutoreletto;
    }

    public Boolean getRichiestacancellabile() {
        return richiestacancellabile;
    }

    public void setRichiestacancellabile(Boolean richiestacancellabile) {
        this.richiestacancellabile = richiestacancellabile;
    }

    public Date getRecordcreato() {
        return recordcreato;
    }

    public void setRecordcreato(Date recordcreato) {
        this.recordcreato = recordcreato;
    }

    public String getRecordcreatoda() {
        return recordcreatoda;
    }

    public void setRecordcreatoda(String recordcreatoda) {
        this.recordcreatoda = recordcreatoda;
    }

    public Date getRecordmodificato() {
        return recordmodificato;
    }

    public void setRecordmodificato(Date recordmodificato) {
        this.recordmodificato = recordmodificato;
    }

    public String getRecordmodificatoda() {
        return recordmodificatoda;
    }

    public void setRecordmodificatoda(String recordmodificatoda) {
        this.recordmodificatoda = recordmodificatoda;
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
        if (!(object instanceof DestinatarioUfficio)) {
            return false;
        }
        DestinatarioUfficio other = (DestinatarioUfficio) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public int compareTo(IDestinatarioRichiesta o) {
        return Comparators.DATA.compare(this, o);
    }

    public static class Comparators {
        public static Comparator<IDestinatarioRichiesta> DATA = new Comparator<IDestinatarioRichiesta>() {
            @Override
            public int compare(IDestinatarioRichiesta o1, IDestinatarioRichiesta o2) {
                return o1.getData().compareTo(o2.getData());
            }
        };
        public static Comparator<IDestinatarioRichiesta> MITTENTE = new Comparator<IDestinatarioRichiesta>() {
            @Override
            public int compare(IDestinatarioRichiesta o1, IDestinatarioRichiesta o2) {
                return o1.getMittente().compareTo(o2.getMittente());
            }
        };
    }

}
