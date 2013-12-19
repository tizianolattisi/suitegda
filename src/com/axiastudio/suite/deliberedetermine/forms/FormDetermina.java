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
import com.axiastudio.suite.deliberedetermine.entities.Determina;
import com.axiastudio.suite.plugins.cmis.CmisPlugin;
import com.axiastudio.suite.plugins.ooops.IDocumentFolder;
import com.axiastudio.suite.plugins.ooops.Template;
import com.axiastudio.suite.pratiche.entities.Fase;
import com.axiastudio.suite.pratiche.entities.FasePratica;
import com.axiastudio.suite.pratiche.forms.FormDettaglio;
import com.axiastudio.suite.procedimenti.SimpleWorkFlow;
import com.axiastudio.suite.procedimenti.SimpleWorkflowDialog;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.*;

import java.util.*;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class FormDetermina extends FormDettaglio implements IDocumentFolder {

    public FormDetermina(String uiFile, Class entityClass, String title){
        super(uiFile, entityClass, title);

        QListWidget procedimento = (QListWidget) findChild(QListWidget.class, "procedimento");
        procedimento.itemDoubleClicked.connect(this, "completaFase(QListWidgetItem)");

    }


    @Override
    protected void indexChanged(int row) {
        super.indexChanged(row);
        popolaProcedimento();
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
        Integer i = (Integer) item.data(Qt.ItemDataRole.UserRole);

        // XXX: se ci sono eventuali modifiche nelle condizioni?
        Determina determina = (Determina) this.getContext().getCurrentEntity();
        SimpleWorkFlow wf = new SimpleWorkFlow(determina);
        FasePratica fasePratica = wf.getFase(i);

        // posso completare solo la fase attiva (la prima non competata disponibile)
        if( !wf.getFaseAttiva().equals(fasePratica) ){
            return;
        }

        SimpleWorkflowDialog swd = new SimpleWorkflowDialog(this, wf , fasePratica);
        int res = swd.exec();

        if( res == 1 ){
            this.getContext().commitChanges();
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

        String documentName = name + "_" + determina.getPratica().getIdpratica() + extension;
        helper.createDocument(subpath, documentName, content, mimeType, title, description);
        cmisPlugin.showForm(determina);
    }
}
