/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.protocollo.forms;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Store;
import com.axiastudio.pypapi.ui.Column;
import com.axiastudio.pypapi.ui.Delegate;
import com.axiastudio.pypapi.ui.TableModel;
import com.axiastudio.pypapi.ui.Util;
import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.suite.SuiteUtil;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.trolltech.qt.core.QByteArray;
import com.trolltech.qt.core.QFile;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.designer.QUiLoader;
import com.trolltech.qt.designer.QUiLoaderException;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QItemSelection;
import com.trolltech.qt.gui.QItemSelectionModel;
import com.trolltech.qt.gui.QMainWindow;
import com.trolltech.qt.gui.QTableView;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class FormScrivania  extends QMainWindow {
    
    public FormScrivania(){
        QFile file = Util.ui2jui(new QFile("classpath:com/axiastudio/suite/protocollo/forms/scrivania.ui"));
        this.loadUi(file);
        this.popolaAttribuzioni();
    }
    
    private void loadUi(QFile uiFile){
        QMainWindow window = null;
        try {
            window = (QMainWindow) QUiLoader.load(uiFile);
        } catch (QUiLoaderException ex) {
            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
        }
        for( QByteArray name: window.dynamicPropertyNames()){
            this.setProperty(name.toString(), window.property(name.toString()));
        }
        this.setCentralWidget(window.centralWidget());
    }

    private void popolaAttribuzioni() {
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        Store attribuzioni = SuiteUtil.attribuzioni(autenticato);
        List<Column> colonne = new ArrayList();
        colonne.add(new Column("iddocumento", "Protocollo", "Numero di protocollo"));
        colonne.add(new Column("tipoprotocollo", "E/U/I", "Entrata / Uscita / Interno"));
        colonne.add(new Column("dataprotocollo", "Data", "Data di protocollazione"));
        colonne.add(new Column("ufficio", "Ufficio", "Ufficio di attribuzione"));
        colonne.add(new Column("principale", "Pr.", "Attribuzione in via principale"));
        colonne.add(new Column("oggetto", "Oggetto", "Oggetto del protocollo"));
        TableModel model = new TableModel(attribuzioni, colonne);
        QTableView tableView = (QTableView) this.findChild(QTableView.class, "attribuzioni");
        tableView.setSelectionBehavior(QAbstractItemView.SelectionBehavior.SelectRows);
        tableView.setSortingEnabled(true);
        tableView.installEventFilter(this);
        tableView.setItemDelegate(new Delegate(tableView));
        model.setEditable(false);
        tableView.setModel(model);
        QItemSelectionModel selectionModel = new QItemSelectionModel(model);
        tableView.setSelectionModel(selectionModel);
        selectionModel.selectionChanged.connect(this, "selectRows(QItemSelection, QItemSelection)");
    }
    
    private void selectRows(QItemSelection selected, QItemSelection deselected){
        /*
        TableModel model = (TableModel) this.tableView.model();
        List<Integer> selectedIndexes = new ArrayList();
        List<Integer> deselectedIndexes = new ArrayList();
        for (QModelIndex i: selected.indexes()){
            if(!selectedIndexes.contains(i.row())){
                selectedIndexes.add(i.row());
            }
        }
        for (QModelIndex i: deselected.indexes()){
            if(!deselectedIndexes.contains(i.row())){
                deselectedIndexes.add(i.row());
            }
        }
        for (Integer idx: selectedIndexes){
            boolean res = this.selection.add(model.getEntityByRow(idx));
        }
        for (Integer idx: deselectedIndexes){
            boolean res = this.selection.remove(model.getEntityByRow(idx));
        }
        this.buttonAccept.setEnabled(this.selection.size()>0);
        */

    }
    
        
}
