/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.protocollo.forms;

import com.axiastudio.pypapi.ui.Form;
import com.axiastudio.pypapi.ui.widgets.PyPaPiTableView;
import com.axiastudio.pypapi.ui.widgets.PyPaPiToolBar;
import com.axiastudio.suite.alfresco.AlfrescoHelper;
import com.axiastudio.suite.alfresco.AlfrescoObject;
import com.axiastudio.suite.protocollo.entities.Protocollo;
import com.trolltech.qt.core.QUrl;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.*;
import java.util.Calendar;

class ProtocolloMenuBar extends PyPaPiToolBar {
    public ProtocolloMenuBar(String title, Form parent){
        super(title, parent);
        this.insertButton("convalidaAttribuzioni", "Convalida attribuzioni",
                "classpath:com/axiastudio/suite/resources/lock_group.png",
                "Convalida degli uffici attribuiti al protocollo", parent);
        this.insertButton("convalidaProtocollo", "Convalida protocollo",
                "classpath:com/axiastudio/suite/resources/lock_mail.png",
                "Convalida della registrazione del protocollo", parent);
        this.insertButton("spazioAlfresco", "Spazio Alfresco",
                "classpath:com/axiastudio/suite/resources/alfresco.png",
                "Apri spazio Alfresco nel browser", parent);
    }
}

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class FormProtocollo extends Form {
    private  ProtocolloMenuBar protocolloMenuBar=null;
    private QTabWidget tabWidget;
    private AlfrescoHelper alfrescoHelper;
    private final String ALFRESCOHOST="192.168.64.54";
    private final Integer ALFRESCOPORT=8080;
    private final String ALFRESCOUSER="pypapi";
    private final String ALFRESCOPASSWORD="0i2kiwi";
    private final String ALFRESCOCMIS="http://"+ALFRESCOHOST+":"+ALFRESCOPORT+"/alfresco/service/cmis";
    private final String ALFRESCOLOGIN="http://"+ALFRESCOHOST+":"+ALFRESCOPORT+"/share/page/dologin?username="+ALFRESCOUSER+"&password="+ALFRESCOPASSWORD+"&success=";
    private final String ALFRESCOSPACE=ALFRESCOLOGIN+"/share/page/site/protocollo/documentlibrary#filter=path%%7C/%s&page=1";
    private final String ALFRESCODOCUMENT=ALFRESCOLOGIN+"/share/page/document-details?nodeRef=%s";
    
    public FormProtocollo(FormProtocollo form){
        super(form.uiFile, form.entityClass, form.title);
    }
    
    public FormProtocollo(String uiFile, Class entityClass, String title){
        super(uiFile, entityClass, title);
        this.protocolloMenuBar = new ProtocolloMenuBar("Protocollo", this);
        this.addToolBar(protocolloMenuBar);
        QLabel labelSpedito = (QLabel) this.findChild(QLabel.class, "labelSpedito");
        labelSpedito.setPixmap(new QPixmap("classpath:com/axiastudio/suite/resources/email_go.png"));
        QLabel labelConvalidaProtocollo = (QLabel) this.findChild(QLabel.class, "labelConvalidaProtocollo");
        labelConvalidaProtocollo.setPixmap(new QPixmap("classpath:com/axiastudio/suite/resources/lock_mail.png"));
        QLabel labelConvalidaAttribuzioni = (QLabel) this.findChild(QLabel.class, "labelConvalidaAttribuzioni");
        labelConvalidaAttribuzioni.setPixmap(new QPixmap("classpath:com/axiastudio/suite/resources/lock_group.png"));
        
        this.tabWidget = (QTabWidget) this.findChild(QTabWidget.class, "tabWidget");
        this.tabWidget.currentChanged.connect(this, "currentTabChanged(int)");
        this.alfrescoHelper = new AlfrescoHelper(ALFRESCOUSER, ALFRESCOPASSWORD, ALFRESCOCMIS);
    }
    
    private void convalidaAttribuzioni() {
        Protocollo protocollo = (Protocollo) this.getContext().getCurrentEntity();
        protocollo.setConvalidaAttribuzioni(Boolean.TRUE);
        this.getContext().getDirty();
    }

    private void convalidaProtocollo() {
        Protocollo protocollo = (Protocollo) this.getContext().getCurrentEntity();
        protocollo.setConvalidaAttribuzioni(Boolean.TRUE);
        protocollo.setConvalidaProtocollo(Boolean.TRUE);
        this.getContext().getDirty();
    }
    
    @Override
    protected void indexChanged(int row) {
        Protocollo protocollo = (Protocollo) this.getContext().getCurrentEntity();
        Boolean convAttribuzioni = protocollo.getConvalidaAttribuzioni() == true;
        Boolean convProtocollo = protocollo.getConvalidaProtocollo() == true;
        PyPaPiTableView tv = (PyPaPiTableView) this.findChild(PyPaPiTableView.class, "tableViewAttribuzioni");
        this.protocolloMenuBar.actionByName("convalidaAttribuzioni").setEnabled(!convAttribuzioni);
        this.protocolloMenuBar.actionByName("convalidaProtocollo").setEnabled(!convProtocollo);
        tv.setEnabled(!convAttribuzioni);
        this.centralWidget().setEnabled(!convProtocollo);
    }

    private void currentTabChanged(int tab) {
        String tabText = this.tabWidget.tabText(tab);
        QListWidget qlw = (QListWidget) this.findChild(QListWidget.class, "listWidgetAlfresco");
        qlw.clear();
        if( "Documenti".equals(tabText) ){
            String path = "/Siti/protocollo/documentLibrary"+this.getAlfrescoPath();
            for(AlfrescoObject object: this.alfrescoHelper.childrenNames(path)){
                QListWidgetItem item = new QListWidgetItem(object.getName());
                item.setData(Qt.ItemDataRole.UserRole, object.getObjectId());
                qlw.addItem(item);
            }
        }
        qlw.itemDoubleClicked.connect(this, "documentoAlfresco(QListWidgetItem)");
    }
    
    private String getAlfrescoPath(){
        Protocollo protocollo = (Protocollo) this.getContext().getCurrentEntity();
        Calendar c = Calendar.getInstance();
        c.setTime(protocollo.getDataprotocollo());
        String path = "/"+c.get(Calendar.YEAR)+
                String.format("/%02d/", c.get(Calendar.MONTH)+1)+
                String.format("%02d/", c.get(Calendar.DAY_OF_MONTH))+
                protocollo.getIddocumento();
        return path;
    }
    
    private void documentoAlfresco(QListWidgetItem item){
        String url = String.format(ALFRESCODOCUMENT, item.data(Qt.ItemDataRole.UserRole));
        QDesktopServices.openUrl(new QUrl(url));
    }

    private void spazioAlfresco(){
        String urlpath = String.format(this.ALFRESCOSPACE, this.getAlfrescoPath());
        QDesktopServices.openUrl(new QUrl(urlpath));
    }

}
