
package com.axiastudio.suite.interoperabilita.entities;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "value"
})
@XmlRootElement(name = "OraRegistrazione")
public class OraRegistrazione {

    @XmlAttribute(name = "tempo")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String tempo;
    @XmlValue
    protected String value;

    /**
     * Gets the value of the tempo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTempo() {
        if (tempo == null) {
            return "locale";
        } else {
            return tempo;
        }
    }

    /**
     * Sets the value of the tempo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTempo(String value) {
        this.tempo = value;
    }

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getvalue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setvalue(String value) {
        this.value = value;
    }

}
