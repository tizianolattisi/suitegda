
package com.axiastudio.suite.interoperabilita.entities.xsd;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for PiuInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
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
     * Gets the value of the metadatiInterni property.
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
     * Sets the value of the metadatiInterni property.
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
     * Gets the value of the metadatiEsterni property.
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
     * Sets the value of the metadatiEsterni property.
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
     * Gets the value of the xmlSchema property.
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
     * Sets the value of the xmlSchema property.
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
