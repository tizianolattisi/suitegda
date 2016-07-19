
package com.axiastudio.suite.interoperabilita.entities;

import javax.xml.bind.annotation.*;


/**
 * <p>Classe Java per Impronta complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
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
     * Recupera il valore della proprietà algoritmo.
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
     * Imposta il valore della proprietà algoritmo.
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
     * Recupera il valore della proprietà codifica.
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
     * Imposta il valore della proprietà codifica.
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
