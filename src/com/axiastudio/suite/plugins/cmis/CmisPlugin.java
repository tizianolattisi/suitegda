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
package com.axiastudio.suite.plugins.cmis;

import com.axiastudio.suite.menjazo.AlfrescoHelper;
import com.axiastudio.suite.menjazo.ClientWindow;
import com.axiastudio.pypapi.plugins.IPlugin;
import com.axiastudio.pypapi.ui.Dialog;
import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.pypapi.ui.widgets.PyPaPiToolBar;
import com.trolltech.qt.gui.QWidget;

import java.util.HashMap;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */

class CMISMenuBar extends PyPaPiToolBar {
    public CMISMenuBar(String title, Window parent, IPlugin plugin){
        super(title, parent);
        this.insertButton("showForm", "CMIS",
                          "classpath:com/axiastudio/suite/menjazo/resources/menjazo.png",
                          "Open CMIS form", plugin);
    }
}

public class CmisPlugin implements IPlugin {
    
    private static final String name="CMIS";
    
    private String cmisUrl;
    private String user;
    private String password;
    private QWidget parent=null;
    private String template;
    private Boolean canInsertToolbar;

    @Override
    public void install(QWidget parent) {
        this.install(parent, Boolean.TRUE);
    }

    @Override
    public void install(QWidget parent, Boolean addToolbar) {
        if( Window.class.isInstance(parent) ){
            this.parent = parent;
            if( addToolbar && this.canInsertToolbar  ){
                CMISMenuBar bar = new CMISMenuBar("CMIS", (Window) parent, this);
                ((Window) parent).addToolBar(bar);
            }
        } else if( Dialog.class.isInstance(parent) ){
            this.parent = parent;
        }
    }

    @Override
    public void uninstall() {
        parent = null;
    }

    public void setup(String cmisUrl, String user, 
                      String password) {
        this.setup(cmisUrl, user, password, null, true);
    }

    public void setup(String cmisUrl, String user, 
                      String password, String template) {
        this.setup(cmisUrl, user, password, template, true);
    }

    public void setup(String cmisUrl, String user, 
                      String password, String template, Boolean toolbar) {
        this.cmisUrl = cmisUrl;
        this.user = user;
        this.password = password;
        this.canInsertToolbar = toolbar;
        if( template == null ){
            this.template = "/";
        } else {
            this.template = template;
        }
    }

    public void showForm() {
        this.showForm(null);
    }

    public void showForm(Object entity) {
        this.showForm(entity, true, true, false, true, true);
    }

    public void showForm(Object entity, Boolean delete, Boolean download, Boolean parent, Boolean upload, Boolean version) {
        this.showForm(entity, delete, download, parent, upload, version, new HashMap());
    }

    public void showForm(Object entity, Boolean delete, Boolean download, Boolean parent, Boolean upload, Boolean version, HashMap map) {
        this.showForm(entity, delete, download, parent, upload, version, new HashMap(), Boolean.FALSE);
    }

    public void showForm(Object entity, Boolean delete, Boolean download, Boolean parent, Boolean upload, Boolean version,
                         HashMap map, Boolean allowNewEntity) {
        if( entity==null ){
            entity = ((Window) this.parent).getContext().getCurrentEntity();
        }
        if( entity == null || (!allowNewEntity && entity.hashCode()==0 ) ){
            return;
        }
        AlfrescoHelper helper = createAlfrescoHelper(entity);
        ClientWindow dialog = new ClientWindow(null, helper, map);
        dialog.setEntity(entity);
        dialog.show();
        dialog.setDeleteEnabled(delete);
        dialog.setDownloadEnabled(download);
        dialog.setParentEnabled(parent);
        dialog.setUploadEnabled(upload);
        dialog.setVersionEnabled(version);
    }

    @Override
    public String getName() {
        return CmisPlugin.name;
    }

    public AlfrescoHelper createAlfrescoHelper(Object entity) {
        String path = CmisUtil.cmisPathGenerator(this.template, entity);
        AlfrescoHelper helper = new AlfrescoHelper(this.cmisUrl, this.user, this.password);
        helper.setPath(path);
        return helper;
    }

    public CmisStreamProvider createCmisStreamProvider(String objectId){
        return new CmisStreamProvider(cmisUrl, user, password, objectId);
    }

}
