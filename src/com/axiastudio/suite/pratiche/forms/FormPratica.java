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
package com.axiastudio.suite.pratiche.forms;

import com.axiastudio.suite.menjazo.AlfrescoHelper;
import com.axiastudio.pypapi.IStreamProvider;
import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.*;
import com.axiastudio.pypapi.plugins.IPlugin;
import com.axiastudio.pypapi.ui.*;
import com.axiastudio.pypapi.ui.widgets.PyPaPiComboBox;
import com.axiastudio.pypapi.ui.widgets.PyPaPiTableView;
import com.axiastudio.suite.SuiteUiUtil;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.base.entities.UfficioUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.generale.forms.DialogStampaEtichetta;
import com.axiastudio.suite.plugins.cmis.CmisPlugin;
import com.axiastudio.suite.plugins.ooops.IDocumentFolder;
import com.axiastudio.suite.plugins.ooops.Template;
import com.axiastudio.suite.pratiche.IDettaglio;
import com.axiastudio.suite.pratiche.PraticaUtil;
import com.axiastudio.suite.pratiche.entities.Fase;
import com.axiastudio.suite.pratiche.entities.FasePratica;
import com.axiastudio.suite.pratiche.entities.Pratica;
import com.axiastudio.suite.pratiche.entities.TipoPratica;
import com.axiastudio.suite.protocollo.entities.Fascicolo;
import com.axiastudio.suite.protocollo.forms.FormTitolario;
import com.trolltech.qt.gui.*;

import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class FormPratica extends Window implements IDocumentFolder {

    private PraticaToolbar praticaToolbar;
    
    public FormPratica(String uiFile, Class entityClass, String title){
        super(uiFile, entityClass, title);
        praticaToolbar = new PraticaToolbar("Dettaglio", this);
        addToolBar(praticaToolbar);
        
        /* tipo */
        QToolButton toolButtonTipo = (QToolButton) this.findChild(QToolButton.class, "toolButtonTipo");
        toolButtonTipo.setIcon(new QIcon("classpath:com/axiastudio/suite/resources/book_open.png"));
        toolButtonTipo.clicked.connect(this, "apriTipo()");
        
        /* fascicolazione */
        QToolButton toolButtonTitolario = (QToolButton) this.findChild(QToolButton.class, "toolButtonTitolario");
        toolButtonTitolario.setIcon(new QIcon("classpath:com/axiastudio/suite/resources/book_open.png"));
        toolButtonTitolario.clicked.connect(this, "apriTitolario()");

        try {
            Method storeFactory = this.getClass().getMethod("storeAttribuzione");
            Register.registerUtility(storeFactory, IStoreFactory.class, "Attribuzione");
            storeFactory = this.getClass().getMethod("storeTipo");
            Register.registerUtility(storeFactory, IStoreFactory.class, "Tipo");
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(FormPratica.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(FormPratica.class.getName()).log(Level.SEVERE, null, ex);
        }

        // visti non editabili
        PyPaPiTableView tableView_visti = (PyPaPiTableView) this.findChild(PyPaPiTableView.class, "tableView_visti");
        Util.setWidgetReadOnly(tableView_visti, true);
        // tab 'Visti' visibile solo da utenti con flag 'Supervisore pratiche'
        // TODO: tab visibile anche da: istruttori, responsabile firma, resp. Bilancio (x determine), assessore/politico competente
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        tableView_visti.setVisible(autenticato.getSupervisorepratiche());

        ((QComboBox) this.findChild(QComboBox.class, "comboBox_fase")).currentIndexChanged.connect(this, "aggiornaFase()");
    }

    private void apriDettaglio(){
        Pratica pratica = (Pratica) this.getContext().getCurrentEntity();
        IDettaglio dettaglio = PraticaUtil.trovaDettaglioDaPratica(pratica);
        if( dettaglio != null ){
            IForm form = Util.formFromEntity(dettaglio);
            QMdiArea workspace = Util.findParentMdiArea(this);
            if( workspace != null ){
                workspace.addSubWindow((QMainWindow) form);
            }
            form.show();

        } else {
            Boolean b = PraticaUtil.eseguiDettaglioEsterno(pratica);
            if( !b ){
                String msg = "Non è stato possibile trovare un dettaglio per la pratica.";
                Util.warningBox(this, "Attenzione", msg);
                return;
            }
        }
    }

    private void cercaDaEtichetta() {
        String barcode = QInputDialog.getText(this, "Ricerca da etichetta", "Etichetta").substring(0, 9);
        if( barcode == null ){
            return;
        }
        barcode = barcode.trim();
        if( barcode.length() < 5 ){
            QMessageBox.warning(this, "Attenzione", "Numero di pratica troppo breve");
            return;
        }
        Controller controller = this.getContext().getController();
        Map map = new HashMap();
        Column column = new Column("idpratica", "idpratica", "idpratica");
        column.setEditorType(CellEditorType.STRING);
        map.put(column, barcode);
        Store store = controller.createCriteriaStore(map);
        if( store.size() == 1 ){
            this.getContext().getModel().replaceRows(store);
            this.getContext().firstElement();
        } else {
            QMessageBox.warning(this, "Attenzione", "Pratica" + barcode + " non trovata");
        }
    }

    private void stampaEtichetta() {
        Pratica pratica = (Pratica) this.getContext().getCurrentEntity();
        Map<String, Object> map = new HashMap();
        map.put("idpratica", pratica.getIdpratica());
        map.put("codiceinterno", pratica.getCodiceinterno());
        if ( pratica.getCodiceaggiuntivo() == null ) {
            map.put("codiceaggiuntivo", "");
        } else {
            map.put("codiceaggiuntivo", pratica.getCodiceaggiuntivo());
        }
        map.put("tipopratica", pratica.getCodiceinterno().substring(0, 3));
        map.put("hash", "1234567890");
        DialogStampaEtichetta dialog = new DialogStampaEtichetta(this, map);
        int exec = dialog.exec();
        if( exec == 1 ){
            System.out.println("Print!");
        }
    }




    /*
     * XXX: copia e incolla in FormTipoSeduta
     */
    private void apriTipo(){
        Pratica pratica = (Pratica) this.getContext().getCurrentEntity();
        if( pratica.getAttribuzione() == null ){
            String msg = "Per poter selezionare una tipologia devi prima attribuire un ufficio";
            Util.warningBox(this, "Error", msg);
            return;
        }
        FormTipoPratica tipi = new FormTipoPratica(this, pratica);
        int exec = tipi.exec();
        if( exec == 1 ){
            TipoPratica selection = tipi.getSelection();
            if( selection != null ){
                PyPaPiComboBox comboBoxTipo = (PyPaPiComboBox) this.findChild(PyPaPiComboBox.class, "comboBoxTipo");
                comboBoxTipo.select(selection);
                this.getContext().getDirty();
            }
        }
    }
  
//  XXX: by FormProtocollo
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

    /*
     * Uno store contenente solo gli uffici dell'utente
     */
    public Store storeAttribuzione(){
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        List<Ufficio> uffici = new ArrayList<Ufficio>();
        for(UfficioUtente uu: autenticato.getUfficioUtenteCollection()){
            if( uu.getInseriscepratica() ){
                uffici.add(uu.getUfficio());
            }
        }
        return new Store(uffici);
    }

    /*
     * Uno store contenente gli oggetti ordinati x descrizione; solo tipologie 'foglie' e non obsolete
     */
    public Store storeTipo(){
        Database db = (Database) Register.queryUtility(IDatabase.class);
        Controller controller = db.createController(TipoPratica.class);
        Store storeTipo = controller.createFullStore();
        List<TipoPratica> oggetti = new ArrayList<TipoPratica>();
        for(Object ogg: storeTipo){
            if ( ((TipoPratica) ogg).getObsoleta().equals(Boolean.FALSE) && ((TipoPratica) ogg).getFoglia().equals(Boolean.TRUE) ) {
                oggetti.add((TipoPratica) ogg);
            }
        }
        Collections.sort(oggetti, TipoPratica.Comparators.CODICE);
        return new Store(oggetti);
    }

    /*
     * Uno store contenente gli oggetti ordinati x descrizione
     */
    public Store storeFase(){
        List<Fase> fasiprat = new ArrayList<Fase>();

        if (this.getContext() == null || this.getContext().getCurrentEntity() == null) {
            return new Store(fasiprat);
        }

        Pratica pratica = (Pratica) this.getContext().getCurrentEntity();
        if (pratica.getId() == null) {
            return new Store(fasiprat);
        }

        if (pratica.getFasePraticaCollection().isEmpty()) {
            Database db = (Database) Register.queryUtility(IDatabase.class);
            Controller controller = db.createController(Fase.class);
            Store storeFase = controller.createFullStore();
            for(Object ogg: storeFase){
                fasiprat.add((Fase) ogg);
            }
            Collections.sort(fasiprat, Fase.Comparators.DESCRIZIONE);
        } else {
            Fase fase = new Fase();
            for(FasePratica ogg: pratica.getFasePraticaCollection()){
                fasiprat.add((Fase) ogg.getFase());
            }
        }
        return new Store(fasiprat);
    }


    @Override
    protected void indexChanged(int row) {
        super.indexChanged(row);
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        Pratica pratica = (Pratica) this.getContext().getCurrentEntity();
        Boolean nuovoInserimento = pratica.getId() == null;

        // abilitazione visualizzazione documenti tramite Menjazo
        this.praticaToolbar.actionByName("apriDocumenti").
                setEnabled(!nuovoInserimento && PraticaUtil.trovaDettaglioDaPratica(pratica) == null);

        // Abilitazione scelta della tipologia
        Util.setWidgetReadOnly((QWidget) this.findChild(QWidget.class, "comboBoxTipo"), !nuovoInserimento);
        ((QToolButton) this.findChild(QToolButton.class, "toolButtonTipo")).setEnabled(nuovoInserimento);
        
        // Se non sei nell'ufficio gestore, ti blocco l'ufficio gestore e il check riservato
        Boolean inUfficioGestore = false;
        for( UfficioUtente uu: autenticato.getUfficioUtenteCollection() ){
            if( uu.getUfficio().equals(pratica.getGestione()) && uu.getModificapratica() ){
                // se la pratica è riservata, mi serve anche il flag
                if( !pratica.getRiservata() || uu.getRiservato() ){
                    inUfficioGestore = true;
                    break;
                }
            }
        }
        ((QComboBox) this.findChild(QComboBox.class, "comboBox_attribuzione")).setEnabled(nuovoInserimento);
        ((QComboBox) this.findChild(QComboBox.class, "comboBox_gestione")).setEnabled(nuovoInserimento || inUfficioGestore);
        ((QCheckBox) this.findChild(QCheckBox.class, "checkBox_riservata")).setEnabled(nuovoInserimento || inUfficioGestore);

        Store store = storeFase();
        PyPaPiComboBox fase=((PyPaPiComboBox) this.findChild(PyPaPiComboBox.class, "comboBox_fase"));
        fase.setLookupStore(store);
        this.getColumn("Fase").setLookupStore(store);
        fase.select(pratica.getFase());
        if ( pratica.getFase()!= null && pratica.getFase().getEvidenza() ) {
            fase.setStyleSheet("QComboBox{background: rgb(255, 0, 0);}");
        } else {
            fase.setStyleSheet("QComboBox{background: rgb(255, 255, 255);}");
        }
    }

    private void aggiornaFase() {
        if ( this.getContext()!=null ) {
            PyPaPiComboBox cmbFase=((PyPaPiComboBox) this.findChild(PyPaPiComboBox.class, "comboBox_fase"));
            if ( cmbFase.getCurrentEntity()!=null && ((Fase) cmbFase.getCurrentEntity()).getEvidenza() ) {
                cmbFase.setStyleSheet("QComboBox{background: rgb(255, 0, 0);}");
            } else {
                cmbFase.setStyleSheet("QComboBox{background: rgb(255, 255, 255);}");
            }
        }
    }

    private void information() {
        SuiteUiUtil.showInfo(this);
    }


    /*
     *  Implementazione IDocumentFolder
     */
    @Override
    public List<Template> getTemplates() {
        List<Template> templates = new ArrayList();
        Pratica pratica = (Pratica) this.getContext().getCurrentEntity();
        //Pratica pratica = SuiteUtil.findPratica(pratica.getIdpratica());
        CmisPlugin cmisPlugin = (CmisPlugin) Register.queryPlugin(FormPratica.class, "CMIS");
        AlfrescoHelper helper = cmisPlugin.createAlfrescoHelper(pratica);
        helper.children("protocollo"); // XXX: per creare il subpath "protocollo"
        List<HashMap> children = helper.children();
        for( HashMap map: children ){
            String name = (String) map.get("name");
            if( name.toLowerCase().endsWith(".odt") || name.toLowerCase().endsWith(".doc") ){
                IStreamProvider streamProvider = cmisPlugin.createCmisStreamProvider((String) map.get("objectId"));
                //RuleSet rulesSet = new RuleSet(new HashMap()); // XXX: da pescare
                Template template = new Template(streamProvider, name, "Documento generato");
                templates.add(template);
            }
        }
        return templates;
    }

    @Override
    public void createDocument(String subpath, String name, String title, String description, byte[] content, String mimeType) {
        Pratica pratica = (Pratica) this.getContext().getCurrentEntity();
        //Pratica pratica = SuiteUtil.findPratica(pratica.getIdpratica());
        CmisPlugin cmisPlugin = (CmisPlugin) Register.queryPlugin(FormPratica.class, "CMIS");
        AlfrescoHelper helper = cmisPlugin.createAlfrescoHelper(pratica);

        // extension
        String extension = "";
        if( mimeType.equals("application/pdf") ){
            extension = ".pdf";
        } else if( mimeType.equals("application/vnd.oasis.opendocument.text") ){
            extension = ".odt";
        } else if( mimeType.equals("application/msword") ){
            extension = ".doc";
        }

        String documentName;
        if( name.endsWith(".odt") || name.endsWith(".doc") ){
            documentName = name.substring(0, name.length()-4).concat(extension);
        } else {
            documentName = name.concat("_").concat(pratica.getIdpratica()).concat(extension);
        }
        helper.createDocument(subpath, documentName, content, mimeType, title, description);
        cmisPlugin.showForm(pratica);
    }

    // XXX: codice simile a FormScrivania
    private void apriDocumenti(){
        Pratica pratica = (Pratica) this.getContext().getCurrentEntity();
        if( pratica == null || pratica.getId() == null ){
            return;
        }
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        List<IPlugin> plugins = (List) Register.queryPlugins(this.getClass());
        for(IPlugin plugin: plugins){
            if( "CMIS".equals(plugin.getName()) ){
                Boolean view = true;
                Boolean delete = true;
                Boolean download = true;
                Boolean parent = false;
                Boolean upload = true;
                Boolean version = true;
                if( view ){
                    ((CmisPlugin) plugin).showForm(pratica, delete, download, parent, upload, version);
                } else {
                    QMessageBox.warning(this, "Attenzione", "Non disponi dei permessi per visualizzare i documenti");
                    return;
                }
            }
        }
    }
    
}
