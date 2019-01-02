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
package com.axiastudio.suite.richieste.forms;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Controller;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.pypapi.ui.Column;
import com.axiastudio.pypapi.ui.TableModel;
import com.axiastudio.pypapi.ui.Util;
import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.pypapi.db.Store;
import com.axiastudio.suite.SuiteUtil;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.richieste.entities.DestinatarioUfficio;
import com.axiastudio.suite.richieste.entities.DestinatarioUtente;
import com.axiastudio.suite.richieste.entities.IDestinatarioRichiesta;
import com.axiastudio.suite.richieste.entities.VistoIndividuale;
import com.trolltech.qt.core.QFile;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.designer.QUiLoader;
import com.trolltech.qt.designer.QUiLoaderException;
import com.trolltech.qt.gui.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pivamichela
 */
public class FormVistaRichieste extends QDialog {

//    private final Object currentEntity;
    private Store<IDestinatarioRichiesta> vistoGenerale;
    private Store<IDestinatarioRichiesta> vistoPersonale;
    private List<Ufficio> ufficiDaiPerLetto;
    private QTableView tableVistoGenerale;
    private QTableView tableVistoPersonale;
    private List<IDestinatarioRichiesta> selectionGenerale = new ArrayList<IDestinatarioRichiesta>();
    private List<IDestinatarioRichiesta> selectionPersonale = new ArrayList<IDestinatarioRichiesta>();
    private final Integer DEFAULT_ROW_HEIGHT = 24;

    public FormVistaRichieste(QWidget parent, List<IDestinatarioRichiesta> vistoGenerale,
                              List<IDestinatarioRichiesta> vistoPersonale, List<Ufficio> ufficiDaiPerLetto) {
        super(parent);
        this.vistoGenerale=new Store(vistoGenerale);
        this.vistoPersonale=new Store(vistoPersonale);
        this.ufficiDaiPerLetto=ufficiDaiPerLetto;
        QFile file = Util.ui2jui(new QFile("classpath:com/axiastudio/suite/richieste/forms/vistarichieste.ui"));
        this.loadUi(file);
        initTableGenerale(this.vistoGenerale);
        initTablePersonale(this.vistoPersonale);

//        this.refreshList(this.tableVistoGenerale, vistoGenerale);
//        this.refreshList(this.tableVistoPersonale, vistoPersonale);
    }

    private void initTableGenerale(Store vistoGenerale) {
        //        this.tableVistoGenerale = (QTableWidget) this.findChild(QTableWidget.class, "tableVistoGenerale");
//        this.tableVistoGenerale.setColumnCount(3);
//        List<String> labels = new ArrayList();
//        labels.add("Mittente");
//        labels.add("Destinatario");
//        labels.add("Testo");
//        this.tableVistoGenerale.setHorizontalHeaderLabels(labels);
        List<Column> colonne = new ArrayList();
        this.tableVistoGenerale = (QTableView) this.findChild(QTableView.class, "tableVistoGenerale");
        tableVistoGenerale.setSelectionBehavior(QAbstractItemView.SelectionBehavior.SelectRows);
        tableVistoGenerale.verticalHeader().setDefaultSectionSize(DEFAULT_ROW_HEIGHT);
        colonne.add(new Column("mittente", "Mittente", ""));
        colonne.add(new Column("nomedestinatario", "Destinatario", ""));
        colonne.add(new Column("testo", "Testo", ""));
        colonne.add(new Column("richiestacancellabile", "Canc", "Richiesta cancellabile se data per letta da tutti i destinatari"));
        TableModel model = new TableModel(vistoGenerale, colonne);
        this.tableVistoGenerale.clearSelection();
        model.setEditable(false);
        this.tableVistoGenerale.setModel(model);
        QItemSelectionModel selectionModel = new QItemSelectionModel(model);
        this.tableVistoGenerale.setSelectionModel(selectionModel);
        selectionModel.selectionChanged.connect(this, "selectRowsGenerale(QItemSelection, QItemSelection)");
        tableVistoGenerale.horizontalHeader().setResizeMode(0, QHeaderView.ResizeMode.Interactive); // mittente
        tableVistoGenerale.horizontalHeader().resizeSection(0, Math.min(tableVistoGenerale.sizeHintForColumn(0), 200));
        tableVistoGenerale.horizontalHeader().setResizeMode(1, QHeaderView.ResizeMode.Interactive); // destinatario
        tableVistoGenerale.horizontalHeader().resizeSection(1, Math.min(tableVistoGenerale.sizeHintForColumn(1), 300));
        tableVistoGenerale.horizontalHeader().setResizeMode(2, QHeaderView.ResizeMode.Stretch); // testo
        tableVistoGenerale.horizontalHeader().setResizeMode(3, QHeaderView.ResizeMode.ResizeToContents); // canc
    }

    private void initTablePersonale(Store vistoPersonale) {
//        this.tableVistoPersonale = (QTableWidget) this.findChild(QTableWidget.class, "tableVistoPersonale");
//        this.tableVistoPersonale.setColumnCount(3);
////        labels = new ArrayList();
////        labels.add("Mittente");
////        labels.add("Destinatario");
////        labels.add("Testo");
//        this.tableVistoPersonale.setHorizontalHeaderLabels(labels);
        List<Column> colonne = new ArrayList();
        this.tableVistoPersonale = (QTableView) this.findChild(QTableView.class, "tableVistoPersonale");
        tableVistoPersonale.setSelectionBehavior(QAbstractItemView.SelectionBehavior.SelectRows);
        tableVistoPersonale.verticalHeader().setDefaultSectionSize(DEFAULT_ROW_HEIGHT);
        colonne.add(new Column("mittente", "Mittente", ""));
        colonne.add(new Column("nomedestinatario", "Destinatario", ""));
        colonne.add(new Column("testo", "Testo", ""));
        colonne.add(new Column("richiestacancellabile", "Canc", "Richiesta cancellabile se data per letta da tutti i destinatari"));
        TableModel model = new TableModel(vistoPersonale, colonne);
        this.tableVistoPersonale.clearSelection();
        model.setEditable(false);
        this.tableVistoPersonale.setModel(model);
        QItemSelectionModel selectionModel = new QItemSelectionModel(model);
        this.tableVistoPersonale.setSelectionModel(selectionModel);
        selectionModel.selectionChanged.connect(this, "selectRowsPersonale(QItemSelection, QItemSelection)");
        tableVistoPersonale.horizontalHeader().setResizeMode(0, QHeaderView.ResizeMode.Interactive); // mittente
        tableVistoPersonale.horizontalHeader().resizeSection(0, Math.min(tableVistoGenerale.sizeHintForColumn(0), 200));
        tableVistoPersonale.horizontalHeader().setResizeMode(1, QHeaderView.ResizeMode.Interactive); // destinatario
        tableVistoPersonale.horizontalHeader().resizeSection(1, Math.min(tableVistoGenerale.sizeHintForColumn(1), 300));
        tableVistoPersonale.horizontalHeader().setResizeMode(2, QHeaderView.ResizeMode.Stretch); // testo
        tableVistoPersonale.horizontalHeader().setResizeMode(3, QHeaderView.ResizeMode.ResizeToContents); // canc
    }


    private void loadUi(QFile uiFile){
        QDialog dialog = null;
        try {
            dialog = (QDialog) QUiLoader.load(uiFile);
        } catch (QUiLoaderException ex) {
            Logger.getLogger(FormVistaRichieste.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.setLayout(dialog.layout());
        this.setModal(true);

        QToolButton toolButton = (QToolButton) this.findChild(QToolButton.class, "tbVistoGenerale");
        toolButton.setIcon(new QIcon("classpath:com/axiastudio/suite/resources/up.png"));
        toolButton.clicked.connect(this, "inserisciInVistoGenerale()");
        toolButton.setToolTip("Dai per letto per tutti");
        toolButton.setEnabled(false);

        toolButton = (QToolButton) this.findChild(QToolButton.class, "tbVistoPersonale");
        toolButton.setIcon(new QIcon("classpath:com/axiastudio/suite/resources/down.png"));
        toolButton.clicked.connect(this, "inserisciInVistoPersonale()");
        toolButton.setToolTip("Dai per letto solo per te");
        toolButton.setEnabled(false);

        toolButton = (QToolButton) this.findChild(QToolButton.class, "tbOk");
        toolButton.setIcon(new QIcon("classpath:com/axiastudio/suite/resources/tick.png"));
        toolButton.clicked.connect(this, "applicaVisto()");

        toolButton = (QToolButton) this.findChild(QToolButton.class, "tbAnnulla");
        toolButton.setIcon(new QIcon("classpath:com/axiastudio/suite/resources/cross.png"));
        toolButton.clicked.connect(this, "reject()");

//        QTableWidget qtw = (QTableWidget) this.findChild(QTableWidget.class, "tableWidget");
//        qtw.verticalHeader().hide();
//        qtw.setShowGrid(false);
//        qtw.setAlternatingRowColors(true);
//        qtw.setSelectionBehavior(QAbstractItemView.SelectionBehavior.SelectRows);
//        qtw.setSortingEnabled(true);
//        qtw.horizontalHeader().setResizeMode(QHeaderView.ResizeMode.ResizeToContents);

//        // file type
//        QComboBox fileType = (QComboBox) this.findChild(QComboBox.class, "comboBoxFileType");
//        fileType.addItem(new QIcon("classpath:com/axiastudio/suite/resources/page_odt.png"),
//                "Open Document (odt)", "writer8");
//        fileType.addItem(new QIcon("classpath:com/axiastudio/suite/resources/page_doc.png"),
//                "Microsoft Word (doc)", "writer_MS_Word_97");
//        fileType.addItem(new QIcon("classpath:com/axiastudio/suite/resources/page_pdf.png"),
//                "Acrobat PDF (pdf)", "writer_pdf_Export");
//
    }


//    private void refreshList(QTableWidget qtw, List<IDestinatarioRichiesta> list){
//        qtw.clearContents();
////        while( qtw.rowCount() > 0 ){
////            qtw.removeRow(0);
////        }
//        Integer i = 0;
//        for( IDestinatarioRichiesta richiesta: list ){
//            qtw.insertRow(i);
//            QTableWidgetItem itemMittente = new QTableWidgetItem(richiesta.getMittente());
//            itemMittente.setFlags(Qt.ItemFlag.ItemIsSelectable, Qt.ItemFlag.ItemIsEnabled);
//            qtw.setItem(i, 0, itemMittente);
//            QTableWidgetItem itemDescription = new QTableWidgetItem(richiesta.getNomedestinatario());
//            itemDescription.setFlags(Qt.ItemFlag.ItemIsSelectable, Qt.ItemFlag.ItemIsEnabled);
//            qtw.setItem(i, 1, itemDescription);
//            QTableWidgetItem itemTesto = new QTableWidgetItem(richiesta.getTesto());
//            itemTesto.setFlags(Qt.ItemFlag.ItemIsSelectable, Qt.ItemFlag.ItemIsEnabled);
//            qtw.setItem(i, 2, itemTesto);
//            i++;
//        }
//    }

    private void selectRowsGenerale(QItemSelection selected, QItemSelection deselected){
        TableModel model = (TableModel) this.tableVistoGenerale.model();
        List<Integer> selectedIndexes = new ArrayList();
        List<Integer> deselectedIndexes = new ArrayList();
        for (QModelIndex i: selected.indexes()){
            if(!selectedIndexes.contains(i.row())){
                selectedIndexes.add(i.row());
            }
        }
        if (deselected != null) {
            for (QModelIndex i: deselected.indexes()){
                if(!deselectedIndexes.contains(i.row())){
                    deselectedIndexes.add(i.row());
                }
            }
        }
        for (Integer idx: selectedIndexes){
            boolean res = this.selectionGenerale.add((IDestinatarioRichiesta) model.getEntityByRow(idx));
        }
        for (Integer idx: deselectedIndexes){
            boolean res = this.selectionGenerale.remove(model.getEntityByRow(idx));
        }

        ((QToolButton) this.findChild(QToolButton.class, "tbVistoPersonale")).setEnabled(this.selectionGenerale.size()>0);
    }

    private void selectRowsPersonale(QItemSelection selected, QItemSelection deselected){
        TableModel model = (TableModel) this.tableVistoPersonale.model();
        List<Integer> selectedIndexes = new ArrayList();
        List<Integer> deselectedIndexes = new ArrayList();
        for (QModelIndex i: selected.indexes()){
            if(!selectedIndexes.contains(i.row())){
                selectedIndexes.add(i.row());
            }
        }
        if (deselected != null) {
            for (QModelIndex i: deselected.indexes()){
                if(!deselectedIndexes.contains(i.row())){
                    deselectedIndexes.add(i.row());
                }
            }
        }
        for (Integer idx: selectedIndexes){
            boolean res = this.selectionPersonale.add((IDestinatarioRichiesta) model.getEntityByRow(idx));
        }
        for (Integer idx: deselectedIndexes){
            boolean res = this.selectionPersonale.remove(model.getEntityByRow(idx));
        }

        boolean ok=false;
        for(IDestinatarioRichiesta richiesta: this.selectionPersonale){
            boolean vistopersona=false;
            if ( DestinatarioUtente.class.isInstance(richiesta) ) {
                ok=true;
                break;
            } else if ( DestinatarioUfficio.class.isInstance(richiesta) ) {
                if ( ufficiDaiPerLetto.contains(((DestinatarioUfficio) richiesta).getDestinatario()) ) {
                    ok=true;
                    break;
                }
            }
        }
        ((QToolButton) this.findChild(QToolButton.class, "tbVistoGenerale")).setEnabled(ok);
    }

    private void inserisciInVistoGenerale() {
        TableModel modelG = (TableModel) tableVistoGenerale.model();
        Store storeG=modelG.getStore();
        TableModel modelP = (TableModel) tableVistoPersonale.model();
        Store storeP=modelP.getStore();
//        boolean ok;
        List<IDestinatarioRichiesta> vistoOk=new ArrayList<IDestinatarioRichiesta>();
        for(IDestinatarioRichiesta richiesta: this.selectionPersonale){
//            ok=false;
            if ( DestinatarioUtente.class.isInstance(richiesta) ) {
//                ok=true;
                vistoOk.add(richiesta);
            } else if ( DestinatarioUfficio.class.isInstance(richiesta) ) {
                if ( ufficiDaiPerLetto.contains(((DestinatarioUfficio) richiesta).getDestinatario()) ) {
//                    ok=true;
                    vistoOk.add(richiesta);
                }
            }
        }
        if ( !vistoOk.isEmpty() ) {
            vistoGenerale.addAll(vistoOk);
            vistoPersonale.removeAll(vistoOk);
//            storeP.removeAll(vistoOk);
//            storeG.addAll(vistoOk);
            this.selectionPersonale.removeAll(vistoOk);
        }
        modelG.setStore(vistoGenerale);
        modelP.setStore(vistoPersonale);
        ((QToolButton) this.findChild(QToolButton.class, "tbVistoGenerale")).setEnabled(false);
    }

    private void inserisciInVistoPersonale() {
        TableModel modelG = (TableModel) tableVistoGenerale.model();
//        Store storeG=modelG.getStore();
        TableModel modelP = (TableModel) tableVistoPersonale.model();
//        Store storeP=modelP.getStore();
        vistoPersonale.addAll(this.selectionGenerale);
        vistoGenerale.removeAll(this.selectionGenerale);
//        storeP.addAll(this.selectionGenerale);
//        storeG.removeAll(this.selectionGenerale);
        modelG.setStore(vistoGenerale);
        modelP.setStore(vistoPersonale);
        this.selectionGenerale.clear();
        ((QToolButton) this.findChild(QToolButton.class, "tbVistoPersonale")).setEnabled(false);
    }

    private void applicaVisto() {
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        Database db = (Database) Register.queryUtility(IDatabase.class);
        Controller controller = db.createController(IDestinatarioRichiesta.class);
        Controller vcontroller = db.createController(VistoIndividuale.class);

        for (Object richiesta : this.vistoGenerale) {
            ((IDestinatarioRichiesta) richiesta).setLetto(Boolean.TRUE);
            ((IDestinatarioRichiesta) richiesta).setDataletto(SuiteUtil.getServerDate());
            ((IDestinatarioRichiesta) richiesta).setEsecutoreletto(autenticato.getLogin());
            controller.commit(richiesta);
        }
        for (Object richiesta :  this.vistoPersonale) {
            VistoIndividuale visto = new VistoIndividuale();
            visto.setRichiesta(((IDestinatarioRichiesta) richiesta).getRichiesta());
            visto.setDataletto(SuiteUtil.getServerDate());
            visto.setEsecutoreletto(autenticato);
            vcontroller.commit(visto);
        }
        this.accept();
    }

}
