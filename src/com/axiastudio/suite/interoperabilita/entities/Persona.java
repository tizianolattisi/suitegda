
package com.axiastudio.suite.interoperabilita.entities;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Classe Java per Persona complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="Persona">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Denominazione"/>
 *           &lt;sequence>
 *             &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Nome" minOccurs="0"/>
 *             &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Cognome"/>
 *             &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Titolo" minOccurs="0"/>
 *             &lt;element ref="{http://www.digitPa.gov.it/protocollo/}CodiceFiscale" minOccurs="0"/>
 *           &lt;/sequence>
 *         &lt;/choice>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Identificativo" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *       &lt;attribute name="rife" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Persona", namespace = "http://www.digitPa.gov.it/protocollo/", propOrder = {
    "denominazione",
    "nome",
    "cognome",
    "titolo",
    "codiceFiscale",
    "identificativo"
})
public class Persona {

    @XmlElement(name = "Denominazione", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected Denominazione denominazione;
    @XmlElement(name = "Nome", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected Nome nome;
    @XmlElement(name = "Cognome", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected Cognome cognome;
    @XmlElement(name = "Titolo", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected Titolo titolo;
    @XmlElement(name = "CodiceFiscale", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected CodiceFiscale codiceFiscale;
    @XmlElement(name = "Identificativo", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected Identificativo identificativo;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;
    @XmlAttribute(name = "rife")
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object rife;

    /**
     * Recupera il valore della proprietà denominazione.
     * 
     * @return
     *     possible object is
     *     {@link Denominazione }
     *     
     */
    public Denominazione getDenominazione() {
        return denominazione;
    }

    /**
     * Imposta il valore della proprietà denominazione.
     * 
     * @param value
     *     allowed object is
     *     {@link Denominazione }
     *     
     */
    public void setDenominazione(Denominazione value) {
        this.denominazione = value;
    }

    /**
     * Recupera il valore della proprietà nome.
     * 
     * @return
     *     possible object is
     *     {@link Nome }
     *     
     */
    public Nome getNome() {
        return nome;
    }

    /**
     * Imposta il valore della proprietà nome.
     * 
     * @param value
     *     allowed object is
     *     {@link Nome }
     *     
     */
    public void setNome(Nome value) {
        this.nome = value;
    }

    /**
     * Recupera il valore della proprietà cognome.
     * 
     * @return
     *     possible object is
     *     {@link Cognome }
     *     
     */
    public Cognome getCognome() {
        return cognome;
    }

    /**
     * Imposta il valore della proprietà cognome.
     * 
     * @param value
     *     allowed object is
     *     {@link Cognome }
     *     
     */
    public void setCognome(Cognome value) {
        this.cognome = value;
    }

    /**
     * Recupera il valore della proprietà titolo.
     * 
     * @return
     *     possible object is
     *     {@link Titolo }
     *     
     */
    public Titolo getTitolo() {
        return titolo;
    }

    /**
     * Imposta il valore della proprietà titolo.
     * 
     * @param value
     *     allowed object is
     *     {@link Titolo }
     *     
     */
    public void setTitolo(Titolo value) {
        this.titolo = value;
    }

    /**
     * Recupera il valore della proprietà codiceFiscale.
     * 
     * @return
     *     possible object is
     *     {@link CodiceFiscale }
     *     
     */
    public CodiceFiscale getCodiceFiscale() {
        return codiceFiscale;
    }

    /**
     * Imposta il valore della proprietà codiceFiscale.
     * 
     * @param value
     *     allowed object is
     *     {@link CodiceFiscale }
     *     
     */
    public void setCodiceFiscale(CodiceFiscale value) {
        this.codiceFiscale = value;
    }

    /**
     * Recupera il valore della proprietà identificativo.
     * 
     * @return
     *     possible object is
     *     {@link Identificativo }
     *     
     */
    public Identificativo getIdentificativo() {
        return identificativo;
    }

    /**
     * Imposta il valore della proprietà identificativo.
     * 
     * @param value
     *     allowed object is
     *     {@link Identificativo }
     *     
     */
    public void setIdentificativo(Identificativo value) {
        this.identificativo = value;
    }

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
     * Recupera il valore della proprietà rife.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getRife() {
        return rife;
    }

    /**
     * Imposta il valore della proprietà rife.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setRife(Object value) {
        this.rife = value;
    }

}
