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
package com.axiastudio.suite.sedute.forms;

import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.pypapi.ui.widgets.PyPaPiComboBox;
import com.axiastudio.suite.pratiche.entities.TipologiaPratica;
import com.axiastudio.suite.pratiche.forms.FormTipologiePratica;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QToolButton;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class FormTipologiaSeduta extends Window {
    
    public FormTipologiaSeduta(String uiFile, Class entityClass, String title){
        super(uiFile, entityClass, title);
        
        /* tipologia */
        QToolButton toolButtonTipologia = (QToolButton) this.findChild(QToolButton.class, "toolButtonTipologia");
        toolButtonTipologia.setIcon(new QIcon("classpath:com/axiastudio/suite/resources/email_go.png"));
        toolButtonTipologia.clicked.connect(this, "apriTipologia()");
    }
        
    
    /*
     * XXX: copia e incolla da FormPratica
     */
    private void apriTipologia(){
        FormTipologiePratica tipologie = new FormTipologiePratica();
        int exec = tipologie.exec();
        if( exec == 1 ){
            TipologiaPratica selection = tipologie.getSelection();
            PyPaPiComboBox comboBoxTipologia = (PyPaPiComboBox) this.findChild(PyPaPiComboBox.class, "comboBoxTipologia");
            comboBoxTipologia.select(selection);
            this.getContext().getDirty();

        }
    }

}
