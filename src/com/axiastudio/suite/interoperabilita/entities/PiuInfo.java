
package com.axiastudio.suite.interoperabilita.entities;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Classe Java per PiuInfo complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="PiuInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}MetadatiInterni"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}MetadatiEsterni"/>
 *       &lt;/choice>
 *       &lt;attribute name="XMLSchema" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PiuInfo", namespace = "http://www.digitPa.gov.it/protocollo/", propOrder = {
    "metadatiInterni",
    "metadatiEsterni"
})
public class PiuInfo {

    @XmlElement(name = "MetadatiInterni", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected MetadatiInterni metadatiInterni;
    @XmlElement(name = "MetadatiEsterni", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected MetadatiEsterni metadatiEsterni;
    @XmlAttribute(name = "XMLSchema", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String xmlSchema;

    /**
     * Recupera il valore della proprietà metadatiInterni.
     * 
     * @return
     *     possible object is
     *     {@link MetadatiInterni }
     *     
     */
    public MetadatiInterni getMetadatiInterni() {
        return metadatiInterni;
    }

    /**
     * Imposta il valore della proprietà metadatiInterni.
     * 
     * @param value
     *     allowed object is
     *     {@link MetadatiInterni }
     *     
     */
    public void setMetadatiInterni(MetadatiInterni value) {
        this.metadatiInterni = value;
    }

    /**
     * Recupera il valore della proprietà metadatiEsterni.
     * 
     * @return
     *     possible object is
     *     {@link MetadatiEsterni }
     *     
     */
    public MetadatiEsterni getMetadatiEsterni() {
        return metadatiEsterni;
    }

    /**
     * Imposta il valore della proprietà metadatiEsterni.
     * 
     * @param value
     *     allowed object is
     *     {@link MetadatiEsterni }
     *     
     */
    public void setMetadatiEsterni(MetadatiEsterni value) {
        this.metadatiEsterni = value;
    }

    /**
     * Recupera il valore della proprietà xmlSchema.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXMLSchema() {
        return xmlSchema;
    }

    /**
     * Imposta il valore della proprietà xmlSchema.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXMLSchema(String value) {
        this.xmlSchema = value;
    }

}
