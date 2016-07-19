
package com.axiastudio.suite.interoperabilita.entities;

import javax.xml.bind.annotation.*;


/**
 * <p>Classe Java per Comune complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
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
     * Recupera il valore della proprietà codiceISTAT.
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
     * Imposta il valore della proprietà codiceISTAT.
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
