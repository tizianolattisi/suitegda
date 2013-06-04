/*
 * Copyright (C) 2013 AXIA Studio (http://www.axiastudio.com)
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
 * You should have received a copy of the GNU Afffero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axiastudio.suite.generale.forms;

import com.axiastudio.mapformat.MessageMapFormat;
import com.axiastudio.pypapi.Application;
import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.suite.generale.entities.Etichetta;
import com.axiastudio.suite.generale.entities.Etichetta_;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QMessageBox;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QSpacerItem;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class DialogStampaEtichetta extends QDialog {
    
    private static final String COMMAND = "lp -d ";
    private QComboBox comboBox = new QComboBox();
    private String className;
    private Map map;
    
    public DialogStampaEtichetta(){
        this(null, new HashMap());
    }

    public DialogStampaEtichetta(QWidget parent){
        this(parent, new HashMap());
    }

    public DialogStampaEtichetta(Map map){
        this(null, map);
    }

    public DialogStampaEtichetta(QWidget parent, Map map){
        super(parent);
        this.map = map;
        className = ((Window) parent).getContext().getRootClass().getName();
        QVBoxLayout layout = new QVBoxLayout();
        layout.addWidget(comboBox);
        QHBoxLayout buttonLayout = new QHBoxLayout();
        buttonLayout.addSpacerItem(new QSpacerItem(10, 10, QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Minimum));
        QToolButton cancel = new QToolButton();
        cancel.setIcon(new QIcon("classpath:com/axiastudio/pypapi/ui/resources/toolbar/cancel.png"));
        cancel.clicked.connect(this, "reject()");
        buttonLayout.addWidget(cancel);
        QToolButton accept = new QToolButton();
        accept.setIcon(new QIcon("classpath:com/axiastudio/pypapi/ui/resources/toolbar/accept.png"));
        accept.clicked.connect(this, "accept()");
        buttonLayout.addWidget(accept);
        layout.addLayout(buttonLayout);
        this.setLayout(layout);
        this.popolaComboBox();
    }
    
    private void popolaComboBox(){
        Application app = Application.getApplicationInstance();
        String device = (String) app.getConfigItem("barcode.device");
        String language = (String) app.getConfigItem("barcode.language");
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Etichetta> cq = cb.createQuery(Etichetta.class);
        Root<Etichetta> root = cq.from(Etichetta.class);
        cq.select(root);
        cq.where(cb.and(cb.equal(root.get(Etichetta_.contesto), className),
                        cb.equal(root.get(Etichetta_.linguaggio), language)));
        TypedQuery<Etichetta> tq = em.createQuery(cq);
        List<Etichetta> etichette = tq.getResultList();
        for( Etichetta etichetta: etichette ){
            comboBox.addItem(etichetta.getDescrizione(), etichetta);
        }
    }

    @Override
    public void accept() {
        Application app = Application.getApplicationInstance();
        String device = (String) app.getConfigItem("barcode.device");
        Etichetta etichetta = (Etichetta) comboBox.itemData(comboBox.currentIndex());
        Runtime runtime = Runtime.getRuntime();
        try {
            Process proc = runtime.exec(COMMAND + " " + device);
            OutputStream outputStream = proc.getOutputStream();
            MessageMapFormat mmp = new MessageMapFormat(etichetta.getDefinizione());
            String codice = mmp.format(this.map);
            outputStream.write(codice.getBytes("UTF-16LE"));
            outputStream.flush();
            proc.waitFor();
            int exit = proc.exitValue();
            proc.destroy();
        } catch (InterruptedException ex) {
            Logger.getLogger(DialogStampaEtichetta.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            QMessageBox.warning(this, "Attenzione", "Impossibile contattare la stampante.");
            Logger.getLogger(DialogStampaEtichetta.class.getName()).log(Level.SEVERE, null, ex);
        }
        super.accept();
    }
    
    
}
