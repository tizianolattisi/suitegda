
package com.axiastudio.suite.interoperabilita.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per Identificatore complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
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
     * Recupera il valore della proprietà codiceAmministrazione.
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
     * Imposta il valore della proprietà codiceAmministrazione.
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
     * Recupera il valore della proprietà codiceAOO.
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
     * Imposta il valore della proprietà codiceAOO.
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
     * Recupera il valore della proprietà codiceRegistro.
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
     * Imposta il valore della proprietà codiceRegistro.
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
     * Recupera il valore della proprietà numeroRegistrazione.
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
     * Imposta il valore della proprietà numeroRegistrazione.
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
     * Recupera il valore della proprietà dataRegistrazione.
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
     * Imposta il valore della proprietà dataRegistrazione.
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
