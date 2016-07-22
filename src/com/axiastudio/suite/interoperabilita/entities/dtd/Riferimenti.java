
package com.axiastudio.suite.interoperabilita.entities.dtd;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "messaggioOrContestoProceduraleOrProcedimento"
})
@XmlRootElement(name = "Riferimenti")
public class Riferimenti {

    @XmlElements({
        @XmlElement(name = "Messaggio", required = true, type = Messaggio.class),
        @XmlElement(name = "ContestoProcedurale", required = true, type = ContestoProcedurale.class),
        @XmlElement(name = "Procedimento", required = true, type = Procedimento.class)
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
