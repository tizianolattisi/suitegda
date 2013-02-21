/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.procedimenti;

import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.finanziaria.entities.Servizio;
import com.axiastudio.suite.procedimenti.entities.CodiceCarica;
import com.axiastudio.suite.procedimenti.entities.Procedimento;
import java.util.Date;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public interface IGestoreDeleghe {
    
    public Boolean checkDelega(CodiceCarica codiceCarica);

    public Boolean checkDelega(CodiceCarica codiceCarica, Servizio servizio);
    
    public Boolean checkDelega(CodiceCarica codiceCarica, Servizio servizio, Procedimento procedimento);

    public Boolean checkDelega(CodiceCarica codiceCarica, Servizio servizio, Procedimento procedimento, Ufficio ufficio);

    public Boolean checkDelega(CodiceCarica codiceCarica, Servizio servizio, Procedimento procedimento, Ufficio ufficio, Utente utente);

    public Boolean checkDelega(CodiceCarica codiceCarica, Servizio servizio, Procedimento procedimento, Ufficio ufficio, Utente utente, Date dataVerifica);

}
