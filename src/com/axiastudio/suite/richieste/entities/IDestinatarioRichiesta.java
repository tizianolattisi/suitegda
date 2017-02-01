package com.axiastudio.suite.richieste.entities;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Mickey
 * Date: 27/11/13
 * Time: 9.59
 * To change this template use File | Settings | File Templates.
 */
public interface IDestinatarioRichiesta {
    Richiesta getRichiesta();

    Boolean getLetto();

    void setLetto(Boolean letto);

    Date getDataletto();

    void setDataletto(Date dataletto);

    String getEsecutoreletto();

    void setEsecutoreletto(String esecutoreletto);

    Date getData();

    Date getDatascadenza();

    StatoRichiesta getStatorichiesta();

    String getTesto();

    String getMittente();

    String getNomedestinatario();

    Boolean getConoscenza();

}
