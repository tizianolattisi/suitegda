
package com.axiastudio.suite.interoperabilita.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per Ruolo complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="Ruolo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Denominazione"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Identificativo" minOccurs="0"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Persona" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Ruolo", namespace = "http://www.digitPa.gov.it/protocollo/", propOrder = {
    "denominazione",
    "identificativo",
    "persona"
})
public class Ruolo {

    @XmlElement(name = "Denominazione", namespace = "http://www.digitPa.gov.it/protocollo/", required = true)
    protected Denominazione denominazione;
    @XmlElement(name = "Identificativo", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected Identificativo identificativo;
    @XmlElement(name = "Persona", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected Persona persona;

    /**
     * Recupera il valore della proprietà denominazione.
     * 
     * @return
     *     possible object is
     *     {@link Denominazione }
     *     
     */
    public Denominazione getDenominazione() {
        return denominazione;
    }

    /**
     * Imposta il valore della proprietà denominazione.
     * 
     * @param value
     *     allowed object is
     *     {@link Denominazione }
     *     
     */
    public void setDenominazione(Denominazione value) {
        this.denominazione = value;
    }

    /**
     * Recupera il valore della proprietà identificativo.
     * 
     * @return
     *     possible object is
     *     {@link Identificativo }
     *     
     */
    public Identificativo getIdentificativo() {
        return identificativo;
    }

    /**
     * Imposta il valore della proprietà identificativo.
     * 
     * @param value
     *     allowed object is
     *     {@link Identificativo }
     *     
     */
    public void setIdentificativo(Identificativo value) {
        this.identificativo = value;
    }

    /**
     * Recupera il valore della proprietà persona.
     * 
     * @return
     *     possible object is
     *     {@link Persona }
     *     
     */
    public Persona getPersona() {
        return persona;
    }

    /**
     * Imposta il valore della proprietà persona.
     * 
     * @param value
     *     allowed object is
     *     {@link Persona }
     *     
     */
    public void setPersona(Persona value) {
        this.persona = value;
    }

}
