
package com.axiastudio.suite.interoperabilita.entities.xsd;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for Impronta complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Impronta">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="algoritmo" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" fixed="SHA-256" />
 *       &lt;attribute name="codifica" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" fixed="base64" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Impronta", namespace = "http://www.digitPa.gov.it/protocollo/", propOrder = {
    "content"
})
public class Impronta {

    @XmlValue
    protected String content;
    @XmlAttribute(name = "algoritmo")
    @XmlSchemaType(name = "anySimpleType")
    protected String algoritmo;
    @XmlAttribute(name = "codifica")
    @XmlSchemaType(name = "anySimpleType")
    protected String codifica;

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
     * Gets the value of the algoritmo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlgoritmo() {
        if (algoritmo == null) {
            return "SHA-256";
        } else {
            return algoritmo;
        }
    }

    /**
     * Sets the value of the algoritmo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlgoritmo(String value) {
        this.algoritmo = value;
    }

    /**
     * Gets the value of the codifica property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodifica() {
        if (codifica == null) {
            return "base64";
        } else {
            return codifica;
        }
    }

    /**
     * Sets the value of the codifica property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodifica(String value) {
        this.codifica = value;
    }

}
