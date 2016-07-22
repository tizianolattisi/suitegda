
package com.axiastudio.suite.interoperabilita.entities.dtd;

import javax.xml.bind.annotation.*;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "denominazione",
    "codiceAOO"
})
@XmlRootElement(name = "AOO")
public class AOO {

    @XmlElement(name = "Denominazione", required = true)
    protected Denominazione denominazione;
    @XmlElement(name = "CodiceAOO")
    protected String codiceAOO;

    /**
     * Gets the value of the denominazione property.
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
     * Sets the value of the denominazione property.
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
     * Gets the value of the codiceAOO property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceAOO() {
        return codiceAOO;
    }

    /**
     * Sets the value of the codiceAOO property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceAOO(String value) {
        this.codiceAOO = value;
    }

}
