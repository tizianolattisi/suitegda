
package com.axiastudio.suite.interoperabilita.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Classe Java per Intestazione complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="Intestazione">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Identificatore"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}PrimaRegistrazione" minOccurs="0"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}OraRegistrazione" minOccurs="0"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Origine"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Destinazione" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}PerConoscenza" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Risposta" minOccurs="0"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Riservato" minOccurs="0"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}InterventoOperatore" minOccurs="0"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}RiferimentoDocumentiCartacei" minOccurs="0"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}RiferimentiTelematici" minOccurs="0"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Oggetto"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Classifica" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Note" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Intestazione", namespace = "http://www.digitPa.gov.it/protocollo/", propOrder = {
    "identificatore",
    "primaRegistrazione",
    "oraRegistrazione",
    "origine",
    "destinazione",
    "perConoscenza",
    "risposta",
    "riservato",
    "interventoOperatore",
    "riferimentoDocumentiCartacei",
    "riferimentiTelematici",
    "oggetto",
    "classifica",
    "note"
})
public class Intestazione {

    @XmlElement(name = "Identificatore", namespace = "http://www.digitPa.gov.it/protocollo/", required = true)
    protected Identificatore identificatore;
    @XmlElement(name = "PrimaRegistrazione", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected PrimaRegistrazione primaRegistrazione;
    @XmlElement(name = "OraRegistrazione", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected OraRegistrazione oraRegistrazione;
    @XmlElement(name = "Origine", namespace = "http://www.digitPa.gov.it/protocollo/", required = true)
    protected Origine origine;
    @XmlElement(name = "Destinazione", namespace = "http://www.digitPa.gov.it/protocollo/", required = true)
    protected List<Destinazione> destinazione;
    @XmlElement(name = "PerConoscenza", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected List<PerConoscenza> perConoscenza;
    @XmlElement(name = "Risposta", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected Risposta risposta;
    @XmlElement(name = "Riservato", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected Riservato riservato;
    @XmlElement(name = "InterventoOperatore", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected InterventoOperatore interventoOperatore;
    @XmlElement(name = "RiferimentoDocumentiCartacei", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected RiferimentoDocumentiCartacei riferimentoDocumentiCartacei;
    @XmlElement(name = "RiferimentiTelematici", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected RiferimentiTelematici riferimentiTelematici;
    @XmlElement(name = "Oggetto", namespace = "http://www.digitPa.gov.it/protocollo/", required = true)
    protected Oggetto oggetto;
    @XmlElement(name = "Classifica", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected List<Classifica> classifica;
    @XmlElement(name = "Note", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected Note note;

    /**
     * Recupera il valore della proprietà identificatore.
     * 
     * @return
     *     possible object is
     *     {@link Identificatore }
     *     
     */
    public Identificatore getIdentificatore() {
        return identificatore;
    }

    /**
     * Imposta il valore della proprietà identificatore.
     * 
     * @param value
     *     allowed object is
     *     {@link Identificatore }
     *     
     */
    public void setIdentificatore(Identificatore value) {
        this.identificatore = value;
    }

    /**
     * Recupera il valore della proprietà primaRegistrazione.
     * 
     * @return
     *     possible object is
     *     {@link PrimaRegistrazione }
     *     
     */
    public PrimaRegistrazione getPrimaRegistrazione() {
        return primaRegistrazione;
    }

    /**
     * Imposta il valore della proprietà primaRegistrazione.
     * 
     * @param value
     *     allowed object is
     *     {@link PrimaRegistrazione }
     *     
     */
    public void setPrimaRegistrazione(PrimaRegistrazione value) {
        this.primaRegistrazione = value;
    }

    /**
     * Recupera il valore della proprietà oraRegistrazione.
     * 
     * @return
     *     possible object is
     *     {@link OraRegistrazione }
     *     
     */
    public OraRegistrazione getOraRegistrazione() {
        return oraRegistrazione;
    }

    /**
     * Imposta il valore della proprietà oraRegistrazione.
     * 
     * @param value
     *     allowed object is
     *     {@link OraRegistrazione }
     *     
     */
    public void setOraRegistrazione(OraRegistrazione value) {
        this.oraRegistrazione = value;
    }

    /**
     * Recupera il valore della proprietà origine.
     * 
     * @return
     *     possible object is
     *     {@link Origine }
     *     
     */
    public Origine getOrigine() {
        return origine;
    }

    /**
     * Imposta il valore della proprietà origine.
     * 
     * @param value
     *     allowed object is
     *     {@link Origine }
     *     
     */
    public void setOrigine(Origine value) {
        this.origine = value;
    }

    /**
     * Gets the value of the destinazione property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the destinazione property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDestinazione().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Destinazione }
     * 
     * 
     */
    public List<Destinazione> getDestinazione() {
        if (destinazione == null) {
            destinazione = new ArrayList<Destinazione>();
        }
        return this.destinazione;
    }

    /**
     * Gets the value of the perConoscenza property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the perConoscenza property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPerConoscenza().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PerConoscenza }
     * 
     * 
     */
    public List<PerConoscenza> getPerConoscenza() {
        if (perConoscenza == null) {
            perConoscenza = new ArrayList<PerConoscenza>();
        }
        return this.perConoscenza;
    }

    /**
     * Recupera il valore della proprietà risposta.
     * 
     * @return
     *     possible object is
     *     {@link Risposta }
     *     
     */
    public Risposta getRisposta() {
        return risposta;
    }

    /**
     * Imposta il valore della proprietà risposta.
     * 
     * @param value
     *     allowed object is
     *     {@link Risposta }
     *     
     */
    public void setRisposta(Risposta value) {
        this.risposta = value;
    }

    /**
     * Recupera il valore della proprietà riservato.
     * 
     * @return
     *     possible object is
     *     {@link Riservato }
     *     
     */
    public Riservato getRiservato() {
        return riservato;
    }

    /**
     * Imposta il valore della proprietà riservato.
     * 
     * @param value
     *     allowed object is
     *     {@link Riservato }
     *     
     */
    public void setRiservato(Riservato value) {
        this.riservato = value;
    }

    /**
     * Recupera il valore della proprietà interventoOperatore.
     * 
     * @return
     *     possible object is
     *     {@link InterventoOperatore }
     *     
     */
    public InterventoOperatore getInterventoOperatore() {
        return interventoOperatore;
    }

    /**
     * Imposta il valore della proprietà interventoOperatore.
     * 
     * @param value
     *     allowed object is
     *     {@link InterventoOperatore }
     *     
     */
    public void setInterventoOperatore(InterventoOperatore value) {
        this.interventoOperatore = value;
    }

    /**
     * Recupera il valore della proprietà riferimentoDocumentiCartacei.
     * 
     * @return
     *     possible object is
     *     {@link RiferimentoDocumentiCartacei }
     *     
     */
    public RiferimentoDocumentiCartacei getRiferimentoDocumentiCartacei() {
        return riferimentoDocumentiCartacei;
    }

    /**
     * Imposta il valore della proprietà riferimentoDocumentiCartacei.
     * 
     * @param value
     *     allowed object is
     *     {@link RiferimentoDocumentiCartacei }
     *     
     */
    public void setRiferimentoDocumentiCartacei(RiferimentoDocumentiCartacei value) {
        this.riferimentoDocumentiCartacei = value;
    }

    /**
     * Recupera il valore della proprietà riferimentiTelematici.
     * 
     * @return
     *     possible object is
     *     {@link RiferimentiTelematici }
     *     
     */
    public RiferimentiTelematici getRiferimentiTelematici() {
        return riferimentiTelematici;
    }

    /**
     * Imposta il valore della proprietà riferimentiTelematici.
     * 
     * @param value
     *     allowed object is
     *     {@link RiferimentiTelematici }
     *     
     */
    public void setRiferimentiTelematici(RiferimentiTelematici value) {
        this.riferimentiTelematici = value;
    }

    /**
     * Recupera il valore della proprietà oggetto.
     * 
     * @return
     *     possible object is
     *     {@link Oggetto }
     *     
     */
    public Oggetto getOggetto() {
        return oggetto;
    }

    /**
     * Imposta il valore della proprietà oggetto.
     * 
     * @param value
     *     allowed object is
     *     {@link Oggetto }
     *     
     */
    public void setOggetto(Oggetto value) {
        this.oggetto = value;
    }

    /**
     * Gets the value of the classifica property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the classifica property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClassifica().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Classifica }
     * 
     * 
     */
    public List<Classifica> getClassifica() {
        if (classifica == null) {
            classifica = new ArrayList<Classifica>();
        }
        return this.classifica;
    }

    /**
     * Recupera il valore della proprietà note.
     * 
     * @return
     *     possible object is
     *     {@link Note }
     *     
     */
    public Note getNote() {
        return note;
    }

    /**
     * Imposta il valore della proprietà note.
     * 
     * @param value
     *     allowed object is
     *     {@link Note }
     *     
     */
    public void setNote(Note value) {
        this.note = value;
    }

}
