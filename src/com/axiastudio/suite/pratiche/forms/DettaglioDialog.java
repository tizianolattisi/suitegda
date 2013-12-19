package com.axiastudio.suite.pratiche.forms;

import com.axiastudio.pypapi.ui.Context;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QVBoxLayout;

/**
 * User: tiziano
 * Date: 19/12/13
 * Time: 16:34
 */
public class DettaglioDialog extends QDialog {

    private Context context;

    public DettaglioDialog(Context context) {
        super();
        this.context = context;
        init();
    }

    private void init(){
        QVBoxLayout vBox = new QVBoxLayout();
        this.setLayout(vBox);
        vBox.addWidget(new QLabel("Attenzione"));
        String msg = "E' stato creato un dettaglio di tipo "
                .concat(this.context.getCurrentEntity().getClass().getSimpleName())
                .concat(".");
        vBox.addWidget(new QLabel(msg));
    }

    @Override
    public void accept() {
        this.context.commitChanges();
        super.accept();
    }

    @Override
    public void reject() {
        this.context.commitChanges();
        super.reject();
    }
}
