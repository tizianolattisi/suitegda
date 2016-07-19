
package com.axiastudio.suite.interoperabilita.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per Messaggio complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="Messaggio">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Identificatore"/>
 *           &lt;element ref="{http://www.digitPa.gov.it/protocollo/}DescrizioneMessaggio"/>
 *         &lt;/choice>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}PrimaRegistrazione" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Messaggio", namespace = "http://www.digitPa.gov.it/protocollo/", propOrder = {
    "identificatore",
    "descrizioneMessaggio",
    "primaRegistrazione"
})
public class Messaggio {

    @XmlElement(name = "Identificatore", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected Identificatore identificatore;
    @XmlElement(name = "DescrizioneMessaggio", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected DescrizioneMessaggio descrizioneMessaggio;
    @XmlElement(name = "PrimaRegistrazione", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected PrimaRegistrazione primaRegistrazione;

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

}
