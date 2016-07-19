
package com.axiastudio.suite.interoperabilita.entities;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Classe Java per Destinatario complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
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
        @XmlElementRef(name = "IndirizzoTelematico", namespace = "http://www.digitPa.gov.it/protocollo/", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "AOO", namespace = "http://www.digitPa.gov.it/protocollo/", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Denominazione", namespace = "http://www.digitPa.gov.it/protocollo/", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "IndirizzoPostale", namespace = "http://www.digitPa.gov.it/protocollo/", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Fax", namespace = "http://www.digitPa.gov.it/protocollo/", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Amministrazione", namespace = "http://www.digitPa.gov.it/protocollo/", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Persona", namespace = "http://www.digitPa.gov.it/protocollo/", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Telefono", namespace = "http://www.digitPa.gov.it/protocollo/", type = JAXBElement.class, required = false)
    })
    protected List<JAXBElement<?>> content;

    /**
     * Recupera il resto del modello di contenuto. 
     * 
     * <p>
     * Questa proprietà "catch-all" viene recuperata per il seguente motivo: 
     * Il nome di campo "Persona" è usato da due diverse parti di uno schema. Vedere: 
     * riga 175 di file:/Users/tiziano/Projects/GDA/desktop/Interoperabilita/src/main/java/it/tn/rivadelgarda/comune/gda/protocollo/interoperabilita/Segnatura.xsd
     * riga 173 di file:/Users/tiziano/Projects/GDA/desktop/Interoperabilita/src/main/java/it/tn/rivadelgarda/comune/gda/protocollo/interoperabilita/Segnatura.xsd
     * <p>
     * Per eliminare questa proprietà, applicare una personalizzazione della proprietà a una 
     * delle seguenti due dichiarazioni per modificarne il nome: 
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
     * {@link JAXBElement }{@code <}{@link IndirizzoTelematico }{@code >}
     * {@link JAXBElement }{@code <}{@link AOO }{@code >}
     * {@link JAXBElement }{@code <}{@link Denominazione }{@code >}
     * {@link JAXBElement }{@code <}{@link IndirizzoPostale }{@code >}
     * {@link JAXBElement }{@code <}{@link Fax }{@code >}
     * {@link JAXBElement }{@code <}{@link Amministrazione }{@code >}
     * {@link JAXBElement }{@code <}{@link Persona }{@code >}
     * {@link JAXBElement }{@code <}{@link Telefono }{@code >}
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
