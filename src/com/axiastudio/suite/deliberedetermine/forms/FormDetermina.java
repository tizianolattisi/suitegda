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
import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.deliberedetermine.entities.Determina;
import com.axiastudio.suite.plugins.cmis.CmisPlugin;
import com.axiastudio.suite.plugins.ooops.IDocumentFolder;
import com.axiastudio.suite.plugins.ooops.Template;
import com.axiastudio.suite.pratiche.entities.Pratica;
import com.trolltech.qt.gui.QPushButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class FormDetermina extends Window implements IDocumentFolder {
    private QPushButton pushButtonResponsabile;
    private QPushButton pushButtonBilancio;
    private QPushButton pushButtonVistoNegato;
    
    public FormDetermina(String uiFile, Class entityClass, String title){
        super(uiFile, entityClass, title);
        pushButtonResponsabile = (QPushButton) this.findChild(QPushButton.class, "pushButtonResponsabile");
        pushButtonBilancio = (QPushButton) this.findChild(QPushButton.class, "pushButtonBilancio");
        pushButtonVistoNegato = (QPushButton) this.findChild(QPushButton.class, "pushButtonVistoNegato");
        pushButtonResponsabile.clicked.connect(this, "vistoResponsabile()");
        pushButtonBilancio.clicked.connect(this, "vistoBilancio()");
        pushButtonVistoNegato.clicked.connect(this, "vistoNegato()");
        
    }
    
    protected Boolean vistoResponsabile() {
        Determina determina = (Determina) this.getContext().getCurrentEntity();
        Utente utente = (Utente) Register.queryUtility(IUtente.class);
        determina.setVistoResponsabile(Boolean.TRUE);
        determina.setDataVistoResponsabile(new Date());
        determina.setUtenteVistoResponsabile(utente);
        return true;
    }

    protected Boolean vistoBilancio() {
        Determina determina = (Determina) this.getContext().getCurrentEntity();
        Utente utente = (Utente) Register.queryUtility(IUtente.class);
        determina.setVistoBilancio(Boolean.TRUE);
        determina.setDataVistoBilancio(new Date());
        determina.setUtenteVistoBilancio(utente);
        return true;
    }

    protected Boolean vistoNegato() {
        Determina determina = (Determina) this.getContext().getCurrentEntity();
        Utente utente = (Utente) Register.queryUtility(IUtente.class);
        determina.setVistoNegato(Boolean.TRUE);
        determina.setDataVistoNegato(new Date());
        determina.setUtenteVistoNegato(utente);
        return true;
        
    }
    
    /*
     * Verifica delle condizioni di abilitazione alla firma del responsabile
     * del servizio.
     */
    protected Boolean checkResponsabile() {
        return false;
    }

    protected Boolean checkBilancio() {
        return true;
    }

    @Override
    protected void indexChanged(int row) {
        super.indexChanged(row);
        verificaAbilitazionePulsanti();
    }

    protected void verificaAbilitazionePulsanti() {
        Determina d = (Determina) this.getContext().getCurrentEntity();
        Boolean vResp = false;
        Boolean vBil = false;
        Boolean vNeg = false;
        if( d != null ){
            vResp = !d.getVistoResponsabile() && this.checkResponsabile();
            vBil = d.getVistoResponsabile() && !d.getVistoBilancio() && this.checkBilancio();
            vNeg = vBil;
        }
        this.pushButtonResponsabile.setEnabled(vResp);
        this.pushButtonBilancio.setEnabled(vBil);
        this.pushButtonVistoNegato.setEnabled(vNeg);
    }

    /* XXX: codice simile a FormPratica */
    @Override
    public List<Template> getTemplates() {
        List<Template> templates = new ArrayList();
        //Pratica pratica = (Pratica) this.getContext().getCurrentEntity();
        Determina determina = (Determina) this.getContext().getCurrentEntity();
        //Pratica pratica = SuiteUtil.findPratica(pratica.getIdpratica());
        CmisPlugin cmisPlugin = (CmisPlugin) Register.queryPlugin(FormDetermina.class, "CMIS");
        AlfrescoHelper helper = cmisPlugin.createAlfrescoHelper(determina);
        List<HashMap> children = helper.children();
        for( HashMap map: children ){
            String name = (String) map.get("name");
            if( name.toLowerCase().endsWith(".odt") || name.toLowerCase().endsWith(".doc") ){
                IStreamProvider streamProvider = cmisPlugin.createCmisStreamProvider((String) map.get("objectId"));
                //RuleSet rulesSet = new RuleSet(new HashMap()); // XXX: da pescare
                Template template = new Template(streamProvider, name, "Documento generato", null);
                templates.add(template);
            }
        }
        return templates;
    }

    /* XXX: codice simile a FormPratica */
    @Override
    public void createDocument(String subpath, String name, byte[] content, String mimeType) {
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

        String documentName = name + "_" + determina.getIdpratica() + extension;
        helper.createDocument(subpath, documentName, content, mimeType);
        cmisPlugin.showForm(determina);
    }
}
