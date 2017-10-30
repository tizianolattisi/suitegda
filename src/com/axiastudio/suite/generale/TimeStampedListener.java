/*
 * Copyright (C) 2012 AXIA Studio (http://www.axiastudio.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axiastudio.suite.generale;

import com.axiastudio.pypapi.Register;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Utente;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class TimeStampedListener {

    /*
     *  Il timestamp di creazione è impostato da:
     *
     *  @Column(name="rec_creato", insertable=false, updatable=false,
     *          columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
     *
     *  rec_creato timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
     *
     */
    @PrePersist
    void prePersist(Object object) {
        ITimeStamped timeStamped = (ITimeStamped) object;
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        timeStamped.setRecordcreatoda(autenticato.getLogin());
    }

    @PreUpdate
    void preUpdate(Object object) {
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        if( object instanceof ITimeStamped ) {
            ITimeStamped timeStamped = (ITimeStamped) object;
            timeStamped.setRecordmodificatoda("--");
            timeStamped.setRecordmodificatoda(autenticato.getLogin());
        }
        if( object instanceof ITimeStampedV ) {
            ITimeStampedV timeStamped = (ITimeStampedV) object;
            timeStamped.setRecordmodificatoda("--");
            timeStamped.setRecordmodificatoda(autenticato.getLogin());
            System.out.print(timeStamped.getRecordmodificato());
        }
/*        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        if( timeStamped.getRecordcreato() != null && today.before(timeStamped.getRecordcreato()) ){
            throw new RuntimeException();
        } // data di modifica generata da trigger su tabella (come x rec_creato)  */
    }

}
