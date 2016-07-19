
package com.axiastudio.suite.interoperabilita.entities;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Classe Java per TestoDelMessaggio complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
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
     * Recupera il valore della proprietà id.
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
     * Imposta il valore della proprietà id.
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
     * Recupera il valore della proprietà tipoMIME.
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
     * Imposta il valore della proprietà tipoMIME.
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
     * Recupera il valore della proprietà tipoRiferimento.
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
     * Imposta il valore della proprietà tipoRiferimento.
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
