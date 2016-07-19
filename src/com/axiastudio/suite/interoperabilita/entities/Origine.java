
package com.axiastudio.suite.interoperabilita.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per Origine complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="Origine">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}IndirizzoTelematico"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Mittente"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Origine", namespace = "http://www.digitPa.gov.it/protocollo/", propOrder = {
    "indirizzoTelematico",
    "mittente"
})
public class Origine {

    @XmlElement(name = "IndirizzoTelematico", namespace = "http://www.digitPa.gov.it/protocollo/", required = true)
    protected IndirizzoTelematico indirizzoTelematico;
    @XmlElement(name = "Mittente", namespace = "http://www.digitPa.gov.it/protocollo/", required = true)
    protected Mittente mittente;

    /**
     * Recupera il valore della proprietà indirizzoTelematico.
     * 
     * @return
     *     possible object is
     *     {@link IndirizzoTelematico }
     *     
     */
    public IndirizzoTelematico getIndirizzoTelematico() {
        return indirizzoTelematico;
    }

    /**
     * Imposta il valore della proprietà indirizzoTelematico.
     * 
     * @param value
     *     allowed object is
     *     {@link IndirizzoTelematico }
     *     
     */
    public void setIndirizzoTelematico(IndirizzoTelematico value) {
        this.indirizzoTelematico = value;
    }

    /**
     * Recupera il valore della proprietà mittente.
     * 
     * @return
     *     possible object is
     *     {@link Mittente }
     *     
     */
    public Mittente getMittente() {
        return mittente;
    }

    /**
     * Imposta il valore della proprietà mittente.
     * 
     * @param value
     *     allowed object is
     *     {@link Mittente }
     *     
     */
    public void setMittente(Mittente value) {
        this.mittente = value;
    }

}
