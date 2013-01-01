/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Controller;
import com.axiastudio.pypapi.db.IController;
import com.axiastudio.pypapi.db.IFactory;
import com.axiastudio.pypapi.db.Store;
import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.pypapi.ui.IForm;
import com.axiastudio.pypapi.ui.IUIFile;
import com.axiastudio.suite.base.entities.CambiaPassword;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.pratiche.forms.FormTipologiePratica;
import com.axiastudio.suite.protocollo.forms.FormScrivania;
import com.axiastudio.suite.protocollo.forms.FormTitolario;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class Mdi extends QMainWindow {
    
    private static String ICON = "classpath:com/axiastudio/pypapi/ui/resources/pypapi32.png";
    private QMdiArea workspace;
    private QTreeWidget tree;
    private QSystemTrayIcon trayIcon;
    
    public Mdi(){
        this.setWindowIcon(new QIcon(ICON));
        this.createWorkspace();
        this.createTree();
        this.createSystemTray();
    }
    
    private void createSystemTray(){
        QMenu menu = new QMenu(this);
        menu.addAction("prova");
        this.trayIcon = new QSystemTrayIcon(new QIcon(ICON), this);
        this.trayIcon.setContextMenu(menu);
        this.trayIcon.show();
        this.trayIcon.showMessage("PyPaPi Suite", "Applicazione avviata.");
        
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
        
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);

        /* scrivania */
        QTreeWidgetItem itemScrivania = new QTreeWidgetItem(this.tree);
        itemScrivania.setText(0, "Scrivania");
        itemScrivania.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/house.png"));
        itemScrivania.setText(1, "SCRIVANIA");

        /* Protocollo informatico */
        QTreeWidgetItem itemProtocolloInformatico = new QTreeWidgetItem(this.tree);
        itemProtocolloInformatico.setText(0, "Protocollo informatico");
        this.tree.addTopLevelItem(itemProtocolloInformatico);
        itemProtocolloInformatico.setDisabled(!autenticato.getOperatoreprotocollo());
        
        QTreeWidgetItem itemProtocollo = new QTreeWidgetItem(itemProtocolloInformatico);
        itemProtocollo.setText(0, "Protocollo");
        itemProtocollo.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/email.png"));
        itemProtocollo.setText(1, "com.axiastudio.suite.protocollo.entities.Protocollo");
        Controller controllerProtocollo = (Controller) Register.queryUtility(IController.class, "com.axiastudio.suite.protocollo.entities.Protocollo");
        Store storeProtocollo = controllerProtocollo.createStore(10);
        itemProtocollo.setData(1, Qt.ItemDataRole.UserRole, storeProtocollo);

        QTreeWidgetItem itemTitolario = new QTreeWidgetItem(itemProtocolloInformatico);
        itemTitolario.setText(0, "Titolario");
        itemTitolario.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/email.png"));
        itemTitolario.setText(1, "TITOLARIO");
        
        /* Anagrafiche */
        QTreeWidgetItem itemAnagrafiche = new QTreeWidgetItem(this.tree);
        itemAnagrafiche.setText(0, "Anagrafiche");
        this.tree.addTopLevelItem(itemAnagrafiche);
        itemAnagrafiche.setDisabled(!autenticato.getOperatoreanagrafiche());

        QTreeWidgetItem itemSoggetti = new QTreeWidgetItem(itemAnagrafiche);
        itemSoggetti.setText(0, "Soggetti");
        itemSoggetti.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/vcard.png"));
        itemSoggetti.setText(1, "com.axiastudio.suite.anagrafiche.entities.Soggetto");
        //Controller controllerSoggetto = (Controller) Register.queryUtility(IController.class, "com.axiastudio.suite.anagrafiche.entities.Soggetto");
        //Store storeSoggetto = controllerSoggetto.createStore(10);
        //itemSoggetti.setData(1, Qt.ItemDataRole.UserRole, storeSoggetto);
        
        /* Pratiche */
        QTreeWidgetItem itemPraticheRoot = new QTreeWidgetItem(this.tree);
        itemPraticheRoot.setText(0, "Pratiche...");
        this.tree.addTopLevelItem(itemPraticheRoot);
        itemPraticheRoot.setDisabled(!autenticato.getOperatorepratiche());

        QTreeWidgetItem itemPratiche = new QTreeWidgetItem(itemPraticheRoot);
        itemPratiche.setText(0, "Pratiche");
        itemPratiche.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/vcard.png"));
        itemPratiche.setText(1, "com.axiastudio.suite.pratiche.entities.Pratica");

        QTreeWidgetItem itemTipologiaPratica = new QTreeWidgetItem(itemPraticheRoot);
        itemTipologiaPratica.setText(0, "Tipologia pratica");
        itemTipologiaPratica.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/vcard.png"));
        itemTipologiaPratica.setText(1, "com.axiastudio.suite.pratiche.entities.TipologiaPratica");
        
        QTreeWidgetItem itemTipologiePratica = new QTreeWidgetItem(itemPraticheRoot);
        itemTipologiePratica.setText(0, "Tipologie Pratica");
        itemTipologiePratica.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/email.png"));
        itemTipologiePratica.setText(1, "TIPOLOGIEPRATICA");

        /* Amministrazione */
        QTreeWidgetItem itemAmministrazione = new QTreeWidgetItem(this.tree);
        itemAmministrazione.setText(0, "Amministrazione");
        this.tree.addTopLevelItem(itemAmministrazione);

        QTreeWidgetItem itemUtenti = new QTreeWidgetItem(itemAmministrazione);
        itemUtenti.setText(0, "Utenti");
        itemUtenti.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/user.png"));
        itemUtenti.setText(1, "com.axiastudio.suite.base.entities.Utente");
        itemUtenti.setDisabled(!autenticato.getAmministratore());

        QTreeWidgetItem itemUffici = new QTreeWidgetItem(itemAmministrazione);
        itemUffici.setText(0, "Uffici");
        itemUffici.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/group.png"));
        itemUffici.setText(1, "com.axiastudio.suite.base.entities.Ufficio");
        itemUffici.setDisabled(!autenticato.getAmministratore());

        QTreeWidgetItem itemPassword = new QTreeWidgetItem(itemAmministrazione);
        itemPassword.setText(0, "Cambio password");
        itemPassword.setIcon(0, new QIcon("classpath:com/axiastudio/pypapi/ui/resources/key.png"));
        itemPassword.setText(1, "PASSWORD");
        
        this.tree.activated.connect(this, "runTask()");
        this.tree.setMinimumWidth(200);

    }
    
    private void runTask() {
        String formName = this.tree.currentItem().text(1);
        /* cambio password */
        if( "PASSWORD".equals(formName) ){
            CambiaPassword passDlg = new CambiaPassword(this);
            int exec = passDlg.exec();
        } else if( "TITOLARIO".equals(formName) ){
            FormTitolario titolario = new FormTitolario();
            this.workspace.addSubWindow(titolario);
            int exec = titolario.exec();
        } else if( "TIPOLOGIEPRATICA".equals(formName) ){
            FormTipologiePratica tipologiepratica = new FormTipologiePratica();
            this.workspace.addSubWindow(tipologiepratica);
            int exec = tipologiepratica.exec();
        } else if( "SCRIVANIA".equals(formName) ){
            FormScrivania form = new FormScrivania();
            this.workspace.addSubWindow(form);
            form.show();
        } else {
            /* form registrata */
            Window form=null;
            Class<? extends Window> formClass = (Class) Register.queryUtility(IForm.class, formName);
            String uiFile = (String) Register.queryUtility(IUIFile.class, formName);
            Class factory = (Class) Register.queryUtility(IFactory.class, formName);
            try {
                Constructor<? extends Window> constructor = formClass.getConstructor(new Class[]{String.class, Class.class, String.class});
                try {
                    form = constructor.newInstance(new Object[]{uiFile, factory, ""});
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(Mdi.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (NoSuchMethodException | SecurityException ex) {
                Logger.getLogger(Mdi.class.getName()).log(Level.SEVERE, null, ex);
            }
            // XXX
            //Controller controller = (Controller) Register.queryUtility(IController.class, factory.getName());
            //Store store = controller.createStore(3);
            Store store = null;
            if( store != null ){
                form.init(store);
            } else {
                form.init();
            }
            this.workspace.addSubWindow(form);
            this.showForm(form);
        }
    }
    
    private void showForm(Window form) {
        if( this.workspace.subWindowList().size()>1 ){
            form.show();
        } else {
            form.showMaximized();
        }
    }
}
