
package com.axiastudio.suite.interoperabilita.entities.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Descrizione complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Descrizione">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Documento"/>
 *           &lt;element ref="{http://www.digitPa.gov.it/protocollo/}TestoDelMessaggio"/>
 *         &lt;/choice>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Allegati" minOccurs="0"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Note" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Descrizione", namespace = "http://www.digitPa.gov.it/protocollo/", propOrder = {
    "documento",
    "testoDelMessaggio",
    "allegati",
    "note"
})
public class Descrizione {

    @XmlElement(name = "Documento", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected Documento documento;
    @XmlElement(name = "TestoDelMessaggio", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected TestoDelMessaggio testoDelMessaggio;
    @XmlElement(name = "Allegati", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected Allegati allegati;
    @XmlElement(name = "Note", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected Note note;

    /**
     * Gets the value of the documento property.
     * 
     * @return
     *     possible object is
     *     {@link Documento }
     *     
     */
    public Documento getDocumento() {
        return documento;
    }

    /**
     * Sets the value of the documento property.
     * 
     * @param value
     *     allowed object is
     *     {@link Documento }
     *     
     */
    public void setDocumento(Documento value) {
        this.documento = value;
    }

    /**
     * Gets the value of the testoDelMessaggio property.
     * 
     * @return
     *     possible object is
     *     {@link TestoDelMessaggio }
     *     
     */
    public TestoDelMessaggio getTestoDelMessaggio() {
        return testoDelMessaggio;
    }

    /**
     * Sets the value of the testoDelMessaggio property.
     * 
     * @param value
     *     allowed object is
     *     {@link TestoDelMessaggio }
     *     
     */
    public void setTestoDelMessaggio(TestoDelMessaggio value) {
        this.testoDelMessaggio = value;
    }

    /**
     * Gets the value of the allegati property.
     * 
     * @return
     *     possible object is
     *     {@link Allegati }
     *     
     */
    public Allegati getAllegati() {
        return allegati;
    }

    /**
     * Sets the value of the allegati property.
     * 
     * @param value
     *     allowed object is
     *     {@link Allegati }
     *     
     */
    public void setAllegati(Allegati value) {
        this.allegati = value;
    }

    /**
     * Gets the value of the note property.
     * 
     * @return
     *     possible object is
     *     {@link Note }
     *     
     */
    public Note getNote() {
        return note;
    }

    /**
     * Sets the value of the note property.
     * 
     * @param value
     *     allowed object is
     *     {@link Note }
     *     
     */
    public void setNote(Note value) {
        this.note = value;
    }

}
