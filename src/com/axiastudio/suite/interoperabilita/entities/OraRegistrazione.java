
package com.axiastudio.suite.interoperabilita.entities;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Classe Java per OraRegistrazione complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="OraRegistrazione">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="tempo" default="locale">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *             &lt;enumeration value="spc"/>
 *             &lt;enumeration value="NMTOKEN"/>
 *             &lt;enumeration value="locale"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OraRegistrazione", namespace = "http://www.digitPa.gov.it/protocollo/", propOrder = {
    "content"
})
public class OraRegistrazione {

    @XmlValue
    protected String content;
    @XmlAttribute(name = "tempo")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String tempo;

    /**
     * Recupera il valore della proprietà content.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContent() {
        return content;
    }

    /**
     * Imposta il valore della proprietà content.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContent(String value) {
        this.content = value;
    }

    /**
     * Recupera il valore della proprietà tempo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTempo() {
        if (tempo == null) {
            return "locale";
        } else {
            return tempo;
        }
    }

    /**
     * Imposta il valore della proprietà tempo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTempo(String value) {
        this.tempo = value;
    }

}
