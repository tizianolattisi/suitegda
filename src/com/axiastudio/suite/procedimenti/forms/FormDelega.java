/*
 * Copyright (C) 2013 AXIA Studio (http://www.axiastudio.com)
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
 * You should have received a copy of the GNU Afffero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axiastudio.suite.procedimenti.forms;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.procedimenti.entities.Delega;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QToolButton;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class FormDelega extends Window {

    public FormDelega(FormDelega form){
        super(form.uiFile, form.entityClass, form.title);
    }
    
    public FormDelega(String uiFile, Class entityClass) {
        super(uiFile, entityClass, null);
    }

    public FormDelega(String uiFile, Class entityClass, String title) {
        super(uiFile, entityClass, title);
        QPushButton pbCreaDelega = (QPushButton) this.findChild(QPushButton.class, "pushButton_creaDelega");
        pbCreaDelega.clicked.connect(this, "creaDelega()");
    }
    
    private void creaDelega(){
        System.out.println("delega");
    }

    @Override
    protected void indexChanged(int row) {
        Delega delega = (Delega) this.getContext().getCurrentEntity();
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        
        // Solo l'amministratore può usare i flag titolare, segretario, utente delegato
        ((QCheckBox) this.findChild(QCheckBox.class, "checkBox_titolare")).setEnabled(autenticato.getAmministratore());
        ((QCheckBox) this.findChild(QCheckBox.class, "checkBox_segretario")).setEnabled(autenticato.getAmministratore());
        ((QCheckBox) this.findChild(QCheckBox.class, "checkBox_delegato")).setEnabled(autenticato.getAmministratore());
        
        // Solo il titolare può delegare
        Boolean puoDelegare = delega.getTitolare() && !((Utente) Register.queryUtility(IUtente.class)).getAmministratore();
        ((QPushButton) this.findChild(QPushButton.class, "pushButton_creaDelega")).setEnabled(puoDelegare);
        super.indexChanged(row);
    }
    
}
