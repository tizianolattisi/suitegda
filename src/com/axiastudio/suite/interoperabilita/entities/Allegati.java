
package com.axiastudio.suite.interoperabilita.entities;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Classe Java per Allegati complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="Allegati">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Documento"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Fascicolo"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Allegati", namespace = "http://www.digitPa.gov.it/protocollo/", propOrder = {
    "documentoOrFascicolo"
})
public class Allegati {

    @XmlElements({
        @XmlElement(name = "Documento", namespace = "http://www.digitPa.gov.it/protocollo/", type = Documento.class),
        @XmlElement(name = "Fascicolo", namespace = "http://www.digitPa.gov.it/protocollo/", type = Fascicolo.class)
    })
    protected List<Object> documentoOrFascicolo;

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

}
