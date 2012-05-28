/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.protocollo.forms;

import com.axiastudio.pypapi.ui.Form;
import com.axiastudio.pypapi.ui.widgets.PyPaPiToolBar;
import com.axiastudio.suite.protocollo.entities.Protocollo;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QPixmap;

class ProtocolloMenuBar extends PyPaPiToolBar {
    public ProtocolloMenuBar(String title, Form parent){
        super(title, parent);
        this.insertButton("convalidaAttribuzioni", "Convalida attribuzioni",
                "classpath:com/axiastudio/suite/resources/lock_group.png",
                "Convalida degli uffici attribuiti al protocollo", parent);
        this.insertButton("convalidaProtocollo", "Convalida protocollo",
                "classpath:com/axiastudio/suite/resources/lock_mail.png",
                "Convalida della registrazione del protocollo", parent);
    }
}

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class FormProtocollo extends Form {
    
    public FormProtocollo(String uiFile, Class entityClass, String title){
        super(uiFile, entityClass, title);
        this.addToolBar(new ProtocolloMenuBar("Protocollo", this));
        QLabel labelSpedito = (QLabel) this.findChild(QLabel.class, "labelSpedito");
        labelSpedito.setPixmap(new QPixmap("classpath:com/axiastudio/suite/resources/email_go.png"));
        QLabel labelConvalidaProtocollo = (QLabel) this.findChild(QLabel.class, "labelConvalidaProtocollo");
        labelConvalidaProtocollo.setPixmap(new QPixmap("classpath:com/axiastudio/suite/resources/lock_mail.png"));
        QLabel labelConvalidaAttribuzioni = (QLabel) this.findChild(QLabel.class, "labelConvalidaAttribuzioni");
        labelConvalidaAttribuzioni.setPixmap(new QPixmap("classpath:com/axiastudio/suite/resources/lock_group.png"));
    }
    
    private void convalidaAttribuzioni() {
        System.out.println("ciao");
        Protocollo protocollo = (Protocollo) this.getContext().getCurrentEntity();
        protocollo.setConvalidaAttribuzioni(Boolean.TRUE);
        this.getContext().getDirty();
    }

    private void convalidaProtocollo() {
        System.out.println("ciao");
    }

}
