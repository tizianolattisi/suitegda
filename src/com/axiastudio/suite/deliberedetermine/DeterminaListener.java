package com.axiastudio.suite.deliberedetermine;

import com.axiastudio.suite.deliberedetermine.entities.Determina;

import javax.persistence.PostPersist;
import javax.persistence.PrePersist;

/**
 * User: tiziano
 * Date: 04/12/13
 * Time: 12:31
 */
public class DeterminaListener {

    @PrePersist
    void prePersist(Determina determina) {

    }

    @PostPersist
    void postPersist(Determina determina) {

    }
}
