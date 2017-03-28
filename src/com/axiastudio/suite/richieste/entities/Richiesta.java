/*
 * Copyright (C) 2013 AXIA Studio (http://www.axiastudio.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Afffero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axiastudio.suite.richieste.entities;


import com.axiastudio.pypapi.Register;
import com.axiastudio.suite.SuiteUtil;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.UfficioUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.generale.ITimeStamped;
import com.axiastudio.suite.generale.TimeStampedListener;
import com.axiastudio.suite.richieste.RichiestaListener;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
@Table(schema="RICHIESTE")
@EntityListeners({RichiestaListener.class, TimeStampedListener.class})
@SequenceGenerator(name="genrichiesta", sequenceName="richieste.richiesta_id_seq", initialValue=1, allocationSize=1)
public class Richiesta implements Serializable, ITimeStamped {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genrichiesta")
    private Long id;
    @Column(name="data")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date data =  Calendar.getInstance().getTime();
    @Column(name="testo")
    private String testo = "";
    @JoinColumn(name = "mittente", referencedColumnName = "id")
    @ManyToOne
    private Utente mittente;
    private Boolean cancellabile = Boolean.TRUE;
    @Temporal(TemporalType.DATE)
    private Date datascadenza;
    private Integer giornipreavviso;
    @JoinColumn(name = "richiestaprecedente", referencedColumnName = "id")
    @ManyToOne
    private Richiesta richiestaprecedente;
    private Integer relazione;
    private Boolean richiestaautomatica = Boolean.FALSE;
    @Enumerated(EnumType.STRING)
    private StatoRichiesta statorichiesta=StatoRichiesta.ND;
    @OneToMany(mappedBy = "richiesta", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<DestinatarioUfficio> destinatarioUfficioCollection;
    @OneToMany(mappedBy = "richiesta", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<DestinatarioUtente> destinatarioUtenteCollection;
    @OneToMany(mappedBy = "richiestaprecedente", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<Richiesta> richiestaSuccessivaCollection;
    @OneToMany(mappedBy = "richiesta", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<RichiestaProtocollo> richiestaProtocolloCollection;
    @OneToMany(mappedBy = "richiesta", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<RichiestaPratica> richiestaPraticaCollection;

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

    /* transient */
    @Transient
    private String pathdocumento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public String getTestop() {
        if ( this.getId()==null ) {
            return "";
        }

        Boolean riservato=Boolean.TRUE;
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        if (this.getMittente().equals(autenticato)) {
            riservato=Boolean.FALSE;
        }

        if ( (this.getRichiestaPraticaCollection()!=null && !this.getRichiestaPraticaCollection().isEmpty()) ||
                this.getRichiestaProtocolloCollection()!=null && ! this.getRichiestaProtocolloCollection().isEmpty() ) {
            riservato=Boolean.FALSE;
        }

        for (DestinatarioUtente du : this.getDestinatarioUtenteCollection()) {
            if (du.getDestinatario().equals(autenticato)) {
                riservato=Boolean.FALSE;
            }
        }

        for (DestinatarioUfficio du : this.getDestinatarioUfficioCollection()) {
            for (UfficioUtente uu : du.getDestinatario().getUfficioUtenteCollection()) {
                if (autenticato.equals(uu.getUtente()) && !uu.getOspite()) {
                    riservato=Boolean.FALSE;
                }
            }
        }
        if( riservato ){
            return "NON DI COMPETENZA";
        }
        return this.getTesto().replace("\n", " ");
    }

    public void setTestop(String testo) {
    }

    public Utente getMittente() {
        return mittente;
    }

    public void setMittente(Utente mittente) {
        this.mittente = mittente;
    }

    public String getMittentep() {
        if ( this.getMittente()==null ) {
            return "";
        }

        Boolean riservato=Boolean.TRUE;

        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        if (this.getMittente().equals(autenticato)) {
            riservato=Boolean.FALSE;
        }

        if ( (this.getRichiestaPraticaCollection()!=null && !this.getRichiestaPraticaCollection().isEmpty()) ||
                this.getRichiestaProtocolloCollection()!=null && ! this.getRichiestaProtocolloCollection().isEmpty() ) {
            riservato=Boolean.FALSE;
        }

        for (DestinatarioUtente du : this.getDestinatarioUtenteCollection()) {
            if (du.getDestinatario().equals(autenticato)) {
                riservato=Boolean.FALSE;
            }
        }

        for (DestinatarioUfficio du : this.getDestinatarioUfficioCollection()) {
            for (UfficioUtente uu : du.getDestinatario().getUfficioUtenteCollection()) {
                if (autenticato.equals(uu.getUtente()) && !uu.getOspite()) {
                    riservato=Boolean.FALSE;
                }
            }
        }
        if( riservato ){
            return "NON DI COMPETENZA";
        }
        return this.getMittente().toString();
    }

    public void setMittentep(String mittente) {
    }

    public Boolean getCancellabile() {
        return cancellabile;
    }

    public void setCancellabile(Boolean cancellabile) {
        this.cancellabile = cancellabile;
        if ( this.getDestinatarioUfficioCollection()!=null ) {
            for ( DestinatarioUfficio du: this.getDestinatarioUfficioCollection() ) {
                du.setRichiestacancellabile(cancellabile);
            }
        }
        if ( this.getDestinatarioUtenteCollection()!=null ) {
            for (DestinatarioUtente du : this.getDestinatarioUtenteCollection()) {
                du.setRichiestacancellabile(cancellabile);
            }
        }
    }

    public Date getDatascadenza() {
        return datascadenza;
    }

    public void setDatascadenza(Date datascadenza) {
        this.datascadenza = datascadenza;
    }

    public Integer getGiornipreavviso() {
        return giornipreavviso;
    }

    public void setGiornipreavviso(Integer giornipreavviso) {
        this.giornipreavviso = giornipreavviso;
    }

    public Richiesta getRichiestaprecedente() {
        return richiestaprecedente;
    }

    public void setRichiestaprecedente(Richiesta richiestaprecedente) {
        this.richiestaprecedente = richiestaprecedente;
    }

    public Integer getRelazione() {
        return relazione;
    }

    public void setRelazione(Integer relazione) {
        this.relazione = relazione;
    }

    public Boolean getRichiestaautomatica() {
        return richiestaautomatica;
    }

    public void setRichiestaautomatica(Boolean richiestaautomatica) {
        this.richiestaautomatica = richiestaautomatica;
    }

    public StatoRichiesta getStatorichiesta() {
        return statorichiesta;
    }

    public void setStatorichiesta(StatoRichiesta fase) {
        this.statorichiesta = fase;
    }

    public String getPathdocumento() {
        if ( this.pathdocumento != null) {
            return this.pathdocumento;
        } else {
            return (new SimpleDateFormat("yyyy/MM/")).format(this.data) + this.id.toString();
        }
    }

    public void setPathdocumento(String pathdocumento) {
        this.pathdocumento = pathdocumento;
    }

    public String getVarPathDocumento() {
        return this.pathdocumento;
    }

    public Collection<DestinatarioUfficio> getDestinatarioUfficioCollection() {
        return destinatarioUfficioCollection;
    }

    public void setDestinatarioUfficioCollection(Collection<DestinatarioUfficio> destinatarioUfficioCollection) {
        this.destinatarioUfficioCollection = destinatarioUfficioCollection;
    }

    public Collection<DestinatarioUtente> getDestinatarioUtenteCollection() {
        return destinatarioUtenteCollection;
    }

    public void setDestinatarioUtenteCollection(Collection<DestinatarioUtente> destinatarioUtenteCollection) {
        this.destinatarioUtenteCollection = destinatarioUtenteCollection;
    }

    public Collection<RichiestaProtocollo> getRichiestaProtocolloCollection() {
        return richiestaProtocolloCollection;
    }

    public void setRichiestaProtocolloCollection(Collection<RichiestaProtocollo> richiestaProtocolloCollection) {
        this.richiestaProtocolloCollection = richiestaProtocolloCollection;
    }

    public Collection<RichiestaPratica> getRichiestaPraticaCollection() {
        return richiestaPraticaCollection;
    }

    public void setRichiestaPraticaCollection(Collection<RichiestaPratica> richiestaPraticaCollection) {
        this.richiestaPraticaCollection = richiestaPraticaCollection;
    }

    public Collection<Richiesta> getRichiestaSuccessivaCollection() {
        return richiestaSuccessivaCollection;
    }

    public void setRichiestaSuccessivaCollection(Collection<Richiesta> richiestaSuccessivaCollection) {
        this.richiestaSuccessivaCollection = richiestaSuccessivaCollection;
    }

    public Collection<Richiesta> getRichiestaPrecedente() {
        if ( getRichiestaprecedente()==null ) {
            return null;
        } else {
            return new ArrayList<Richiesta>(Collections.singletonList(getRichiestaprecedente()));
        }
    }

    public void setRichiestaPrecedente(Collection<Richiesta> richiestaPrecedente) {
    }


    public String getNomemittente() {
        if (getMittente()==null) {
            return null;
        } else {
            return getMittente().toString();
        }
    }

    public void setNomemittente(String nomeMittente) {
    }

    public String getDescRichiesta() {
        if ( this==null ) {
            return null;
        } else {
            return getId().toString() + "- (" + SuiteUtil.DATETIME_FORMAT.format(getData()) + ") " + getTesto();
        }
    }

    public void setDescRichiesta(String descRichiesta) {
    }


    @Override
    public Date getRecordcreato() {
        return recordcreato;
    }

    @Override
    public void setRecordcreato(Date recordcreato) {
        this.recordcreato = recordcreato;
    }

    @Override
    public String getRecordcreatoda() {
        return recordcreatoda;
    }

    @Override
    public void setRecordcreatoda(String recordcreatoda) {
        this.recordcreatoda = recordcreatoda;
    }

    @Override
    public Date getRecordmodificato() {
        return recordmodificato;
    }

    @Override
    public void setRecordmodificato(Date recordmodificato) {
        this.recordmodificato = recordmodificato;
    }

    @Override
    public String getRecordmodificatoda() {
        return recordmodificatoda;
    }

    @Override
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
        if (!(object instanceof Richiesta)) {
            return false;
        }
        Richiesta other = (Richiesta) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }
}
