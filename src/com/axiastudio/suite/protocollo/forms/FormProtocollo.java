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
package com.axiastudio.suite.protocollo.forms;

import com.axiastudio.pypapi.ui.TableModel;
import com.axiastudio.pypapi.ui.Util;
import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.pypapi.ui.widgets.PyPaPiComboBox;
import com.axiastudio.pypapi.ui.widgets.PyPaPiTableView;
import com.axiastudio.suite.protocollo.entities.Attribuzione;
import com.axiastudio.suite.protocollo.entities.Fascicolo;
import com.axiastudio.suite.protocollo.entities.Protocollo;
import com.axiastudio.suite.protocollo.entities.TipoProtocollo;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.gui.*;
import java.util.List;


/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class FormProtocollo extends Window {
    /**
     *
     */
    public ProtocolloMenuBar protocolloMenuBar;
    private QTabWidget tabWidget;
    
    
    public FormProtocollo(FormProtocollo form){
        super(form.uiFile, form.entityClass, form.title);
        this.protocolloMenuBar = null;
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
        
        /* fascicolazione */
        QToolButton toolButtonTitolario = (QToolButton) this.findChild(QToolButton.class, "toolButtonTitolario");
        toolButtonTitolario.setIcon(new QIcon("classpath:com/axiastudio/suite/resources/email_go.png"));
        toolButtonTitolario.clicked.connect(this, "apriTitolario()");
        
        /* impostazione attribuzione principale */
        ((QPushButton) this.findChild(QPushButton.class, "pushButton_principale")).clicked.connect(this, "impostaAttribuzionePrincipale()");
               
    }
    
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
    
    private void impostaAttribuzionePrincipale(){
        PyPaPiTableView tv = (PyPaPiTableView) this.findChild(PyPaPiTableView.class, "tableView_attribuzioni");
        List<QModelIndex> rows = tv.selectionModel().selectedRows();
        if( rows.isEmpty()){
            return;
        }
        Protocollo protocollo = (Protocollo) this.getContext().getCurrentEntity();
        for( Attribuzione attribuzione: protocollo.getAttribuzioneCollection() ){
            attribuzione.setPrincipale(Boolean.FALSE);
        }
        for (QModelIndex idx: rows){
            Attribuzione attribuzione = (Attribuzione) ((TableModel) tv.model()).getEntityByRow(idx.row());
            attribuzione.setPrincipale(Boolean.TRUE);
        }
        this.getContext().getDirty();
    }
    
    @Override
    protected void indexChanged(int row) {
        super.indexChanged(row);
        Protocollo protocollo = (Protocollo) this.getContext().getCurrentEntity();
        Boolean convAttribuzioni = protocollo.getConvalidaAttribuzioni() == true;
        Boolean convProtocollo = protocollo.getConvalidaProtocollo() == true;
        PyPaPiTableView tv = (PyPaPiTableView) this.findChild(PyPaPiTableView.class, "tableView_attribuzioni");
        this.protocolloMenuBar.actionByName("convalidaAttribuzioni").setEnabled(!convAttribuzioni);
        this.protocolloMenuBar.actionByName("convalidaProtocollo").setEnabled(!convProtocollo);
        tv.setEnabled(!convAttribuzioni);
        this.centralWidget().setEnabled(!convProtocollo);
        
        Util.setWidgetReadOnly((QWidget) this.findChild(QDateEdit.class, "dateEdit_data"), true);
        Util.setWidgetReadOnly((QWidget) this.findChild(QLineEdit.class, "lineEdit_iddocumento"), true);
        Util.setWidgetReadOnly((QWidget) this.findChild(QCheckBox.class, "annullato"), true);
        Util.setWidgetReadOnly((QWidget) this.findChild(QCheckBox.class, "annullamentorichiesto"), true);
        
        if( protocollo.getId() != null ){
            Util.setWidgetReadOnly((QWidget) this.findChild(QComboBox.class, "comboBox_sportello"), true);
            Util.setWidgetReadOnly((QWidget) this.findChild(QComboBox.class, "comboBox_tipo"), true);
        }
        
        if( protocollo.getTipo().equals(TipoProtocollo.USCITA) ){
            
        }
    }
        
}
