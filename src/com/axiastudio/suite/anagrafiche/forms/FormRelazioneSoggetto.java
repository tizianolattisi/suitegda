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
package com.axiastudio.suite.anagrafiche.forms;

import com.axiastudio.pypapi.ui.Context;
import com.axiastudio.pypapi.ui.Dialog;
import com.axiastudio.suite.anagrafiche.entities.RelazioneSoggetto;
import com.trolltech.qt.core.Qt.CheckState;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QLabel;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class FormRelazioneSoggetto extends Dialog {
    
    public FormRelazioneSoggetto(String uiFile, Class entityClass, String title){
        super(uiFile, entityClass, title);
        ((QCheckBox) this.findChild(QCheckBox.class, "checkBox_inverti")).stateChanged.connect(this, "aggiornaPredicato()");
        ((QComboBox) this.findChild(QComboBox.class, "comboBox_relazione")).currentStringChanged.connect(this, "aggiornaPredicato()");
        //this.aggiornaPredicato();
    }
    
    private void aggiornaPredicato(){
        String out="";
        QLabel predicato = (QLabel) this.findChild(QLabel.class, "label_predicato");
        Boolean inverti = ((QCheckBox) this.findChild(QCheckBox.class, "checkBox_inverti")).checkState().equals(CheckState.Checked);
        Context context = this.getContext();
        if( context == null ){
            return;
        }
        RelazioneSoggetto rs = (RelazioneSoggetto) this.getContext().getModel().getStore().get(0); // XXX: perché non currentEntity??
        if( rs == null ){
            return;
        }
        rs.setInvertita(inverti);
        predicato.setText(rs.getPredicato());
    }

}
