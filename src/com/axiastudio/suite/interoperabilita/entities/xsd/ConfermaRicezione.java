
package com.axiastudio.suite.interoperabilita.entities.xsd;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for ConfermaRicezione complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ConfermaRicezione">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Identificatore"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}MessaggioRicevuto"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Riferimenti" minOccurs="0"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Descrizione" minOccurs="0"/>
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
@XmlType(name = "ConfermaRicezione", namespace = "http://www.digitPa.gov.it/protocollo/", propOrder = {
    "identificatore",
    "messaggioRicevuto",
    "riferimenti",
    "descrizione"
})
@XmlRootElement
public class ConfermaRicezione {

    @XmlElement(name = "Identificatore", namespace = "http://www.digitPa.gov.it/protocollo/", required = true)
    protected Identificatore identificatore;
    @XmlElement(name = "MessaggioRicevuto", namespace = "http://www.digitPa.gov.it/protocollo/", required = true)
    protected MessaggioRicevuto messaggioRicevuto;
    @XmlElement(name = "Riferimenti", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected Riferimenti riferimenti;
    @XmlElement(name = "Descrizione", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected Descrizione descrizione;
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
     * Gets the value of the messaggioRicevuto property.
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
     * Sets the value of the messaggioRicevuto property.
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
     * Gets the value of the riferimenti property.
     * 
     * @return
     *     possible object is
     *     {@link Riferimenti }
     *     
     */
    public Riferimenti getRiferimenti() {
        return riferimenti;
    }

    /**
     * Sets the value of the riferimenti property.
     * 
     * @param value
     *     allowed object is
     *     {@link Riferimenti }
     *     
     */
    public void setRiferimenti(Riferimenti value) {
        this.riferimenti = value;
    }

    /**
     * Gets the value of the descrizione property.
     * 
     * @return
     *     possible object is
     *     {@link Descrizione }
     *     
     */
    public Descrizione getDescrizione() {
        return descrizione;
    }

    /**
     * Sets the value of the descrizione property.
     * 
     * @param value
     *     allowed object is
     *     {@link Descrizione }
     *     
     */
    public void setDescrizione(Descrizione value) {
        this.descrizione = value;
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
