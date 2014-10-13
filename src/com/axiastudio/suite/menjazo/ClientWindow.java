/*
 * Copyright (C) 2013 AXIA Studio (http://www.axiastudio.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axiastudio.suite.menjazo;

import com.axiastudio.iwas.DatamatrixSize;
import com.axiastudio.iwas.IWas;
import com.axiastudio.pypapi.ui.Util;
import com.axiastudio.suite.SuiteUtil;
import com.axiastudio.suite.protocollo.entities.Protocollo;
import com.axiastudio.suite.protocollo.entities.TipoProtocollo;
import com.itextpdf.text.DocumentException;
import com.trolltech.qt.core.QDir;
import com.trolltech.qt.core.QFile;
import com.trolltech.qt.core.QIODevice;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSignalMapper;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.QTemporaryFile;
import com.trolltech.qt.core.QUrl;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.*;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class ClientWindow extends QMainWindow {
    
    private final AlfrescoHelper helper;
    private Object entity=null;
    private Boolean dirty=false;
    private QTableWidget tableWidget;
    private QAction parentAction = new QAction(this);
    private QAction openAction = new QAction(this);
    private QAction downloadAction = new QAction(this);
    private QAction uploadAction = new QAction(this);
    private QAction versionAction = new QAction(this);
    private QAction deleteAction = new QAction(this);
    private List<Map> ids = new ArrayList();
    private Map<String,TempFile> tempFiles = new HashMap();
    
    public ClientWindow(QWidget parent, AlfrescoHelper helper){
        super(parent);
        this.helper = helper;
        this.initLayout();
        QRect screenGeometry = QApplication.desktop().screenGeometry();
        int x = screenGeometry.center().x()-500;
        int y = screenGeometry.center().y()-250;
        QPoint topLeft = new QPoint(x, y);
        QSize size = this.geometry().size();
        size.scale(1000, 500, Qt.AspectRatioMode.IgnoreAspectRatio);
        QRect newGeometry = new QRect(topLeft, size);
        this.setGeometry(newGeometry);
        this.refreshList();
        this.setWindowTitle("Documenti");
    }
    
    public void setParentEnabled(Boolean enabled){
        parentAction.setEnabled(enabled);
    }

    public void setDownloadEnabled(Boolean enabled){
        downloadAction.setEnabled(enabled);
    }

    public void setUploadEnabled(Boolean enabled){
        uploadAction.setEnabled(enabled);
    }

    public void setVersionEnabled(Boolean enabled){
        versionAction.setEnabled(enabled);
    }

    public void setDeleteEnabled(Boolean enabled){
        deleteAction.setEnabled(enabled);
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

    private void initLayout(){

        QToolBar toolBar = new QToolBar();
        parentAction.setIcon(new QIcon("classpath:com/axiastudio/suite/menjazo/resources/arrow_up.png"));
        parentAction.setText("Cartella precedente");
        parentAction.setToolTip("Vai alla cartella precedente");
        parentAction.triggered.connect(this, "goParentFolder()");
        toolBar.addAction(parentAction);
        openAction.setIcon(new QIcon("classpath:com/axiastudio/suite/menjazo/resources/open.png"));
        openAction.setText("Apri");
        openAction.setToolTip("Scarica e apri il documento selezionato");
        openAction.triggered.connect(this, "open()");
        toolBar.addAction(openAction);
        downloadAction.setIcon(new QIcon("classpath:com/axiastudio/suite/menjazo/resources/download.png"));
        downloadAction.setText("Scarica");
        downloadAction.setToolTip("Scarica il documento selezionato");
        downloadAction.triggered.connect(this, "download()");
        toolBar.addAction(downloadAction);
        uploadAction.setIcon(new QIcon("classpath:com/axiastudio/suite/menjazo/resources/upload.png"));
        uploadAction.setText("Carica");
        uploadAction.setToolTip("Aggiungi un nuovo documento");
        uploadAction.triggered.connect(this, "upload()");
        toolBar.addAction(uploadAction);
        versionAction.setIcon(new QIcon("classpath:com/axiastudio/suite/menjazo/resources/version.png"));
        versionAction.setText("Versione");
        versionAction.setToolTip("Carica una nuova versione del documento selezionato (versionamento)");
        versionAction.triggered.connect(this, "uploadVersion()");
        toolBar.addAction(versionAction);
        deleteAction.setIcon(new QIcon("classpath:com/axiastudio/suite/menjazo/resources/delete.png"));
        deleteAction.setText("Cancella");
        deleteAction.setToolTip("Cancella il documento selezionato");
        deleteAction.triggered.connect(this, "delete()");
        toolBar.addAction(deleteAction);

        // document list
        tableWidget = new QTableWidget();
        tableWidget.verticalHeader().hide();
        tableWidget.setShowGrid(false);
        tableWidget.setAlternatingRowColors(true);
        tableWidget.setSelectionBehavior(QAbstractItemView.SelectionBehavior.SelectRows);
        tableWidget.setSortingEnabled(true);
        tableWidget.horizontalHeader().setResizeMode(QHeaderView.ResizeMode.Interactive);
        
        /* drag and drop */
        tableWidget.setDragEnabled(true);
        //qtw.setAcceptDrops(true);
        tableWidget.setDropIndicatorShown(true);
        tableWidget.viewport().setAcceptDrops(true);
        tableWidget.setDragDropMode(QAbstractItemView.DragDropMode.DragDrop);
                
        // Layout
        this.setCentralWidget(tableWidget);
        this.addToolBar(toolBar);
        
        // Dock titled
        QDockWidget titledDockWidget = new QDockWidget("Attributi", this);
        QWidget titledWidget = new QWidget(titledDockWidget);
        QVBoxLayout vBoxTitled = new QVBoxLayout();
        vBoxTitled.addWidget(new QLabel("Titolo"));
        QLineEdit lineEditTitle = new QLineEdit();
        lineEditTitle.setObjectName("title");
        vBoxTitled.addWidget(lineEditTitle);
        vBoxTitled.addWidget(new QLabel("Descrizione"));
        QTextEdit textEditDescription = new QTextEdit();
        textEditDescription.setObjectName("description");
        vBoxTitled.addWidget(textEditDescription);
        QToolButton toolButtonSaveProperties = new QToolButton();
        toolButtonSaveProperties.setObjectName("toolButtonSave");
        toolButtonSaveProperties.setIcon(new QIcon("classpath:com/axiastudio/suite/menjazo/resources/disk.png"));
        toolButtonSaveProperties.setEnabled(false);
        vBoxTitled.addWidget(toolButtonSaveProperties);
        vBoxTitled.addStretch();
        titledWidget.setLayout(vBoxTitled);
        titledDockWidget.setWidget(titledWidget);
        this.addDockWidget(Qt.DockWidgetArea.RightDockWidgetArea, titledDockWidget);
        
        // Dock versions
        QDockWidget versionsDockWidget = new QDockWidget("Versioni", this);
        versionsDockWidget.setObjectName("versionsDockWidget");
        this.addDockWidget(Qt.DockWidgetArea.RightDockWidgetArea, versionsDockWidget);

        // signals
        lineEditTitle.textEdited.connect(this, "getDirty()");
        textEditDescription.textChanged.connect(this, "getDirty()");
        tableWidget.currentItemChanged.connect(this, "refreshProperties()");
        toolButtonSaveProperties.clicked.connect(this, "saveProperties()");
        tableWidget.itemDoubleClicked.connect(this, "open()");
    }
    
    private Boolean isDirty() {
        return dirty;
    }
    
    private void getDirty(){
        if( dirty == false ){
            dirty = true;
            ((QToolButton) this.findChild(QToolButton.class, "toolButtonSave")).setEnabled(true);
        }
    }
    
    private void cleanDirty(){
        if( dirty == true ){
            dirty = false;
            ((QToolButton) this.findChild(QToolButton.class, "toolButtonSave")).setEnabled(false);
        }
    }

    private void uploadVersion(){
        int currentRow = getOrderedCurrentRow();
        if( currentRow < 0 ){
            return;
        }
        Map map = this.ids.get(currentRow);
        String objectId = (String) map.get("objectId");
        String selectedFile = null;
        if( this.tempFiles.containsKey(objectId) ){
            int res = QMessageBox.question(this, "Etichetta di versione", "Vuoi aggiornare le modifiche al documento (verrà creata una nuova revisione)?",
                    QMessageBox.StandardButton.Yes, QMessageBox.StandardButton.No);
            if( QMessageBox.StandardButton.Yes.value() == res ){
                selectedFile = this.tempFiles.get(objectId).getLocalPath();
            }
        } else {
            QFileDialog dialog = new QFileDialog(this, "Carica file");
            int exec = dialog.exec();
            List<String> selectedFiles = dialog.selectedFiles();
            if( selectedFiles.size()== 1 ){
                selectedFile = selectedFiles.get(0);
            } // TODO: else alert
        }
        if( selectedFile != null ){
            QFile file = new QFile(selectedFile);
            String label = QInputDialog.getText(this, "Etichetta di versione", "Inserisci una etichetta per la versione", QLineEdit.EchoMode.Normal, "Nuova versione");
            if (file.open(QIODevice.OpenModeFlag.ReadOnly)){
                byte[] content = file.readAll().toByteArray();
                this.helper.createVersion(objectId, content, label);
            }
        }
        this.refreshList();
    }
    
    private void upload() throws IOException, DocumentException {
        QFileDialog dialog = new QFileDialog(this, "Carica file");
        int exec = dialog.exec();
        List<String> selectedFiles = dialog.selectedFiles();
        String subpath = ""; // XXX
        for( String fileName: selectedFiles ){
            byte[] content;
            if( fileName.toLowerCase().endsWith("pdf") &&
                    entity != null && entity instanceof Protocollo && TipoProtocollo.USCITA.equals(((Protocollo) entity).getTipo()) &&
                    Util.questionBox(this, "Etichetta", "Desideri applicare sul documento l'etichetta?")) {
                Protocollo protocollo = (Protocollo) entity;
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                String denominazione = SuiteUtil.trovaCostante("DENOMINAZIONE").getValore();
                String codiceAmministrazione = SuiteUtil.trovaCostante("CODICE_AMMINISTRAZIONE").getValore();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm");
                String data = dateFormat.format(protocollo.getDataprotocollo());
                dateFormat = new SimpleDateFormat("#MM#dd#");
                String dataCodice = dateFormat.format(protocollo.getDataprotocollo());
                if( denominazione.startsWith("Comune") || denominazione.startsWith("COMUNE") ){
                    denominazione = denominazione.substring(7);
                }
                String iddocumento = protocollo.getIddocumento();
                String codice = codiceAmministrazione + "#" + iddocumento + dataCodice ;
                IWas.create()
                        .load(new FileInputStream(fileName))
                        .offset(65f, 660f)
                        .text("COMUNE", 10, 0f, 47f)
                        .text(denominazione, 10, 0f, 38f)
                        .text("Prot.N.", 8, 0f, 28f)
                        .text(iddocumento, 10, 0f, 18f)
                        .text(data, 8, 0f, 9f)
                        .text(codiceAmministrazione, 8, 0f, 0f)
                        .datamatrix(codice, DatamatrixSize._22x22, 85f, 16f, 1.9f)
                        .toStream(outputStream);
                content = outputStream.toByteArray();
            } else {
                QFile file = new QFile(fileName);
                if (file.open(QIODevice.OpenModeFlag.ReadOnly)){
                    content = file.readAll().toByteArray();
                } else {
                    return;
                }
            }
            String[] split = fileName.split("/");
            String name = split[split.length-1];
            this.helper.createDocument(subpath, name, content);
        }
        this.refreshList();
    }

    private void goParentFolder(){
        String path = this.helper.getPath();
        String[] split = path.split("/");
        String newPath="";
        for( int i=0; i<split.length-1; i++ ){
            if( !split[i].equals("") ){
                newPath += "/" + split[i];
            }
        }
        this.helper.setPath(newPath);
        this.refreshList();   
    }
    
    private void open(){
        int currentRow = getOrderedCurrentRow();
        if( currentRow < 0 ){
            return;
        }
        Map map = this.ids.get(currentRow);
        if( "cmis:folder".equals((String) (map).get("baseTypeId")) ){
            this.helper.setPath((String) (map).get("path"));
            this.refreshList();
        } else if("cmis:document".equals((String) (map).get("baseTypeId"))){
            String objectId = (String) (map).get("objectId");
            String fileName = (String) (map).get("contentStreamFileName");
            InputStream in = this.helper.getDocumentStream(objectId);
            openAsTemporaryFile(fileName, in, objectId);
        }
    }

    private void download(){
        int currentRow = getOrderedCurrentRow();
        if( currentRow < 0 ){
            return;
        }
        Map map = this.ids.get(currentRow);
        if( "cmis:folder".equals((String) (map).get("baseTypeId")) ){
            this.helper.setPath((String) (map).get("path"));
            this.refreshList();
        } else if("cmis:document".equals((String) (map).get("baseTypeId"))){
            String objectId = (String) (map).get("objectId");
            String fileName = (String) (map).get("contentStreamFileName");
            InputStream in = this.helper.getDocumentStream(objectId);
            saveFile(fileName, in, objectId);
        }
    }

    private void downloadVersion(String value){
        String[] tkns = value.split(";");
        String objectId = tkns[0] + ";" + tkns[1];
        String fileName = tkns[2];
        InputStream in = this.helper.getDocumentStream(objectId);
        openAsTemporaryFile(fileName, in, objectId);
    }
    
    private void delete(){
        int currentRow = getOrderedCurrentRow();
        if( currentRow < 0 ){
            return;
        }
        int res = QMessageBox.question(this, "", "Vuoi cancellare il file selezionato?",
                        QMessageBox.StandardButton.Yes, QMessageBox.StandardButton.No);
        if( QMessageBox.StandardButton.Yes.value() == res ){
            Map map = this.ids.get(currentRow);
            String objectId = (String) map.get("objectId");
            this.helper.deleteDocument(objectId);
            this.refreshList();
        }
    }
    
    private void refreshList(){        
        while( tableWidget.rowCount() > 0 ){
            tableWidget.removeRow(0);
        }
        String subpath = ""; // XXX
        List<HashMap> children = this.helper.children(subpath);
        
        tableWidget.setColumnCount(7);
        List<String> labels = new ArrayList();
        labels.add("Nome");
        labels.add("Titolo");
        labels.add("Versione");
        labels.add("Dimensioni");
        labels.add("Data di creazione");
        labels.add("Data di modifica");
        labels.add("Tipo Mime");
        tableWidget.setHorizontalHeaderLabels(labels);

        int i = 0;
        this.ids.clear();
        for(HashMap map: children){
            
            tableWidget.insertRow(i);
            String name = (String) map.get("name");
            String title = (String) map.get("title");
            String lastModificationDate = (String) map.get("lastModificationDate");
            String contentStreamMimeType = (String) map.get("contentStreamMimeType");
            String contentStreamLength = (String) map.get("contentStreamLength");
            String creationDate = (String) map.get("creationDate");
            String versionLabel = (String) map.get("versionLabel");

            // TODO: check if the extension exists
            QTableWidgetItem itemName = new QTableWidgetItem(name);
            if( "cmis:document".equals((String) map.get("baseTypeId")) ){
                String ext = name.substring(name.indexOf(".")+1);
                String iconName = "classpath:com/axiastudio/suite/menjazo/resources/page_"+ ext +".png";
                itemName.setIcon(new QIcon(iconName));
            } else if( "cmis:folder".equals((String) map.get("baseTypeId")) ){
                itemName.setIcon(new QIcon("classpath:com/axiastudio/suite/menjazo/resources/folder.png"));
            }
            
            // map in ids
            this.ids.add(map);
            
            // File name
            //itemName.setData(Qt.ItemDataRole.UserRole, map);
            itemName.setFlags(Qt.ItemFlag.ItemIsSelectable, Qt.ItemFlag.ItemIsEnabled);
            tableWidget.setItem(i, 0, itemName);
            itemName.setData(Qt.ItemDataRole.UserRole, i);
            
            // Title
            QTableWidgetItem itemTitle = new QTableWidgetItem(title);
            itemTitle.setFlags(Qt.ItemFlag.ItemIsSelectable, Qt.ItemFlag.ItemIsEnabled);
            tableWidget.setItem(i, 1, itemTitle);
            itemTitle.setData(Qt.ItemDataRole.UserRole, i);
            
            // Version
            QTableWidgetItem itemVersionLabel = new QTableWidgetItem(versionLabel);
            itemVersionLabel.setFlags(Qt.ItemFlag.ItemIsSelectable, Qt.ItemFlag.ItemIsEnabled);
            tableWidget.setItem(i, 2, itemVersionLabel);
            itemVersionLabel.setData(Qt.ItemDataRole.UserRole, i);
            
            // Size
            String readableContentStreamLength="-";
            if( contentStreamLength != null ){
                Long size = Long.parseLong(contentStreamLength);
                readableContentStreamLength = readableSize(size);
            }
            QTableWidgetItem itemContentStreamLength = new QTableWidgetItem(readableContentStreamLength);
            itemContentStreamLength.setFlags(Qt.ItemFlag.ItemIsSelectable, Qt.ItemFlag.ItemIsEnabled);
            tableWidget.setItem(i, 3, itemContentStreamLength);
            itemContentStreamLength.setData(Qt.ItemDataRole.UserRole, i);
            
            // Creation date
            QTableWidgetItem itemCreationDate = new QTableWidgetItem(creationDate);
            itemCreationDate.setFlags(Qt.ItemFlag.ItemIsSelectable, Qt.ItemFlag.ItemIsEnabled);
            tableWidget.setItem(i, 4, itemCreationDate);
            itemCreationDate.setData(Qt.ItemDataRole.UserRole, i);
            
            // Modification date
            QTableWidgetItem itemLastModificationDate = new QTableWidgetItem(lastModificationDate);
            itemLastModificationDate.setFlags(Qt.ItemFlag.ItemIsSelectable, Qt.ItemFlag.ItemIsEnabled);
            tableWidget.setItem(i, 5, itemLastModificationDate);
            itemLastModificationDate.setData(Qt.ItemDataRole.UserRole, i);
            
            // Mime type
            QTableWidgetItem itemContentStreamMimeType = new QTableWidgetItem(contentStreamMimeType);
            itemContentStreamMimeType.setFlags(Qt.ItemFlag.ItemIsSelectable, Qt.ItemFlag.ItemIsEnabled);
            tableWidget.setItem(i, 6, itemContentStreamMimeType);
            itemContentStreamMimeType.setData(Qt.ItemDataRole.UserRole, i);
            
            i++;
        }
        tableWidget.resizeColumnToContents(0);
        
    }

    /*
    private QTableWidgetItem getCurrentItem0() {
        if( tableWidget.currentRow() >= 0 ){
            return tableWidget.currentItem();
            //return qtw.itemAt(qtw.currentRow(), 0);
        }
        return null;
    }*/

    private void debug(HashMap map) {
        for( Object key: map.keySet() ){
            System.out.println(key + ": " + map.get(key));
        }
    }
    
    private void refreshProperties() {
        int currentRow = getOrderedCurrentRow();
        if( currentRow < 0 ){
            return;
        }
        Map map = this.ids.get(currentRow);
        String objectId = (String) map.get("objectId");

        if( "cmis:document".equals((String) map.get("baseTypeId"))){
            ((QLineEdit) this.findChild(QLineEdit.class, "title")).setText((String) map.get("title"));
            ((QTextEdit) this.findChild(QTextEdit.class, "description")).setText((String) map.get("description"));
            cleanDirty();
            List<HashMap> allVersions = this.helper.getAllVersions((String) map.get("objectId"));
            QDockWidget dockWidget = (QDockWidget) this.findChild(QWidget.class, "versionsDockWidget");
            QWidget versionsWidget = new QWidget(dockWidget);
            QVBoxLayout vBoxVersions = new QVBoxLayout();
            versionsWidget.setLayout(vBoxVersions);
            dockWidget.setWidget(versionsWidget);
            QSignalMapper signalMapper = new QSignalMapper();
            for(Map mapVersion: allVersions){
                QHBoxLayout hBox = new QHBoxLayout();
                String checkinComment = "";
                if( mapVersion.get("checkinComment") != null ){
                    checkinComment = (String) mapVersion.get("checkinComment");
                }
                String stringLabel = (String) mapVersion.get("versionLabel") + " " + checkinComment;
                QLabel qLabel = new QLabel(stringLabel);
                String toolTip = "";
                if( mapVersion.get("title") != null ){
                    toolTip += (String) mapVersion.get("title");
                }
                if( mapVersion.get("description") != null ){
                    toolTip += "\n\n" + (String) mapVersion.get("description");
                }
                qLabel.setToolTip(toolTip);
                hBox.addWidget(qLabel);
                QToolButton toolButton = new QToolButton();
                toolButton.setIcon(new QIcon("classpath:com/axiastudio/suite/menjazo/resources/download.png"));
                toolButton.clicked.connect(signalMapper, "map()");
                String value = ((String) mapVersion.get("objectId")) + ";" + ((String) mapVersion.get("contentStreamFileName"));
                signalMapper.setMapping(toolButton, value);
                hBox.addWidget(toolButton);
                vBoxVersions.addLayout(hBox);
            }
            signalMapper.mappedString.connect(this, "downloadVersion(String)");
            vBoxVersions.addStretch();
        }
    }
    
    private void saveProperties(){
        int currentRow = getOrderedCurrentRow();
        if( currentRow < 0 ){
            return;
        }
        Map map = this.ids.get(currentRow);
        String title = ((QLineEdit) this.findChild(QLineEdit.class, "title")).text();
        String description = ((QTextEdit) this.findChild(QTextEdit.class, "description")).toPlainText();
        Map<String, Object> properties = new HashMap();
        properties.put("cm:title", title);
        properties.put("cm:description", description);
        this.helper.setDocumentProperties((String) map.get("objectId"), properties);
        cleanDirty();
        refreshList();
    }
    
    private static String readableSize(Long size) {
        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    private void readDocumentContent(InputStream in, QTemporaryFile temporaryFile) {
        //temporaryFile.setAutoRemove(false);
        byte[] bytes = new byte[1024];
        int read = 0;
        try {
            while( (read=in.read(bytes)) != -1){
                int write = temporaryFile.write(Arrays.copyOfRange(bytes, 0, read));
            }
            in.close();
            temporaryFile.flush();
            temporaryFile.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void readDocumentContent(InputStream in, FileOutputStream out) {
        byte[] bytes = new byte[1024];
        int read = 0;
        try {
            while( (read=in.read(bytes)) != -1){
                out.write(Arrays.copyOfRange(bytes, 0, read));
            }
            in.close();
            out.flush();
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int getOrderedCurrentRow() {
        QTableWidgetItem currentItem = tableWidget.currentItem();
        if( currentItem != null ){
            Object data = currentItem.data(Qt.ItemDataRole.UserRole);
            if( data != null ){
                return (Integer) tableWidget.currentItem().data(Qt.ItemDataRole.UserRole);
            }
        }
        return -1;
    }
    
    private void openAsTemporaryFile(String fileName, InputStream in, String idObject) {
        // the first file, locked by Menjazo
        QTemporaryFile temporaryFile = new QTemporaryFile(QDir.tempPath()+"/menjazo.XXXXXX");
        temporaryFile.open(new QFile.OpenMode(QFile.OpenModeFlag.WriteOnly,
                QFile.OpenModeFlag.Unbuffered));
        readDocumentContent(in, temporaryFile);
        //this.tempFiles.add(new TempFile(null, temporaryFile.fileName(), false, null));
        String newTemporaryFileName = temporaryFile.fileName()+fileName;
        temporaryFile.copy(newTemporaryFileName);
        temporaryFile.close();
        QFile.remove(temporaryFile.fileName());

        this.tempFiles.put(idObject, new TempFile(idObject, newTemporaryFileName, true, null));
        boolean res = QDesktopServices.openUrl(QUrl.fromLocalFile(newTemporaryFileName));
    }

    private void saveFile(String fileName, InputStream in, String idObject) {
        String name = QFileDialog.getSaveFileName(this, "Save file");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(name);
            readDocumentContent(in, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
}
