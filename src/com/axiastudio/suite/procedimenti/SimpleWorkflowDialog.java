package com.axiastudio.suite.procedimenti;

import com.axiastudio.suite.pratiche.entities.FasePratica;
import com.trolltech.qt.gui.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User: tiziano
 * Date: 18/12/13
 * Time: 14:21
 */
public class SimpleWorkflowDialog extends QDialog {

    private final SimpleWorkFlow simpleWorkFlow;
    private FasePratica fasePratica;
    private QComboBox choice;

    public SimpleWorkflowDialog(QWidget parent, SimpleWorkFlow simpleWorkFlow, FasePratica fasePratica) {
        super(parent);
        this.fasePratica = fasePratica;
        this.simpleWorkFlow = simpleWorkFlow;
        init();
    }

    private void init(){
        QVBoxLayout vBox = new QVBoxLayout();
        this.setLayout(vBox);
        vBox.addWidget(new QLabel(fasePratica.getFase().getDescrizione()));
        vBox.addWidget(new QLabel(fasePratica.getTesto()));
        QCheckBox checkBox = new QCheckBox("ho letto e compreso");
        if( fasePratica.getUsoresponsabile() ){
            vBox.addWidget(checkBox);
        }

        choice = new QComboBox();
        vBox.addWidget(choice);
        if( fasePratica.getConfermata() != null ){
            choice.addItem(fasePratica.getTestoconfermata());
        }
        if( fasePratica.getRifiutata() != null ){
            choice.addItem(fasePratica.getTestorifiutata());
        }

        QHBoxLayout hBox = new QHBoxLayout();
        vBox.addLayout(hBox);
        hBox.addStretch();
        QToolButton buttonCancel = new QToolButton();
        buttonCancel.setIcon(new QIcon("classpath:com/axiastudio/pypapi/ui/resources/toolbar/cancel.png"));
        buttonCancel.clicked.connect(this, "reject()");
        hBox.addWidget(buttonCancel);
        QToolButton buttonAccept = new QToolButton();
        buttonAccept.setIcon(new QIcon("classpath:com/axiastudio/pypapi/ui/resources/toolbar/accept.png"));
        buttonAccept.clicked.connect(this, "accept()");
        hBox.addWidget(buttonAccept);

        if( fasePratica.getUsoresponsabile() ){
            buttonAccept.setEnabled(false);
            checkBox.toggled.connect(buttonAccept, "setEnabled(boolean)");
        }


        }

    @Override
    public void accept() {
        int idx = choice.currentIndex();
        if( idx == 0 ){
            Boolean res = simpleWorkFlow.azione(fasePratica);
            if( res ){
                simpleWorkFlow.completaFase(fasePratica);
            } else {
                String msg = simpleWorkFlow.getResult().toString();
                QMessageBox.critical(this, "Attenzione", msg, QMessageBox.StandardButton.Ok, QMessageBox.StandardButton.Ok);
                return;
            }
        } else if( idx == 1 ){
            simpleWorkFlow.completaFase(fasePratica, Boolean.FALSE);
        }
        super.accept();
    }
}
