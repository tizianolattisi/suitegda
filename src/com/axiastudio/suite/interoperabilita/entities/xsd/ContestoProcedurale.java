
package com.axiastudio.suite.interoperabilita.entities.xsd;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for ContestoProcedurale complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ContestoProcedurale">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}CodiceAmministrazione"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}CodiceAOO"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Identificativo"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}TipoContestoProcedurale" minOccurs="0"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Oggetto" minOccurs="0"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Classifica" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}DataAvvio" minOccurs="0"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Note" minOccurs="0"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}PiuInfo" minOccurs="0"/>
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
@XmlType(name = "ContestoProcedurale", namespace = "http://www.digitPa.gov.it/protocollo/", propOrder = {
    "codiceAmministrazione",
    "codiceAOO",
    "identificativo",
    "tipoContestoProcedurale",
    "oggetto",
    "classifica",
    "dataAvvio",
    "note",
    "piuInfo"
})
public class ContestoProcedurale {

    @XmlElement(name = "CodiceAmministrazione", namespace = "http://www.digitPa.gov.it/protocollo/", required = true)
    protected CodiceAmministrazione codiceAmministrazione;
    @XmlElement(name = "CodiceAOO", namespace = "http://www.digitPa.gov.it/protocollo/", required = true)
    protected CodiceAOO codiceAOO;
    @XmlElement(name = "Identificativo", namespace = "http://www.digitPa.gov.it/protocollo/", required = true)
    protected Identificativo identificativo;
    @XmlElement(name = "TipoContestoProcedurale", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected TipoContestoProcedurale tipoContestoProcedurale;
    @XmlElement(name = "Oggetto", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected Oggetto oggetto;
    @XmlElement(name = "Classifica", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected List<Classifica> classifica;
    @XmlElement(name = "DataAvvio", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected DataAvvio dataAvvio;
    @XmlElement(name = "Note", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected Note note;
    @XmlElement(name = "PiuInfo", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected PiuInfo piuInfo;
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
     * Gets the value of the codiceAmministrazione property.
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
     * Sets the value of the codiceAmministrazione property.
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
     * Gets the value of the codiceAOO property.
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
     * Sets the value of the codiceAOO property.
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
     * Gets the value of the tipoContestoProcedurale property.
     * 
     * @return
     *     possible object is
     *     {@link TipoContestoProcedurale }
     *     
     */
    public TipoContestoProcedurale getTipoContestoProcedurale() {
        return tipoContestoProcedurale;
    }

    /**
     * Sets the value of the tipoContestoProcedurale property.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoContestoProcedurale }
     *     
     */
    public void setTipoContestoProcedurale(TipoContestoProcedurale value) {
        this.tipoContestoProcedurale = value;
    }

    /**
     * Gets the value of the oggetto property.
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
     * Sets the value of the oggetto property.
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
     * Gets the value of the dataAvvio property.
     * 
     * @return
     *     possible object is
     *     {@link DataAvvio }
     *     
     */
    public DataAvvio getDataAvvio() {
        return dataAvvio;
    }

    /**
     * Sets the value of the dataAvvio property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataAvvio }
     *     
     */
    public void setDataAvvio(DataAvvio value) {
        this.dataAvvio = value;
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

    /**
     * Gets the value of the piuInfo property.
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
     * Sets the value of the piuInfo property.
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
