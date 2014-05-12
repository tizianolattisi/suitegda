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
import com.trolltech.qt.gui.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.print.*;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.standard.PrinterName;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class DialogStampaEtichetta extends QDialog {
    
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
        cq.where(cb.and(cb.equal(root.get("contesto"), className),
                        cb.equal(root.get("linguaggio"), language)));
        TypedQuery<Etichetta> tq = em.createQuery(cq);
        List<Etichetta> etichette = tq.getResultList();
        for( Etichetta etichetta: etichette ){
            comboBox.addItem(etichetta.getDescrizione(), etichetta);
        }
    }

    @Override
    public void accept() {
        try {
//locate printer
            Application app = Application.getApplicationInstance();
            String device = (String) app.getConfigItem("barcode.device");
            AttributeSet attributeSet = new HashAttributeSet();
            attributeSet.add(new PrinterName(device, null));
            PrintService[] printService = PrintServiceLookup.lookupPrintServices(null, attributeSet);
            if (printService.length == 0) {
                QMessageBox.warning(this, "Attenzione", "Impossibile contattare la stampante.");
                return;
            } else {
                System.out.println("Printer online: "+printService[0]);
            }
//create a print job
            DocPrintJob job = printService[0].createPrintJob();
//define the print document
            Etichetta etichetta = (Etichetta) comboBox.itemData(comboBox.currentIndex());
            MessageMapFormat mmp = new MessageMapFormat(etichetta.getDefinizione());
            String codice = mmp.format(this.map);
            InputStream is = new ByteArrayInputStream(codice.getBytes());
//print the data
            Doc doc = new SimpleDoc(is, DocFlavor.INPUT_STREAM.AUTOSENSE, null);
            job.print(doc, null);

            is.close();
            System.out.println("Printing Done!!");
        } catch (Exception ex) {
            Logger.getLogger(DialogStampaEtichetta.class.getName()).log(Level.SEVERE, null, ex);
        }
        super.accept();
    }

}
