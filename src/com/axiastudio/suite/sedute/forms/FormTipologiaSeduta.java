/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
