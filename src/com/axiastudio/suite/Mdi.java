/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.ui.Form;
import com.axiastudio.pypapi.ui.IForm;
import com.trolltech.qt.gui.*;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class Mdi extends QMainWindow {
    private QMdiArea workspace;
    private QTreeWidget tree;
    
    public Mdi(){
        this.createWorkspace();
        this.createTree();
        
    }
    
    private void createWorkspace() {
        QSplitter splitter = new QSplitter();
        this.tree = new QTreeWidget(splitter);
        this.workspace = new QMdiArea(splitter);
        this.setCentralWidget(splitter);        
    }
    
    private void createTree() {
        this.tree.setColumnCount(2);
        this.tree.setHeaderLabel("PyPaPi Suite");
        this.tree.setColumnHidden(1, true);
        
        /* Protocollo informatico */
        QTreeWidgetItem itemProtocolloInformatico = new QTreeWidgetItem(this.tree);
        itemProtocolloInformatico.setText(0, "Protocollo informatico");
        this.tree.addTopLevelItem(itemProtocolloInformatico);
        
        QTreeWidgetItem itemProtocollo = new QTreeWidgetItem(itemProtocolloInformatico);
        itemProtocollo.setText(0, "Protocollo");
        itemProtocollo.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/email.png"));
        itemProtocollo.setText(1, "com.axiastudio.suite.protocollo.entities.Protocollo");

        /* Anagrafiche */
        QTreeWidgetItem itemAnagrafiche = new QTreeWidgetItem(this.tree);
        itemAnagrafiche.setText(0, "Anagrafiche");
        this.tree.addTopLevelItem(itemAnagrafiche);

        QTreeWidgetItem itemSoggetti = new QTreeWidgetItem(itemAnagrafiche);
        itemSoggetti.setText(0, "Soggetti");
        itemSoggetti.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/vcard.png"));
        itemSoggetti.setText(1, "com.axiastudio.suite.anagrafiche.entities.Soggetto");

        /* Amministrazione */
        QTreeWidgetItem itemAmministrazione = new QTreeWidgetItem(this.tree);
        itemAmministrazione.setText(0, "Amministrazione");
        this.tree.addTopLevelItem(itemAmministrazione);

        QTreeWidgetItem itemUtenti = new QTreeWidgetItem(itemAmministrazione);
        itemUtenti.setText(0, "Utenti");
        itemUtenti.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/user.png"));

        QTreeWidgetItem itemUffici = new QTreeWidgetItem(itemAmministrazione);
        itemUffici.setText(0, "Uffici");
        itemUffici.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/group.png"));
        itemUffici.setText(1, "com.axiastudio.suite.base.entities.Ufficio");
        
        this.tree.activated.connect(this, "runTask()");
        this.tree.setMinimumWidth(200);

    }
    
    private void runTask() {
        String formName = this.tree.currentItem().text(1);
        Form form = (Form) Register.queryUtility(IForm.class, formName);
        this.workspace.addSubWindow(form);
        this.showForm(form);
    }
    
    private void showForm(Form form) {
        if( this.workspace.subWindowList().size()>1 ){
            form.show();
        } else {
            form.showMaximized();
        }
    }
}
