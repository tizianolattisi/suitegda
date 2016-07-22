
package com.axiastudio.suite.interoperabilita.entities.xsd;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for Persona complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
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
     * Gets the value of the denominazione property.
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
     * Sets the value of the denominazione property.
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
     * Gets the value of the nome property.
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
     * Sets the value of the nome property.
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
     * Gets the value of the cognome property.
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
     * Sets the value of the cognome property.
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
     * Gets the value of the titolo property.
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
     * Sets the value of the titolo property.
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
     * Gets the value of the codiceFiscale property.
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
     * Sets the value of the codiceFiscale property.
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
     * Gets the value of the identificativo property.
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
     * Sets the value of the identificativo property.
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
     * Gets the value of the id property.
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
     * Sets the value of the id property.
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
     * Gets the value of the rife property.
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
     * Sets the value of the rife property.
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
