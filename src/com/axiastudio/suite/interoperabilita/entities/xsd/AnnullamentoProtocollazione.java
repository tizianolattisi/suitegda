
package com.axiastudio.suite.interoperabilita.entities.xsd;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for AnnullamentoProtocollazione complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AnnullamentoProtocollazione">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Identificatore"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Motivo"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Provvedimento"/>
 *       &lt;/sequence>
 *       &lt;attribute name="versione" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" fixed="aaaa-mm-gg" />
 *       &lt;attribute name="xml-lang" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" fixed="it" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AnnullamentoProtocollazione", namespace = "http://www.digitPa.gov.it/protocollo/", propOrder = {
    "identificatore",
    "motivo",
    "provvedimento"
})
public class AnnullamentoProtocollazione {

    @XmlElement(name = "Identificatore", namespace = "http://www.digitPa.gov.it/protocollo/", required = true)
    protected Identificatore identificatore;
    @XmlElement(name = "Motivo", namespace = "http://www.digitPa.gov.it/protocollo/", required = true)
    protected Motivo motivo;
    @XmlElement(name = "Provvedimento", namespace = "http://www.digitPa.gov.it/protocollo/", required = true)
    protected Provvedimento provvedimento;
    @XmlAttribute(name = "versione")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String versione;
    @XmlAttribute(name = "xml-lang")
    @XmlSchemaType(name = "anySimpleType")
    protected String xmlLang;

    /**
     * Gets the value of the identificatore property.
     * 
     * @return
     *     possible object is
     *     {@link Identificatore }
     *     
     */
    public Identificatore getIdentificatore() {
        return identificatore;
    }

    /**
     * Sets the value of the identificatore property.
     * 
     * @param value
     *     allowed object is
     *     {@link Identificatore }
     *     
     */
    public void setIdentificatore(Identificatore value) {
        this.identificatore = value;
    }

    /**
     * Gets the value of the motivo property.
     * 
     * @return
     *     possible object is
     *     {@link Motivo }
     *     
     */
    public Motivo getMotivo() {
        return motivo;
    }

    /**
     * Sets the value of the motivo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Motivo }
     *     
     */
    public void setMotivo(Motivo value) {
        this.motivo = value;
    }

    /**
     * Gets the value of the provvedimento property.
     * 
     * @return
     *     possible object is
     *     {@link Provvedimento }
     *     
     */
    public Provvedimento getProvvedimento() {
        return provvedimento;
    }

    /**
     * Sets the value of the provvedimento property.
     * 
     * @param value
     *     allowed object is
     *     {@link Provvedimento }
     *     
     */
    public void setProvvedimento(Provvedimento value) {
        this.provvedimento = value;
    }

    /**
     * Gets the value of the versione property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersione() {
        if (versione == null) {
            return "aaaa-mm-gg";
        } else {
            return versione;
        }
    }

    /**
     * Sets the value of the versione property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersione(String value) {
        this.versione = value;
    }

    /**
     * Gets the value of the xmlLang property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXmlLang() {
        if (xmlLang == null) {
            return "it";
        } else {
            return xmlLang;
        }
    }

    /**
     * Sets the value of the xmlLang property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXmlLang(String value) {
        this.xmlLang = value;
    }

}
