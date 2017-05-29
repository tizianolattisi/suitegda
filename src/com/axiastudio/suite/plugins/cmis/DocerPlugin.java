/*
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

import com.axiastudio.pypapi.plugins.IPlugin;
import com.axiastudio.pypapi.ui.Dialog;
import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.pypapi.ui.widgets.PyPaPiToolBar;
import com.trolltech.qt.gui.QWidget;
import it.tn.rivadelgarda.comune.gda.WebAppBridge;
import it.tn.rivadelgarda.comune.gda.WebAppBridgeBuilder;
import it.tn.rivadelgarda.comune.gda.docer.DocerHelper;

/**
 *
 * @author Michela Piva - Comune di Riva del Garda
 */

class DocerMenuBar extends PyPaPiToolBar {
    public DocerMenuBar(String title, Window parent, IPlugin plugin){
        super(title, parent);
        this.insertButton("showForm", "Doc/ER",
                          "classpath:com/axiastudio/suite/plugins/cmis/docer.png",
                          "Open Doc/ER form", plugin);
    }
}

public class DocerPlugin implements IPlugin {
    
    private static final String name="DocER";
    
    private String docerUrl;
    private String user;
    private String password;
    private QWidget parent=null;
    private String urlpath;
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
                DocerMenuBar bar = new DocerMenuBar("DocER", (Window) parent, this);
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

    public void setup(String docerUrl, String user,
                      String password) {
        this.setup(docerUrl, user, password, null, true);
    }

    public void setup(String docerUrl, String user,
                      String password, String template) {
        this.setup(docerUrl, user, password, template, true);
    }

    public void setup(String docerUrl, String user,
                      String password, String urlpath, Boolean toolbar) {
        this.docerUrl = docerUrl;
        this.user = user;
        this.password = password;
        this.canInsertToolbar = toolbar;
        this.urlpath = urlpath;
    }

    public void showForm(Object entity, String url) {
        this.showForm(entity, url, true);
    }

    public void showForm(Object entity, String url, Boolean allowNewEntity) {
        if( entity==null ){
            entity = ((Window) this.parent).getContext().getCurrentEntity();
        }
        if( entity == null || (!allowNewEntity && entity.hashCode()==0 ) ){
            return;
        }
        WebAppBridge bridge = WebAppBridgeBuilder.create()
                .url(this.urlpath + url)
//                .developerExtrasEnabled(Boolean.TRUE)                       // abilità "inspect" dal menu contestuale
                .javaScriptEnabled(Boolean.TRUE)                            // abilità l'esecuzione di JavaScript
//                .javaScriptCanOpenWindows(Boolean.TRUE)                     // JS può aprire finestre in popup
//                .javaScriptCanCloseWindows(Boolean.TRUE)                    // JS può chiudere finestre
//                .javaScriptCanAccessClipboard(Boolean.TRUE)                 // JS legge e scrive da clipboard
                .cookieJarEnabled(Boolean.TRUE)                             // la pagina può impostare dei cookie
                .downloadEnabled(Boolean.TRUE)                              // download abilitati
//                .downloadContentTypes(new String[]{"application/pdf"})      // content type permessi al download
                .downloadPath("/home/pivamichela")                   // cartella proposta per il download
//                .loadFinishedCallback(callbackClass, "callback()")          // callback da eseguire a pagina caricata
                .build();

        bridge.show();
    }

    @Override
    public String getName() {
        return DocerPlugin.name;
    }

//    public DocerHelper createDocerHelper(Object entity) {
////        String path = DocerUtil.cmisPathGenerator(this.template, entity);
//        DocerHelper helper = new DocerHelper(this.docerUrl, this.user, this.password);
//        return helper;
//    }

//    public DocerStreamProvider createDocerStreamProvider(String objectId){
//        return new DocerStreamProvider(docerUrl, user, password, objectId);
//    }

}
