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
package com.axiastudio.suite.plugins.ooops;

import com.axiastudio.menjazo.AlfrescoHelper;
import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.ui.IForm;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.plugins.cmis.CmisPlugin;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tiziano
 */
public class RuleSet {
    
    private HashMap<String,String> rules;

    public RuleSet(HashMap<String,String> json) {
        this.rules = json;
    }

    public Map<String, Object> eval(Object entity) {
        HashMap<String,Object> mapOut = new HashMap();
        Binding binding = new Binding();
        binding.setVariable("param", entity);
        Utente utente = (Utente) Register.queryUtility(IUtente.class);
        binding.setVariable("utente", utente);

        // documenti
        Class formClass = (Class) Register.queryUtility(IForm.class, entity.getClass().getName());
        CmisPlugin cmisPlugin = (CmisPlugin) Register.queryPlugin(formClass, "CMIS");
        AlfrescoHelper alfrescoHelper = cmisPlugin.createAlfrescoHelper(entity);
        List<String> documenti = new ArrayList<String>();
        List<HashMap> children=null;
        try {
            children = alfrescoHelper.children();
        } catch (Exception e){
            // log?
        }
        // XXX: e se Alfresco non è disponibile?
        if( children != null ){
            for( Map child: children ){
                String fileName = (String) child.get("contentStreamFileName");
                if( fileName != null ){ // XXX: per saltare le cartelle
                    documenti.add(fileName);
                }
            }
        }
        binding.setVariable("documenti", documenti);


        GroovyShell shell = new GroovyShell(binding);
        for( String key: this.rules.keySet() ){
            String groovy = this.rules.get(key) + "(param)";
            if( groovy != null && !"".equals(groovy) ){
                Object value=null;
                try {
                    value = shell.evaluate(groovy);
                } catch (Exception ex){
                    String msg = "Errore leggendo la regola per il campo " + key + ": \n" + this.rules.get(key);
                    Logger.getLogger(RuleSet.class.getName()).log(Level.WARNING, msg, ex);
                }
                mapOut.put(key, value);
            } else {
                mapOut.put(key, "[unset]");
            }
        }
        return mapOut;
    }
    
}
