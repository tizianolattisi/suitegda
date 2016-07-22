
package com.axiastudio.suite.interoperabilita.entities.xsd;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for Comune complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Comune">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="codiceISTAT" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Comune", namespace = "http://www.digitPa.gov.it/protocollo/", propOrder = {
    "content"
})
public class Comune {

    @XmlValue
    protected String content;
    @XmlAttribute(name = "codiceISTAT")
    @XmlSchemaType(name = "anySimpleType")
    protected String codiceISTAT;

    /**
     * Gets the value of the content property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the value of the content property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContent(String value) {
        this.content = value;
    }

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

}
