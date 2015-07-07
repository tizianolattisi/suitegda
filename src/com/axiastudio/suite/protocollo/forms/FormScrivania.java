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
import com.axiastudio.pypapi.ui.*;
import com.axiastudio.pypapi.ui.widgets.PyPaPiComboBox;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.base.entities.UfficioUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.menjazo.AlfrescoHelper;
import com.axiastudio.suite.plugins.cmis.CmisPlugin;
import com.axiastudio.suite.protocollo.ProfiloUtenteProtocollo;
import com.axiastudio.suite.protocollo.entities.*;
import com.axiastudio.suite.richieste.entities.DestinatarioUfficio;
import com.axiastudio.suite.richieste.entities.IDestinatarioRichiesta;
import com.axiastudio.suite.richieste.entities.Richiesta;
import com.trolltech.qt.core.QByteArray;
import com.trolltech.qt.core.QFile;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.designer.QUiLoader;
import com.trolltech.qt.designer.QUiLoaderException;
import com.trolltech.qt.gui.*;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class FormScrivania  extends QMainWindow {
    private Store<Attribuzione> attribuzioneStoreGenerale = new Store<Attribuzione>(null);
    private List<Attribuzione> selectionProtocollo = new ArrayList<Attribuzione>();
    private List<DestinatarioUfficio> selectionRichiesta = new ArrayList<DestinatarioUfficio>();
    private QLabel totRecord;
    private final Integer DEFAULT_ROW_HEIGHT = 24;
    public ScrivaniaMenuBar scrivaniaMenuBar;

    public FormScrivania(){
        QFile file = Util.ui2jui(new QFile("classpath:com/axiastudio/suite/protocollo/forms/scrivania.ui"));
        this.loadUi(file);

        this.scrivaniaMenuBar = new ScrivaniaMenuBar("Scrivania", this);
        this.addToolBar(scrivaniaMenuBar);

        /* table view protocolli */
        QTableView tableView = (QTableView) this.findChild(QTableView.class, "attribuzioni");
        tableView.setSelectionBehavior(QAbstractItemView.SelectionBehavior.SelectRows);
        //tableView.setSortingEnabled(true);  // TODO: gestire l'ordinamento sulla tabella scrivania
        //tableView.installEventFilter(this);
        tableView.setItemDelegate(new DelegateScrivania(tableView));
        tableView.verticalHeader().setDefaultSectionSize(DEFAULT_ROW_HEIGHT);
        tableView.doubleClicked.connect(this, "apriProtocollo()");

        PyPaPiComboBox ufficio = (PyPaPiComboBox) this.findChild(QComboBox.class, "comboBoxUfficio");
        Store storeUffici = storeUfficioFiltro();
        ufficio.setLookupStore(storeUffici);
        ufficio.setCurrentIndex(storeUffici.size()-1);

        totRecord = (QLabel) this.findChild(QLabel.class, "labelTotRecord");

        QPushButton pushButtonFiltra = (QPushButton) this.findChild(QPushButton.class, "pushButtonFiltra");
        pushButtonFiltra.clicked.connect(this, "filtraPerUfficio()");

        /* table view richieste */
        QTableView tableViewRichieste = (QTableView) this.findChild(QTableView.class, "richieste");
        tableViewRichieste.setSelectionBehavior(QAbstractItemView.SelectionBehavior.SelectRows);
        //tableViewRichieste.setSortingEnabled(true); // TODO: gestire l'ordinamento sulla tabella scrivania
        //tableView.setItemDelegate(new DelegateScrivania(tableView));
        tableViewRichieste.verticalHeader().setDefaultSectionSize(DEFAULT_ROW_HEIGHT);
        tableViewRichieste.doubleClicked.connect(this, "apriRichiesta()");

        this.popolaAttribuzioni();

        // disabilito le richieste
        ((QTabWidget) this.findChild(QTabWidget.class, "tabWidget")).setTabEnabled(1, false);
        //this.popolaRichieste();
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
        this.setWindowTitle(window.windowTitle());
    }

    private void popolaAttribuzioni() {
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        List<Attribuzione> attribuzioni = em.createNamedQuery("trovaAttribuzioniUtente", Attribuzione.class)
                                            .setParameter("id", autenticato.getId())
                                            .getResultList();
        attribuzioneStoreGenerale.clear();
        attribuzioneStoreGenerale.addAll(attribuzioni);
        totRecord.setText("Totale record: "+ String.valueOf(attribuzioneStoreGenerale.size()));
        selectionProtocollo.clear();
        PyPaPiComboBox ufficio = (PyPaPiComboBox) this.findChild(QComboBox.class, "comboBoxUfficio");
        ufficio.setCurrentIndex(ufficio.getLookupStore().size() - 1);

        List<Column> colonne = new ArrayList();
        QTableView tableView = (QTableView) this.findChild(QTableView.class, "attribuzioni");
        colonne.add(new Column("evidenza", "Ev.", "Attribuzione in evidenza"));
        colonne.add(new Column("iddocumento", "Protocollo", "Numero di protocollo"));
        colonne.add(new Column("tipoprotocollo", "E/U/I", "Entrata / Uscita / Interno"));
        colonne.add(new Column("dataprotocollo", "Data", "Data di protocollazione"));
        colonne.add(new Column("ufficio", "Ufficio", "Ufficio di attribuzione"));
        colonne.add(new Column("principale", "Pr.", "Attribuzione in via principale"));
        colonne.add(new Column("oggetto", "Oggetto", "Oggetto del protocollo"));
        TableModel model = new TableModel(attribuzioneStoreGenerale, colonne);
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
        tableView.horizontalHeader().setResizeMode(4, QHeaderView.ResizeMode.Interactive); // ufficio
        tableView.horizontalHeader().setResizeMode(5, QHeaderView.ResizeMode.ResizeToContents); // principale
        tableView.horizontalHeader().setResizeMode(6, QHeaderView.ResizeMode.Stretch);          // oggetto
    }

    private void popolaRichieste() {
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        List<IDestinatarioRichiesta> destinatariUtente = em.createNamedQuery("trovaDestinatarioUtente", IDestinatarioRichiesta.class)
                .setParameter("id", autenticato.getId())
                .getResultList();
        List<IDestinatarioRichiesta> destinatari = em.createNamedQuery("trovaDestinatarioUfficio", IDestinatarioRichiesta.class)
                .setParameter("id", autenticato.getId())
                .getResultList();
        destinatari.addAll(destinatariUtente);
        Store store = new Store(destinatari);
        List<Column> colonne = new ArrayList();
        QTableView tableView = (QTableView) this.findChild(QTableView.class, "richieste");
        colonne.add(new Column("mittente", "Mittente", "Mittente della richiesta"));
        colonne.add(new Column("datascadenza", "Data scadenza", "Data di scadenza della richiesta"));
        colonne.add(new Column("nomedestinatario", "Inviata a", "Richiesta inviata a..."));
        colonne.add(new Column("testo", "Testo", "Testo della richiesta"));
        colonne.add(new Column("data", "Data invio", "Data della richiesta"));
        colonne.add(new Column("conoscenza", "cc", "Per conoscenza"));
        colonne.add(new Column("letto", "Evasa", "Richiesta evasa/conclusa"));
        TableModel model = new TableModel(store, colonne);
        tableView.clearSelection();
        model.setEditable(false);
        tableView.setModel(model);
        QItemSelectionModel selectionModel = new QItemSelectionModel(model);
        tableView.setSelectionModel(selectionModel);
        selectionModel.selectionChanged.connect(this, "selectRowsRichieste(QItemSelection, QItemSelection)");
        tableView.horizontalHeader().setResizeMode(0, QHeaderView.ResizeMode.ResizeToContents); // data
        tableView.horizontalHeader().setResizeMode(1, QHeaderView.ResizeMode.ResizeToContents); // data
        tableView.horizontalHeader().setResizeMode(2, QHeaderView.ResizeMode.ResizeToContents); // data
        tableView.horizontalHeader().setResizeMode(3, QHeaderView.ResizeMode.Stretch); // testo
        tableView.horizontalHeader().setResizeMode(4, QHeaderView.ResizeMode.ResizeToContents); // data
        tableView.horizontalHeader().setResizeMode(5, QHeaderView.ResizeMode.ResizeToContents); // data
        tableView.horizontalHeader().setResizeMode(6, QHeaderView.ResizeMode.ResizeToContents); // data
    }

    private void selectRowsRichieste(QItemSelection selected, QItemSelection deselected){
        QTableView tableView = (QTableView) this.findChild(QTableView.class, "richieste");
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
            boolean res = this.selectionRichiesta.add((DestinatarioUfficio) model.getEntityByRow(idx));
        }
        for (Integer idx: deselectedIndexes){
            boolean res = this.selectionRichiesta.remove((DestinatarioUfficio) model.getEntityByRow(idx));
        }
    }


    private void selectRows(QItemSelection selected, QItemSelection deselected){

        QTableView tableView = (QTableView) this.findChild(QTableView.class, "attribuzioni");
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
            boolean res = this.selectionProtocollo.add((Attribuzione) model.getEntityByRow(idx));
        }
        for (Integer idx: deselectedIndexes){
            boolean res = this.selectionProtocollo.remove((Attribuzione) model.getEntityByRow(idx));
        }

        refreshInfo();
    }

    private void refreshInfo() {
        this.scrivaniaMenuBar.actionByName("daiPerLetto").setEnabled(this.selectionProtocollo.size()>0);
        this.scrivaniaMenuBar.actionByName("apriProtocollo").setEnabled(this.selectionProtocollo.size()==1);
        this.scrivaniaMenuBar.actionByName("apriDocumenti").setEnabled(this.selectionProtocollo.size() == 1);

        // oggetto, uffici, soggetti
        QTextEdit textEdit_oggetto = (QTextEdit) this.findChild(QTextEdit.class, "textEdit_oggetto");
        QListWidget listWidget_uffici = (QListWidget) this.findChild(QListWidget.class, "listWidget_uffici");
        QListWidget listWidget_attribuzioni = (QListWidget) this.findChild(QListWidget.class, "listWidget_attribuzioni");
        QListWidget listWidget_soggetti = (QListWidget) this.findChild(QListWidget.class, "listWidget_soggetti");
        listWidget_uffici.clear();
        listWidget_soggetti.clear();
        listWidget_attribuzioni.clear();
        if( this.selectionProtocollo.size() == 1 ){
            Attribuzione attribuzione = this.selectionProtocollo.get(0);
            Protocollo protocollo = attribuzione.getProtocollo();
            textEdit_oggetto.setText(protocollo.getOggetto());
            for( UfficioProtocollo up: protocollo.getUfficioProtocolloCollection() ){
                QListWidgetItem item = new QListWidgetItem();
                item.setText(up.getUfficio().toString());
                listWidget_uffici.addItem(item);
            }
            for( Attribuzione a: protocollo.getAttribuzioneCollection() ){
                QListWidgetItem item = new QListWidgetItem();
                String pre = "";
                if( a.getPrincipale() ){
                    pre = "* ";
                }
                item.setText(pre+a.getUfficio().toString());
                listWidget_attribuzioni.addItem(item);
            }
            for( SoggettoProtocollo sp: protocollo.getSoggettoProtocolloCollection() ){
                if ( ! sp.getAnnullato() ) {
                    QListWidgetItem item = new QListWidgetItem();
                    item.setText(sp.toString());
                    listWidget_soggetti.addItem(item);
                }
            }
            // numero di documenti contenuti nella cartella Alfresco
            CmisPlugin cmisPlugin = (CmisPlugin) Register.queryPlugin(Protocollo.class, "CMIS");
            AlfrescoHelper alfrescoHelper = cmisPlugin.createAlfrescoHelper(protocollo);
            Long n = alfrescoHelper.numberOfDocument();
            QGroupBox groupBox = (QGroupBox) this.findChild(QGroupBox.class, "groupBox");
            groupBox.setTitle("Anteprima - " + n + " documenti.");
        } else {
            textEdit_oggetto.setText("");
        }
    }

    private void daiPerLetto(){
        Database db = (Database) Register.queryUtility(IDatabase.class);
        Controller controller = db.createController(Attribuzione.class);
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        List<Ufficio> ufficiDaiPerLetto = new ArrayList();
        for(UfficioUtente uu: autenticato.getUfficioUtenteCollection()){
            if( uu.getDaiperletto() ){
                ufficiDaiPerLetto.add(uu.getUfficio());
            }
        }
        List<Attribuzione> attribuzioniLette = new ArrayList();
        for(Attribuzione attribuzione: this.selectionProtocollo){
            if ( ufficiDaiPerLetto.contains(attribuzione.getUfficio()) ) {
                attribuzione.setLetto(Boolean.TRUE);
                attribuzione.setDataletto(Calendar.getInstance().getTime());
                attribuzione.setEsecutoreletto(autenticato.getLogin());
                controller.commit(attribuzione);
                attribuzioniLette.add(attribuzione);
            }
        }
        attribuzioneStoreGenerale.removeAll(attribuzioniLette);
        QTableView tableView = (QTableView) this.findChild(QTableView.class, "attribuzioni");
        TableModel model = (TableModel) tableView.model();
        Store store=model.getStore();
        store.removeAll(attribuzioniLette);
        model.setStore(store);
        totRecord.setText("Totale record: " + String.valueOf(store.size()));

        this.selectionProtocollo.clear();
        this.refreshInfo();
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

    private void apriRichiesta(){
        Richiesta richiesta = this.selectionRichiesta.get(0).getRichiesta();
        IForm form = Util.formFromEntity(richiesta);
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
        Protocollo protocollo = this.selectionProtocollo.get(0).getProtocollo();
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        ProfiloUtenteProtocollo pup = new ProfiloUtenteProtocollo(protocollo, autenticato);
        List<IPlugin> plugins = (List) Register.queryPlugins(FormScrivania.class);
        for(IPlugin plugin: plugins){
            if( "CMIS".equals(plugin.getName()) ){
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

                ((CmisPlugin) plugin).showForm(protocollo, delete, download, parent, upload, version, stampMap);
            }
        }
    }
    
    private void aggiornaLista(){
        this.popolaAttribuzioni();
    }

    private void filtraPerUfficio(){
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
    }

    /*
 * Uno store contenente solo gli uffici dell'utente
 */
    public Store storeUfficioFiltro(){
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        List<Ufficio> uffici = new ArrayList();
        for(UfficioUtente uu: autenticato.getUfficioUtenteCollection()){
            if( uu.getRicerca() ){
                uffici.add(uu.getUfficio());
            }
        }
        return new Store(uffici);
    }
    private void cercaDaEtichetta() {
        String barcode = QInputDialog.getText(this, "Ricerca da etichetta", "Etichetta");
        if( barcode == null ){
            return;
        }
        barcode = barcode.trim();
        if( barcode.length() < 5 ){
            QMessageBox.warning(this, "Attenzione", "Numero di protocollo troppo breve");
            return;
        }
        String year = barcode.substring(0, 4);
        Integer n = Integer.parseInt(barcode.substring(4));
        barcode = year + String.format("%08d", n);

        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        List<Attribuzione> attribuzioni = em.createNamedQuery("trovaAttribuzioniUtenteProtocollo", Attribuzione.class)
                .setParameter("id", autenticato.getId()).setParameter("protocollo", barcode)
                .getResultList();
        if (attribuzioni.size() > 0) {
            attribuzioneStoreGenerale.clear();
            attribuzioneStoreGenerale.addAll(attribuzioni);
            totRecord.setText("Totale record: "+ String.valueOf(attribuzioneStoreGenerale.size()));
            selectionProtocollo.clear();
            PyPaPiComboBox ufficio = (PyPaPiComboBox) this.findChild(QComboBox.class, "comboBoxUfficio");
            ufficio.setCurrentIndex(ufficio.getLookupStore().size() - 1);

            List<Column> colonne = new ArrayList();
            QTableView tableView = (QTableView) this.findChild(QTableView.class, "attribuzioni");
            colonne.add(new Column("evidenza", "Ev.", "Attribuzione in evidenza"));
            colonne.add(new Column("iddocumento", "Protocollo", "Numero di protocollo"));
            colonne.add(new Column("tipoprotocollo", "E/U/I", "Entrata / Uscita / Interno"));
            colonne.add(new Column("dataprotocollo", "Data", "Data di protocollazione"));
            colonne.add(new Column("ufficio", "Ufficio", "Ufficio di attribuzione"));
            colonne.add(new Column("principale", "Pr.", "Attribuzione in via principale"));
            colonne.add(new Column("oggetto", "Oggetto", "Oggetto del protocollo"));
            colonne.add(new Column("letto", "Letto", "Dato per letto"));
            TableModel model = new TableModel(attribuzioneStoreGenerale, colonne);
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
            tableView.horizontalHeader().setResizeMode(4, QHeaderView.ResizeMode.Interactive); // ufficio
            tableView.horizontalHeader().setResizeMode(5, QHeaderView.ResizeMode.ResizeToContents); // principale
            tableView.horizontalHeader().setResizeMode(6, QHeaderView.ResizeMode.Stretch);          // oggetto
            tableView.horizontalHeader().setResizeMode(7, QHeaderView.ResizeMode.ResizeToContents); // letto
        } else {
            QMessageBox.warning(this, "Attenzione", "Protocollo" + barcode + " non trovato");
        }
    }

}

