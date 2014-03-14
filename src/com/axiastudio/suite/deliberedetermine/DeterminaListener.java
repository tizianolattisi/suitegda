package com.axiastudio.suite.deliberedetermine;

import com.axiastudio.suite.deliberedetermine.entities.Determina;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

/**
 * User: tiziano
 * Date: 04/12/13
 * Time: 12:31
 */
public class DeterminaListener {

    @PrePersist
    void prePersist(Determina determina) {
        // determina con anno corrente
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        int year = calendar.get(Calendar.YEAR);
        determina.setAnno(year);
    }

}
