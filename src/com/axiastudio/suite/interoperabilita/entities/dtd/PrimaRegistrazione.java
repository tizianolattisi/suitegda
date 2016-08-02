
package com.axiastudio.suite.interoperabilita.entities.dtd;

import javax.xml.bind.annotation.*;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "identificatore"
})
@XmlRootElement(name = "PrimaRegistrazione")
public class PrimaRegistrazione {

    @XmlElement(name = "Identificatore", required = true)
    protected Identificatore identificatore;

    /**
     * Gets the value of the identificatore property.
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
     * Sets the value of the identificatore property.
     * 
     * @param value
     *     allowed object is
     *     {@link Identificatore }
     *     
     */
    public void setIdentificatore(Identificatore value) {
        this.identificatore = value;
    }

}
