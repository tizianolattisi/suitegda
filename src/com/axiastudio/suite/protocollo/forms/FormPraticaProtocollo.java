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
package com.axiastudio.suite.protocollo.forms;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.*;
import com.axiastudio.pypapi.ui.*;
import com.axiastudio.suite.protocollo.entities.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class FormPraticaProtocollo extends Dialog {
    /**
     *
     */

        public FormPraticaProtocollo(String uiFile, Class entityClass, String title){
        super(uiFile, entityClass, title);
        try {
            Method storeFactory = this.getClass().getMethod("storeOggetto");
            Register.registerUtility(storeFactory, IStoreFactory.class, "Oggetto");
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(FormPraticaProtocollo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(FormPraticaProtocollo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    /*
     * Uno store contenente gli oggetti ordinati x descrizione
     */
    public Store storeOggetto(){
        Database db = (Database) Register.queryUtility(IDatabase.class);
        Controller controller = db.createController(Oggetto.class);
        Store storeOggetto = controller.createFullStore();
        List<Oggetto> oggetti = new ArrayList();
        for(Object ogg: storeOggetto){
            oggetti.add((Oggetto) ogg);
        }
        Collections.sort(oggetti, Oggetto.Comparators.DESCRIZIONE);
        return new Store(oggetti);
    }

}
