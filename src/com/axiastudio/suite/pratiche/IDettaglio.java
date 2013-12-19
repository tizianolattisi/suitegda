package com.axiastudio.suite.pratiche;

import com.axiastudio.suite.pratiche.entities.Pratica;

/**
 * User: tiziano
 * Date: 04/12/13
 * Time: 09:02
 */
public interface IDettaglio {

    Pratica getPratica();
    void setPratica(Pratica pratica);

    // campi proxy (setter in NOP)
    String getIdpratica();
    void setIdpratica(String idpratica);
    public String getCodiceinterno();
    public void setCodiceinterno(String codiceinterno);
}
