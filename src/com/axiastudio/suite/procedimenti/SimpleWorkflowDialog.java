package com.axiastudio.suite.procedimenti;

import com.axiastudio.suite.pratiche.entities.FasePratica;
import com.trolltech.qt.gui.*;

/**
 * User: tiziano
 * Date: 18/12/13
 * Time: 14:21
 */
public class SimpleWorkflowDialog extends QDialog {

    private final SimpleWorkFlow simpleWorkFlow;
    private FasePratica fasePratica;
    private QComboBox choice;
    private QTextEdit commento;

    public SimpleWorkflowDialog(QWidget parent, SimpleWorkFlow simpleWorkFlow, FasePratica fasePratica) {
        super(parent);
        this.fasePratica = fasePratica;
        this.simpleWorkFlow = simpleWorkFlow;
        if( !simpleWorkFlow.permesso(fasePratica) ) {
            String msg = simpleWorkFlow.getResult().toString();
            initNo(msg);
            return;
        }
        if( !simpleWorkFlow.condizione(fasePratica) ){
            // inizializza la dialog per l'avvertimento
            String msg = simpleWorkFlow.getResult().toString();
            init(false, msg);
        } else {
            // inizializza la dialog per il passaggio di fase
            init(true, "");
        }
    }



    private void init(Boolean continua, String msg){
        if (!continua) {
            int res=QMessageBox.question(this, "Attenzione - non sarà possibile continuare l'iter",
                    msg + "\nSi desidera tornare al punto precedente dell'iter procedurale?",
                    QMessageBox.StandardButton.Yes, QMessageBox.StandardButton.No);
            if (res==QMessageBox.StandardButton.No.value()) {
                initNo("Operazione annullata");
                return;
            }
        }

        QVBoxLayout vBox = new QVBoxLayout();
        this.setLayout(vBox);
        TitoloDelega titoloDelega = simpleWorkFlow.checkTitoloDelega(fasePratica);
        if( titoloDelega != null && titoloDelega.getCarica() != null ){
            String descrizioneTitoloODelega = "Agisci come " + titoloDelega.getCarica().toString();
            if( titoloDelega.getDelega() ){
                descrizioneTitoloODelega += " delegato da " + titoloDelega.getDelegante().toString();
            }
            vBox.addWidget(new QLabel(descrizioneTitoloODelega));
        }
        vBox.addWidget(new QLabel(fasePratica.getFase().getDescrizione()));
        vBox.addWidget(new QLabel(fasePratica.getTesto()));
        QCheckBox checkBox = new QCheckBox("ho letto e compreso");
        if( fasePratica.getUsoresponsabile() ){
            vBox.addWidget(checkBox);
        }

        choice = new QComboBox();
        vBox.addWidget(choice);
        if( continua && fasePratica.getConfermata() != null ){
            choice.addItem(fasePratica.getTestoconfermata(), 0);
        }
        if( fasePratica.getRifiutata() != null ){
            choice.addItem(fasePratica.getTestorifiutata(), 1);
        }

        commento = new QTextEdit();
        vBox.addWidget(commento);

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


    private void initNo(String msg){
        QVBoxLayout vBox = new QVBoxLayout();
        this.setLayout(vBox);
        vBox.addWidget(new QLabel("Attenzione"));
        vBox.addWidget(new QLabel(msg));
    }

    
    @Override
    public void accept() {
        int idx = choice.currentIndex();
        if (idx != -1) {
            int value = (Integer) choice.itemData(idx);
            if (value == 0) {
                if (!simpleWorkFlow.creaVisto(fasePratica, Boolean.FALSE, commento.toPlainText(), Boolean.TRUE, Boolean.FALSE)) {
                    QMessageBox.critical(this, "Attenzione", simpleWorkFlow.getResult().toString(),
                            QMessageBox.StandardButton.Ok, QMessageBox.StandardButton.Ok);
                    return;
                }
                Boolean res = simpleWorkFlow.azione(fasePratica);
                if (res) {
                    if (!simpleWorkFlow.completaFase(fasePratica)) {
                        QMessageBox.critical(this, "Attenzione", simpleWorkFlow.getResult().toString(),
                                QMessageBox.StandardButton.Ok, QMessageBox.StandardButton.Ok);
                        return;
                    }
                } else {
                    String msg = "Non è stato possibile concludere la fase.";
                    if (simpleWorkFlow.getResult() != null) {
                        msg = simpleWorkFlow.getResult().toString();
                    }
                    QMessageBox.critical(this, "Attenzione", msg, QMessageBox.StandardButton.Ok, QMessageBox.StandardButton.Ok);
                    return;
                }
            } else if (value == 1) {
                if (!simpleWorkFlow.completaFase(fasePratica, Boolean.FALSE, commento.toPlainText())) {
                    QMessageBox.critical(this, "Attenzione", simpleWorkFlow.getResult().toString(),
                            QMessageBox.StandardButton.Ok, QMessageBox.StandardButton.Ok);
                    return;
                }
            }
            super.accept();
        }
    }
}
