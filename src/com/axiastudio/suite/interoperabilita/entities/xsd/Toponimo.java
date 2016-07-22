
package com.axiastudio.suite.interoperabilita.entities.xsd;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for Toponimo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Toponimo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="dug" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Toponimo", namespace = "http://www.digitPa.gov.it/protocollo/", propOrder = {
    "content"
})
public class Toponimo {

    @XmlValue
    protected String content;
    @XmlAttribute(name = "dug")
    @XmlSchemaType(name = "anySimpleType")
    protected String dug;

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

}
