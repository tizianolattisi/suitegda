
package com.axiastudio.suite.interoperabilita.entities;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Classe Java per AnnullamentoProtocollazione complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
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
     * Recupera il valore della proprietà identificatore.
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
     * Imposta il valore della proprietà identificatore.
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
     * Recupera il valore della proprietà motivo.
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
     * Imposta il valore della proprietà motivo.
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
     * Recupera il valore della proprietà provvedimento.
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
     * Imposta il valore della proprietà provvedimento.
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
     * Recupera il valore della proprietà versione.
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
     * Imposta il valore della proprietà versione.
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
     * Recupera il valore della proprietà xmlLang.
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
     * Imposta il valore della proprietà xmlLang.
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
