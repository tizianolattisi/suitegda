package com.axiastudio.suite.portale.forms;

import com.axiastudio.pypapi.ui.Dialog;
import com.trolltech.qt.gui.QToolButton;

/**
 * User: Comune di Riva del Garda
 * Date: 24/07/2018
 */

public class FormComunicato extends Dialog {

    public FormComunicato(String uiFile, Class entityClass, String title) {
        super(uiFile, entityClass, title);
        ((QToolButton) this.findChild(QToolButton.class, "accept_button")).clicked.connect(this, "accept()");
    }

    @Override
    public void accept() {
        ((QToolButton) this.findChild(QToolButton.class, "accept_button")).setFocus();
        this.getContext().commitChanges();
        super.accept();
    }
}
