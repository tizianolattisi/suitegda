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
import com.axiastudio.pypapi.db.Controller;
import com.axiastudio.pypapi.db.IController;
import com.axiastudio.pypapi.db.IStoreFactory;
import com.axiastudio.pypapi.db.Store;
import com.axiastudio.pypapi.ui.Dialog;
import com.axiastudio.suite.protocollo.entities.Titolo;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class FormSoggettoProtocollo extends Dialog {
    
    public FormSoggettoProtocollo(String uiFile, Class entityClass, String title){
        super(uiFile, entityClass, title);
        try {
            Method storeFactory = this.getClass().getMethod("storeTitolo");
            Register.registerUtility(storeFactory, IStoreFactory.class, "Titolo");
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(FormSoggettoProtocollo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(FormSoggettoProtocollo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

/*
 * Uno store contenente gli oggetti ordinati x descrizione
 */
    public Store storeTitolo(){
        Controller controller = (Controller) Register.queryUtility(IController.class, "com.axiastudio.suite.protocollo.entities.Titolo");
        Store storeTitolo = controller.createFullStore();
        List<Titolo> titoli = new ArrayList<Titolo>();
        for(Object ogg: storeTitolo){
            titoli.add((Titolo) ogg);
        }
        Collections.sort(titoli, Titolo.Comparators.DESCRIZIONE);
        return new Store(titoli);
    }


}
