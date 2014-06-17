/*
 * Copyright (C) 2012 AXIA Studio (http://www.axiastudio.com)
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
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axiastudio.suite.deliberedetermine.entities;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Controller;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.suite.SuiteUtil;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.deliberedetermine.DeterminaListener;
import com.axiastudio.suite.finanziaria.entities.Progetto;
import com.axiastudio.suite.finanziaria.entities.Servizio;
import com.axiastudio.suite.generale.ITimeStamped;
import com.axiastudio.suite.generale.TimeStampedListener;
import com.axiastudio.suite.pratiche.IAtto;
import com.axiastudio.suite.pratiche.IDettaglio;
import com.axiastudio.suite.pratiche.entities.DipendenzaPratica;
import com.axiastudio.suite.pratiche.entities.Fase;
import com.axiastudio.suite.pratiche.entities.Pratica;
import com.axiastudio.suite.pratiche.entities.Visto;
import com.axiastudio.suite.procedimenti.entities.CodiceCarica;
import com.axiastudio.suite.protocollo.IProtocollabile;
import com.axiastudio.suite.protocollo.entities.Protocollo;
import com.axiastudio.suite.protocollo.entities.UfficioProtocollo;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
@Entity
@EntityListeners({DeterminaListener.class, TimeStampedListener.class})
@Table(schema="deliberedetermine")
@SequenceGenerator(name="gendetermina", sequenceName="deliberedetermine.determina_id_seq", initialValue=1, allocationSize=1)
@NamedQuery(name="inAttesaDiVistoDiBilancio",
        query = "SELECT d FROM Determina d JOIN d.pratica p JOIN p.fasePraticaCollection fp " +
                "WHERE fp.attiva = true AND fp.fase.id = :idfase")
public class Determina implements Serializable, ITimeStamped, IDettaglio, IProtocollabile, IAtto {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="gendetermina")
    private Long id;
    @JoinColumn(name = "idpratica", referencedColumnName = "idpratica")
    @ManyToOne(cascade={CascadeType.MERGE, CascadeType.REFRESH})
    private Pratica pratica;
    @Column(name="codiceinterno", unique=true)
    private String codiceinterno;
    @Column(name="oggetto", length=2048)
    private String oggetto;
    @OneToMany(mappedBy = "determina", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<ServizioDetermina> servizioDeterminaCollection;
    @OneToMany(mappedBy = "determina", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<MovimentoDetermina> movimentoDeterminaCollection;
    @OneToMany(mappedBy = "determina", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<SpesaImpegnoEsistente> spesaImpegnoEsistenteCollection;
    @OneToMany(mappedBy = "determina", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<UfficioDetermina> ufficioDeterminaCollection;
    @Column(name="dispesa")
    private Boolean dispesa=Boolean.FALSE;
    @Column(name="spesaimpegnoesistente")
    private Boolean spesaimpegnoesistente =Boolean.FALSE;
    @Column(name="dientrata")
    private Boolean dientrata =Boolean.FALSE;
    @Column(name="diregolarizzazione")
    private Boolean diregolarizzazione =Boolean.FALSE;
    @Column(name="diliquidazione")
    private Boolean diliquidazione =Boolean.FALSE;
    @Column(name="diincarico")
    private Boolean diincarico =Boolean.FALSE;
    @Column(name="referentepolitico")
    private String referentePolitico;
    @JoinColumn(name = "responsabile", referencedColumnName = "id")
    @ManyToOne
    private Utente responsabile;
    @Column(name="anno")
    private Integer anno;
    @Column(name="numero")
    private Integer numero;
    @Column(name="data")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date data;
    /* protocollo */
    @JoinColumn(name = "protocollo", referencedColumnName = "iddocumento")
    @ManyToOne
    private Protocollo protocollo;
    @Enumerated(EnumType.STRING)
    private TipoPubblicazione pubblicabile=TipoPubblicazione.PUBBLICABILE;
    @Column(name="pluriennale")
    private Boolean pluriennale=Boolean.FALSE;
    @Column(name="finoadanno")
    private Integer finoAdAnno;
    @JoinColumn(name = "progetto", referencedColumnName = "id")
    @ManyToOne
    private Progetto progetto;

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

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Pratica getPratica() {
        return pratica;
    }

    @Override
    public void setPratica(Pratica pratica) {
        this.pratica = pratica;
    }

    @Override
    public String getIdpratica() {
        if( this.pratica != null ){
            return pratica.getIdpratica();
        }
        return null;
    }

    @Override
    public void setIdpratica(String idpratica) {
        // NOP
    }

    @Override
    public String getCodiceinterno() {
        return codiceinterno;
    }

    @Override
    public void setCodiceinterno(String codiceinterno) {
        this.codiceinterno = codiceinterno;
    }

    @Override
    public Servizio getServizio() {
        Collection<ServizioDetermina> serviziDetermina = getServizioDeterminaCollection();
        for( ServizioDetermina servizioDetermina: getServizioDeterminaCollection() ){
            if( servizioDetermina.getPrincipale() ){
                return servizioDetermina.getServizio();
            }
        }
        return null;
    }

    @Override
    public Ufficio getUfficio() {
        Servizio servizio = getServizio();
        if( servizio != null ){
            return servizio.getUfficio();
        }
        return null;
    }

    @Override
    public String getOggetto() {
        return oggetto;
    }

    @Override
    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }

    public Date getDatapratica() {
        if( this.pratica != null ){
            return pratica.getDatapratica();
        }
        return null;
    }

    public void setDatapratica(Date datapratica) {
        // NOP
    }

    public Collection<ServizioDetermina> getServizioDeterminaCollection() {
        return servizioDeterminaCollection;
    }

    public void setServizioDeterminaCollection(Collection<ServizioDetermina> servizioDeterminaCollection) {
        this.servizioDeterminaCollection = servizioDeterminaCollection;
    }

    public Collection<MovimentoDetermina> getMovimentoDeterminaCollection() {
        return movimentoDeterminaCollection;
    }

    public void setMovimentoDeterminaCollection(Collection<MovimentoDetermina> movimentoDeterminaCollection) {
        this.movimentoDeterminaCollection = movimentoDeterminaCollection;
    }

    public Collection<SpesaImpegnoEsistente> getSpesaImpegnoEsistenteCollection() {
        return spesaImpegnoEsistenteCollection;
    }

    public void setSpesaImpegnoEsistenteCollection(Collection<SpesaImpegnoEsistente> spesaImpegnoEsistenteCollection) {
        this.spesaImpegnoEsistenteCollection = spesaImpegnoEsistenteCollection;
    }

    public Collection<UfficioDetermina> getUfficioDeterminaCollection() {
        return ufficioDeterminaCollection;
    }

    public void setUfficioDeterminaCollection(Collection<UfficioDetermina> ufficioDeterminaCollection) {
        this.ufficioDeterminaCollection = ufficioDeterminaCollection;
    }

    public Boolean getDispesa() {
        return dispesa;
    }

    public void setDispesa(Boolean dispesa) {
        this.dispesa = dispesa;
    }

    public Boolean getSpesaimpegnoesistente() {
        return spesaimpegnoesistente;
    }

    public void setSpesaimpegnoesistente(Boolean spesaprecedenteimpegno) {
        this.spesaimpegnoesistente = spesaprecedenteimpegno;
    }

    public Boolean getDientrata() {
        return dientrata;
    }

    public void setDientrata(Boolean diEntrata) {
        this.dientrata = diEntrata;
    }

    public Boolean getDiregolarizzazione() {
        return diregolarizzazione;
    }

    public void setDiregolarizzazione(Boolean diRegolarizzazione) {
        this.diregolarizzazione = diRegolarizzazione;
    }

    public Boolean getDiliquidazione() {
        return diliquidazione;
    }

    public void setDiliquidazione(Boolean diLiquidazione) {
        this.diliquidazione = diLiquidazione;
    }

    public Boolean getDiincarico() {
        return diincarico;
    }

    public void setDiincarico(Boolean diIncarico) {
        this.diincarico = diIncarico;
    }

    public String getReferentePolitico() {
        return referentePolitico;
    }

    public void setReferentePolitico(String referentePolitico) {
        this.referentePolitico = referentePolitico;
    }

    public Integer getAnno() {
        return anno;
    }

    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Protocollo getProtocollo() {
        return protocollo;
    }

    public void setProtocollo(Protocollo protocollo) {
        this.protocollo = protocollo;
    }

    public Utente getResponsabile() {
        return responsabile;
    }

    public void setResponsabile(Utente responsabile) {
        this.responsabile = responsabile;
    }

    public TipoPubblicazione getPubblicabile() {
        return pubblicabile;
    }

    public void setPubblicabile(TipoPubblicazione pubblicabile) {
        this.pubblicabile = pubblicabile;
    }

    public Boolean getPluriennale() {
        return pluriennale;
    }

    public void setPluriennale(Boolean pluriennale) {
        this.pluriennale = pluriennale;
    }

    public Integer getFinoAdAnno() {
        return finoAdAnno;
    }

    public void setFinoAdAnno(Integer finoAdAnno) {
        this.finoAdAnno = finoAdAnno;
    }

    public Progetto getProgetto() {
        return progetto;
    }

    public void setProgetto(Progetto progetto) {
        this.progetto = progetto;
    }

    public String getNumeroprotocollo() {
        return getProtocollo().getIddocumento();
    }

    public void setNumeroprotocollo(String numeroprotocollo) {
        // NOP
    }

    public String getServizioPrincipale() {
        if (getServizio() != null) {
            return getServizio().getDescrizione();
        }
        return null;
    }

    public void setServizioPrincipale(String servizioPrincipale) {
        // NOP
    }

    public String getPraticaprincipale() {
        if (getPratica().getDipendenzaPraticaCollection() != null) {
            for (DipendenzaPratica dp: getPratica().getDipendenzaPraticaCollection()) {
                if (dp.getDipendenza().getId() == -1 && dp.getInvertita() ) {
                    return dp.getPraticadipendente().getCodiceinterno();
                }
            }
        }
        return null;
    }

    public void setPraticaprincipale(String praticaprincipale) {
        // NOP
    }

    public String getFirma() {
        String firma = " RESPONSABILE DEL SERVIZIO DI BILANCIO\n";

        Visto vistoResp = this.getVisto("FASE_VISTO_RESPONSABILE");
        if( vistoResp != null ){
            if( vistoResp.getUtente().equals(vistoResp.getResponsabile()) ){
                firma = "IL" + firma;
            } else {
                firma = "PER TEMPORANEA ASSENZA DEL" + firma;
            }
        }

        if ( this.getProtocollo() != null && this.getProtocollo().getUfficioProtocolloCollection() != null ) {
            UfficioProtocollo ufficio = (UfficioProtocollo) this.getProtocollo().getUfficioProtocolloCollection().iterator().next();
            firma +=  ufficio.getUfficio().getDenominazione() +"\n";
        } else {
            firma += "[ufficio]\n";
        }

        if ( vistoResp != null && vistoResp.getCodiceCarica() != null ) {
            if ( vistoResp.getCodiceCarica().equals(CodiceCarica.RESPONSABILE_DI_SERVIZIO) &&
                    ! vistoResp.getUtente().equals(vistoResp.getResponsabile()) ) {
                firma += "IL FUNZIONARIO INCARICATO\n";
            } else if ( vistoResp.getCodiceCarica().equals(CodiceCarica.SEGRETARIO) ) {
                firma += "IL SEGRETARIO GENERALE\n";
            } else if ( vistoResp.getCodiceCarica().equals(CodiceCarica.VICE_SEGRETARIO) ) {
                firma += "IL VICESEGRETARIO GENERALE\n";
            }
            firma += vistoResp.getUtente();
        }
        return firma;
    }

    public void setFirma(String firma) {
        // NOP
    }

    private Visto getVisto(String tipoVisto) {
        if( this.getPratica() != null ){
            Database db = (Database) Register.queryUtility(IDatabase.class);
            Long idFaseVisto = Long.parseLong(SuiteUtil.trovaCostante(tipoVisto).getValore());
            Controller controller = db.createController(Fase.class);
            Fase fase = (Fase)controller.get(idFaseVisto);
            EntityManager em = db.getEntityManagerFactory().createEntityManager();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Visto> cq = cb.createQuery(Visto.class);
            Root<Visto> root = cq.from(Visto.class);
            cq.select(root);
            cq.where(cb.and(cb.equal(root.get("pratica"), this.getPratica()),
                    cb.equal(root.get("fase"), fase),
                    cb.equal(root.get("negato"), false)));
            cq.orderBy(cb.desc(root.get("data")));
            TypedQuery<Visto> tq = em.createQuery(cq);
            List<Visto> resultList = tq.getResultList();
            if( resultList.size()>0 ){
                return resultList.get(0);
            }
        }
        return null;
    }

    public Visto getVistoResponsabile() {
        return getVisto("FASE_VISTO_RESPONSABILE");
    }
    public void setVistoResponsabile(Visto visto){  }

    public Visto getVistoBilancio() {
        return getVisto("FASE_VISTO_BILANCIO");
    }
    public void setVistoBilancio(Visto visto){  }

    public Visto getVistoBilancioNegato() {
        return getVisto("FASE_VISTO_BILANCIO_NEGATO");
    }
    public void setVistoBilancioNegato(Visto visto){  }

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
        if (!(object instanceof Determina)) {
            return false;
        }
        Determina other = (Determina) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.axiastudio.suite.deliberedetermine.entities.Determina[ id=" + id + " ]";
    }

}
