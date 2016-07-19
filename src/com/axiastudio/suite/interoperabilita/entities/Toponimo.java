
package com.axiastudio.suite.interoperabilita.entities;

import javax.xml.bind.annotation.*;


/**
 * <p>Classe Java per Toponimo complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
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
     * Recupera il valore della proprietà content.
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
     * Imposta il valore della proprietà content.
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
     * Recupera il valore della proprietà dug.
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
     * Imposta il valore della proprietà dug.
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
