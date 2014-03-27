
package com.axiastudio.suite.interoperabilita.entities;

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
@XmlRootElement(name = "Comune")
public class Comune {

    @XmlAttribute(name = "codiceISTAT")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String codiceISTAT;
    @XmlValue
    protected String value;

    /**
     * Gets the value of the codiceISTAT property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceISTAT() {
        return codiceISTAT;
    }

    /**
     * Sets the value of the codiceISTAT property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceISTAT(String value) {
        this.codiceISTAT = value;
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
