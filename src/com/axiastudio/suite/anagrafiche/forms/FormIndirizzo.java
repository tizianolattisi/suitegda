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
package com.axiastudio.suite.anagrafiche.forms;

import com.axiastudio.pypapi.ui.Dialog;
import com.axiastudio.suite.anagrafiche.entities.Provincia;
import com.trolltech.qt.gui.QComboBox;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class FormIndirizzo extends Dialog {

    public FormIndirizzo(String uiFile, Class entityClass, String title) {
        super(uiFile, entityClass, title);   
        this.inizializzaSigle();
    }
    
    private void inizializzaSigle(){
        QComboBox provincia = (QComboBox) this.findChild(QComboBox.class, "comboBoxProvincia");
        provincia.clear();
        List<Provincia> province = Arrays.asList(Provincia.class.getEnumConstants());
        for( Provincia p: province){
            provincia.addItem(p.name());
        }
    }
    
}
