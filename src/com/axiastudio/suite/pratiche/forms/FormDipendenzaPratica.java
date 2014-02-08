/*
 * Copyright (C) 2013 AXIA Studio (http://www.axiastudio.com)
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
 * You should have received a copy of the GNU Afffero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axiastudio.suite.pratiche.forms;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Controller;
import com.axiastudio.pypapi.db.IController;
import com.axiastudio.pypapi.db.IStoreFactory;
import com.axiastudio.pypapi.db.Store;
import com.axiastudio.pypapi.ui.Context;
import com.axiastudio.pypapi.ui.Dialog;
import com.axiastudio.pypapi.ui.widgets.PyPaPiComboBox;
import com.axiastudio.suite.pratiche.entities.Dipendenza;
import com.axiastudio.suite.pratiche.entities.DipendenzaPratica;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QLabel;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class FormDipendenzaPratica extends Dialog {
    
    public FormDipendenzaPratica(String uiFile, Class entityClass, String title){
        super(uiFile, entityClass, title);
        ((QCheckBox) this.findChild(QCheckBox.class, "checkBox_inverti")).stateChanged.connect(this, "aggiornaPredicato()");
        ((QComboBox) this.findChild(QComboBox.class, "comboBox_dipendenza")).currentIndexChanged.connect(this, "aggiornaPredicato()");
        this.storeInitialized.connect(this, "storeAbilitaInvertita()");

        try {
            Method storeFactory = this.getClass().getMethod("storeDipendenza");
            Register.registerUtility(storeFactory, IStoreFactory.class, "Dipendenza");
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(FormDipendenzaPratica.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(FormDipendenzaPratica.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void aggiornaPredicato(){
        String out="";
        QLabel predicato = (QLabel) this.findChild(QLabel.class, "label_predicato");
        Boolean inverti = ((QCheckBox) this.findChild(QCheckBox.class, "checkBox_inverti")).checkState().equals(Qt.CheckState.Checked);
        Context context = this.getContext();
        if( context == null ){
            return;
        }
        DipendenzaPratica dp = (DipendenzaPratica) this.getContext().getModel().getStore().get(0); // XXX: perché non currentEntity??
        if( dp == null ){
            return;
        }
        PyPaPiComboBox cmbDipendenza = (PyPaPiComboBox) this.findChild(PyPaPiComboBox.class, "comboBox_dipendenza");
        Dipendenza dip = (Dipendenza) cmbDipendenza.getCurrentEntity();
        predicato.setText(dp.getPredicato(dip, inverti));
    }

    public void storeAbilitaInvertita() {
        DipendenzaPratica dp = (DipendenzaPratica) this.getContext().getModel().getStore().get(0); // XXX: perché non currentEntity??
        if( dp == null || dp.getInvertita()==null ) {
            ((QCheckBox) this.findChild(QCheckBox.class, "checkBox_inverti")).setEnabled(false);
        }
    }

    /*
     * Uno store contenente gli oggetti ordinati x descrizione
     */
    public Store storeDipendenza(){
        Controller controller = (Controller) Register.queryUtility(IController.class, "com.axiastudio.suite.pratiche.entities.Dipendenza");
        Store storeDipendenza = controller.createFullStore();
        List<Dipendenza> dipendenze = new ArrayList<Dipendenza>();
        for(Object ogg: storeDipendenza){
            dipendenze.add((Dipendenza) ogg);
        }
        Collections.sort(dipendenze, Dipendenza.Comparators.DESCRIZIONE);
        return new Store(dipendenze);
    }

}
