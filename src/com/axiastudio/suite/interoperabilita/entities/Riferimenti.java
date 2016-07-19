
package com.axiastudio.suite.interoperabilita.entities;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Classe Java per Riferimenti complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="Riferimenti">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Messaggio"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}ContestoProcedurale"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Procedimento"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Riferimenti", namespace = "http://www.digitPa.gov.it/protocollo/", propOrder = {
    "messaggioOrContestoProceduraleOrProcedimento"
})
public class Riferimenti {

    @XmlElements({
        @XmlElement(name = "Messaggio", namespace = "http://www.digitPa.gov.it/protocollo/", type = Messaggio.class),
        @XmlElement(name = "ContestoProcedurale", namespace = "http://www.digitPa.gov.it/protocollo/", type = ContestoProcedurale.class),
        @XmlElement(name = "Procedimento", namespace = "http://www.digitPa.gov.it/protocollo/", type = Procedimento.class)
    })
    protected List<Object> messaggioOrContestoProceduraleOrProcedimento;

    /**
     * Gets the value of the messaggioOrContestoProceduraleOrProcedimento property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the messaggioOrContestoProceduraleOrProcedimento property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMessaggioOrContestoProceduraleOrProcedimento().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Messaggio }
     * {@link ContestoProcedurale }
     * {@link Procedimento }
     * 
     * 
     */
    public List<Object> getMessaggioOrContestoProceduraleOrProcedimento() {
        if (messaggioOrContestoProceduraleOrProcedimento == null) {
            messaggioOrContestoProceduraleOrProcedimento = new ArrayList<Object>();
        }
        return this.messaggioOrContestoProceduraleOrProcedimento;
    }

}
