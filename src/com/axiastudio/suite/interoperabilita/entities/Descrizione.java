
package com.axiastudio.suite.interoperabilita.entities;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "documentoOrTestoDelMessaggio",
    "allegati",
    "note"
})
@XmlRootElement(name = "Descrizione")
public class Descrizione {

    @XmlElements({
        @XmlElement(name = "Documento", required = true, type = Documento.class),
        @XmlElement(name = "TestoDelMessaggio", required = true, type = TestoDelMessaggio.class)
    })
    protected List<Object> documentoOrTestoDelMessaggio;
    @XmlElement(name = "Allegati")
    protected Allegati allegati;
    @XmlElement(name = "Note")
    protected String note;

    /**
     * Gets the value of the documentoOrTestoDelMessaggio property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the documentoOrTestoDelMessaggio property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDocumentoOrTestoDelMessaggio().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Documento }
     * {@link TestoDelMessaggio }
     * 
     * 
     */
    public List<Object> getDocumentoOrTestoDelMessaggio() {
        if (documentoOrTestoDelMessaggio == null) {
            documentoOrTestoDelMessaggio = new ArrayList<Object>();
        }
        return this.documentoOrTestoDelMessaggio;
    }

    /**
     * Gets the value of the allegati property.
     * 
     * @return
     *     possible object is
     *     {@link Allegati }
     *     
     */
    public Allegati getAllegati() {
        return allegati;
    }

    /**
     * Sets the value of the allegati property.
     * 
     * @param value
     *     allowed object is
     *     {@link Allegati }
     *     
     */
    public void setAllegati(Allegati value) {
        this.allegati = value;
    }

    /**
     * Gets the value of the note property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNote() {
        return note;
    }

    /**
     * Sets the value of the note property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNote(String value) {
        this.note = value;
    }

}
