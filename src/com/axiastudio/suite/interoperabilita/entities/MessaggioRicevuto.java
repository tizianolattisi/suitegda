
package com.axiastudio.suite.interoperabilita.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per MessaggioRicevuto complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="MessaggioRicevuto">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;sequence>
 *           &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Identificatore"/>
 *           &lt;element ref="{http://www.digitPa.gov.it/protocollo/}PrimaRegistrazione" minOccurs="0"/>
 *         &lt;/sequence>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}DescrizioneMessaggio"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessaggioRicevuto", namespace = "http://www.digitPa.gov.it/protocollo/", propOrder = {
    "identificatore",
    "primaRegistrazione",
    "descrizioneMessaggio"
})
public class MessaggioRicevuto {

    @XmlElement(name = "Identificatore", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected Identificatore identificatore;
    @XmlElement(name = "PrimaRegistrazione", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected PrimaRegistrazione primaRegistrazione;
    @XmlElement(name = "DescrizioneMessaggio", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected DescrizioneMessaggio descrizioneMessaggio;

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
     * Recupera il valore della proprietà primaRegistrazione.
     * 
     * @return
     *     possible object is
     *     {@link PrimaRegistrazione }
     *     
     */
    public PrimaRegistrazione getPrimaRegistrazione() {
        return primaRegistrazione;
    }

    /**
     * Imposta il valore della proprietà primaRegistrazione.
     * 
     * @param value
     *     allowed object is
     *     {@link PrimaRegistrazione }
     *     
     */
    public void setPrimaRegistrazione(PrimaRegistrazione value) {
        this.primaRegistrazione = value;
    }

    /**
     * Recupera il valore della proprietà descrizioneMessaggio.
     * 
     * @return
     *     possible object is
     *     {@link DescrizioneMessaggio }
     *     
     */
    public DescrizioneMessaggio getDescrizioneMessaggio() {
        return descrizioneMessaggio;
    }

    /**
     * Imposta il valore della proprietà descrizioneMessaggio.
     * 
     * @param value
     *     allowed object is
     *     {@link DescrizioneMessaggio }
     *     
     */
    public void setDescrizioneMessaggio(DescrizioneMessaggio value) {
        this.descrizioneMessaggio = value;
    }

}
