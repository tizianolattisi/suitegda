
package com.axiastudio.suite.interoperabilita.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per Indirizzo complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="Indirizzo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Toponimo"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Civico"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}CAP"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Comune"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Provincia"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Nazione" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Indirizzo", namespace = "http://www.digitPa.gov.it/protocollo/", propOrder = {
    "toponimo",
    "civico",
    "cap",
    "comune",
    "provincia",
    "nazione"
})
public class Indirizzo {

    @XmlElement(name = "Toponimo", namespace = "http://www.digitPa.gov.it/protocollo/", required = true)
    protected Toponimo toponimo;
    @XmlElement(name = "Civico", namespace = "http://www.digitPa.gov.it/protocollo/", required = true)
    protected Civico civico;
    @XmlElement(name = "CAP", namespace = "http://www.digitPa.gov.it/protocollo/", required = true)
    protected CAP cap;
    @XmlElement(name = "Comune", namespace = "http://www.digitPa.gov.it/protocollo/", required = true)
    protected Comune comune;
    @XmlElement(name = "Provincia", namespace = "http://www.digitPa.gov.it/protocollo/", required = true)
    protected Provincia provincia;
    @XmlElement(name = "Nazione", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected Nazione nazione;

    /**
     * Recupera il valore della proprietà toponimo.
     * 
     * @return
     *     possible object is
     *     {@link Toponimo }
     *     
     */
    public Toponimo getToponimo() {
        return toponimo;
    }

    /**
     * Imposta il valore della proprietà toponimo.
     * 
     * @param value
     *     allowed object is
     *     {@link Toponimo }
     *     
     */
    public void setToponimo(Toponimo value) {
        this.toponimo = value;
    }

    /**
     * Recupera il valore della proprietà civico.
     * 
     * @return
     *     possible object is
     *     {@link Civico }
     *     
     */
    public Civico getCivico() {
        return civico;
    }

    /**
     * Imposta il valore della proprietà civico.
     * 
     * @param value
     *     allowed object is
     *     {@link Civico }
     *     
     */
    public void setCivico(Civico value) {
        this.civico = value;
    }

    /**
     * Recupera il valore della proprietà cap.
     * 
     * @return
     *     possible object is
     *     {@link CAP }
     *     
     */
    public CAP getCAP() {
        return cap;
    }

    /**
     * Imposta il valore della proprietà cap.
     * 
     * @param value
     *     allowed object is
     *     {@link CAP }
     *     
     */
    public void setCAP(CAP value) {
        this.cap = value;
    }

    /**
     * Recupera il valore della proprietà comune.
     * 
     * @return
     *     possible object is
     *     {@link Comune }
     *     
     */
    public Comune getComune() {
        return comune;
    }

    /**
     * Imposta il valore della proprietà comune.
     * 
     * @param value
     *     allowed object is
     *     {@link Comune }
     *     
     */
    public void setComune(Comune value) {
        this.comune = value;
    }

    /**
     * Recupera il valore della proprietà provincia.
     * 
     * @return
     *     possible object is
     *     {@link Provincia }
     *     
     */
    public Provincia getProvincia() {
        return provincia;
    }

    /**
     * Imposta il valore della proprietà provincia.
     * 
     * @param value
     *     allowed object is
     *     {@link Provincia }
     *     
     */
    public void setProvincia(Provincia value) {
        this.provincia = value;
    }

    /**
     * Recupera il valore della proprietà nazione.
     * 
     * @return
     *     possible object is
     *     {@link Nazione }
     *     
     */
    public Nazione getNazione() {
        return nazione;
    }

    /**
     * Imposta il valore della proprietà nazione.
     * 
     * @param value
     *     allowed object is
     *     {@link Nazione }
     *     
     */
    public void setNazione(Nazione value) {
        this.nazione = value;
    }

}
