
package com.axiastudio.suite.interoperabilita.entities;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Classe Java per NotificaEccezione complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="NotificaEccezione">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Identificatore" minOccurs="0"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}MessaggioRicevuto"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Motivo"/>
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
@XmlType(name = "NotificaEccezione", namespace = "http://www.digitPa.gov.it/protocollo/", propOrder = {
    "identificatore",
    "messaggioRicevuto",
    "motivo"
})
public class NotificaEccezione {

    @XmlElement(name = "Identificatore", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected Identificatore identificatore;
    @XmlElement(name = "MessaggioRicevuto", namespace = "http://www.digitPa.gov.it/protocollo/", required = true)
    protected MessaggioRicevuto messaggioRicevuto;
    @XmlElement(name = "Motivo", namespace = "http://www.digitPa.gov.it/protocollo/", required = true)
    protected Motivo motivo;
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
     * Recupera il valore della proprietà messaggioRicevuto.
     * 
     * @return
     *     possible object is
     *     {@link MessaggioRicevuto }
     *     
     */
    public MessaggioRicevuto getMessaggioRicevuto() {
        return messaggioRicevuto;
    }

    /**
     * Imposta il valore della proprietà messaggioRicevuto.
     * 
     * @param value
     *     allowed object is
     *     {@link MessaggioRicevuto }
     *     
     */
    public void setMessaggioRicevuto(MessaggioRicevuto value) {
        this.messaggioRicevuto = value;
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
