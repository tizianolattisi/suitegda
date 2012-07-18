/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
        QComboBox tipologiaSoggetto = (QComboBox) this.findChild(QComboBox.class, "comboBoxTipologiaSoggetto");
        tipologiaSoggetto.currentIndexChanged.connect(this, "refresh(Integer)");
    }
    
    private void refresh(Integer idx){
        QTabWidget tab = (QTabWidget) this.findChild(QTabWidget.class, "tabWidgetHeader");
        tab.setCurrentIndex(idx);
    }
    
}
