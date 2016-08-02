
package com.axiastudio.suite.interoperabilita.entities.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Identificatore complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Identificatore">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}CodiceAmministrazione"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}CodiceAOO"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}CodiceRegistro"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}NumeroRegistrazione"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}DataRegistrazione"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Identificatore", namespace = "http://www.digitPa.gov.it/protocollo/", propOrder = {
    "codiceAmministrazione",
    "codiceAOO",
    "codiceRegistro",
    "numeroRegistrazione",
    "dataRegistrazione"
})
public class Identificatore {

    @XmlElement(name = "CodiceAmministrazione", namespace = "http://www.digitPa.gov.it/protocollo/", required = true)
    protected CodiceAmministrazione codiceAmministrazione;
    @XmlElement(name = "CodiceAOO", namespace = "http://www.digitPa.gov.it/protocollo/", required = true)
    protected CodiceAOO codiceAOO;
    @XmlElement(name = "CodiceRegistro", namespace = "http://www.digitPa.gov.it/protocollo/", required = true)
    protected CodiceRegistro codiceRegistro;
    @XmlElement(name = "NumeroRegistrazione", namespace = "http://www.digitPa.gov.it/protocollo/", required = true)
    protected NumeroRegistrazione numeroRegistrazione;
    @XmlElement(name = "DataRegistrazione", namespace = "http://www.digitPa.gov.it/protocollo/", required = true)
    protected DataRegistrazione dataRegistrazione;

    /**
     * Gets the value of the codiceAmministrazione property.
     * 
     * @return
     *     possible object is
     *     {@link CodiceAmministrazione }
     *     
     */
    public CodiceAmministrazione getCodiceAmministrazione() {
        return codiceAmministrazione;
    }

    /**
     * Sets the value of the codiceAmministrazione property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodiceAmministrazione }
     *     
     */
    public void setCodiceAmministrazione(CodiceAmministrazione value) {
        this.codiceAmministrazione = value;
    }

    /**
     * Gets the value of the codiceAOO property.
     * 
     * @return
     *     possible object is
     *     {@link CodiceAOO }
     *     
     */
    public CodiceAOO getCodiceAOO() {
        return codiceAOO;
    }

    /**
     * Sets the value of the codiceAOO property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodiceAOO }
     *     
     */
    public void setCodiceAOO(CodiceAOO value) {
        this.codiceAOO = value;
    }

    /**
     * Gets the value of the codiceRegistro property.
     * 
     * @return
     *     possible object is
     *     {@link CodiceRegistro }
     *     
     */
    public CodiceRegistro getCodiceRegistro() {
        return codiceRegistro;
    }

    /**
     * Sets the value of the codiceRegistro property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodiceRegistro }
     *     
     */
    public void setCodiceRegistro(CodiceRegistro value) {
        this.codiceRegistro = value;
    }

    /**
     * Gets the value of the numeroRegistrazione property.
     * 
     * @return
     *     possible object is
     *     {@link NumeroRegistrazione }
     *     
     */
    public NumeroRegistrazione getNumeroRegistrazione() {
        return numeroRegistrazione;
    }

    /**
     * Sets the value of the numeroRegistrazione property.
     * 
     * @param value
     *     allowed object is
     *     {@link NumeroRegistrazione }
     *     
     */
    public void setNumeroRegistrazione(NumeroRegistrazione value) {
        this.numeroRegistrazione = value;
    }

    /**
     * Gets the value of the dataRegistrazione property.
     * 
     * @return
     *     possible object is
     *     {@link DataRegistrazione }
     *     
     */
    public DataRegistrazione getDataRegistrazione() {
        return dataRegistrazione;
    }

    /**
     * Sets the value of the dataRegistrazione property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataRegistrazione }
     *     
     */
    public void setDataRegistrazione(DataRegistrazione value) {
        this.dataRegistrazione = value;
    }

}
