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

import com.axiastudio.suite.anagrafiche.entities.Riferimento;
import com.axiastudio.suite.anagrafiche.entities.TipoRiferimento;
import com.axiastudio.suite.anagrafiche.entities.TipoSoggetto;
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
import com.axiastudio.suite.pec.NuovoMessaggioRequest;
import com.axiastudio.suite.pec.NuovoMessaggioResponse;
import com.axiastudio.suite.pec.UploadAllegatoRequest;
import com.axiastudio.suite.plugins.cmis.CmisPlugin;
import com.axiastudio.suite.plugins.cmis.CmisUtil;
import com.axiastudio.suite.procedimenti.GestoreDeleghe;
import com.axiastudio.suite.procedimenti.entities.Carica;
import com.axiastudio.suite.procedimenti.entities.Delega;
import com.axiastudio.suite.protocollo.ProfiloUtenteProtocollo;
import com.axiastudio.suite.protocollo.entities.*;
import com.axiastudio.suite.pubblicazioni.PubblicazioneUtil;
import com.axiastudio.suite.pubblicazioni.entities.Pubblicazione;
import com.axiastudio.suite.scanndo.ScanNDo;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.gui.*;
import org.apache.chemistry.opencmis.client.api.Document;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.media.multipart.file.StreamDataBodyPart;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.*;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class FormProtocollo extends Window {

    public static final String JPEC_SERVER_URL = "http://192.168.64.200:8080/gdapec/";
    public static final String DOCS_SERVER_URL = "http://192.168.64.200:8080/gda-documentale/rest/protocollo/";
    private static final String DOCS_FEED = "1234";

    /**
     *
     */
    public ProtocolloMenuBar protocolloMenuBar;
    private QTabWidget tabWidget;
    private QTabWidget tabWidgetSoggettiProtocollo;

    Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
    List<Ufficio> uffici = new ArrayList();
    List<Ufficio> ufficiRicerca = new ArrayList();
    List<Ufficio> ufficiPrivato = new ArrayList();
    AlfrescoHelper alfrescoHelper;

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

        /* Tab della finestra */
        tabWidgetSoggettiProtocollo = (QTabWidget) this.findChild(QTabWidget.class, "tabWidget_sinistra");
        tabWidget = (QTabWidget) this.findChild(QTabWidget.class, "tabWidget");
        tabWidget.currentChanged.connect(this, "focusTabModificato()");

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
        Carica carica = GestoreDeleghe.findCarica("RESPONSABILE_ATTRIBUZIONI");
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
        this.protocolloMenuBar.actionByName("inviaPec").setEnabled( !this.getContext().getIsDirty() && convProtocollo && consDocumenti &&
                protocollo.getTipo().equals(TipoProtocollo.USCITA) &&
                protocollo.getTiporiferimentomittente() != null );

        // convalida attribuzioni
        PyPaPiTableView tableViewAttribuzioni = (PyPaPiTableView) this.findChild(PyPaPiTableView.class, "tableView_attribuzioni");
        Boolean modificaAttribuzioni = nuovoInserimento || autenticato.getAttributoreprotocollo() || (!protocollo.getConvalidaattribuzioni() && profilo.inSportelloOAttribuzionePrincipale());
        Util.setWidgetReadOnly(tableViewAttribuzioni, !modificaAttribuzioni);

        // sempre read-only
        Util.setWidgetReadOnly((QWidget) this.findChild(QDateEdit.class, "dateEdit_data"), true);
        Util.setWidgetReadOnly((QWidget) this.findChild(QLineEdit.class, "lineEdit_iddocumento"), true);
        Util.setWidgetReadOnly((QWidget) this.findChild(QCheckBox.class, "annullato"), true);
        Util.setWidgetReadOnly((QWidget) this.findChild(QCheckBox.class, "annullamentorichiesto"), true);
        Util.setWidgetReadOnly((QWidget) this.findChild(QTextEdit.class, "textEdit_Segnatura"), true);

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

        // protocollo convalidato: disabilitazione di tutto tranne note e pratiche
        String[] roWidgets = {"textEdit_oggetto", "tableView_soggettiprotocollo",
                "tableView_soggettiriservatiprotocollo", "tableView_ufficiprotocollo",
                "comboBoxTitolario", "comboBox_tiporiferimentomittente", "lineEdit_nrriferimentomittente",
                "dateEdit_datariferimentomittente", "richiederisposta", "riservato",
                "corrispostoostornato","textEdit_PECBody"};
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
        tabWidgetSoggettiProtocollo.setTabEnabled(1, protocollo.getRiservato());

        // Indicazione num riferimenti precedenti/successivi su label della tab
        tabWidget.setTabText(1, "Riferimenti precedenti (" + protocollo.getRiferimentoProtocolloCollection().size() +
                ") e successivi (" + protocollo.getRiferimentoProtocolloSuccessivoCollection().size() + ")");

        // Gestione abilitazione tab PEC; aggiornamento informazioni se attiva
        if ( !protocollo.getTipo().equals(TipoProtocollo.INTERNO) && protocollo.getTiporiferimentomittente()!=null
        //        && protocollo.getTiporiferimentomittente().getDescrizione().equals("PEC")
                ) {
            tabWidget.setTabText(3, "PEC");
            tabWidget.setTabEnabled(3, true);
            if ( tabWidget.currentIndex()==3 ) {
                // Richiesta informazioni sullo stato dell'invio
                QTextEdit textStatoPec = (QTextEdit) this.findChild(QTextEdit.class, "textEdit_StatoPEC");
                textStatoPec.setText(getStatoPec());
            }
        } else {
            tabWidget.setTabText(3, "");
            tabWidget.setTabEnabled(3, false);
        }

        // numero di documenti contenuti nella cartella Alfresco
        CmisPlugin cmisPlugin = (CmisPlugin) Register.queryPlugin(Protocollo.class, "CMIS");
        alfrescoHelper = cmisPlugin.createAlfrescoHelper(protocollo);
        Long n = alfrescoHelper.numberOfDocument();
        labelConsolida.setText(labelConsolida.text() + "\n " + n + " documento/i");
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
        tabWidgetSoggettiProtocollo.setTabText(0, labelSinistra);
        tabWidgetSoggettiProtocollo.setTabText(1, labelSinistra + " riservati (" + nrRiservati + ")");
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

    private void inviaPec() {

        Protocollo protocollo = (Protocollo) this.getContext().getCurrentEntity();

        if( !protocollo.getTipo().equals(TipoProtocollo.USCITA) ){
            QMessageBox.warning(this, "Attenzione", "E' possibile inviare tramite PEC solo per i protocolli in uscita.");
            return;
        }
/*        if( protocollo.getUfficioProtocolloCollection().size()!=1 ){
            QMessageBox.warning(this, "Attenzione", "E' necessario che sia presente un unico ufficio mittente.");
            return;
        }
        Ufficio mittente = protocollo.getUfficioProtocolloCollection().iterator().next().getUfficio(); */

        Ufficio mittente = new Ufficio();
        for ( Attribuzione attribuzione: protocollo.getAttribuzioneCollection() ) {
            if (attribuzione.getPrincipale()) {
                mittente = attribuzione.getUfficio();
                if( mittente.getPec() == null || mittente.getPec() == "" ){
                    QMessageBox.warning(this, "Attenzione", "L'attribuzione in via principale deve essere configurata con una mailbox PEC.");
                    return;
                }
                break;
            }
        }
        if ( mittente == null) {
            QMessageBox.critical(this, "Attenzione", "E' necessario che sia presente un'attribuzione in via principale.");
            return;
        }

        List<String> destinatari = new ArrayList<>();
        Map<String, SoggettoProtocollo> mappaDestinatari = new HashMap<>();
        int giaInviato = 0;
        int destinatariPec = 0;
        for( SoggettoProtocollo soggettoProtocollo: protocollo.getSoggettoProtocolloCollection() ){
            if ( soggettoProtocollo.getPec() ){
                destinatariPec++;
                if ( soggettoProtocollo.getMessaggiopec()==null ) {
                    List<String> pecDisponibili = new ArrayList<String>();
                    for (Riferimento riferimento : soggettoProtocollo.getSoggetto().getRiferimentoCollection()) {
                        if (TipoRiferimento.PEC.equals(riferimento.getTipo())) {
                            pecDisponibili.add(riferimento.getRiferimento());
                        }
                    }
                    String pecSelezionata;
                    if (pecDisponibili.size() == 0) {
                        pecSelezionata = null;
                    } else if (pecDisponibili.size() == 1) {
                        pecSelezionata = pecDisponibili.get(0);
                    } else {
                        pecSelezionata = QInputDialog.getItem(this,
                                "Seleziona PEC destinatario",
                                "Scegliere l'indirizzo PEC per il destinatario " + soggettoProtocollo.getSoggettoformattato(),
                                pecDisponibili,
                                0,
                                false);
                    }
                    if (pecSelezionata != null) {
                        destinatari.add(pecSelezionata);
                        mappaDestinatari.put(pecSelezionata, soggettoProtocollo);
                    } else {
                        // destinatario non raggiungibilie tramite pec
                        QMessageBox.warning(this, "Attenzione", "Il destinatario " + soggettoProtocollo.getSoggettoformattato() + " non è raggiungibile tramite PEC.");
                        return;
                    }
                } else {
                    giaInviato++;
                }
            }
        }
        if ( giaInviato==destinatariPec ) {
            QMessageBox.warning(this, "Attenzione", "PEC già inviata a tutti i destinatari indicati.");
            return;
        }
        if( destinatari.size()==0 ){
            QMessageBox.warning(this, "Attenzione", "I destinatari devono aver configurato un indirizzo PEC.");
            return;
        }
        if( destinatariPec<protocollo.getSoggettoProtocolloCollection().size() ){
            int conferma = QMessageBox.question(this,
                    "Attenzione", "Alcuni destinatari non hanno attivo il flag per l'invio tramite PEC. Si desidera procedere?",
                    QMessageBox.StandardButton.Yes, QMessageBox.StandardButton.No);
            if( QMessageBox.StandardButton.No.equals(conferma) ){
                System.out.println("sono uscito");
                return;
            }
        }

        CmisPlugin plugin = (CmisPlugin) Register.queryPlugin(protocollo.getClass(), "CMIS");
        AlfrescoHelper helper = plugin.createAlfrescoHelper(protocollo);
        if( helper.children().size() == 0 ){
            int conferma = QMessageBox.question(this, "Attenzione", "Non ci sono file allegati. Si desidera continuare?",
                    QMessageBox.StandardButton.Yes, QMessageBox.StandardButton.No);
            if( QMessageBox.StandardButton.No.equals(conferma) ){
                System.out.println("sono uscito");
                return;
            }
        }

        int conferma = QMessageBox.question(this,
                "Conferma", "Conferma invio PEC", QMessageBox.StandardButton.Yes, QMessageBox.StandardButton.No);
        if( QMessageBox.StandardButton.No.equals(conferma)){
            System.out.println("sono uscito");
            return;
        }

        for( String destinatario: destinatari ) {


            Client client = ClientBuilder.newBuilder()
                    .register(MultiPartFeature.class)
                    .property(ClientProperties.CHUNKED_ENCODING_SIZE, 1024)
                    .build();

            WebTarget messaggiTarget = client.target(JPEC_SERVER_URL).path("api/messaggi");


            NuovoMessaggioRequest messaggiRequest = new NuovoMessaggioRequest();
            messaggiRequest.setMailbox(mittente.getPec());

            messaggiRequest.addDestinatario(destinatario);
            messaggiRequest.setOggetto(protocollo.getOggetto());
            messaggiRequest.setTestoMessaggio(protocollo.getPecBody());
            messaggiRequest.setProtocollo(protocollo.getIddocumento());

            // url del documentale
            String template = "${dataprotocollo,date,yyyy}/${dataprotocollo,date,MM}/${dataprotocollo,date,dd}/${iddocumento}/";
            String path = CmisUtil.cmisPathGenerator(template, protocollo);
            String hash = md5Hash(protocollo.getIddocumento() + DOCS_FEED);
            String urlDocumentale = DOCS_SERVER_URL + path + hash + "/documento";
            messaggiRequest.setUrlDocumentale(urlDocumentale);

            // messaggio
            NuovoMessaggioResponse messaggioResponse;
            try {
                messaggioResponse = messaggiTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(messaggiRequest, MediaType.APPLICATION_JSON), NuovoMessaggioResponse.class);
            } catch (ClientErrorException restError) {
                String error_response = restError.getResponse().readEntity(String.class);
                System.out.println(error_response);
                QMessageBox.critical(this, "Errore", "Errore nella preparazione della PEC da inviare.");
                return;
            }
            SoggettoProtocollo soggettoProtocollo = mappaDestinatari.get(destinatario);
            soggettoProtocollo.setMessaggiopec(messaggioResponse.getMessageId());

            // allegati
            List<String> nomiFile = new ArrayList<>();
            if (helper.children().size() > 0) { // c'è qualcosa da allegare
                for (Map<String, String> map : helper.children()) {
                    String objectId = map.get("objectId");
                    String name = map.get("name");
                    String mime = map.get("mime");
                    Integer length = Integer.parseInt(map.get("contentStreamLength"));
                    Document document = helper.getDocument(objectId);

                    WebTarget allegatiTarget = client.target(JPEC_SERVER_URL).path("api/allegati/upload");
                    MultiPart multiPart = new MultiPart();
                    multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);

                    UploadAllegatoRequest allegatoRequest = new UploadAllegatoRequest();
                    allegatoRequest.setContentType(mime);
                    allegatoRequest.setFileName(name);
                    nomiFile.add(name);
                    allegatoRequest.setSize(length);
                    allegatoRequest.setIdMessaggio(messaggioResponse.getMessageId());
                    FormDataBodyPart allegatoDataBodyPart = new FormDataBodyPart("allegato", allegatoRequest, MediaType.APPLICATION_JSON_TYPE);
                    multiPart.bodyPart(allegatoDataBodyPart);

                    StreamDataBodyPart fileDataBodyPart = new StreamDataBodyPart("file", document.getContentStream().getStream(), allegatoRequest.getFileName(), MediaType.APPLICATION_OCTET_STREAM_TYPE);
                    multiPart.bodyPart(fileDataBodyPart);

                    Response response = allegatiTarget.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(multiPart, multiPart.getMediaType()));

                    if (!(response.getStatus() == Response.Status.OK.getStatusCode())) {
                        QMessageBox.critical(this, "Attenzione!", "Problemi con l'allegato " + name);
                        return;
                    }
                }
            }

            // Segnatura.xml
            Boolean SEGNATURA = Boolean.TRUE;
            Boolean DRY_RUN = Boolean.FALSE;
            Boolean SEGNATURA_A_TUTTI = Boolean.TRUE;
            if(SEGNATURA && (SEGNATURA_A_TUTTI || TipoSoggetto.ENTE.equals(soggettoProtocollo.getSoggetto().getTipo())) ) {
                Segnatura segnatura = JAXBHelper.segnaturaDaProtocollo(protocollo,
                        soggettoProtocollo.getSoggetto(),
                        destinatario,
                        nomiFile);
                String segnaturaXml = JAXBHelper.scriviSegnatura(segnatura);
                if( DRY_RUN ){
                    System.out.println(segnaturaXml);
                } else {
                    WebTarget allegatiTarget = client.target(JPEC_SERVER_URL).path("api/allegati/upload");
                    MultiPart multiPart = new MultiPart();
                    multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);
                    UploadAllegatoRequest allegatoRequest = new UploadAllegatoRequest();
                    allegatoRequest.setContentType("text/xml");
                    allegatoRequest.setFileName("Segnatura.xml");
                    allegatoRequest.setSize(segnaturaXml.length());
                    allegatoRequest.setIdMessaggio(messaggioResponse.getMessageId());
                    FormDataBodyPart allegatoDataBodyPart = new FormDataBodyPart("allegato", allegatoRequest, MediaType.APPLICATION_JSON_TYPE);
                    multiPart.bodyPart(allegatoDataBodyPart);

                    StreamDataBodyPart fileDataBodyPart = new StreamDataBodyPart("file", new ByteArrayInputStream(segnaturaXml.getBytes()), allegatoRequest.getFileName(), MediaType.APPLICATION_OCTET_STREAM_TYPE);
                    multiPart.bodyPart(fileDataBodyPart);

                    Response response = allegatiTarget.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(multiPart, multiPart.getMediaType()));
                }
            }


            // salvare
            getContext().commitChanges();

            // eseguo il PUT
            try {
                URL url = new URL(JPEC_SERVER_URL + "api/azioni/invia/" + messaggioResponse.getMessageId());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("PUT");
                conn.getInputStream();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("fatto");
            QMessageBox.information(this, "PEC", "PEC in spedizione.");
        }
    }

    /*
    private String segnaturaXml() {
        Protocollo protocollo = (Protocollo) this.getContext().getCurrentEntity();
        CmisPlugin plugin = (CmisPlugin) Register.queryPlugin(protocollo.getClass(), "CMIS");
        AlfrescoHelper helper = plugin.createAlfrescoHelper(protocollo);
        Segnatura segnatura = JAXBHelper.segnaturaDaProtocollo(protocollo);
        String xml = JAXBHelper.scriviSegnatura(segnatura);
        return xml;
    }
    */

    private String md5Hash(String s) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return "00000000000000000000000000000000";
        }
        md.update(s.getBytes());
        byte[] digest = md.digest();
        StringBuffer sb = new StringBuffer();
        for( int i=0; i<digest.length; i++ ){
            sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    private void focusTabModificato() {
        if ( tabWidget.currentIndex()==3 ) {
            // Richiesta informazioni sullo stato dell'invio
            QTextEdit textStatoPec = (QTextEdit) this.findChild(QTextEdit.class, "textEdit_StatoPEC");
            textStatoPec.setText(getStatoPec());
        }
    }

    private String getStatoPec() {
        String out = "";
        Protocollo protocollo = (Protocollo) this.getContext().getCurrentEntity();
        for (SoggettoProtocollo soggettoProtocollo : protocollo.getSoggettoProtocolloCollection()) {
            Long messaggiopec = soggettoProtocollo.getMessaggiopec();
            if (messaggiopec != null) {
                Client client = ClientBuilder.newClient();
                WebTarget target = client.target(JPEC_SERVER_URL).path("api/messaggi/" + messaggiopec + "/stato");
                String stato = target.request(MediaType.APPLICATION_JSON).get(String.class);
                out += soggettoProtocollo.getSoggetto().toString() + " - ";
                out += stato;
                out += " (PEC n. " + messaggiopec + ")\n";
//                out += messaggiopec;
//                out += " (" + stato + ")\n";
            }
        }
        return out;
    }
}
