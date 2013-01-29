/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.protocollo.forms;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.IStoreFactory;
import com.axiastudio.pypapi.db.Store;
import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.pypapi.ui.widgets.PyPaPiComboBox;
import com.axiastudio.pypapi.ui.widgets.PyPaPiTableView;
import com.axiastudio.pypapi.ui.widgets.PyPaPiToolBar;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.base.entities.UfficioUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.protocollo.entities.Fascicolo;
import com.axiastudio.suite.protocollo.entities.Protocollo;
import com.trolltech.qt.gui.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class ProtocolloMenuBar extends PyPaPiToolBar {
    public ProtocolloMenuBar(String title, Window parent){
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
public class FormProtocollo extends Window {
    private  ProtocolloMenuBar protocolloMenuBar=null;
    private QTabWidget tabWidget;
    
    
    public FormProtocollo(FormProtocollo form){
        super(form.uiFile, form.entityClass, form.title);
    }
    
    public FormProtocollo(String uiFile, Class entityClass, String title){
        super(uiFile, entityClass, title);
        this.protocolloMenuBar = new ProtocolloMenuBar("Protocollo", this);
        this.addToolBar(protocolloMenuBar);
        QLabel labelSpedito = (QLabel) this.findChild(QLabel.class, "labelSpedito");
        labelSpedito.setPixmap(new QPixmap("classpath:com/axiastudio/suite/resources/email_go.png"));
        QLabel labelConvalidaProtocollo = (QLabel) this.findChild(QLabel.class, "labelConvalidaProtocollo");
        labelConvalidaProtocollo.setPixmap(new QPixmap("classpath:com/axiastudio/suite/resources/lock_mail.png"));
        QLabel labelConvalidaAttribuzioni = (QLabel) this.findChild(QLabel.class, "labelConvalidaAttribuzioni");
        labelConvalidaAttribuzioni.setPixmap(new QPixmap("classpath:com/axiastudio/suite/resources/lock_group.png"));
        
        /* filtro per la selezione dello sportello */
        /* commentato perché il controllo ora è nella callback
        try {
            Method storeFactory = this.getClass().getMethod("storeSportello");
            Register.registerUtility(storeFactory, IStoreFactory.class, "Sportello");
        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(FormProtocollo.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
        /* fascicolazione */
        QToolButton toolButtonTitolario = (QToolButton) this.findChild(QToolButton.class, "toolButtonTitolario");
        toolButtonTitolario.setIcon(new QIcon("classpath:com/axiastudio/suite/resources/email_go.png"));
        toolButtonTitolario.clicked.connect(this, "apriTitolario()");
        
    }
    
    /*
     * Uno store contenente solo gli uffici dell'utente
     */
    /*
    public Store storeSportello(){
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        List<Ufficio> uffici = new ArrayList();
        for(UfficioUtente uu: autenticato.getUfficioUtenteCollection()){
            uffici.add(uu.getUfficio());
        }
        return new Store(uffici);
    }*/
    
    private void convalidaAttribuzioni() {
        Protocollo protocollo = (Protocollo) this.getContext().getCurrentEntity();
        protocollo.setConvalidaAttribuzioni(Boolean.TRUE);
        this.getContext().getDirty();
    }

    private void convalidaProtocollo() {
        Protocollo protocollo = (Protocollo) this.getContext().getCurrentEntity();
        protocollo.setConvalidaAttribuzioni(Boolean.TRUE);
        protocollo.setConvalidaProtocollo(Boolean.TRUE);
        this.getContext().getDirty();
    }
    
    private void apriTitolario() {
        FormTitolario titolario = new FormTitolario();
        int exec = titolario.exec();
        if( exec == 1 ){
            Fascicolo selection = titolario.getSelection();
            PyPaPiComboBox comboBoxTitolario = (PyPaPiComboBox) this.findChild(PyPaPiComboBox.class, "comboBoxTitolario");
            comboBoxTitolario.select(selection);
            this.getContext().getDirty();
        }
    }
    
    @Override
    protected void indexChanged(int row) {
        Protocollo protocollo = (Protocollo) this.getContext().getCurrentEntity();
        Boolean convAttribuzioni = protocollo.getConvalidaAttribuzioni() == true;
        Boolean convProtocollo = protocollo.getConvalidaProtocollo() == true;
        PyPaPiTableView tv = (PyPaPiTableView) this.findChild(PyPaPiTableView.class, "tableViewAttribuzioni");
        this.protocolloMenuBar.actionByName("convalidaAttribuzioni").setEnabled(!convAttribuzioni);
        this.protocolloMenuBar.actionByName("convalidaProtocollo").setEnabled(!convProtocollo);
        tv.setEnabled(!convAttribuzioni);
        this.centralWidget().setEnabled(!convProtocollo);
        super.indexChanged(row);
    }

}
