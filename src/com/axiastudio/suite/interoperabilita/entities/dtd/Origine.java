
package com.axiastudio.suite.interoperabilita.entities.dtd;

import javax.xml.bind.annotation.*;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "indirizzoTelematico",
    "mittente"
})
@XmlRootElement(name = "Origine")
public class Origine {

    @XmlElement(name = "IndirizzoTelematico", required = true)
    protected IndirizzoTelematico indirizzoTelematico;
    @XmlElement(name = "Mittente", required = true)
    protected Mittente mittente;

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

    /**
     * Gets the value of the mittente property.
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
     * Sets the value of the mittente property.
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
