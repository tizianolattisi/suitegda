
package com.axiastudio.suite.interoperabilita.entities;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Classe Java per Amministrazione complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="Amministrazione">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Denominazione"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}CodiceAmministrazione" minOccurs="0"/>
 *         &lt;choice>
 *           &lt;element ref="{http://www.digitPa.gov.it/protocollo/}UnitaOrganizzativa"/>
 *           &lt;sequence>
 *             &lt;choice maxOccurs="unbounded" minOccurs="0">
 *               &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Ruolo"/>
 *               &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Persona"/>
 *             &lt;/choice>
 *             &lt;element ref="{http://www.digitPa.gov.it/protocollo/}IndirizzoPostale"/>
 *             &lt;element ref="{http://www.digitPa.gov.it/protocollo/}IndirizzoTelematico" maxOccurs="unbounded" minOccurs="0"/>
 *             &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Telefono" maxOccurs="unbounded" minOccurs="0"/>
 *             &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Fax" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;/sequence>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Amministrazione", namespace = "http://www.digitPa.gov.it/protocollo/", propOrder = {
    "denominazione",
    "codiceAmministrazione",
    "unitaOrganizzativa",
    "ruoloOrPersona",
    "indirizzoPostale",
    "indirizzoTelematico",
    "telefono",
    "fax"
})
public class Amministrazione {

    @XmlElement(name = "Denominazione", namespace = "http://www.digitPa.gov.it/protocollo/", required = true)
    protected Denominazione denominazione;
    @XmlElement(name = "CodiceAmministrazione", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected CodiceAmministrazione codiceAmministrazione;
    @XmlElement(name = "UnitaOrganizzativa", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected UnitaOrganizzativa unitaOrganizzativa;
    @XmlElements({
        @XmlElement(name = "Ruolo", namespace = "http://www.digitPa.gov.it/protocollo/", type = Ruolo.class),
        @XmlElement(name = "Persona", namespace = "http://www.digitPa.gov.it/protocollo/", type = Persona.class)
    })
    protected List<Object> ruoloOrPersona;
    @XmlElement(name = "IndirizzoPostale", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected IndirizzoPostale indirizzoPostale;
    @XmlElement(name = "IndirizzoTelematico", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected List<IndirizzoTelematico> indirizzoTelematico;
    @XmlElement(name = "Telefono", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected List<Telefono> telefono;
    @XmlElement(name = "Fax", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected List<Fax> fax;

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
     * Recupera il valore della proprietà unitaOrganizzativa.
     * 
     * @return
     *     possible object is
     *     {@link UnitaOrganizzativa }
     *     
     */
    public UnitaOrganizzativa getUnitaOrganizzativa() {
        return unitaOrganizzativa;
    }

    /**
     * Imposta il valore della proprietà unitaOrganizzativa.
     * 
     * @param value
     *     allowed object is
     *     {@link UnitaOrganizzativa }
     *     
     */
    public void setUnitaOrganizzativa(UnitaOrganizzativa value) {
        this.unitaOrganizzativa = value;
    }

    /**
     * Gets the value of the ruoloOrPersona property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ruoloOrPersona property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRuoloOrPersona().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Ruolo }
     * {@link Persona }
     * 
     * 
     */
    public List<Object> getRuoloOrPersona() {
        if (ruoloOrPersona == null) {
            ruoloOrPersona = new ArrayList<Object>();
        }
        return this.ruoloOrPersona;
    }

    /**
     * Recupera il valore della proprietà indirizzoPostale.
     * 
     * @return
     *     possible object is
     *     {@link IndirizzoPostale }
     *     
     */
    public IndirizzoPostale getIndirizzoPostale() {
        return indirizzoPostale;
    }

    /**
     * Imposta il valore della proprietà indirizzoPostale.
     * 
     * @param value
     *     allowed object is
     *     {@link IndirizzoPostale }
     *     
     */
    public void setIndirizzoPostale(IndirizzoPostale value) {
        this.indirizzoPostale = value;
    }

    /**
     * Gets the value of the indirizzoTelematico property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the indirizzoTelematico property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIndirizzoTelematico().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IndirizzoTelematico }
     * 
     * 
     */
    public List<IndirizzoTelematico> getIndirizzoTelematico() {
        if (indirizzoTelematico == null) {
            indirizzoTelematico = new ArrayList<IndirizzoTelematico>();
        }
        return this.indirizzoTelematico;
    }

    /**
     * Gets the value of the telefono property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the telefono property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTelefono().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Telefono }
     * 
     * 
     */
    public List<Telefono> getTelefono() {
        if (telefono == null) {
            telefono = new ArrayList<Telefono>();
        }
        return this.telefono;
    }

    /**
     * Gets the value of the fax property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fax property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFax().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Fax }
     * 
     * 
     */
    public List<Fax> getFax() {
        if (fax == null) {
            fax = new ArrayList<Fax>();
        }
        return this.fax;
    }

}
