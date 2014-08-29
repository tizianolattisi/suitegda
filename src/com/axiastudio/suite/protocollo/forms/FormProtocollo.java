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

import com.axiastudio.suite.menjazo.AlfrescoHelper;
import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.*;
import com.axiastudio.pypapi.plugins.IPlugin;
import com.axiastudio.pypapi.ui.*;
import com.axiastudio.pypapi.ui.widgets.PyPaPiComboBox;
import com.axiastudio.pypapi.ui.widgets.PyPaPiTableView;
import com.axiastudio.suite.SuiteUiUtil;
import com.axiastudio.suite.SuiteUtil;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.base.entities.UfficioUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.generale.forms.DialogStampaEtichetta;
import com.axiastudio.suite.interoperabilita.entities.Segnatura;
import com.axiastudio.suite.interoperabilita.utilities.JAXBHelper;
import com.axiastudio.suite.plugins.cmis.CmisPlugin;
import com.axiastudio.suite.procedimenti.GestoreDeleghe;
import com.axiastudio.suite.procedimenti.entities.Carica;
import com.axiastudio.suite.procedimenti.entities.CodiceCarica;
import com.axiastudio.suite.procedimenti.entities.Delega;
import com.axiastudio.suite.protocollo.ProfiloUtenteProtocollo;
import com.axiastudio.suite.protocollo.entities.*;
import com.axiastudio.suite.pubblicazioni.PubblicazioneUtil;
import com.axiastudio.suite.pubblicazioni.entities.Pubblicazione;
import com.axiastudio.suite.scanndo.ScanNDo;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.gui.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class FormProtocollo extends Window {
    /**
     *
     */
    public ProtocolloMenuBar protocolloMenuBar;
    private QTabWidget tabWidget;
    
    
    public FormProtocollo(FormProtocollo form){
        super(form.uiFile, form.entityClass, form.title);
        this.protocolloMenuBar = null;
    }
    
    public FormProtocollo(String uiFile, Class entityClass, String title){
        super(uiFile, entityClass, title);
        this.protocolloMenuBar = new ProtocolloMenuBar("Protocollo", this);
        this.addToolBar(protocolloMenuBar);
        QLabel labelSpedito = (QLabel) this.findChild(QLabel.class, "labelSpedito");
        labelSpedito.setPixmap(new QPixmap("classpath:com/axiastudio/suite/resources/email_go.png"));
        QLabel labelConvalidaProtocollo = (QLabel) this.findChild(QLabel.class, "labelConvalidaProtocollo");
        labelConvalidaProtocollo.setPixmap(new QPixmap("classpath:com/axiastudio/suite/resources/lock_mail.png"));
        QLabel labelConvalidaAttribuzioni = (QLabel) this.findChild(QLabel.class, "labelConvalidaAttribuzioni");
        labelConvalidaAttribuzioni.setPixmap(new QPixmap("classpath:com/axiastudio/suite/resources/lock_group.png"));
        QLabel labelConsolidaDocumenti = (QLabel) this.findChild(QLabel.class, "labelConsolidaDocumenti");
        labelConsolidaDocumenti.setPixmap(new QPixmap("classpath:com/axiastudio/suite/resources/lock_folder.png"));
        
        try {
            Method storeFactory = this.getClass().getMethod("storeSportello");
            Register.registerUtility(storeFactory, IStoreFactory.class, "Sportello");
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(FormProtocollo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(FormProtocollo.class.getName()).log(Level.SEVERE, null, ex);
        }

        ((QComboBox) this.findChild(QComboBox.class, "comboBox_tipo")).currentIndexChanged.connect(this, "aggiornaTipo()");
        
        /* fascicolazione */
        QToolButton toolButtonTitolario = (QToolButton) this.findChild(QToolButton.class, "toolButtonTitolario");
        toolButtonTitolario.setIcon(new QIcon("classpath:com/axiastudio/suite/resources/email_go.png"));
        toolButtonTitolario.clicked.connect(this, "apriTitolario()");
        
        /* I riferimenti successivi sono sempre in sola lettura */
        PyPaPiTableView tableViewRiferimentiSuccessivi = (PyPaPiTableView) this.findChild(PyPaPiTableView.class, "tableView_riferimentisuccessivi");
        Util.setWidgetReadOnly(tableViewRiferimentiSuccessivi, true);

        /* Gestione attribuzione principale e pratica in originale */
        PyPaPiTableView tableViewAttribuzioni = (PyPaPiTableView) this.findChild(PyPaPiTableView.class, "tableView_attribuzioni");
        tableViewAttribuzioni.entityInserted.connect(this, "attribuzioneInserita(Object)");
        tableViewAttribuzioni.entityRemoved.connect(this, "attribuzioneRimossa(Object)");
        PyPaPiTableView tableViewPratica = (PyPaPiTableView) this.findChild(PyPaPiTableView.class, "tableView_pratiche");
        tableViewPratica.entityInserted.connect(this, "praticaInserita(Object)");
        tableViewPratica.entityRemoved.connect(this, "praticaRimossa(Object)");
        tableViewPratica.entityUpdated.connect(this, "praticaAggiornata(Object)");
        
        /* Gestione annullamenti protocollo */
        PyPaPiTableView tableViewAnnullamento = (PyPaPiTableView) this.findChild(PyPaPiTableView.class, "tableView_annullamenti");
        tableViewAnnullamento.entityRemoved.connect(this, "annullamentoRimosso(Object)");
    }

    /*
     * La prima attribuzione diventa in via principale, e non può più essere rimossa
     */
    private void attribuzioneInserita(Object obj){
        Protocollo protocollo = (Protocollo) this.getContext().getCurrentEntity();
        Attribuzione inserita = (Attribuzione) obj;
        if( protocollo.getAttribuzioneCollection().size() == 1 ){
            inserita.setPrincipale(Boolean.TRUE);
        }
    }
    private void attribuzioneRimossa(Object obj){
        Protocollo protocollo = (Protocollo) this.getContext().getCurrentEntity();
        Attribuzione rimossa = (Attribuzione) obj;
        if( rimossa.getPrincipale() ){
            QMessageBox.warning(this, "Attenzione", "L'attribuzione principale non può venir rimossa.");
            PyPaPiTableView tableViewAttribuzione = (PyPaPiTableView) this.findChild(PyPaPiTableView.class, "tableView_attribuzioni");
            ((ITableModel) tableViewAttribuzione.model()).getContextHandle().insertElement(rimossa);
        }
    }
    
    /*
     * Solo il richiedente può annullare la sua richiesta di annullamento
     */
    private void annullamentoRimosso(Object obj){
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        AnnullamentoProtocollo annullamento = (AnnullamentoProtocollo) obj;
        if( !autenticato.getLogin().equals(annullamento.getEsecutorerichiesta()) ){
            QMessageBox.warning(this, "Attenzione", "Solo il richiedente può annullare la sua richiesta di annullamento.");
            PyPaPiTableView tableViewAttribuzione = (PyPaPiTableView) this.findChild(PyPaPiTableView.class, "tableView_attribuzioni");
            ((ITableModel) tableViewAttribuzione.model()).getContextHandle().insertElement(annullamento);
        }
    }

    /*
     * La prima pratica contiene il protocollo in originale e non può essere rimossa
     */
    private void praticaInserita(Object obj){
        Protocollo protocollo = (Protocollo) this.getContext().getCurrentEntity();
        PraticaProtocollo praticaProtocollo = (PraticaProtocollo) obj;
        if( protocollo.getPraticaProtocolloCollection().size() == 1 ){
            praticaProtocollo.setOriginale(Boolean.TRUE);
        }
    }
    private void praticaRimossa(Object obj){
        Protocollo protocollo = (Protocollo) this.getContext().getCurrentEntity();
        PraticaProtocollo rimossa = (PraticaProtocollo) obj;
        if( protocollo.getPraticaProtocolloCollection().size() == 0 || rimossa.getOriginale()){
            QMessageBox.warning(this, "Attenzione", "Il protocollo non può essere rimosso dalla pratica che lo contiene in originale.");
            PyPaPiTableView tableViewPratica = (PyPaPiTableView) this.findChild(PyPaPiTableView.class, "tableView_pratiche");
            rimossa.setOriginale(Boolean.TRUE);
            ((ITableModel) tableViewPratica.model()).getContextHandle().insertElement(rimossa);
        }
    }
    private void praticaAggiornata(Object obj){
        Protocollo protocollo = (Protocollo) this.getContext().getCurrentEntity();
        PraticaProtocollo aggiornata = (PraticaProtocollo) obj;
        System.out.println(aggiornata);
    }
    
    /*
     * Uno store contenente solo gli uffici dell'utente
     */
    public Store storeSportello(){
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        List<Ufficio> uffici = new ArrayList();
        for(UfficioUtente uu: autenticato.getUfficioUtenteCollection()){
            if( uu.getUfficio().getSportello() ){
                uffici.add(uu.getUfficio());
            }
        }
        return new Store(uffici);
    }

    private void convalidaAttribuzioni() {
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManagerFactory emf = db.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        Carica carica = GestoreDeleghe.findCarica(CodiceCarica.RESPONSABILE_ATTRIBUZIONI);
        List<Delega> deleghe = em.createNamedQuery("trovaIncaricatiODelegati", Delega.class)
                .setParameter("carica", carica)
                .getResultList();
        Protocollo protocollo = (Protocollo) this.getContext().getCurrentEntity();
        protocollo.setConvalidaattribuzioni(Boolean.TRUE);
        List<String> items = new ArrayList();
        items.add("nessuno");
        for( Delega delega: deleghe ){
            items.add(delega.getUtente().getNome());
        }
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        Integer def;
        if( items.size()>1 && autenticato.getAttributoreprotocollo() ){
            def = 1;
        } else {
            def = 0;
        }
        String choice = QInputDialog.getItem(this,
                "Verificatore delle attribuzioni",
                "Dichiara il verificatore delle attribuzioni",
                items,
                def,
                false);
        Integer idx = items.lastIndexOf(choice);
        if( idx > 0 ){
            Delega verificatore = deleghe.get(idx-1);
            protocollo.setControlloreposta(verificatore.getUtente().getLogin());
        }
        this.getContext().getDirty();
    }

    private void convalidaProtocollo() {
        Protocollo protocollo = (Protocollo) this.getContext().getCurrentEntity();
        protocollo.setConvalidaattribuzioni(Boolean.TRUE);
        protocollo.setConvalidaprotocollo(Boolean.TRUE);
        this.getContext().getDirty();
    }

    private void consolidaDocumenti() {
        Protocollo protocollo = (Protocollo) this.getContext().getCurrentEntity();
        protocollo.setConsolidadocumenti(Boolean.TRUE);
        this.getContext().getDirty();
    }
    
    private void apriTitolario() {
        FormTitolario titolario = new FormTitolario();
        int exec = titolario.exec();
        if( exec == 1 ){
            Fascicolo selection = titolario.getSelection();
            PyPaPiComboBox comboBoxTitolario = (PyPaPiComboBox) this.findChild(PyPaPiComboBox.class, "comboBoxTitolario");
            comboBoxTitolario.select(selection);
            this.getContext().getDirty();
        }
    }
    
    private void impostaAttribuzionePrincipale(){
        PyPaPiTableView tv = (PyPaPiTableView) this.findChild(PyPaPiTableView.class, "tableView_attribuzioni");
        List<QModelIndex> rows = tv.selectionModel().selectedRows();
        if( rows.isEmpty()){
            return;
        }
        Protocollo protocollo = (Protocollo) this.getContext().getCurrentEntity();
        for( Attribuzione attribuzione: protocollo.getAttribuzioneCollection() ){
            attribuzione.setPrincipale(Boolean.FALSE);
        }
        for (QModelIndex idx: rows){
            Attribuzione attribuzione = (Attribuzione) ((ITableModel) tv.model()).getEntityByRow(idx.row());
            attribuzione.setPrincipale(Boolean.TRUE);
        }
        this.getContext().getDirty();
    }
    
    @Override
    protected void indexChanged(int row) {
        super.indexChanged(row);
        Protocollo protocollo = (Protocollo) this.getContext().getCurrentEntity();
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        ProfiloUtenteProtocollo profilo = new ProfiloUtenteProtocollo(protocollo, autenticato);
        Boolean nuovoInserimento = protocollo.getId() == null;
        Boolean convAttribuzioni = protocollo.getConvalidaattribuzioni();
        Boolean convProtocollo = protocollo.getConvalidaprotocollo();
        Boolean consDocumenti = protocollo.getConsolidadocumenti();

        // colore identificativo x entrata/uscita/interno
        if ( protocollo.getTipo().equals(TipoProtocollo.ENTRATA) ) {
            ((QLineEdit) this.findChild(QLineEdit.class, "lineEdit_iddocumento")).setStyleSheet("QLineEdit{background: rgb(255, 255, 127);}");
        } else if ( protocollo.getTipo().equals(TipoProtocollo.INTERNO) ) {
            ((QLineEdit) this.findChild(QLineEdit.class, "lineEdit_iddocumento")).setStyleSheet("QLineEdit{background: rgb(170, 255, 127);}");
        } else if ( protocollo.getTipo().equals(TipoProtocollo.USCITA) ) {
            ((QLineEdit) this.findChild(QLineEdit.class, "lineEdit_iddocumento")).setStyleSheet("QLineEdit{background: rgb(170, 255, 255);}");
        }

        // abilitazione azioni: convalida, consolida e spedizione
        this.protocolloMenuBar.actionByName("convalidaAttribuzioni").setEnabled(!convAttribuzioni);
        this.protocolloMenuBar.actionByName("convalidaProtocollo").setEnabled(!convProtocollo);
        this.protocolloMenuBar.actionByName("consolidaDocumenti").setEnabled(!consDocumenti && profilo.inAttribuzionePrincipaleC());
        Util.setWidgetReadOnly((QWidget) this.findChild(QCheckBox.class, "spedito"), protocollo.getSpedito());
        this.protocolloMenuBar.actionByName("pubblicaProtocollo").setEnabled(autenticato.getPubblicaalbo());
        this.protocolloMenuBar.actionByName("stampaEtichetta").setEnabled(!nuovoInserimento);

        // convalida attribuzioni
        PyPaPiTableView tableViewAttribuzioni = (PyPaPiTableView) this.findChild(PyPaPiTableView.class, "tableView_attribuzioni");
        Boolean modificaAttribuzioni = nuovoInserimento || autenticato.getAttributoreprotocollo() || (!protocollo.getConvalidaattribuzioni() && profilo.inSportelloOAttribuzionePrincipale());
        Util.setWidgetReadOnly(tableViewAttribuzioni, !modificaAttribuzioni);

        // sempre read-only
        Util.setWidgetReadOnly((QWidget) this.findChild(QDateEdit.class, "dateEdit_data"), true);
        Util.setWidgetReadOnly((QWidget) this.findChild(QLineEdit.class, "lineEdit_iddocumento"), true);
        Util.setWidgetReadOnly((QWidget) this.findChild(QCheckBox.class, "annullato"), true);
        Util.setWidgetReadOnly((QWidget) this.findChild(QCheckBox.class, "annullamentorichiesto"), true);

        // solo primo inserimento
        Util.setWidgetReadOnly((QWidget) this.findChild(QComboBox.class, "comboBox_sportello"), !nuovoInserimento);
        Util.setWidgetReadOnly((QWidget) this.findChild(QComboBox.class, "comboBox_tipo"), !nuovoInserimento);
        aggiornaLabelSoggetti(protocollo);


        // sportello
        QComboBox comboBox_sportello = (QComboBox) this.findChild(QComboBox.class, "comboBox_sportello");
        QLineEdit lineEdit_sportello = (QLineEdit) this.findChild(QLineEdit.class, "lineEdit_sportello");
        if( protocollo.getId() == null ){
            lineEdit_sportello.setText("");
            lineEdit_sportello.hide();
            comboBox_sportello.show();
        } else {
            lineEdit_sportello.setText(protocollo.getSportello().toString());
            comboBox_sportello.hide();
            lineEdit_sportello.show();
        }

        // etichette convalida e spedizione
        QLabel labelSpedizione = (QLabel) this.findChild(QLabel.class, "label_spedizione");
        if( protocollo.getSpedito() && protocollo.getDataspedizione()!=null){
            labelSpedizione.setText(SuiteUtil.DATETIME_FORMAT.format(protocollo.getDataspedizione()) + " " + protocollo.getEsecutorespedizione());
        } else {
            labelSpedizione.setText("-");
        }
        QLabel labelConvalidau = (QLabel) this.findChild(QLabel.class, "label_convalidau");
        if( protocollo.getConvalidaattribuzioni() && protocollo.getDataconvalidaattribuzioni()!=null){
            labelConvalidau.setText(SuiteUtil.DATETIME_FORMAT.format(protocollo.getDataconvalidaattribuzioni()) + " " + protocollo.getEsecutoreconvalidaattribuzioni());
        } else {
            labelConvalidau.setText("-");
        }
        QLabel labelConvalida = (QLabel) this.findChild(QLabel.class, "label_convalida");
        if( protocollo.getConvalidaprotocollo() && protocollo.getDataconvalidaprotocollo()!=null){
            labelConvalida.setText(SuiteUtil.DATETIME_FORMAT.format(protocollo.getDataconvalidaprotocollo()) + " " + protocollo.getEsecutoreconvalidaprotocollo());
        } else {
            labelConvalida.setText("-");
        }
        QLabel labelConsolida = (QLabel) this.findChild(QLabel.class, "label_consolida");
        if( protocollo.getConsolidadocumenti() && protocollo.getDataconsolidadocumenti()!=null){
            labelConsolida.setText(SuiteUtil.DATETIME_FORMAT.format(protocollo.getDataconsolidadocumenti()) + " " + protocollo.getEsecutoreconsolidadocumenti());
        } else {
            labelConsolida.setText("-");
        }

        // evidenza protocollo annullato
        if( protocollo.getAnnullato() ){
            this.setStyleSheet("color: red;");
        } else {
            this.setStyleSheet("");
        }

        // protocollo convalidato: disabilitazione di tutto tranne oggetto e pratiche
        String[] roWidgets = {"textEdit_oggetto", "tableView_soggettiprotocollo",
                "tableView_soggettiriservatiprotocollo", "tableView_ufficiprotocollo",
                "comboBoxTitolario", "comboBox_tiporiferimentomittente", "lineEdit_nrriferimentomittente",
                "dateEdit_datariferimentomittente", "richiederisposta", "riservato",
                "corrispostoostornato"};
        for( String widgetName: roWidgets ){
            Util.setWidgetReadOnly((QWidget) this.findChild(QWidget.class, widgetName), protocollo.getConvalidaprotocollo());
        }
        ((QToolButton) this.findChild(QToolButton.class, "toolButtonTitolario")).setEnabled(!protocollo.getConvalidaprotocollo());

        // protocollo annullato: non possibile inserire o eliminare richieste di annullamento
        Util.setWidgetReadOnly((QWidget) this.findChild(QWidget.class, "tableView_annullamenti"), protocollo.getAnnullato());

        // Visibilità dei soggetti riservati
        PyPaPiTableView tvSoggettiRiservati =  (PyPaPiTableView) this.findChild(PyPaPiTableView.class, "tableView_soggettiriservatiprotocollo");
        if( !(nuovoInserimento || profilo.inSportelloOAttribuzioneR() || autenticato.getSupervisoreprotocollo()) ){
            tvSoggettiRiservati.hide();
        } else {
            tvSoggettiRiservati.show();
        }
        QTabWidget tabWidgetSoggettiProtocollo = (QTabWidget) this.findChild(QTabWidget.class, "tabWidget_sinistra");
        tabWidgetSoggettiProtocollo.setTabEnabled(1, protocollo.getRiservato());

        // Indicazione num riferimenti precedenti/successivi su label della tab
        QTabWidget tabWidgetRiferimentiProtocollo = (QTabWidget) this.findChild(QTabWidget.class, "tabWidget");
        tabWidgetRiferimentiProtocollo.setTabText(1, "Riferimenti precedenti (" + protocollo.getRiferimentoProtocolloCollection().size() +
                ") e successivi (" + protocollo.getRiferimentoProtocolloSuccessivoCollection().size() + ")");

    }

    private void aggiornaTipo() {
        aggiornaLabelSoggetti();
        QComboBox cmbTipo = ((QComboBox) this.findChild(QComboBox.class, "comboBox_tipo"));
        // colore identificativo x entrata/uscita/interno
        TipoProtocollo tipoProtocollo = TipoProtocollo.values()[cmbTipo.currentIndex()];
        if ( tipoProtocollo.equals(TipoProtocollo.ENTRATA) ) {
            ((QLineEdit) this.findChild(QLineEdit.class, "lineEdit_iddocumento")).setStyleSheet("QLineEdit{background: rgb(255, 255, 127);}");
        } else if ( tipoProtocollo.equals(TipoProtocollo.INTERNO) ) {
            ((QLineEdit) this.findChild(QLineEdit.class, "lineEdit_iddocumento")).setStyleSheet("QLineEdit{background: rgb(170, 255, 127);}");
        } else if ( tipoProtocollo.equals(TipoProtocollo.USCITA) ) {
            ((QLineEdit) this.findChild(QLineEdit.class, "lineEdit_iddocumento")).setStyleSheet("QLineEdit{background: rgb(170, 255, 255);}");
        }
    }

    private void aggiornaLabelSoggetti() {
        aggiornaLabelSoggetti((Protocollo) this.getContext().getCurrentEntity());
    }

    private void aggiornaLabelSoggetti(Protocollo protocollo) {
        // alternanza mittenti-destinatari
        String labelSinistra;
        String labelDestra;
        int nrRiservati = 0;
        TipoProtocollo tipoProtocollo = TipoProtocollo.ENTRATA;
        if ( protocollo == null || protocollo.getId() == null ) {
            QComboBox cmbTipo = ((QComboBox) this.findChild(QComboBox.class, "comboBox_tipo"));
            tipoProtocollo = TipoProtocollo.values()[cmbTipo.currentIndex()];
        } else {
            nrRiservati = protocollo.getSoggettoRiservatoProtocolloCollection().size();
            tipoProtocollo = protocollo.getTipo();
        }

        if( tipoProtocollo.equals(TipoProtocollo.ENTRATA) ){
            labelSinistra = "Mittenti";
            labelDestra = "Destinatari";
        } else {
            labelDestra = "Mittenti";
            labelSinistra = "Destinatari";
        }
        ((QLabel) this.findChild(QLabel.class, "label_destra")).setText(labelDestra);
        QTabWidget tabWidgetSoggettiProtocollo = (QTabWidget) this.findChild(QTabWidget.class, "tabWidget_sinistra");
        tabWidgetSoggettiProtocollo.setTabText(0, labelSinistra);
        tabWidgetSoggettiProtocollo.setTabText(1, labelSinistra+" riservati (" + nrRiservati +")");
    }

    private void information() {
        String extra = "";
        Protocollo protocollo = (Protocollo) this.getContext().getCurrentEntity();
        String controllorePosta = protocollo.getControlloreposta();
        if( controllorePosta != null ){
            extra += "<br/><br/>Verificatore attribuzioni: " + protocollo.getControlloreposta();
        }
        SuiteUiUtil.showInfo(this, extra);
    }
    
    // XXX: codice simile a FormScrivania
    private void apriDocumenti(){
        Protocollo protocollo = (Protocollo) this.getContext().getCurrentEntity();
        if( protocollo == null || protocollo.getId() == null ){
            return;
        }
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        ProfiloUtenteProtocollo pup = new ProfiloUtenteProtocollo(protocollo, autenticato);
        List<IPlugin> plugins = (List) Register.queryPlugins(this.getClass());
        for(IPlugin plugin: plugins){
            if( "CMIS".equals(plugin.getName()) ){
                Boolean view = false;
                Boolean delete = false;
                Boolean download = false;
                Boolean parent = false;
                Boolean upload = false;
                Boolean version = false;
                // gli utenti 'supervisore protocollo' possono vedere tutti i documenti; gli utenti 'ricercatore protocollo'
                //    solo i documenti non riservati
                if( protocollo.getRiservato() ){
                    view = (pup.inSportelloOAttribuzioneV() && pup.inSportelloOAttribuzioneR()) || autenticato.getSupervisoreprotocollo();
                    download = view;
                } else {
                    view = autenticato.getSupervisoreprotocollo() || autenticato.getRicercatoreprotocollo() ||
                            pup.inSportelloOAttribuzioneV();
                    download = view;
                }
                if( protocollo.getConsolidadocumenti() ){
                    delete = false;
                    version = pup.inAttribuzionePrincipaleC();
                    upload = version && autenticato.getNuovodocsuconsolidato();
                } else {
                    upload = pup.inSportelloOAttribuzionePrincipale();
                    delete = upload;
                    version = upload;
                }
                if( view ){
                    ((CmisPlugin) plugin).showForm(protocollo, delete, download, parent, upload, version);
                } else {
                    QMessageBox.warning(this, "Attenzione", "Non disponi dei permessi per visualizzare i documenti");
                    return;
                }
            }
        }
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
        Controller controller = this.getContext().getController();
        Map map = new HashMap();
        Column column = new Column("iddocumento", "iddocumento", "iddocumento");
        column.setEditorType(CellEditorType.STRING);
        String year = barcode.substring(0, 4);
        Integer n = Integer.parseInt(barcode.substring(4));
        barcode = year + String.format("%08d", n);
        map.put(column, barcode);
        Store store = controller.createCriteriaStore(map);
        if( store.size() == 1 ){
            this.getContext().getModel().replaceRows(store);
            this.getContext().firstElement();
        } else {
            QMessageBox.warning(this, "Attenzione", "Protocollo" + barcode + " non trovato");
        }
    }
    
    private void stampaEtichetta() {
        Protocollo protocollo = (Protocollo) this.getContext().getCurrentEntity();
        Map<String, Object> map = new HashMap();
        map.put("iddocumento", protocollo.getIddocumento());
        map.put("dataprotocollo", protocollo.getDataprotocollo());
        map.put("hash", "1234567890");
        DialogStampaEtichetta dialog = new DialogStampaEtichetta(this, map);
        int exec = dialog.exec();
        if( exec == 1 ){
            System.out.println("Print!");            
        }
    }

    private void scanNDo() {
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        if( !autenticato.getSpedisceprotocollo() ){
            Util.warningBox(this, "Utente non abilitato", "Attenzione, l'utente non è abilitato.");
            return;
        }
        Protocollo protocollo = (Protocollo) this.getContext().getCurrentEntity();
        ScanNDo form = new ScanNDo(protocollo.getIddocumento());
        QMdiArea workspace = Util.findParentMdiArea(this);
        if( workspace != null ){
            workspace.addSubWindow((QDialog) form);
        }
        form.show();
    }

    private void pubblicaProtocollo() {
        Protocollo protocollo = (Protocollo) this.getContext().getCurrentEntity();
        Pubblicazione pubblicazione = PubblicazioneUtil.pubblicaProtocollo(protocollo);
        IForm form = Util.formFromEntity(pubblicazione);
        QMdiArea workspace = Util.findParentMdiArea(this);
        if( workspace != null ){
            workspace.addSubWindow((QMainWindow) form);
        }
        form.show();
    }

    private void segnaturaXml() {
        Protocollo protocollo = (Protocollo) this.getContext().getCurrentEntity();
        if( protocollo.getId() == null ){
            QMessageBox.warning(this, "Attenzione", "E' necessario registrare il protocollo per poter generare la segntura.");
            return;
        }
        if( !protocollo.getTipo().equals(TipoProtocollo.USCITA) ){
            QMessageBox.warning(this, "Attenzione", "E' possibile generare la segnatura solo per i protocolli in uscita.");
            return;
        }
        CmisPlugin plugin = (CmisPlugin) Register.queryPlugin(protocollo.getClass(), "CMIS");
        AlfrescoHelper helper = plugin.createAlfrescoHelper(protocollo);
        if( helper.children().size() == 0 ){
            QMessageBox.warning(this, "Attenzione", "Deve essere presente almeno un documento.");
            return;
        }

        Segnatura segnatura = JAXBHelper.segnaturaDaProtocollo(protocollo);
        String xml = JAXBHelper.scriviSegnatura(segnatura);
        helper.createDocument("", "Segnatura.xml", xml.getBytes());

    }
        
}
