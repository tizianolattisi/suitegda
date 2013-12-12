package com.axiastudio.suite.procedimenti.forms;

import com.axiastudio.menjazo.AlfrescoHelper;
import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Controller;
import com.axiastudio.pypapi.db.IController;
import com.axiastudio.pypapi.db.IStoreFactory;
import com.axiastudio.pypapi.db.Store;
import com.axiastudio.pypapi.ui.Dialog;
import com.axiastudio.suite.AdminConsole;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.finanziaria.entities.IFinanziaria;
import com.axiastudio.suite.plugins.cmis.CmisPlugin;
import com.axiastudio.suite.pratiche.IDettaglio;
import com.axiastudio.suite.pratiche.entities.Pratica;
import com.axiastudio.suite.procedimenti.IGestoreDeleghe;
import com.axiastudio.suite.procedimenti.SimpleWorkFlow;
import com.axiastudio.suite.procedimenti.entities.CodiceCarica;
import com.axiastudio.suite.procedimenti.entities.FaseProcedimento;
import com.axiastudio.suite.procedimenti.entities.Procedimento;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QPushButton;
import groovy.lang.Binding;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: tiziano
 * Date: 10/12/13
 * Time: 17:53
 */
public class FormFaseProcedimento extends Dialog {

    public FormFaseProcedimento(String uiFile, Class entityClass) {
        this(uiFile, entityClass, "");
    }

    public FormFaseProcedimento(String uiFile, Class entityClass, String title) {
        super(uiFile, entityClass, title);

        QPushButton test = (QPushButton) this.findChild(QPushButton.class, "pushButton_console");
        test.clicked.connect(this, "openConsole()");
    }

    private void openConsole(){
        String entityName = ((QLineEdit) this.findChild(QLineEdit.class, "lineEdit_entita")).text();
        String entityId = ((QLineEdit) this.findChild(QLineEdit.class, "lineEdit_id")).text();
        Long id = Long.parseLong(entityId);
        Controller controller = (Controller) Register.queryUtility(IController.class, entityName);
        Object obj = controller.get(id);
        FaseProcedimento faseProcedimento = (FaseProcedimento) this.getContext().getCurrentEntity();
        Procedimento procedimento = faseProcedimento.getProcedimento();

        // Apertura della console
        AdminConsole console = new AdminConsole(this, obj);
        console.show();

    }


}
