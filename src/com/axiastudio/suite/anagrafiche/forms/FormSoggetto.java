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

import com.axiastudio.pypapi.ui.Window;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QTabWidget;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class FormSoggetto extends Window {
    
    public FormSoggetto(String uiFile, Class entityClass, String title){
        super(uiFile, entityClass, title);
        QComboBox tipoSoggetto = (QComboBox) this.findChild(QComboBox.class, "comboBoxTipoSoggetto");
        tipoSoggetto.currentIndexChanged.connect(this, "refresh(Integer)");
        this.refresh(tipoSoggetto.currentIndex());
    }
    
    private void refresh(Integer idx){
        QTabWidget tab = (QTabWidget) this.findChild(QTabWidget.class, "tabWidgetHeader");
        tab.setCurrentIndex(idx);
        for( int i=0; i<3; i++ ){
                tab.setTabEnabled(i, i==idx);
        }
    }
    
}
