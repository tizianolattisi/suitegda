
package com.axiastudio.suite.interoperabilita.entities;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Classe Java per MetadatiEsterni complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="MetadatiEsterni">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}NomeFile"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Impronta" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="codifica" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *             &lt;enumeration value="binary"/>
 *             &lt;enumeration value="xtoken"/>
 *             &lt;enumeration value="quotedprintable"/>
 *             &lt;enumeration value="7bit"/>
 *             &lt;enumeration value="base64"/>
 *             &lt;enumeration value="8bit"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="estensione" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
 *       &lt;attribute name="formato" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MetadatiEsterni", namespace = "http://www.digitPa.gov.it/protocollo/", propOrder = {
    "nomeFile",
    "impronta"
})
public class MetadatiEsterni {

    @XmlElement(name = "NomeFile", namespace = "http://www.digitPa.gov.it/protocollo/", required = true)
    protected NomeFile nomeFile;
    @XmlElement(name = "Impronta", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected Impronta impronta;
    @XmlAttribute(name = "codifica", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String codifica;
    @XmlAttribute(name = "estensione")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String estensione;
    @XmlAttribute(name = "formato", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String formato;

    /**
     * Recupera il valore della proprietà nomeFile.
     * 
     * @return
     *     possible object is
     *     {@link NomeFile }
     *     
     */
    public NomeFile getNomeFile() {
        return nomeFile;
    }

    /**
     * Imposta il valore della proprietà nomeFile.
     * 
     * @param value
     *     allowed object is
     *     {@link NomeFile }
     *     
     */
    public void setNomeFile(NomeFile value) {
        this.nomeFile = value;
    }

    /**
     * Recupera il valore della proprietà impronta.
     * 
     * @return
     *     possible object is
     *     {@link Impronta }
     *     
     */
    public Impronta getImpronta() {
        return impronta;
    }

    /**
     * Imposta il valore della proprietà impronta.
     * 
     * @param value
     *     allowed object is
     *     {@link Impronta }
     *     
     */
    public void setImpronta(Impronta value) {
        this.impronta = value;
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
        return codifica;
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

    /**
     * Recupera il valore della proprietà estensione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstensione() {
        return estensione;
    }

    /**
     * Imposta il valore della proprietà estensione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstensione(String value) {
        this.estensione = value;
    }

    /**
     * Recupera il valore della proprietà formato.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFormato() {
        return formato;
    }

    /**
     * Imposta il valore della proprietà formato.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFormato(String value) {
        this.formato = value;
    }

}
