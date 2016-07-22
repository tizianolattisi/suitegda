
package com.axiastudio.suite.interoperabilita.entities.xsd;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for Destinatario complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Destinatario">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;sequence>
 *             &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Amministrazione"/>
 *             &lt;element ref="{http://www.digitPa.gov.it/protocollo/}AOO" minOccurs="0"/>
 *           &lt;/sequence>
 *           &lt;sequence>
 *             &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Denominazione"/>
 *             &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Persona" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;/sequence>
 *           &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Persona" maxOccurs="unbounded"/>
 *         &lt;/choice>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}IndirizzoTelematico" minOccurs="0"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Telefono" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Fax" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}IndirizzoPostale" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Destinatario", namespace = "http://www.digitPa.gov.it/protocollo/", propOrder = {
    "content"
})
public class Destinatario {

    @XmlElementRefs({
        @XmlElementRef(name = "Amministrazione", namespace = "http://www.digitPa.gov.it/protocollo/", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "IndirizzoPostale", namespace = "http://www.digitPa.gov.it/protocollo/", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Telefono", namespace = "http://www.digitPa.gov.it/protocollo/", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Persona", namespace = "http://www.digitPa.gov.it/protocollo/", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Denominazione", namespace = "http://www.digitPa.gov.it/protocollo/", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "AOO", namespace = "http://www.digitPa.gov.it/protocollo/", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Fax", namespace = "http://www.digitPa.gov.it/protocollo/", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "IndirizzoTelematico", namespace = "http://www.digitPa.gov.it/protocollo/", type = JAXBElement.class, required = false)
    })
    protected List<JAXBElement<?>> content;

    /**
     * Gets the rest of the content model. 
     * 
     * <p>
     * You are getting this "catch-all" property because of the following reason: 
     * The field name "Persona" is used by two different parts of a schema. See: 
     * line 181 of file:/home/mirco/Progetti/ivSource/gda/gda-interoperabilita/src/main/resources/Segnatura.xsd
     * line 179 of file:/home/mirco/Progetti/ivSource/gda/gda-interoperabilita/src/main/resources/Segnatura.xsd
     * <p>
     * To get rid of this property, apply a property customization to one 
     * of both of the following declarations to change their names: 
     * Gets the value of the content property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link Amministrazione }{@code >}
     * {@link JAXBElement }{@code <}{@link IndirizzoPostale }{@code >}
     * {@link JAXBElement }{@code <}{@link Telefono }{@code >}
     * {@link JAXBElement }{@code <}{@link Persona }{@code >}
     * {@link JAXBElement }{@code <}{@link Denominazione }{@code >}
     * {@link JAXBElement }{@code <}{@link AOO }{@code >}
     * {@link JAXBElement }{@code <}{@link IndirizzoTelematico }{@code >}
     * {@link JAXBElement }{@code <}{@link Fax }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getContent() {
        if (content == null) {
            content = new ArrayList<JAXBElement<?>>();
        }
        return this.content;
    }

}
