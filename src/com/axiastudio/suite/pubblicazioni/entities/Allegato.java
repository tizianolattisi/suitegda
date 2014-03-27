package com.axiastudio.suite.pubblicazioni.entities;

import java.io.File;

/**
 * User: tiziano
 * Date: 05/03/14
 * Time: 12:01
 */
public class Allegato {

    File fileallegato;
    String titoloallegato;

    public Allegato(File fileallegato, String titoloallegato) {
        this.fileallegato = fileallegato;
        this.titoloallegato = titoloallegato;
    }

    public File getFileallegato() {
        return fileallegato;
    }

    public String getTitoloallegato() {
        return titoloallegato;
    }
}
