
package com.axiastudio.suite.interoperabilita.entities;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "metadatiInterniOrMetadatiEsterni"
})
@XmlRootElement(name = "PiuInfo")
public class PiuInfo {

    @XmlAttribute(name = "XMLSchema", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String xmlSchema;
    @XmlElements({
        @XmlElement(name = "MetadatiInterni", required = true, type = MetadatiInterni.class),
        @XmlElement(name = "MetadatiEsterni", required = true, type = MetadatiEsterni.class)
    })
    protected List<Object> metadatiInterniOrMetadatiEsterni;

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

    /**
     * Gets the value of the metadatiInterniOrMetadatiEsterni property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the metadatiInterniOrMetadatiEsterni property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMetadatiInterniOrMetadatiEsterni().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MetadatiInterni }
     * {@link MetadatiEsterni }
     * 
     * 
     */
    public List<Object> getMetadatiInterniOrMetadatiEsterni() {
        if (metadatiInterniOrMetadatiEsterni == null) {
            metadatiInterniOrMetadatiEsterni = new ArrayList<Object>();
        }
        return this.metadatiInterniOrMetadatiEsterni;
    }

}
