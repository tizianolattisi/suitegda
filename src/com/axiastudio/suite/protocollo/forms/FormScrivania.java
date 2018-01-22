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
import com.axiastudio.suite.base.entities.*;
import com.axiastudio.suite.menjazo.AlfrescoHelper;
import com.axiastudio.suite.plugins.cmis.CmisPlugin;
import com.axiastudio.suite.protocollo.ProfiloUtenteProtocollo;
import com.axiastudio.suite.protocollo.entities.*;
import com.axiastudio.suite.richieste.entities.DestinatarioUfficio;
import com.axiastudio.suite.richieste.entities.DestinatarioUtente;
import com.axiastudio.suite.richieste.entities.IDestinatarioRichiesta;
import com.axiastudio.suite.richieste.entities.Richiesta;
import com.trolltech.qt.core.*;
import com.trolltech.qt.designer.QUiLoader;
import com.trolltech.qt.designer.QUiLoaderException;
import com.trolltech.qt.gui.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class FormScrivania  extends QMainWindow {
    private Store<Attribuzione> attribuzioneStoreGenerale = new Store<Attribuzione>(null);
    private Store<IDestinatarioRichiesta> destinatarioStoreGenerale = new Store<IDestinatarioRichiesta>(null);
    private List<Attribuzione> selectionProtocollo = new ArrayList<Attribuzione>();
    private List<IDestinatarioRichiesta> selectionRichiesta = new ArrayList<IDestinatarioRichiesta>();
    private final Integer DEFAULT_ROW_HEIGHT = 24;
    public ScrivaniaMenuBar scrivaniaMenuBar;
    private QTabWidget tabWidget;
    Timer timer = new Timer();
    AggiornaRichieste aggiornaRichieste = new AggiornaRichieste();

    public FormScrivania(){
        QFile file = Util.ui2jui(new QFile("classpath:com/axiastudio/suite/protocollo/forms/scrivania.ui"));
        this.loadUi(file);

        this.scrivaniaMenuBar = new ScrivaniaMenuBar("Scrivania", this);
        this.addToolBar(scrivaniaMenuBar);

        /* tab widget */
        tabWidget= (QTabWidget) this.findChild(QTabWidget.class, "tabScrivania");
        tabWidget.currentChanged.connect(this, "cambiaToolbar()");

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

        QComboBox comboTipo = (QComboBox) this.findChild(QComboBox.class, "comboBoxTipo");
        comboTipo.setCurrentIndex(comboTipo.count()-1);

        QCheckBox checkPEC = (QCheckBox) this.findChild(QCheckBox.class, "checkBoxPEC");
        checkPEC.setCheckState(Qt.CheckState.PartiallyChecked);

        /* table view richieste */
        QTableView tableViewRichieste = (QTableView) this.findChild(QTableView.class, "richieste");
        tableViewRichieste.setSelectionBehavior(QAbstractItemView.SelectionBehavior.SelectRows);
        //tableViewRichieste.setSortingEnabled(true); // TODO: gestire l'ordinamento sulla tabella scrivania
//        tableViewRichieste.setItemDelegate(new Delegate(tableView));
        tableViewRichieste.verticalHeader().setDefaultSectionSize(DEFAULT_ROW_HEIGHT);
        tableViewRichieste.doubleClicked.connect(this, "apriRichiesta()");

        this.popolaAttribuzioni();

 //       ((QTabWidget) this.findChild(QTabWidget.class, "tabWidget")).setTabEnabled(1, false);
        this.popolaRichieste();
//        timer.schedule(aggiornaRichieste, 2 * 60 * 1000, 1 * 60 * 1000);
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

        PyPaPiComboBox comboUfficio = (PyPaPiComboBox) this.findChild(QComboBox.class, "comboBoxUfficio");
        int idx = comboUfficio.currentIndex();
        Ufficio ufficio = (Ufficio) comboUfficio.itemData(idx);

        QComboBox comboTipo = (QComboBox) this.findChild(QComboBox.class, "comboBoxTipo");
        String tipo=comboTipo.currentText();

        QCheckBox checkPEC = (QCheckBox) this.findChild(QCheckBox.class, "checkBoxPEC");

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Attribuzione> cq = cb.createQuery(Attribuzione.class);
        Root<Attribuzione> root = cq.from(Attribuzione.class);
        Join<Attribuzione,Ufficio> itemUffico = root.join(Attribuzione_.ufficio);
        Join<Ufficio, UfficioUtente> itemUfficioUtente = itemUffico.join(Ufficio_.ufficioUtenteCollection);
        Join<Attribuzione, Protocollo> itemProtocollo = root.join(Attribuzione_.protocollo);
        Join<Protocollo, TipoRiferimentoMittente> itemTipoRiferimento = itemProtocollo.join(Protocollo_.tiporiferimentomittente, JoinType.LEFT);
        cq=cq.select(root);

        List<Predicate> predicates = new ArrayList();
        predicates.add(cb.isFalse(root.get(com.axiastudio.suite.protocollo.entities.Attribuzione_.letto)));
        predicates.add(cb.isTrue(itemUfficioUtente.get(UfficioUtente_.ricerca)));
        predicates.add(cb.equal(itemUfficioUtente.get(UfficioUtente_.utente), autenticato));
        if (ufficio != null) {
            predicates.add(cb.equal(root.get(Attribuzione_.ufficio), ufficio));
        }
        if ("ENTRATA".equals(tipo)) {
            predicates.add(cb.equal(itemProtocollo.get(Protocollo_.tipo), TipoProtocollo.ENTRATA));
        } else if ("USCITA".equals(tipo)) {
            predicates.add(cb.equal(itemProtocollo.get(Protocollo_.tipo), TipoProtocollo.USCITA));
        } else if ("INTERNO".equals(tipo)) {
            predicates.add(cb.equal(itemProtocollo.get(Protocollo_.tipo), TipoProtocollo.INTERNO));
        }
        if ( checkPEC.checkState() == Qt.CheckState.Checked ) {
            predicates.add(cb.equal(itemTipoRiferimento.get(TipoRiferimentoMittente_.descrizione), "PEC"));
        } else if ( checkPEC.checkState() == Qt.CheckState.Unchecked ) {
            predicates.add(cb.or(cb.notEqual(itemTipoRiferimento.get(TipoRiferimentoMittente_.descrizione), "PEC"),
                    cb.isNull(itemTipoRiferimento.get(TipoRiferimentoMittente_.descrizione))));
        }
        cq = cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        Order ord=cb.desc(cb.selectCase()
                .when(cb.greaterThan(root.get(Attribuzione_.dataprincipale), root.get(Attribuzione_.recordcreato)), root.get(Attribuzione_.dataprincipale))
                .otherwise(root.get(Attribuzione_.recordcreato)));
        cq=cq.orderBy(ord);

        TypedQuery<Attribuzione> query = em.createQuery(cq);
        List<Attribuzione> attribuzioni=query.getResultList();

        configuraTabellaAttribuzioni(attribuzioni, new ArrayList<Integer>(Arrays.asList(9,10)), new ArrayList<Integer>());
    }

    private void configuraTabellaAttribuzioni(List<Attribuzione> attribuzioni, List<Integer> hiddenCols, List<Integer> shownCols) {
        attribuzioneStoreGenerale.clear();
        attribuzioneStoreGenerale.addAll(attribuzioni);
        tabWidget.setTabText(0, "Protocolli (" + String.valueOf(attribuzioneStoreGenerale.size()) + ")");
        selectionProtocollo.clear();

        List<Column> colonne = new ArrayList();
        QTableView tableView = (QTableView) this.findChild(QTableView.class, "attribuzioni");
//        colonne.add(new Column("evidenza", "Ev.", "Attribuzione in evidenza"));
        colonne.add(new Column("convalidaprotocollo", "", ""));
        colonne.add(new Column("iddocumento", "Protocollo", "Numero di protocollo"));
        colonne.add(new Column("tipoprotocollo", "E/U/I", "Entrata / Uscita / Interno"));
        colonne.add(new Column("dataprotocollo", "Data", "Data di protocollazione"));
        colonne.add(new Column("dataassegnazione", "Dt.attrib/modifica", "Data di attribuzione del protocollo o modifica flag principale"));
        colonne.add(new Column("pec", "PEC", "PEC"));
        colonne.add(new Column("principale", "Pr.", "Attribuzione in via principale"));
        colonne.add(new Column("ufficio", "Ufficio", "Ufficio di attribuzione"));
        colonne.add(new Column("oggetto", "Oggetto", "Oggetto del protocollo"));
        colonne.add(new Column("letto", "Letto", "Dato per letto"));
        colonne.add(new Column("statoPec", "stato PEC", "stato PEC"));
        TableModel model = new TableModel(attribuzioneStoreGenerale, colonne);
        tableView.clearSelection();
        model.setEditable(false);
        tableView.setModel(model);
        QItemSelectionModel selectionModel = new QItemSelectionModel(model);
        tableView.setSelectionModel(selectionModel);
        selectionModel.selectionChanged.connect(this, "selectRows(QItemSelection, QItemSelection)");
        tableView.horizontalHeader().setSectionHidden(0, true); // convalida
        tableView.horizontalHeader().setResizeMode(1, QHeaderView.ResizeMode.Interactive); // iddocumento
        tableView.horizontalHeader().resizeSection(1, tableView.sizeHintForColumn(1)+23);
        tableView.horizontalHeader().setResizeMode(2, QHeaderView.ResizeMode.Interactive); // tipoprotocollo
        tableView.horizontalHeader().resizeSection(2, tableView.sizeHintForColumn(2)+16);
        tableView.horizontalHeader().setResizeMode(3, QHeaderView.ResizeMode.Interactive); // dataprotocollo
        tableView.horizontalHeader().resizeSection(3, tableView.sizeHintForColumn(3)+10);
        tableView.horizontalHeader().setResizeMode(4, QHeaderView.ResizeMode.Interactive); // dataassegnazione
        tableView.horizontalHeader().resizeSection(4, tableView.sizeHintForColumn(4)+10);
        tableView.horizontalHeader().setResizeMode(5, QHeaderView.ResizeMode.ResizeToContents); // PEC
        tableView.horizontalHeader().setResizeMode(6, QHeaderView.ResizeMode.ResizeToContents); // principale
        tableView.horizontalHeader().setResizeMode(7, QHeaderView.ResizeMode.Interactive); // ufficio
        tableView.horizontalHeader().setResizeMode(8, QHeaderView.ResizeMode.Stretch);          // oggetto
        tableView.horizontalHeader().setResizeMode(9, QHeaderView.ResizeMode.ResizeToContents); // letto

        for (Integer i: hiddenCols) {
            tableView.horizontalHeader().setSectionHidden(i, true);
        }
        for (Integer i: shownCols) {
            tableView.horizontalHeader().setSectionHidden(i, false);
        }
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
        Collections.sort(destinatari,  Collections.reverseOrder(DestinatarioUfficio.Comparators.DATA));
        destinatarioStoreGenerale.clear();
        destinatarioStoreGenerale.addAll(destinatari);
        tabWidget.setTabText(1, "Messaggi (" + String.valueOf(destinatarioStoreGenerale.size()) + ")");
        selectionRichiesta.clear();

        List<Column> colonne = new ArrayList();
        QTableView tableView = (QTableView) this.findChild(QTableView.class, "richieste");
        colonne.add(new Column("mittente", "Mittente", "Mittente della richiesta"));
        colonne.add(new Column("data", "Data invio", "Data della richiesta"));
        colonne.add(new Column("datascadenza", "Data scadenza", "Data di scadenza della richiesta"));
        colonne.add(new Column("nomedestinatario", "Inviato a", "Richiesta inviata a..."));
        colonne.add(new Column("testo", "Testo", "Testo della richiesta"));
        colonne.add(new Column("conoscenza", "cc", "Per conoscenza"));
        colonne.add(new Column("letto", "Letto", "Richiesta letta/evasa/conclusa"));
//        colonne.add(new Column("statorichiesta", "Stato", "Stato richiesta"));
        TableModel model = new TableModel(destinatarioStoreGenerale, colonne);
        tableView.clearSelection();
        model.setEditable(false);
        tableView.setModel(model);
        QItemSelectionModel selectionModel = new QItemSelectionModel(model);
        tableView.setSelectionModel(selectionModel);
        selectionModel.selectionChanged.connect(this, "selectRowsRichieste(QItemSelection, QItemSelection)");
        tableView.horizontalHeader().setResizeMode(0, QHeaderView.ResizeMode.ResizeToContents); // mitt
        tableView.horizontalHeader().setResizeMode(1, QHeaderView.ResizeMode.ResizeToContents); // data
        tableView.horizontalHeader().setResizeMode(2, QHeaderView.ResizeMode.ResizeToContents); // datascadenza
        tableView.horizontalHeader().setResizeMode(3, QHeaderView.ResizeMode.ResizeToContents); // dest
        tableView.horizontalHeader().setResizeMode(4, QHeaderView.ResizeMode.Stretch); // testo
        tableView.horizontalHeader().setResizeMode(5, QHeaderView.ResizeMode.ResizeToContents); // CC
        tableView.horizontalHeader().setResizeMode(6, QHeaderView.ResizeMode.ResizeToContents); // letto
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
            boolean res = this.selectionRichiesta.add((IDestinatarioRichiesta) model.getEntityByRow(idx));
        }
        for (Integer idx: deselectedIndexes){
            boolean res = this.selectionRichiesta.remove(model.getEntityByRow(idx));
        }

        refreshInfoRichieste();
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
        if (deselected != null) {
            for (QModelIndex i: deselected.indexes()){
                if(!deselectedIndexes.contains(i.row())){
                    deselectedIndexes.add(i.row());
                }
            }
        }
        for (Integer idx: selectedIndexes){
            boolean res = this.selectionProtocollo.add((Attribuzione) model.getEntityByRow(idx));
        }
        for (Integer idx: deselectedIndexes){
            boolean res = this.selectionProtocollo.remove(model.getEntityByRow(idx));
        }

        refreshInfo();
    }

    private void refreshInfo() {
        this.scrivaniaMenuBar.actionByName("daiPerLetto").setEnabled(this.selectionProtocollo.size()>0);
        this.scrivaniaMenuBar.actionByName("apriProtocollo").setEnabled(this.selectionProtocollo.size()==1);
        this.scrivaniaMenuBar.actionByName("apriDocumenti").setEnabled(this.selectionProtocollo.size()==1);

        // oggetto, uffici, soggetti
        QTextEdit textEdit_oggetto = (QTextEdit) this.findChild(QTextEdit.class, "textEdit_oggetto");
        QGroupBox groupBox = (QGroupBox) this.findChild(QGroupBox.class, "groupBox");
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
            groupBox.setTitle("Anteprima - " + n + " documento/i");
        } else {
            textEdit_oggetto.setText("");
            groupBox.setTitle("Anteprima");
        }
    }

    private void refreshInfoRichieste() {
        this.scrivaniaMenuBar.actionByName("daiPerLettoRichieste").setEnabled(this.selectionRichiesta.size()>0);
        this.scrivaniaMenuBar.actionByName("apriRichiesta").setEnabled(this.selectionRichiesta.size()==1);
        this.scrivaniaMenuBar.actionByName("apriDocumentiRichiesta").setEnabled(this.selectionRichiesta.size()==1);

        // oggetto, destinatari
        QTextEdit textEdit_oggetto = (QTextEdit) this.findChild(QTextEdit.class, "textEdit_oggettoRichiesta");
        QListWidget listWidget_destinatari = (QListWidget) this.findChild(QListWidget.class, "listWidget_destinatari");
        QGroupBox groupBox = (QGroupBox) this.findChild(QGroupBox.class, "groupBoxRichiesta");
        listWidget_destinatari.clear();
        if( this.selectionRichiesta.size() == 1 ){
            IDestinatarioRichiesta destinatario = this.selectionRichiesta.get(0);
            Richiesta richiesta = destinatario.getRichiesta();
            textEdit_oggetto.setText(richiesta.getTesto());
            for( IDestinatarioRichiesta dest: richiesta.getDestinatarioUfficioCollection() ){
                QListWidgetItem item = new QListWidgetItem();
                item.setText(dest.getNomedestinatario());
                listWidget_destinatari.addItem(item);
            }
            // numero di documenti contenuti nella cartella Alfresco
            CmisPlugin cmisPlugin = (CmisPlugin) Register.queryPlugin(Richiesta.class, "CMIS");
            AlfrescoHelper alfrescoHelper = cmisPlugin.createAlfrescoHelper(richiesta);
            Long n = alfrescoHelper.numberOfDocument();
            groupBox.setTitle("Anteprima - " + n + " documento/i");
        } else {
            textEdit_oggetto.setText("");
            groupBox.setTitle("Anteprima");
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
                attribuzione=(Attribuzione) controller.refresh(attribuzione);
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
        tabWidget.setTabText(0, "Protocolli (" + String.valueOf(attribuzioneStoreGenerale.size()) + ")");

        this.selectionProtocollo.clear();
        this.refreshInfo();
    }

    private void daiPerLettoRichieste(){
        Database db = (Database) Register.queryUtility(IDatabase.class);
        Controller controller = db.createController(IDestinatarioRichiesta.class);
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        List<Ufficio> ufficiDaiPerLetto = new ArrayList();
        for(UfficioUtente uu: autenticato.getUfficioUtenteCollection()){
            if( uu.getDaiperletto() ){
                ufficiDaiPerLetto.add(uu.getUfficio());
            }
        }
        List<IDestinatarioRichiesta> richiesteLette = new ArrayList();
        for(IDestinatarioRichiesta destRichiesta: this.selectionRichiesta){
            destRichiesta.setLetto(Boolean.TRUE);
            destRichiesta.setDataletto(Calendar.getInstance().getTime());
            destRichiesta.setEsecutoreletto(autenticato.getLogin());
            controller.commit(destRichiesta);
            richiesteLette.add(destRichiesta);
        }
        destinatarioStoreGenerale.removeAll(richiesteLette);
        QTableView tableView = (QTableView) this.findChild(QTableView.class, "richieste");
        TableModel model = (TableModel) tableView.model();
        Store store=model.getStore();
        store.removeAll(richiesteLette);
        model.setStore(store);
        tabWidget.setTabText(1, "Messaggi (" + String.valueOf(destinatarioStoreGenerale.size()) + ")");

        this.selectionRichiesta.clear();
        this.refreshInfoRichieste();
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
                    if ( protocollo.getTiporiferimentomittente()!=null && "PEC".equals(protocollo.getTiporiferimentomittente().getDescrizione()) &&
                            (protocollo.getPecProtocollo()==null || protocollo.getPecProtocollo().getStato()==null ||
                                    protocollo.getPecProtocollo().getStato().compareTo(StatoPec.DAINVIARE)>0)) {
                        version = false;
                    } else {
                        version = pup.inAttribuzionePrincipaleC();
                    }
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

    private void apriDocumentiRichiesta(){
        Richiesta richiesta = this.selectionRichiesta.get(0).getRichiesta();
        List<IPlugin> plugins = (List) Register.queryPlugins(Richiesta.class);
        for(IPlugin plugin: plugins) {
            if ("CMIS".equals(plugin.getName())) {
                Boolean view = false;
                Boolean delete = false;
                Boolean download = false;
                Boolean parent = false;
                Boolean upload = false;
                Boolean version = false;
                AlfrescoHelper alfrescoHelper = ((CmisPlugin) plugin).createAlfrescoHelper(richiesta);
                if ( alfrescoHelper.numberOfDocument()==0 ) {
                    QMessageBox.information(this, "Attenzione", "Nessun documento collegato al messaggio/richiesta.");
                    return;
                }
                Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
                for ( DestinatarioUtente du: richiesta.getDestinatarioUtenteCollection() ) {
                    if ( autenticato.equals(du.getDestinatario()) ) {
                        view = download = true;
                        break;
                    }
                }
                if ( !view ) {
                    for ( DestinatarioUfficio du: richiesta.getDestinatarioUfficioCollection() ) {
                        for (UfficioUtente uu: du.getDestinatario().getUfficioUtenteCollection()) {
                            if ( autenticato.equals(uu.getUtente()) && !uu.getOspite() && uu.getVisualizza() ) {
                                view = download = true;
                                break;
                            }
                        }
                    }
                }
                if (view) {
                    ((CmisPlugin) plugin).showForm(richiesta, delete, download, parent, upload, version, null);
                } else {
                    QMessageBox.warning(this, "Attenzione", "Non disponi dei permessi per visualizzare i documenti");
                    return;
                }
            }
        }
    }

    private void aggiornaLista(){
        this.popolaAttribuzioni();
    }

    private void aggiornaListaRichieste(){
        this.popolaRichieste();
    }

    private void nuovaRichiesta() {
        IForm win = Util.formFromEntity(new Richiesta());
        QMdiArea workspace = Util.findParentMdiArea(this);
        if( workspace != null ){
            workspace.addSubWindow((QMainWindow) win);
        }
        win.show();
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
        tabWidget.setTabText(0, "Protocolli (" + String.valueOf(attribuzioneStoreGenerale.size()) + ")");
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
            configuraTabellaAttribuzioni(attribuzioni, new ArrayList<Integer>(Collections.singletonList(10)),
                    new ArrayList<Integer>(Collections.singletonList(9)));
        } else {
            QMessageBox.warning(this, "Attenzione", "Protocollo" + barcode + " non trovato");
        }
    }

    private void cambiaToolbar() {
        if ( tabWidget.currentIndex() > -1 ) {
            if ( tabWidget.currentIndex() == 0 ) {
                for (String strAction:scrivaniaMenuBar.richiestaMenu) {
                    scrivaniaMenuBar.actionByName(strAction).setVisible(false);
                }
                for (String strAction:scrivaniaMenuBar.protocolloMenu) {
                    scrivaniaMenuBar.actionByName(strAction).setVisible(true);
                }
            } else if ( tabWidget.currentIndex() == 1 ) {
                for (String strAction:scrivaniaMenuBar.protocolloMenu) {
                    scrivaniaMenuBar.actionByName(strAction).setVisible(false);
                }
                for (String strAction:scrivaniaMenuBar.richiestaMenu) {
                    scrivaniaMenuBar.actionByName(strAction).setVisible(true);
                }
            }
        }
    }

    private void info() {
        QDialog dialog = null;
        QFile file = Util.ui2jui(new QFile("classpath:com/axiastudio/suite/protocollo/forms/legendascrivania.ui"));
        try {
            dialog = (QDialog) QUiLoader.load(file);
        } catch (QUiLoaderException ex) {
            Logger.getLogger(Dialog.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        dialog.exec();
    }

    class AggiornaRichieste extends TimerTask {
        public void run() {
            System.out.print("timer");
            popolaRichieste();
        }
    }
}

