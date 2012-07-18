/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
