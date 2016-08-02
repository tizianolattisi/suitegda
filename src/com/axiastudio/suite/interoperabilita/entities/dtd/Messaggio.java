
package com.axiastudio.suite.interoperabilita.entities.dtd;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "identificatoreOrDescrizioneMessaggio",
    "primaRegistrazione"
})
@XmlRootElement(name = "Messaggio")
public class Messaggio {

    @XmlElements({
        @XmlElement(name = "Identificatore", required = true, type = Identificatore.class),
        @XmlElement(name = "DescrizioneMessaggio", required = true, type = DescrizioneMessaggio.class)
    })
    protected List<Object> identificatoreOrDescrizioneMessaggio;
    @XmlElement(name = "PrimaRegistrazione")
    protected PrimaRegistrazione primaRegistrazione;

    /**
     * Gets the value of the identificatoreOrDescrizioneMessaggio property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the identificatoreOrDescrizioneMessaggio property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIdentificatoreOrDescrizioneMessaggio().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Identificatore }
     * {@link DescrizioneMessaggio }
     * 
     * 
     */
    public List<Object> getIdentificatoreOrDescrizioneMessaggio() {
        if (identificatoreOrDescrizioneMessaggio == null) {
            identificatoreOrDescrizioneMessaggio = new ArrayList<Object>();
        }
        return this.identificatoreOrDescrizioneMessaggio;
    }

    /**
     * Gets the value of the primaRegistrazione property.
     * 
     * @return
     *     possible object is
     *     {@link PrimaRegistrazione }
     *     
     */
    public PrimaRegistrazione getPrimaRegistrazione() {
        return primaRegistrazione;
    }

    /**
     * Sets the value of the primaRegistrazione property.
     * 
     * @param value
     *     allowed object is
     *     {@link PrimaRegistrazione }
     *     
     */
    public void setPrimaRegistrazione(PrimaRegistrazione value) {
        this.primaRegistrazione = value;
    }

}
