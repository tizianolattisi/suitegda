
package com.axiastudio.suite.interoperabilita.entities;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Classe Java per Fascicolo complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="Fascicolo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}CodiceAmministrazione" minOccurs="0"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}CodiceAOO" minOccurs="0"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Oggetto" minOccurs="0"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Identificativo" minOccurs="0"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Classifica" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Note" minOccurs="0"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}PiuInfo" minOccurs="0"/>
 *         &lt;choice maxOccurs="unbounded">
 *           &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Documento"/>
 *           &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Fascicolo"/>
 *         &lt;/choice>
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
@XmlType(name = "Fascicolo", namespace = "http://www.digitPa.gov.it/protocollo/", propOrder = {
    "codiceAmministrazione",
    "codiceAOO",
    "oggetto",
    "identificativo",
    "classifica",
    "note",
    "piuInfo",
    "documentoOrFascicolo"
})
public class Fascicolo {

    @XmlElement(name = "CodiceAmministrazione", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected CodiceAmministrazione codiceAmministrazione;
    @XmlElement(name = "CodiceAOO", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected CodiceAOO codiceAOO;
    @XmlElement(name = "Oggetto", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected Oggetto oggetto;
    @XmlElement(name = "Identificativo", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected Identificativo identificativo;
    @XmlElement(name = "Classifica", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected List<Classifica> classifica;
    @XmlElement(name = "Note", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected Note note;
    @XmlElement(name = "PiuInfo", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected PiuInfo piuInfo;
    @XmlElements({
        @XmlElement(name = "Documento", namespace = "http://www.digitPa.gov.it/protocollo/", type = Documento.class),
        @XmlElement(name = "Fascicolo", namespace = "http://www.digitPa.gov.it/protocollo/", type = Fascicolo.class)
    })
    protected List<Object> documentoOrFascicolo;
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
     * Recupera il valore della proprietà codiceAmministrazione.
     * 
     * @return
     *     possible object is
     *     {@link CodiceAmministrazione }
     *     
     */
    public CodiceAmministrazione getCodiceAmministrazione() {
        return codiceAmministrazione;
    }

    /**
     * Imposta il valore della proprietà codiceAmministrazione.
     * 
     * @param value
     *     allowed object is
     *     {@link CodiceAmministrazione }
     *     
     */
    public void setCodiceAmministrazione(CodiceAmministrazione value) {
        this.codiceAmministrazione = value;
    }

    /**
     * Recupera il valore della proprietà codiceAOO.
     * 
     * @return
     *     possible object is
     *     {@link CodiceAOO }
     *     
     */
    public CodiceAOO getCodiceAOO() {
        return codiceAOO;
    }

    /**
     * Imposta il valore della proprietà codiceAOO.
     * 
     * @param value
     *     allowed object is
     *     {@link CodiceAOO }
     *     
     */
    public void setCodiceAOO(CodiceAOO value) {
        this.codiceAOO = value;
    }

    /**
     * Recupera il valore della proprietà oggetto.
     * 
     * @return
     *     possible object is
     *     {@link Oggetto }
     *     
     */
    public Oggetto getOggetto() {
        return oggetto;
    }

    /**
     * Imposta il valore della proprietà oggetto.
     * 
     * @param value
     *     allowed object is
     *     {@link Oggetto }
     *     
     */
    public void setOggetto(Oggetto value) {
        this.oggetto = value;
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
     * Gets the value of the classifica property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the classifica property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClassifica().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Classifica }
     * 
     * 
     */
    public List<Classifica> getClassifica() {
        if (classifica == null) {
            classifica = new ArrayList<Classifica>();
        }
        return this.classifica;
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

    /**
     * Recupera il valore della proprietà piuInfo.
     * 
     * @return
     *     possible object is
     *     {@link PiuInfo }
     *     
     */
    public PiuInfo getPiuInfo() {
        return piuInfo;
    }

    /**
     * Imposta il valore della proprietà piuInfo.
     * 
     * @param value
     *     allowed object is
     *     {@link PiuInfo }
     *     
     */
    public void setPiuInfo(PiuInfo value) {
        this.piuInfo = value;
    }

    /**
     * Gets the value of the documentoOrFascicolo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the documentoOrFascicolo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDocumentoOrFascicolo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Documento }
     * {@link Fascicolo }
     * 
     * 
     */
    public List<Object> getDocumentoOrFascicolo() {
        if (documentoOrFascicolo == null) {
            documentoOrFascicolo = new ArrayList<Object>();
        }
        return this.documentoOrFascicolo;
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
