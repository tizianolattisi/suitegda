
package com.axiastudio.suite.interoperabilita.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per Descrizione complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
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
     * Recupera il valore della proprietà documento.
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
     * Imposta il valore della proprietà documento.
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
     * Recupera il valore della proprietà testoDelMessaggio.
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
     * Imposta il valore della proprietà testoDelMessaggio.
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
     * Recupera il valore della proprietà allegati.
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
     * Imposta il valore della proprietà allegati.
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
     * Recupera il valore della proprietà note.
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
     * Imposta il valore della proprietà note.
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
