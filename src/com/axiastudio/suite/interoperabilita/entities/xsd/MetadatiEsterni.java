
package com.axiastudio.suite.interoperabilita.entities.xsd;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for MetadatiEsterni complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
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
     * Gets the value of the nomeFile property.
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
     * Sets the value of the nomeFile property.
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
     * Gets the value of the impronta property.
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
     * Sets the value of the impronta property.
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
     * Gets the value of the codifica property.
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

    /**
     * Gets the value of the estensione property.
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
     * Sets the value of the estensione property.
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
     * Gets the value of the formato property.
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
     * Sets the value of the formato property.
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
