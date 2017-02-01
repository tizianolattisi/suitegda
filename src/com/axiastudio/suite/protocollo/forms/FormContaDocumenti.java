/*
 * Copyright (C) 2015 Comune di Riva del Garda
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
import com.axiastudio.pypapi.db.*;
import com.axiastudio.pypapi.ui.*;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.base.entities.UfficioUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.menjazo.AlfrescoHelper;
import com.axiastudio.suite.plugins.cmis.CmisPlugin;
import com.axiastudio.suite.protocollo.ProfiloUtenteProtocollo;
import com.axiastudio.suite.protocollo.entities.*;
import com.trolltech.qt.core.QByteArray;
import com.trolltech.qt.core.QFile;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.designer.QUiLoader;
import com.trolltech.qt.designer.QUiLoaderException;
import com.trolltech.qt.gui.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michela Piva
 */
public class FormContaDocumenti extends QMainWindow {
    private Store<ContaDocumenti> protocolloStoreGenerale = new Store<ContaDocumenti>(null);
    private List<ContaDocumenti> selectionProtocollo = new ArrayList<ContaDocumenti>();
    private List<Ufficio> ufficiUtente = new ArrayList<Ufficio>();
    private QTableView tableView;
    private QLabel totRecord;
    private QCheckBox noDocumenti;
    private final Integer DEFAULT_ROW_HEIGHT = 24;
    private ContaDocumentiMenuBar menuBar;
    private CmisPlugin cmisPlugin;

    public FormContaDocumenti(){
        QFile file = Util.ui2jui(new QFile("classpath:com/axiastudio/suite/protocollo/forms/contadocumenti.ui"));
        this.loadUi(file);

        this.menuBar = new ContaDocumentiMenuBar("", this);
        this.addToolBar(menuBar);

        ufficiUtente=listUfficiUtente();

        /* table view protocolli */
        tableView = (QTableView) this.findChild(QTableView.class, "protocolli");
        tableView.setSelectionBehavior(QAbstractItemView.SelectionBehavior.SelectRows);
        //tableView.setSortingEnabled(true);  // TODO: gestire l'ordinamento sulla tabella scrivania
        //tableView.installEventFilter(this);
        tableView.verticalHeader().setDefaultSectionSize(DEFAULT_ROW_HEIGHT);
        tableView.doubleClicked.connect(this, "apriProtocollo()");

        totRecord = (QLabel) this.findChild(QLabel.class, "labelTotRecord");
        noDocumenti = (QCheckBox) this.findChild(QCheckBox.class, "checkBoxNoDocumenti");

        cmisPlugin = (CmisPlugin) Register.queryPlugin(Protocollo.class, "CMIS");
    }
    
   private void loadUi(QFile uiFile){
        QMainWindow window = null;
        try {
            window = (QMainWindow) QUiLoader.load(uiFile);
        } catch (QUiLoaderException ex) {
            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
        }
        for( QByteArray name: window != null ? window.dynamicPropertyNames() : null){
            this.setProperty(name.toString(), window.property(name.toString()));
        }
        this.setCentralWidget(window.centralWidget());
        this.setWindowTitle(window.windowTitle());
    }


    public void search(){
        Database db = (Database) Register.queryUtility(IDatabase.class);
        Controller controller = db.createController(Protocollo.class);
        PickerDialog pd = new PickerDialog(this, controller);

        int res = pd.exec();
        if (res == 1) {
            protocolloStoreGenerale.clear();
            Boolean noDoc = (noDocumenti.checkState() == Qt.CheckState.Checked);
            int i = 0;
            for ( Protocollo protocollo: (List<Protocollo>) pd.getSelection() ) {
                for ( Attribuzione attr: protocollo.getAttribuzioneCollection() ) {
                    if (attr.getPrincipale() && ufficiUtente.contains(attr.getUfficio())) {
                        ContaDocumenti contadoc = new ContaDocumenti();
                        contadoc.setProtocollo(protocollo);
                        contadoc.setUfficio(attr.getUfficio());
                        if ( i<200 ) {
                            // numero di documenti contenuti nella cartella Alfresco
                            AlfrescoHelper alfrescoHelper = cmisPlugin.createAlfrescoHelper(protocollo);
                            contadoc.setNumDocumenti(alfrescoHelper.numberOfDocument());
                        } else if ( i==200) {
                            QMessageBox.warning(this, "Attenzione", "Raggiunta la quota massima di 200 protocolli. Conteggio dei documenti non attivo per i successivi.");
                        }
                        if ( !noDoc || (contadoc.getNumDocumenti()!= null && contadoc.getNumDocumenti()==0) ) {
                            protocolloStoreGenerale.add(contadoc);
                        }
                        i++;
                    }
                }

            }
            this.popolaTabella();
        }
        pd.dispose();
        totRecord.setText("Totale record: "+ String.valueOf(protocolloStoreGenerale.size()));
    }


    private void popolaTabella() {
        selectionProtocollo.clear();
/*        PyPaPiComboBox ufficio = (PyPaPiComboBox) this.findChild(QComboBox.class, "comboBoxUfficio");
        ufficio.setCurrentIndex(ufficio.getLookupStore().size() - 1); */

        List<Column> colonne = new ArrayList();
        colonne.add(new Column("iddocumento", "Protocollo", "Numero di protocollo"));
        colonne.add(new Column("tipoprotocollo", "E/U/I", "Entrata / Uscita / Interno"));
        colonne.add(new Column("dataprotocollo", "Data", "Data di protocollazione"));
        colonne.add(new Column("ufficio", "Ufficio", "Ufficio di attribuzione"));
        colonne.add(new Column("oggetto", "Oggetto", "Oggetto del protocollo"));
        colonne.add(new Column("numDocumenti", "N.doc", "Numero di documenti"));
        TableModel model = new TableModel(protocolloStoreGenerale, colonne);
        tableView.clearSelection();
        model.setEditable(false);
        tableView.setModel(model);
        QItemSelectionModel selectionModel = new QItemSelectionModel(model);
        tableView.setSelectionModel(selectionModel);
        selectionModel.selectionChanged.connect(this, "selectRows(QItemSelection, QItemSelection)");
        tableView.horizontalHeader().setResizeMode(0, QHeaderView.ResizeMode.ResizeToContents); // iddocumento
        tableView.horizontalHeader().setResizeMode(1, QHeaderView.ResizeMode.ResizeToContents); // tipoprotocollo
        tableView.horizontalHeader().setResizeMode(2, QHeaderView.ResizeMode.ResizeToContents); // dataprotocollo
        tableView.horizontalHeader().setResizeMode(3, QHeaderView.ResizeMode.Interactive); // ufficio
        tableView.horizontalHeader().setResizeMode(4, QHeaderView.ResizeMode.Stretch);          // oggetto
        tableView.horizontalHeader().setResizeMode(5, QHeaderView.ResizeMode.ResizeToContents); // dataprotocollo
    }

    private void selectRows(QItemSelection selected, QItemSelection deselected){
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
            boolean res = this.selectionProtocollo.add((ContaDocumenti) model.getEntityByRow(idx));
        }
        for (Integer idx: deselectedIndexes){
            boolean res = this.selectionProtocollo.remove(model.getEntityByRow(idx));
        }

        refreshInfo();
    }

    private void refreshInfo() {
        this.menuBar.actionByName("apriProtocollo").setEnabled(this.selectionProtocollo.size() == 1);
        this.menuBar.actionByName("apriDocumenti").setEnabled(this.selectionProtocollo.size() == 1);
    }

    private void apriDocumenti(){
        Protocollo protocollo = this.selectionProtocollo.get(0).getProtocollo();
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        ProfiloUtenteProtocollo pup = new ProfiloUtenteProtocollo(protocollo, autenticato);
        Boolean view = false;
        Boolean delete = false;
        Boolean download = false;
        Boolean parent = false;
        Boolean upload = false;
        Boolean version = false;
        if( protocollo.getRiservato() ){
            view = pup.inSportelloOAttribuzioneV() && pup.inSportelloOAttribuzioneR();
            download = view;
        } else {
            view = autenticato.getSupervisoreprotocollo() || pup.inSportelloOAttribuzioneV();
            download = view;
        }
        if( protocollo.getConsolidadocumenti() ){
            delete = false;
            version = pup.inAttribuzionePrincipaleC();
            upload = version;
        } else {
            upload = pup.inSportelloOAttribuzionePrincipale();
            delete = upload;
            version = upload;
        }
        HashMap stampMap = new HashMap();
        stampMap.put("iddocumento", protocollo.getIddocumento());
        stampMap.put("dataprotocollo", protocollo.getDataprotocollo());
        String codiceinterno="";
        for( PraticaProtocollo pratica : protocollo.getPraticaProtocolloCollection() ) {
            if ( pratica.getOriginale() ) {
                codiceinterno=pratica.getPratica().getCodiceinterno();
            }
        }
        stampMap.put("codiceinterno", codiceinterno);
        stampMap.put("utente", autenticato.getNome().toUpperCase());

        cmisPlugin.showForm(protocollo, delete, download, parent, upload, version, stampMap);
    }

    private void apriProtocollo(){
        if( selectionProtocollo.size() != 1 ){
            return;
        }
        Protocollo protocollo = this.selectionProtocollo.get(0).getProtocollo();
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

    
/*    private void aggiornaLista(){
        this.popolaAttribuzioni();
    }*/

/*    private void filtraPerUfficio(){
        Store<Attribuzione> store = new Store<Attribuzione>(null);
        PyPaPiComboBox comboUfficio = (PyPaPiComboBox) this.findChild(QComboBox.class, "comboBoxUfficio");
        int idx = comboUfficio.currentIndex();
        Ufficio ufficio = (Ufficio) comboUfficio.itemData(idx);
        if (ufficio == null) {
            store = attribuzioneStoreGenerale;
        } else {
            for (Object obj: attribuzioneStoreGenerale) {
                Attribuzione attribuzione=(Attribuzione) obj;
                if (attribuzione.getUfficio().equals(ufficio)) {
                    store.add(attribuzione);
                }
            }
        }
        QTableView tableView = (QTableView) this.findChild(QTableView.class, "attribuzioni");
        TableModel model = (TableModel) tableView.model();
        model.setStore(store);
        totRecord.setText("Totale record: "+ String.valueOf(store.size()));
        this.selectionProtocollo.clear();
        this.refreshInfo();
    }*/

/*
 * Uno store contenente solo gli uffici dell'utente
 */
    public List<Ufficio> listUfficiUtente(){
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        List<Ufficio> uffici = new ArrayList();
        for(UfficioUtente uu: autenticato.getUfficioUtenteCollection()){
            if( uu.getRicerca() ){
                uffici.add(uu.getUfficio());
            }
        }
        return uffici;
    }

}

