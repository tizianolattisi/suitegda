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

import com.axiastudio.menjazo.AlfrescoHelper;
import com.axiastudio.pypapi.IStreamProvider;
import com.axiastudio.pypapi.Register;
import com.axiastudio.suite.plugins.cmis.CmisPlugin;
import com.axiastudio.suite.plugins.cmis.CmisStreamProvider;
import com.axiastudio.suite.plugins.ooops.IDocumentFolder;
import com.axiastudio.suite.plugins.ooops.Template;
import com.axiastudio.pypapi.ui.Util;
import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.pypapi.ui.widgets.PyPaPiComboBox;
import com.axiastudio.suite.SuiteUiUtil;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.UfficioUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.pratiche.entities.Pratica;
import com.axiastudio.suite.pratiche.entities.TipoPratica;
import com.axiastudio.suite.protocollo.entities.Fascicolo;
import com.axiastudio.suite.protocollo.forms.FormTitolario;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class FormPratica extends Window implements IDocumentFolder {
    
    public FormPratica(String uiFile, Class entityClass, String title){
        super(uiFile, entityClass, title);
        
        /* tipo */
        QToolButton toolButtonTipo = (QToolButton) this.findChild(QToolButton.class, "toolButtonTipo");
        toolButtonTipo.setIcon(new QIcon("classpath:com/axiastudio/suite/resources/email_go.png"));
        toolButtonTipo.clicked.connect(this, "apriTipo()");
        
        /* fascicolazione */
        QToolButton toolButtonTitolario = (QToolButton) this.findChild(QToolButton.class, "toolButtonTitolario");
        toolButtonTitolario.setIcon(new QIcon("classpath:com/axiastudio/suite/resources/email_go.png"));
        toolButtonTitolario.clicked.connect(this, "apriTitolario()");       
    }
    
    /*
     * XXX: copia e incolla in FormTipoSeduta
     */
    private void apriTipo(){
        Pratica pratica = (Pratica) this.getContext().getCurrentEntity();
        if( pratica.getAttribuzione() == null ){
            String msg = "Per poter selezionare una tipologia devi prima attribuire un ufficio";
            Util.warningBox((QWidget) this, "Error", msg);
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
    
    @Override
    protected void indexChanged(int row) {
        super.indexChanged(row);
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        Pratica pratica = (Pratica) this.getContext().getCurrentEntity();
        Boolean nuovoInserimento = pratica.getId() == null;
        
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

        String documentName = name + "_" + pratica.getIdpratica() + extension;
        helper.createDocument(subpath, documentName, content, mimeType);
        cmisPlugin.showForm(pratica);
    }
    
}
