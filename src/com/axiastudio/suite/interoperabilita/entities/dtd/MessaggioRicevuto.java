
package com.axiastudio.suite.interoperabilita.entities.dtd;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "identificatoreOrPrimaRegistrazioneOrDescrizioneMessaggio"
})
@XmlRootElement(name = "MessaggioRicevuto")
public class MessaggioRicevuto {

    @XmlElements({
        @XmlElement(name = "Identificatore", required = true, type = Identificatore.class),
        @XmlElement(name = "PrimaRegistrazione", required = true, type = PrimaRegistrazione.class),
        @XmlElement(name = "DescrizioneMessaggio", required = true, type = DescrizioneMessaggio.class)
    })
    protected List<Object> identificatoreOrPrimaRegistrazioneOrDescrizioneMessaggio;

    /**
     * Gets the value of the identificatoreOrPrimaRegistrazioneOrDescrizioneMessaggio property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the identificatoreOrPrimaRegistrazioneOrDescrizioneMessaggio property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIdentificatoreOrPrimaRegistrazioneOrDescrizioneMessaggio().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Identificatore }
     * {@link PrimaRegistrazione }
     * {@link DescrizioneMessaggio }
     * 
     * 
     */
    public List<Object> getIdentificatoreOrPrimaRegistrazioneOrDescrizioneMessaggio() {
        if (identificatoreOrPrimaRegistrazioneOrDescrizioneMessaggio == null) {
            identificatoreOrPrimaRegistrazioneOrDescrizioneMessaggio = new ArrayList<Object>();
        }
        return this.identificatoreOrPrimaRegistrazioneOrDescrizioneMessaggio;
    }

}
