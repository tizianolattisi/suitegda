package com.axiastudio.suite.modelli.forms;

import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.suite.AdminConsole;
import com.axiastudio.suite.SuiteUtil;
import com.axiastudio.suite.pratiche.IDettaglio;
import com.axiastudio.suite.pratiche.PraticaUtil;
import com.axiastudio.suite.pratiche.entities.Pratica;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QPushButton;

/**
 * User: tiziano
 * Date: 03/02/14
 * Time: 09:01
 */
public class FormModello extends Window {

    public FormModello(String uiFile, Class entityClass, String title) {
        super(uiFile, entityClass, title);

        ((QPushButton) this.findChild(QPushButton.class, "pushButton_test")).clicked.connect(this, "test()");
    }

    private void test(){
        QLineEdit lineEdit = (QLineEdit) this.findChild(QLineEdit.class, "lineEdit_codicepratica");
        QCheckBox checkBox = (QCheckBox) this.findChild(QCheckBox.class, "checkBox_dettaglio");
        String codicepratica = lineEdit.text();
        Boolean trovaDettaglio = checkBox.isChecked();
        Pratica pratica = SuiteUtil.trovaPratica(codicepratica);
        Object obj;
        if( trovaDettaglio ){
            IDettaglio dettaglio = PraticaUtil.trovaDettaglioDaPratica(pratica);
            obj = dettaglio;
        } else {
            obj = pratica;
        }
        AdminConsole console = new AdminConsole(this, obj);
        console.show();
    }

}
