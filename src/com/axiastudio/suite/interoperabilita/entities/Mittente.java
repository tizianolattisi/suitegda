
package com.axiastudio.suite.interoperabilita.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "amministrazione",
    "aoo"
})
@XmlRootElement(name = "Mittente")
public class Mittente {

    @XmlElement(name = "Amministrazione", required = true)
    protected Amministrazione amministrazione;
    @XmlElement(name = "AOO", required = true)
    protected AOO aoo;

    /**
     * Gets the value of the amministrazione property.
     * 
     * @return
     *     possible object is
     *     {@link Amministrazione }
     *     
     */
    public Amministrazione getAmministrazione() {
        return amministrazione;
    }

    /**
     * Sets the value of the amministrazione property.
     * 
     * @param value
     *     allowed object is
     *     {@link Amministrazione }
     *     
     */
    public void setAmministrazione(Amministrazione value) {
        this.amministrazione = value;
    }

    /**
     * Gets the value of the aoo property.
     * 
     * @return
     *     possible object is
     *     {@link AOO }
     *     
     */
    public AOO getAOO() {
        return aoo;
    }

    /**
     * Sets the value of the aoo property.
     * 
     * @param value
     *     allowed object is
     *     {@link AOO }
     *     
     */
    public void setAOO(AOO value) {
        this.aoo = value;
    }

}
