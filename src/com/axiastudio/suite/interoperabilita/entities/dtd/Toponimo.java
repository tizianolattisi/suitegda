
package com.axiastudio.suite.interoperabilita.entities.dtd;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "value"
})
@XmlRootElement(name = "Toponimo")
public class Toponimo {

    @XmlAttribute(name = "dug")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String dug;
    @XmlValue
    protected String value;

    /**
     * Gets the value of the dug property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDug() {
        return dug;
    }

    /**
     * Sets the value of the dug property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDug(String value) {
        this.dug = value;
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
