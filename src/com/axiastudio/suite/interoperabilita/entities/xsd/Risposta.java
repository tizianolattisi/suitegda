
package com.axiastudio.suite.interoperabilita.entities.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Risposta complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Risposta">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}IndirizzoTelematico"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Risposta", namespace = "http://www.digitPa.gov.it/protocollo/", propOrder = {
    "indirizzoTelematico"
})
public class Risposta {

    @XmlElement(name = "IndirizzoTelematico", namespace = "http://www.digitPa.gov.it/protocollo/", required = true)
    protected IndirizzoTelematico indirizzoTelematico;

    /**
     * Gets the value of the indirizzoTelematico property.
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
     * Sets the value of the indirizzoTelematico property.
     * 
     * @param value
     *     allowed object is
     *     {@link IndirizzoTelematico }
     *     
     */
    public void setIndirizzoTelematico(IndirizzoTelematico value) {
        this.indirizzoTelematico = value;
    }

}
