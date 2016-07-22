
package com.axiastudio.suite.interoperabilita.entities.xsd;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for TestoDelMessaggio complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TestoDelMessaggio">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="tipoMIME" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="tipoRiferimento" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" fixed="MIME" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TestoDelMessaggio", namespace = "http://www.digitPa.gov.it/protocollo/")
public class TestoDelMessaggio {

    @XmlAttribute(name = "id")
    @XmlSchemaType(name = "anySimpleType")
    protected String id;
    @XmlAttribute(name = "tipoMIME")
    @XmlSchemaType(name = "anySimpleType")
    protected String tipoMIME;
    @XmlAttribute(name = "tipoRiferimento")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String tipoRiferimento;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the tipoMIME property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoMIME() {
        return tipoMIME;
    }

    /**
     * Sets the value of the tipoMIME property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoMIME(String value) {
        this.tipoMIME = value;
    }

    /**
     * Gets the value of the tipoRiferimento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoRiferimento() {
        if (tipoRiferimento == null) {
            return "MIME";
        } else {
            return tipoRiferimento;
        }
    }

    /**
     * Sets the value of the tipoRiferimento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoRiferimento(String value) {
        this.tipoRiferimento = value;
    }

}
