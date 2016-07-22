
package com.axiastudio.suite.interoperabilita.entities.dtd;

import javax.xml.bind.annotation.*;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "persona"
})
@XmlRootElement(name = "Responsabile")
public class Responsabile {

    @XmlElement(name = "Persona", required = true)
    protected Persona persona;

    /**
     * Gets the value of the persona property.
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
     * Sets the value of the persona property.
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
