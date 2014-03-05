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
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QDateEdit;
import com.trolltech.qt.gui.QPushButton;

import java.util.Date;

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
    
    /*
     * Crea una nuova delega a partire dalla delega visualizzata
     */
    private void creaDelega(){
        Delega titolarita = (Delega) this.getContext().getCurrentEntity();
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        
        // Solo se l'utente autenticato è titolare
        if( titolarita.getTitolare() && titolarita.getUtente().equals(autenticato) ){
            Delega delega = new Delega();
            delega.setDelegante(autenticato);
            delega.setCarica(titolarita.getCarica());
            if( titolarita.getUfficio() != null ){
                delega.setUfficio(titolarita.getUfficio());
            }
            if( titolarita.getServizio()!= null ){
                delega.setServizio(titolarita.getServizio());
            }
            if( titolarita.getProcedimento()!= null ){
                delega.setProcedimento(titolarita.getProcedimento());
            }
            delega.setTitolare(Boolean.FALSE);
            delega.setSegretario(Boolean.FALSE);
            delega.setSuassenza(Boolean.FALSE);
            delega.setDelegato(Boolean.TRUE);
            delega.setInizio(new Date());
            this.getContext().insertElement(delega);
        }
    }

    @Override
    protected void indexChanged(int row) {
        Delega delega = (Delega) this.getContext().getCurrentEntity();
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        
        // Amministratore, titolare dell'incarico, delegato dal titolare, o delegante ad altro utente?
        Boolean eAmministratore = autenticato.getAmministratore();
        Boolean eTitolare = delega.getTitolare() && autenticato.equals(delega.getUtente());
        Boolean eDelegato = delega.getDelegato() && autenticato.equals(delega.getUtente());
        Boolean eDelegante = delega.getDelegato() && autenticato.equals(delega.getDelegante());
        
        // Solo l'amministratore può usare i flag titolare, segretario, utente delegato, e modificare le selezioni a piacere
        ((QCheckBox) this.findChild(QCheckBox.class, "checkBox_titolare")).setEnabled(eAmministratore);
        ((QCheckBox) this.findChild(QCheckBox.class, "checkBox_segretario")).setEnabled(eAmministratore);
        ((QCheckBox) this.findChild(QCheckBox.class, "checkBox_delegato")).setEnabled(eAmministratore);
        
        // Solo l'amministratore o il delegante possono modificare la delega solo su assenza e le date
        ((QCheckBox) this.findChild(QCheckBox.class, "checkBox_assenza")).setEnabled(eAmministratore || eDelegante);
        ((QDateEdit) this.findChild(QDateEdit.class, "dateEdit_inizio")).setEnabled(eAmministratore || eDelegante);
        ((QDateEdit) this.findChild(QDateEdit.class, "dateEdit_fine")).setEnabled(eAmministratore || eDelegante);
        
        // Solo l'amministratore può modificare la carica
        ((QComboBox) this.findChild(QComboBox.class, "comboBox_carica")).setEnabled(eAmministratore);

        // Solo l'amministratore e il delegante possono modificare ufficio, servizio e procedimento e utente delegato
        ((QComboBox) this.findChild(QComboBox.class, "comboBox_ufficio")).setEnabled(eAmministratore || eDelegante);
        ((QComboBox) this.findChild(QComboBox.class, "comboBox_servizio")).setEnabled(eAmministratore || eDelegante);
        ((QComboBox) this.findChild(QComboBox.class, "comboBox_procedimento")).setEnabled(eAmministratore || eDelegante);
        ((QComboBox) this.findChild(QComboBox.class, "comboBox_utente")).setEnabled(eAmministratore || eDelegante);

        // Solo l'amministratore può modificare il delegante
        ((QComboBox) this.findChild(QComboBox.class, "comboBox_delegante")).setEnabled(eAmministratore);

        // Solo il titolare non amministratore può delegare
        ((QPushButton) this.findChild(QPushButton.class, "pushButton_creaDelega")).setEnabled(eTitolare && !eAmministratore);
        super.indexChanged(row);
        
    }
    
}
