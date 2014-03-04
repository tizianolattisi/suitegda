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
package com.axiastudio.suite.base.entities;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.*;
import com.axiastudio.suite.SuiteUtil;
import com.trolltech.qt.gui.*;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class CambiaPassword extends QDialog {
    private final QLineEdit oldPassword;
    private final QLineEdit newPassword1;
    private final QLineEdit newPassword2;
    private Utente utente;

    public CambiaPassword(QWidget parent) {
        super(parent);
        QGridLayout layout = new QGridLayout();
        QLabel labelOldPassword = new QLabel("Vecchia password");
        layout.addWidget(labelOldPassword, 0, 0);
        this.oldPassword = new QLineEdit();
        this.oldPassword.setEchoMode(QLineEdit.EchoMode.Password);
        layout.addWidget(oldPassword, 0, 1);
        QLabel labelNewPassword1 = new QLabel("Nuova password");
        layout.addWidget(labelNewPassword1, 1, 0);
        this.newPassword1 = new QLineEdit();
        this.newPassword1.setEchoMode(QLineEdit.EchoMode.Password);
        layout.addWidget(newPassword1, 1, 1);
        QLabel labelNewPassword2 = new QLabel("Ridigita nuova password");
        layout.addWidget(labelNewPassword2, 2, 0);
        this.newPassword2 = new QLineEdit();
        this.newPassword2.setEchoMode(QLineEdit.EchoMode.Password);
        layout.addWidget(newPassword2, 2, 1);
        QPushButton pushButtonCancel = new QPushButton("Annulla");
        layout.addWidget(pushButtonCancel, 3, 0);
        QPushButton pushButtonOk = new QPushButton("OK");
        layout.addWidget(pushButtonOk, 3, 1);
        this.setLayout(layout);
        pushButtonOk.clicked.connect(this, "accept()");
        pushButtonCancel.clicked.connect(this, "reject()");
        pushButtonOk.setDefault(true);
    }

    @Override
    public void accept() {
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        if( !SuiteUtil.digest(this.oldPassword.text()).equals(autenticato.getPassword()) ){
            QMessageBox.warning(this, "Errore nella modifica della password", "La vecchia password inserita non è corretta");
            return;
        }
        if( this.newPassword1.text().equals(this.newPassword2.text()) ){
            autenticato.setPassword(SuiteUtil.digest(this.newPassword1.text()));
            Database db = (Database) Register.queryUtility(IDatabase.class);
            Controller controller = db.createController(Utente.class);
            Validation res = controller.commit(autenticato);
            if( res.getResponse() ){
                QMessageBox.information(this, "Password modificata", "La password è stata modificata con successo");
                super.accept();
            } else {
                QMessageBox.warning(this, "Errore nella modifica della password", res.getMessage());
            }
        } else {
            QMessageBox.critical(this, "Password errata", "Le due password inserite non coincidono");
        }
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }
    
}
