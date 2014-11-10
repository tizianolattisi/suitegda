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
import com.axiastudio.suite.pratiche.entities.TipoPratica;
import com.axiastudio.suite.pratiche.forms.FormTipoPraticaTree;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QToolButton;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class FormTipoSeduta extends Window {
    
    public FormTipoSeduta(String uiFile, Class entityClass, String title){
        super(uiFile, entityClass, title);
        
        /* tipo */
        QToolButton toolButtonTipo = (QToolButton) this.findChild(QToolButton.class, "toolButtonTipo");
        toolButtonTipo.setIcon(new QIcon("classpath:com/axiastudio/suite/resources/email_go.png"));
        toolButtonTipo.clicked.connect(this, "apriTipo()");
    }
        
    
    /*
     * XXX: copia e incolla da FormPratica
     */
    private void apriTipo(){
        FormTipoPraticaTree tipo = new FormTipoPraticaTree();
        int exec = tipo.exec();
        if( exec == 1 ){
            TipoPratica selection = tipo.getSelection();
            PyPaPiComboBox comboBoxTipo = (PyPaPiComboBox) this.findChild(PyPaPiComboBox.class, "comboBoxTipo");
            comboBoxTipo.select(selection);
            this.getContext().getDirty();

        }
    }

}
