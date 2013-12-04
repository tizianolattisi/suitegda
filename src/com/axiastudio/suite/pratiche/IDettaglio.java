package com.axiastudio.suite.pratiche;

import com.axiastudio.suite.pratiche.entities.Pratica;

/**
 * User: tiziano
 * Date: 04/12/13
 * Time: 09:02
 */
public interface IDettaglio {
    String getIdpratica();
    void setIdpratica(String idpratica);

    Pratica getPratica();
    void setPratica(Pratica pratica);
}
