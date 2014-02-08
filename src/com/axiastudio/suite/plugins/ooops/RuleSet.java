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

import com.axiastudio.pypapi.Register;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Utente;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.util.HashMap;
import java.util.Map;

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
        GroovyShell shell = new GroovyShell(binding);
        for( String key: this.rules.keySet() ){
            String groovy = this.rules.get(key) + "(param)";
            if( groovy != null && !"".equals(groovy) ){
                Object value = shell.evaluate(groovy);
                mapOut.put(key, value);
            } else {
                mapOut.put(key, "[unset]");
            }
        }
        return mapOut;
    }
    
}
