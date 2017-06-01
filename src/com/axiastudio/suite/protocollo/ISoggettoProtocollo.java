package com.axiastudio.suite.protocollo;

import com.axiastudio.suite.anagrafiche.entities.Soggetto;
import com.axiastudio.suite.protocollo.entities.Protocollo;

import java.util.Date;

/**
 * User: Michela Piva -  Comune di Riva del Garda
 * Date: 08/01/14
 * Time: 11:15
 */
public interface ISoggettoProtocollo {

    Soggetto getSoggetto();
    Protocollo getProtocollo();
    Boolean getAnnullato();
    Boolean getPec();
    Date getDatainizio();
    void setDatainizio(Date datainizio);
    String getIndirizzo();
    void setIndirizzo(String indirizzo);
    Soggetto getSoggettoReferente();
    void setSoggettoReferente(Soggetto soggettoReferente);
    Long getMessaggiopec();
    void setMessaggiopec(Long messaggiopec);
    String getSoggettoformattato();
    String getPredicato();
}
