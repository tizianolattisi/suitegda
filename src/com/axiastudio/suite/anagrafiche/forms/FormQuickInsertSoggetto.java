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
package com.axiastudio.suite.anagrafiche.forms;

import com.axiastudio.pypapi.ui.IQuickInsertDialog;
import com.axiastudio.suite.anagrafiche.entities.Indirizzo;
import com.axiastudio.suite.anagrafiche.entities.SessoSoggetto;
import com.axiastudio.suite.anagrafiche.entities.Soggetto;
import com.axiastudio.suite.anagrafiche.entities.TipologiaSoggetto;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QTabWidget;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class FormQuickInsertSoggetto extends QDialog implements IQuickInsertDialog {

    private Object entity=null;
    private QTabWidget tabs;
    private QLineEdit lineEditNome;
    private QLineEdit lineEditCognome;
    private QComboBox comboBoxSesso;
    private QLineEdit lineEditCodicefiscale;
    private QLineEdit lineEditRagionesociale;
    private QLineEdit lineEditCodicefiscale2;
    private QLineEdit lineEditPartitaiva;
    private QLineEdit lineEditDenominazione;
    private QLineEdit lineEditDenominazione2;
    private QLineEdit lineEditDenominazione3;
    private QLineEdit lineEditCodicefiscale3;
    private QLineEdit lineEditIndirizzo;
    private QLineEdit lineEditCap;
    private QLineEdit lineEditLocalita;
    private QLineEdit lineEditProvincia;
    private QLineEdit lineEditStato;
    
    public FormQuickInsertSoggetto(QWidget parent) {
        super(parent);
        this.initLayout();
    }

    public FormQuickInsertSoggetto() {
        this(null);
    }
    
    private void initLayout(){
        QVBoxLayout layout = new QVBoxLayout(this);
        layout.setSpacing(4);
        layout.setObjectName("layout");
        QHBoxLayout buttonLayout = new QHBoxLayout();
        buttonLayout.setObjectName("buttonLayout");
        QToolButton buttonAccept = new QToolButton(this);
        buttonAccept.setIcon(new QIcon("classpath:com/axiastudio/pypapi/ui/resources/toolbar/accept.png"));
        buttonAccept.setObjectName("buttonAccept");
        QToolButton buttonCancel = new QToolButton(this);
        buttonCancel.setIcon(new QIcon("classpath:com/axiastudio/pypapi/ui/resources/toolbar/cancel.png"));
        buttonCancel.setObjectName("buttonCancel");
        buttonLayout.addWidget(buttonAccept);
        buttonLayout.addWidget(buttonCancel);
        
        this.tabs = new QTabWidget();

        // persona
        QWidget widgetPersona = new QWidget();
        QGridLayout gridLayoutPersona = new QGridLayout();
        widgetPersona.setLayout(gridLayoutPersona);
        lineEditNome = new QLineEdit();
        lineEditNome.setMaxLength(50);
        lineEditCognome = new QLineEdit();
        lineEditCognome.setMaxLength(50);
        comboBoxSesso = new QComboBox();
        List<String> sessi = new ArrayList();
        sessi.add("ND");
        sessi.add("M");
        sessi.add("F");
        comboBoxSesso.insertItems(0, sessi);
        lineEditCodicefiscale = new QLineEdit();
        lineEditCodicefiscale.setMaxLength(16);
        gridLayoutPersona.addWidget(new QLabel("Nome"), 0, 0);
        gridLayoutPersona.addWidget(lineEditNome, 0, 1);
        gridLayoutPersona.addWidget(new QLabel("Cognome"), 1, 0);
        gridLayoutPersona.addWidget(lineEditCognome, 1, 1);
        gridLayoutPersona.addWidget(new QLabel("Sesso"), 2, 0);
        gridLayoutPersona.addWidget(comboBoxSesso, 2, 1);
        gridLayoutPersona.addWidget(new QLabel("C.F."), 3, 0);
        gridLayoutPersona.addWidget(lineEditCodicefiscale, 3, 1);
        tabs.addTab(widgetPersona, "Persona");
        
        // azienda
        QWidget widgetAzienda = new QWidget();
        QGridLayout gridLayoutAzienda = new QGridLayout();
        widgetAzienda.setLayout(gridLayoutAzienda);
        lineEditRagionesociale = new QLineEdit();
        lineEditRagionesociale.setMaxLength(100);
        lineEditCodicefiscale2 = new QLineEdit();
        lineEditCodicefiscale2.setMaxLength(16);
        lineEditPartitaiva = new QLineEdit();
        lineEditPartitaiva.setMaxLength(11);
        gridLayoutAzienda.addWidget(new QLabel("Ragione sociale"), 0, 0);
        gridLayoutAzienda.addWidget(lineEditRagionesociale, 0, 1);
        gridLayoutAzienda.addWidget(new QLabel("C.F."), 1, 0);
        gridLayoutAzienda.addWidget(lineEditCodicefiscale2, 1, 1);
        gridLayoutAzienda.addWidget(new QLabel("P.IVA"), 2, 0);
        gridLayoutAzienda.addWidget(lineEditPartitaiva, 2, 1);
        tabs.addTab(widgetAzienda, "Azienda");
        
        // ente
        QWidget widgetEnte = new QWidget();
        QGridLayout gridLayoutEnte = new QGridLayout();
        widgetEnte.setLayout(gridLayoutEnte);
        lineEditDenominazione = new QLineEdit();
        lineEditDenominazione.setMaxLength(100);
        lineEditDenominazione2 = new QLineEdit();
        lineEditDenominazione2.setMaxLength(100);
        lineEditDenominazione3 = new QLineEdit();
        lineEditDenominazione3.setMaxLength(100);
        lineEditCodicefiscale3 = new QLineEdit();
        lineEditCodicefiscale3.setMaxLength(16);
        gridLayoutEnte.addWidget(new QLabel("Denominazione 1"), 0, 0);
        gridLayoutEnte.addWidget(lineEditDenominazione, 0, 1);
        gridLayoutEnte.addWidget(new QLabel("Denominazione 2"), 1, 0);
        gridLayoutEnte.addWidget(lineEditDenominazione2, 1, 1);
        gridLayoutEnte.addWidget(new QLabel("Denominazione 3"), 2, 0);
        gridLayoutEnte.addWidget(lineEditDenominazione3, 2, 1);
        gridLayoutEnte.addWidget(new QLabel("C.F."), 3, 0);
        gridLayoutEnte.addWidget(lineEditCodicefiscale3, 3, 1);
        tabs.addTab(widgetEnte, "Ente");
        
        layout.addWidget(tabs);
        
        // indirizzo
        QGroupBox groupBoxIndirizzo = new QGroupBox("Indirizzo");
        QGridLayout gridLayoutIndirizzo = new QGridLayout();
        groupBoxIndirizzo.setLayout(gridLayoutIndirizzo);
        lineEditIndirizzo = new QLineEdit();
        lineEditIndirizzo.setMaxLength(100);
        gridLayoutIndirizzo.addWidget(new QLabel("Indirizzo"), 0, 0);
        gridLayoutIndirizzo.addWidget(lineEditIndirizzo, 0, 1);
        lineEditCap = new QLineEdit();
        lineEditCap.setMaxLength(5);
        gridLayoutIndirizzo.addWidget(new QLabel("Cap"), 1, 0);
        gridLayoutIndirizzo.addWidget(lineEditCap, 1, 1);
        lineEditLocalita = new QLineEdit();
        lineEditLocalita.setMaxLength(50);
        gridLayoutIndirizzo.addWidget(new QLabel("Località"), 2, 0);
        gridLayoutIndirizzo.addWidget(lineEditLocalita, 2, 1);
        lineEditProvincia = new QLineEdit();
        lineEditProvincia.setMaxLength(2);
        gridLayoutIndirizzo.addWidget(new QLabel("Provincia"), 3, 0);
        gridLayoutIndirizzo.addWidget(lineEditProvincia, 3, 1);
        lineEditStato = new QLineEdit();
        lineEditStato.setMaxLength(25);
        gridLayoutIndirizzo.addWidget(new QLabel("Stato"), 4, 0);
        gridLayoutIndirizzo.addWidget(lineEditStato, 4, 1);
        layout.addWidget(groupBoxIndirizzo);
        
        layout.addLayout(buttonLayout);
        this.resize(500, 300);
        buttonAccept.clicked.connect(this, "insertAndAccept()");
        buttonCancel.clicked.connect(this, "reject()");
    }
    
    private void insertAndAccept(){
        int idx = this.tabs.currentIndex();
        Soggetto s = new Soggetto();
        if( idx == 0 ){
            List<SessoSoggetto> sessi = new ArrayList();
            sessi.add(SessoSoggetto.ND);
            sessi.add(SessoSoggetto.M);
            sessi.add(SessoSoggetto.F);
            s.setTipologiaSoggetto(TipologiaSoggetto.PERSONA);
            s.setNome(lineEditNome.text());
            s.setCognome(lineEditCognome.text());
            s.setCodiceFiscale(lineEditCodicefiscale.text());
            s.setSessoSoggetto(sessi.get(comboBoxSesso.currentIndex()));
        } else if( idx == 1 ){
            s.setTipologiaSoggetto(TipologiaSoggetto.AZIENDA);
            s.setRagionesociale(lineEditRagionesociale.text());
            s.setCodiceFiscale(lineEditCodicefiscale2.text());
            //s.setPartitaiva(lineEditPartitaiva.text());
        } else if( idx == 2 ){
            s.setTipologiaSoggetto(TipologiaSoggetto.ENTE);
            s.setDenominazione(lineEditDenominazione.text());
            //s.setDenominazione2(lineEditDenominazione2.text());
            //s.setDenominazione3(lineEditDenominazione3.text());
            s.setCodiceFiscale(lineEditCodicefiscale3.text());
        }
        List<Indirizzo> indirizzi = new ArrayList();
        Indirizzo indirizzo = new Indirizzo();
        indirizzo.setVia(lineEditIndirizzo.text());
        //indirizzo.setCivico(null);
        //indirizzo.setProvincia(lineEditProvincia.text());
        s.setIndirizzoCollection(indirizzi);
        this.entity = s;
        this.accept();
    }

    public Object getEntity() {
        return entity;
    }

    @Override
    public int exec() {
        return super.exec();
    }
    
}
