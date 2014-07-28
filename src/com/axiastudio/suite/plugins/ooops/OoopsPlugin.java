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

import com.axiastudio.pypapi.plugins.IPlugin;
import com.axiastudio.pypapi.ui.Dialog;
import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.pypapi.ui.widgets.PyPaPiToolBar;
import com.trolltech.qt.gui.QWidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 * 
 * ./soffice "-accept=socket,host=localhost,port=8100;urp;"
 * 
 */

class OoopsMenuBar extends PyPaPiToolBar {
    public OoopsMenuBar(String title, Window parent, Object plugin){
        super(title, parent);
        this.insertButton("showForm", "Ooops",
                          "classpath:com/axiastudio/suite/resources/ooops.png",
                          "Open Ooops form", plugin);
}
}

public class OoopsPlugin implements IPlugin {
    
    public static final String name="Ooops";
    private QWidget parent=null;
    private List<Template> templates = new ArrayList();
    private String connectionString;
    private Boolean hidden;

    @Override
    public void install(QWidget parent) {
            this.install(parent, Boolean.FALSE);
    }

    @Override
    public void uninstall() {
        parent = null;
    }

    @Override
    public void install(QWidget parent, Boolean addToolbar) {
        if( Window.class.isInstance(parent) && addToolbar){
            OoopsMenuBar bar = new OoopsMenuBar("Ooops", (Window) parent, this);
            ((Window) parent).addToolBar(bar);
            this.parent = (Window) parent;
        } else if( Dialog.class.isInstance(parent) ){
            this.parent = (Dialog) parent;
        } else {
            this.parent = parent;
        }
    }

    public void addTemplate(Template template){
        this.templates.add(template);
    }
    
    @Override
    public String getName() {
        return this.name;
    }

    public void showForm() {
        Object entity = ((Window) parent).getContext().getCurrentEntity();
        if( entity == null || entity.hashCode() == 0 ){
            return;
        }
        List<Template> dialogTemplates = new ArrayList();
        HashMap<String, RuleSet> ruleSetMap = new HashMap();
        for( Template template: this.templates ){
            dialogTemplates.add(template);
            ruleSetMap.put(template.getName(), template.getRuleSet());
        }
        /* is the form a template provider? */
        if( IDocumentFolder.class.isInstance(this.parent) ){
            List<Template> entityTemplates = ((IDocumentFolder) this.parent).getTemplates();
            for( Template template: entityTemplates ){
                if( template.getRuleSet() == null ){
                    String templateName = template.getName().split("_")[0];
                    RuleSet ruleSet = ruleSetMap.get(templateName);
                    template.setRuleSet(ruleSet);
                }
                dialogTemplates.add(template);
            }
        }
        OoopsHelper helper = new OoopsHelper(this.connectionString, this.hidden);
        OoopsDialog dialog = new OoopsDialog(this.parent, helper, dialogTemplates);
        dialog.show();
    }
    
    public void setup(String connectionString){
        this.setup(connectionString, true);
    }

    public void setup(String connectionString, Boolean hidden){
        this.connectionString = connectionString;
        this.hidden = hidden;
    }


}
