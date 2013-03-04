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
package com.axiastudio.suite.protocollo.forms;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Controller;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.pypapi.db.Store;
import com.axiastudio.pypapi.plugins.IPlugin;
import com.axiastudio.pypapi.plugins.cmis.CmisPlugin;
import com.axiastudio.pypapi.ui.Column;
import com.axiastudio.pypapi.ui.IForm;
import com.axiastudio.pypapi.ui.TableModel;
import com.axiastudio.pypapi.ui.Util;
import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.suite.SuiteUtil;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.protocollo.entities.Attribuzione;
import com.axiastudio.suite.protocollo.entities.Protocollo;
import com.trolltech.qt.core.QByteArray;
import com.trolltech.qt.core.QFile;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.designer.QUiLoader;
import com.trolltech.qt.designer.QUiLoaderException;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QHeaderView;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QItemSelection;
import com.trolltech.qt.gui.QItemSelectionModel;
import com.trolltech.qt.gui.QMainWindow;
import com.trolltech.qt.gui.QMdiArea;
import com.trolltech.qt.gui.QPushButton;
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
    private List<Attribuzione> selection = new ArrayList();
    
    public FormScrivania(){
        QFile file = Util.ui2jui(new QFile("classpath:com/axiastudio/suite/protocollo/forms/scrivania.ui"));
        this.loadUi(file);
        QPushButton pushButtonDaiPerLetto = (QPushButton) this.findChild(QPushButton.class, "pushButtonDaiPerLetto");
        pushButtonDaiPerLetto.setIcon(new QIcon("classpath:com/axiastudio/suite/resources/tick.png"));
        pushButtonDaiPerLetto.clicked.connect(this, "daiPerLetto()");
        pushButtonDaiPerLetto.setEnabled(false);
        QPushButton pushButtonApriProtocollo = (QPushButton) this.findChild(QPushButton.class, "pushButtonApriProtocollo");
        pushButtonApriProtocollo.setIcon(new QIcon("classpath:com/axiastudio/suite/resources/email.png"));
        pushButtonApriProtocollo.clicked.connect(this, "apriProtocollo()");
        pushButtonApriProtocollo.setEnabled(false);
        QPushButton pushButtonApriDocumenti = (QPushButton) this.findChild(QPushButton.class, "pushButtonApriDocumenti");
        pushButtonApriDocumenti.setIcon(new QIcon("classpath:com/axiastudio/suite/resources/cmis.png"));
        pushButtonApriDocumenti.clicked.connect(this, "apriDocumenti()");
        pushButtonApriDocumenti.setEnabled(false);
        QPushButton pushButtonAggiornaLista = (QPushButton) this.findChild(QPushButton.class, "pushButtonAggiornaLista");
        pushButtonAggiornaLista.setIcon(new QIcon("classpath:com/axiastudio/pypapi/ui/resources/toolbar/arrow_refresh.png"));
        pushButtonAggiornaLista.clicked.connect(this, "aggiornaLista()");

        QTableView tableView = (QTableView) this.findChild(QTableView.class, "attribuzioni");
        tableView.setSelectionBehavior(QAbstractItemView.SelectionBehavior.SelectRows);
        tableView.setSortingEnabled(true);
        //tableView.installEventFilter(this);
        tableView.setItemDelegate(new DelegateScrivania(tableView));
        tableView.doubleClicked.connect(this, "apriProtocollo()");
        

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
        QTableView tableView = (QTableView) this.findChild(QTableView.class, "attribuzioni");
        colonne.add(new Column("evidenza", "Ev.", "Attribuzione in evidenza"));
        colonne.add(new Column("iddocumento", "Protocollo", "Numero di protocollo"));
        colonne.add(new Column("tipoprotocollo", "E/U/I", "Entrata / Uscita / Interno"));
        colonne.add(new Column("dataprotocollo", "Data", "Data di protocollazione"));
        colonne.add(new Column("ufficio", "Ufficio", "Ufficio di attribuzione"));
        colonne.add(new Column("principale", "Pr.", "Attribuzione in via principale"));
        colonne.add(new Column("oggetto", "Oggetto", "Oggetto del protocollo"));
        TableModel model = new TableModel(attribuzioni, colonne);
        tableView.clearSelection();
        model.setEditable(false);
        tableView.setModel(model);
        QItemSelectionModel selectionModel = new QItemSelectionModel(model);
        tableView.setSelectionModel(selectionModel);
        selectionModel.selectionChanged.connect(this, "selectRows(QItemSelection, QItemSelection)");
        tableView.horizontalHeader().setResizeMode(0, QHeaderView.ResizeMode.ResizeToContents); // evidenza
        tableView.horizontalHeader().setResizeMode(1, QHeaderView.ResizeMode.ResizeToContents); // iddocumento
        tableView.horizontalHeader().setResizeMode(2, QHeaderView.ResizeMode.ResizeToContents); // tipoprotocollo
        tableView.horizontalHeader().setResizeMode(3, QHeaderView.ResizeMode.ResizeToContents); // dataprotocollo
        tableView.horizontalHeader().setResizeMode(4, QHeaderView.ResizeMode.ResizeToContents); // ufficio
        tableView.horizontalHeader().setResizeMode(5, QHeaderView.ResizeMode.ResizeToContents); // principale
        tableView.horizontalHeader().setResizeMode(6, QHeaderView.ResizeMode.Stretch);          // oggetto
    }
    
    private void selectRows(QItemSelection selected, QItemSelection deselected){
        QTableView tableView = (QTableView) this.findChild(QTableView.class, "attribuzioni");
        QPushButton pushButtonDaiPerLetto = (QPushButton) this.findChild(QPushButton.class, "pushButtonDaiPerLetto");
        QPushButton pushButtonApriProtocollo = (QPushButton) this.findChild(QPushButton.class, "pushButtonApriProtocollo");
        QPushButton pushButtonApriDocumenti = (QPushButton) this.findChild(QPushButton.class, "pushButtonApriDocumenti");
        TableModel model = (TableModel) tableView.model();
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
            boolean res = this.selection.add((Attribuzione) model.getEntityByRow(idx));
        }
        for (Integer idx: deselectedIndexes){
            boolean res = this.selection.remove((Attribuzione) model.getEntityByRow(idx));
        }
        pushButtonDaiPerLetto.setEnabled(this.selection.size()>0);
        pushButtonApriProtocollo.setEnabled(this.selection.size()==1);
        pushButtonApriDocumenti.setEnabled(this.selection.size()==1);
    }
    
    private void daiPerLetto(){
        Database db = (Database) Register.queryUtility(IDatabase.class);
        Controller controller = new Controller(db.getEntityManagerFactory(), Attribuzione.class);
        for(Attribuzione attribuzione: this.selection){
            attribuzione.setLetto(Boolean.TRUE);
            controller.commit(attribuzione);
        }
        this.popolaAttribuzioni();
    }

    private void apriProtocollo(){
        Protocollo protocollo = this.selection.get(0).getProtocollo();
        IForm form = Util.formFromEntity(protocollo);
        if( form == null ){
            return;
        }
        QMdiArea workspace = Util.findParentMdiArea(this);
        if( workspace != null ){
            workspace.addSubWindow((QMainWindow) form);
        }
        form.show();
    }

    private void apriDocumenti(){
        Protocollo protocollo = this.selection.get(0).getProtocollo();
        List<IPlugin> plugins = (List) Register.queryPlugins(FormProtocollo.class);
        for(IPlugin plugin: plugins){
            if( "CMIS".equals(plugin.getName()) ){
                ((CmisPlugin) plugin).showForm(protocollo);
            }
        }
    }
    
    private void aggiornaLista(){
        this.popolaAttribuzioni();
    }

}
