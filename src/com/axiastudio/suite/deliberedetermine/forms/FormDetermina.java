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
package com.axiastudio.suite.deliberedetermine.forms;

import com.axiastudio.menjazo.AlfrescoHelper;
import com.axiastudio.pypapi.IStreamProvider;
import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Controller;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.pypapi.ui.Delegate;
import com.axiastudio.pypapi.ui.ITableModel;
import com.axiastudio.pypapi.ui.widgets.PyPaPiTableView;
import com.axiastudio.suite.SuiteUtil;
import com.axiastudio.suite.deliberedetermine.DeterminaUtil;
import com.axiastudio.suite.deliberedetermine.entities.Determina;
import com.axiastudio.suite.deliberedetermine.entities.ServizioDetermina;
import com.axiastudio.suite.finanziaria.entities.Servizio;
import com.axiastudio.suite.plugins.cmis.CmisPlugin;
import com.axiastudio.suite.plugins.ooops.IDocumentFolder;
import com.axiastudio.suite.plugins.ooops.Template;
import com.axiastudio.suite.pratiche.entities.Fase;
import com.axiastudio.suite.pratiche.entities.FasePratica;
import com.axiastudio.suite.pratiche.entities.Visto;
import com.axiastudio.suite.pratiche.forms.FormDettaglio;
import com.axiastudio.suite.procedimenti.SimpleWorkFlow;
import com.axiastudio.suite.procedimenti.SimpleWorkflowDialog;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class FormDetermina extends FormDettaglio implements IDocumentFolder {

    public FormDetermina(String uiFile, Class entityClass, String title){
        super(uiFile, entityClass, title);

        QListWidget procedimento = (QListWidget) findChild(QListWidget.class, "procedimento");
        procedimento.itemDoubleClicked.connect(this, "completaFase(QListWidgetItem)");

        PyPaPiTableView tableViewServizi = (PyPaPiTableView) this.findChild(PyPaPiTableView.class, "tableView_servizi");
        tableViewServizi.entityInserted.connect(this, "servizioInserito(Object)");
        tableViewServizi.entityRemoved.connect(this, "servizioRimosso(Object)");

        PyPaPiTableView tableView_impegni = (PyPaPiTableView) findChild(PyPaPiTableView.class, "tableView_impegni");
        tableView_impegni.setItemDelegate(new Delegate(tableView_impegni));
        PyPaPiTableView tableView_spese = (PyPaPiTableView) findChild(PyPaPiTableView.class, "tableView_spese");
        tableView_spese.setItemDelegate(new Delegate(tableView_spese));
    }

    @Override
    protected void indexChanged(int row) {
        super.indexChanged(row);
        popolaProcedimento();
        popolaVisti();

        ((QCheckBox) this.findChild(QCheckBox.class, "checkBox_spesaImpegnoEsistente")).setEnabled(
                ((QCheckBox) this.findChild(QCheckBox.class, "checkBox_spesa")).isChecked());
        ((QCheckBox) this.findChild(QCheckBox.class, "checkBox_pluriennale")).setEnabled(
                ((QCheckBox) this.findChild(QCheckBox.class, "checkBox_spesa")).isChecked() ||
                        ((QCheckBox) this.findChild(QCheckBox.class, "checkBox_entrata")).isChecked());
        ((QSpinBox) this.findChild(QSpinBox.class, "spinBox_finoAl")).setVisible(
                ((QCheckBox) this.findChild(QCheckBox.class, "checkBox_pluriennale")).isChecked());

    }

    private void popolaVisti() {
        Determina determina = (Determina) this.getContext().getCurrentEntity();

        Visto vistoResponsabile = determina.getVistoResponsabile();
        String testoResponsabile = "-";
        if( vistoResponsabile != null ){
            testoResponsabile = SuiteUtil.DATE_FORMAT.format(vistoResponsabile.getData()) + ", " + vistoResponsabile.getUtente();
            if( vistoResponsabile.getResponsabile() != null && !vistoResponsabile.getResponsabile().equals(vistoResponsabile.getUtente()) ){
                testoResponsabile += " (resp. " + vistoResponsabile.getResponsabile() + ")";
            }
        }
        QLabel responsabile = (QLabel) findChild(QLabel.class, "label_vistoResponsabile");
        responsabile.setText(testoResponsabile);

        Visto vistoBilancio = determina.getVistoBilancio();
        String testoBilancio = "-";
        if( vistoBilancio != null ){
            testoBilancio = SuiteUtil.DATE_FORMAT.format(vistoBilancio.getData()) + ", " + vistoBilancio.getUtente();
            if( vistoBilancio.getResponsabile() != null && !vistoBilancio.getResponsabile().equals(vistoBilancio.getUtente()) ){
                testoBilancio += " (resp. " + vistoBilancio.getResponsabile() + ")";
            }
        }
        QLabel bilancio = (QLabel) findChild(QLabel.class, "label_vistoBilancio");
        bilancio.setText(testoBilancio);

        Visto vistoBilancioNegato = determina.getVistoBilancioNegato();
        String testoBilancioNegato = "-";
        if( vistoBilancioNegato != null ){
            testoBilancioNegato = SuiteUtil.DATE_FORMAT.format(vistoBilancioNegato.getData()) + ", " + vistoBilancioNegato.getUtente();
            if( vistoBilancioNegato.getResponsabile() != null && !vistoBilancioNegato.getResponsabile().equals(vistoBilancioNegato.getUtente()) ){
                testoBilancioNegato += " (resp. " + vistoBilancioNegato.getResponsabile() + ")";
            }
        }
        QLabel bilancioNegato = (QLabel) findChild(QLabel.class, "label_vistoBilancioNegato");
        bilancioNegato.setText(testoBilancioNegato);

    }

    private void popolaProcedimento() {
        QListWidget listWidget = (QListWidget) findChild(QListWidget.class, "procedimento");
        Determina determina = (Determina) this.getContext().getCurrentEntity();
        if( determina.getId() == null ){
            return;
        }
        SimpleWorkFlow wf = new SimpleWorkFlow(determina);
        listWidget.clear();
        Integer i=0;
        for( FasePratica fp: wf.getFasi() ){
            Fase fase = fp.getFase();
            QIcon icon=null;
            if( fp.getCompletata() ){
                icon = new QIcon("classpath:com/axiastudio/suite/resources/tick.png");
            } else if ( fp.equals(wf.getFaseAttiva()) ){
                icon = new QIcon("classpath:com/axiastudio/suite/resources/star.png");
            }
            QListWidgetItem item = new QListWidgetItem(icon, fase.getDescrizione());
            item.setData(Qt.ItemDataRole.UserRole, i);
            listWidget.addItem(item);
            i++;
        }
    }

    private void completaFase(QListWidgetItem item){

        if( getContext().getIsDirty() ){
            QMessageBox.warning(this, "Attenzione", "Per completare una fase la determina deve essere prima salvata.");
            return;
        }
        Integer i = (Integer) item.data(Qt.ItemDataRole.UserRole);

        // XXX: se ci sono eventuali modifiche nelle condizioni?
        Determina determina = (Determina) this.getContext().getCurrentEntity();
        SimpleWorkFlow wf = new SimpleWorkFlow(determina);
        FasePratica fasePratica = wf.getFase(i);

        // posso completare solo la fase attiva
        if( !wf.getFaseAttiva().equals(fasePratica) ){
            return;
        }

        SimpleWorkflowDialog swd = new SimpleWorkflowDialog(this, wf , fasePratica);
        int res = swd.exec();

        if( res == 1 ){
/*            if(determina.getVistoResponsabile() != null && (determina.getNumero() == null || determina.getNumero() == 0)){
                DeterminaUtil.numeroDiDetermina(determina);
                Database db = (Database) Register.queryUtility(IDatabase.class);
                Controller controller = db.createController(Determina.class);
                controller.commit(determina);
            }   */
            this.getContext().commitChanges();
        }
    }

 /*
 * Il primo servizio diventa principale, e non può più essere rimosso
 * Se non indicato il referente politico, viene inserito quello di default x il servizio
 */
    private void servizioInserito(Object obj){
        Determina determina = (Determina) this.getContext().getCurrentEntity();
        ServizioDetermina inserita = (ServizioDetermina) obj;
        if( determina.getServizioDeterminaCollection().size() == 1 ){
            inserita.setPrincipale(Boolean.TRUE);
        }
        if ( determina.getReferentePolitico() == null || determina.getReferentePolitico().equals("")) {
            determina.setReferentePolitico(((Servizio) inserita.getServizio()).getReferentepolitico());
            ((QLineEdit) findChild(QLineEdit.class, "lineEdit_RefPolitico")).setText(determina.getReferentePolitico());
        }
    }
    private void servizioRimosso(Object obj){
        Determina determina = (Determina) this.getContext().getCurrentEntity();
        ServizioDetermina rimossa = (ServizioDetermina) obj;
        if( rimossa.getPrincipale() ){
            QMessageBox.warning(this, "Attenzione", "Il servizio principale non può venir rimosso.");
            PyPaPiTableView tableViewServizio = (PyPaPiTableView) this.findChild(PyPaPiTableView.class, "tableView_servizi");
            ((ITableModel) tableViewServizio.model()).getContextHandle().insertElement(rimossa);
        }
    }


    /* XXX: codice simile a FormPratica */
    @Override
    public List<Template> getTemplates() {
        List<Template> templates = new ArrayList<Template>();
        //Pratica pratica = (Pratica) this.getContext().getCurrentEntity();
        Determina determina = (Determina) this.getContext().getCurrentEntity();
        //Pratica pratica = SuiteUtil.findPratica(pratica.getIdpratica());
        CmisPlugin cmisPlugin = (CmisPlugin) Register.queryPlugin(FormDetermina.class, "CMIS");
        AlfrescoHelper helper = cmisPlugin.createAlfrescoHelper(determina);
        helper.children("protocollo"); // XXX: per creare il subpath "protocollo"
        List<HashMap> children = helper.children();
        for( HashMap map: children ){
            String name = (String) map.get("name");
            if( name.toLowerCase().endsWith(".odt") || name.toLowerCase().endsWith(".doc") ){
                String title = (String) map.get("title");
                String description = (String) map.get("description");
                IStreamProvider streamProvider = cmisPlugin.createCmisStreamProvider((String) map.get("objectId"));
                //RuleSet rulesSet = new RuleSet(new HashMap()); // XXX: da pescare
                Template template = new Template(streamProvider, name, title, description);
                templates.add(template);
            }
        }
        return templates;
    }

    /* XXX: codice simile a FormPratica */
    @Override
    public void createDocument(String subpath, String name, String title, String description, byte[] content, String mimeType) {
        Determina determina = (Determina) this.getContext().getCurrentEntity();
        //Pratica pratica = SuiteUtil.findPratica(pratica.getIdpratica());
        CmisPlugin cmisPlugin = (CmisPlugin) Register.queryPlugin(FormDetermina.class, "CMIS");
        AlfrescoHelper helper = cmisPlugin.createAlfrescoHelper(determina);

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
            documentName = name.concat("_").concat(determina.getPratica().getIdpratica()).concat(extension);
        }
        helper.createDocument(subpath, documentName, content, mimeType, title, description);
        cmisPlugin.showForm(determina);
    }
}
