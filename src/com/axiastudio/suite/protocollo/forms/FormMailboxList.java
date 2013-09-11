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
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axiastudio.suite.protocollo.forms;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.suite.email.EmailHelper;
import com.axiastudio.suite.protocollo.entities.Mailbox;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class FormMailboxList extends QDialog {

    private QTableWidget tableWidget;

    public FormMailboxList(){
        this(null);
    }

    public FormMailboxList(QWidget parent){
        super(parent);
        initLayout();
        refreshMailboxes();
    }

    private void initLayout(){
        tableWidget = new QTableWidget();
        tableWidget.verticalHeader().hide();
        tableWidget.setSelectionBehavior(QAbstractItemView.SelectionBehavior.SelectRows);
        tableWidget.horizontalHeader().setResizeMode(QHeaderView.ResizeMode.ResizeToContents);
        QVBoxLayout layout = new QVBoxLayout();
        QHBoxLayout buttonLayout = new QHBoxLayout();
        QPushButton refreshButton = new QPushButton("Aggiorna");
        refreshButton.clicked.connect(this, "refreshMailboxes()");
        buttonLayout.addWidget(refreshButton);
        QPushButton openButton = new QPushButton("Apri");
        openButton.clicked.connect(this, "openMailbox()");
        buttonLayout.addWidget(openButton);
        buttonLayout.addSpacerItem(new QSpacerItem(10, 10, QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Minimum));
        layout.addLayout(buttonLayout);
        layout.addWidget(this.tableWidget);
        this.setLayout(layout);
    }

    private void refreshMailboxes(){
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManagerFactory emf = db.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(Mailbox.class);
        Root root = cq.from(Mailbox.class);
        Query q = em.createQuery(cq);
        List<Mailbox> mailboxes = q.getResultList();
        while( tableWidget.rowCount() > 0 ){
            tableWidget.removeRow(0);
        }
        tableWidget.setColumnCount(3);
        List<String> labels = new ArrayList();
        labels.add("Mailbox");
        labels.add("Da leggere");
        labels.add("Totali");
        tableWidget.setHorizontalHeaderLabels(labels);
        for( int i=0; i<mailboxes.size(); i++ ){
            Mailbox mailbox = mailboxes.get(i);
            tableWidget.insertRow(i);
            QTableWidgetItem itemHost = new QTableWidgetItem(mailbox.getDescrizione());
            itemHost.setData(Qt.ItemDataRole.UserRole, mailbox);
            itemHost.setFlags(Qt.ItemFlag.ItemIsSelectable, Qt.ItemFlag.ItemIsEnabled);
            tableWidget.setItem(i, 0, itemHost);
            EmailHelper helper = new EmailHelper(mailbox);
            helper.open();
            QTableWidgetItem itemTotali = new QTableWidgetItem(helper.getMessageCount().toString());
            itemTotali.setFlags(Qt.ItemFlag.ItemIsSelectable, Qt.ItemFlag.ItemIsEnabled);
            QTableWidgetItem itemNonLetti = new QTableWidgetItem(helper.getUnreadMessageCount().toString());
            itemNonLetti.setFlags(Qt.ItemFlag.ItemIsSelectable, Qt.ItemFlag.ItemIsEnabled);
            helper.close();
            tableWidget.setItem(i, 1, itemNonLetti);
            tableWidget.setItem(i, 2, itemTotali);
        }
    }

    private void openMailbox() {
        QTableWidgetItem item = tableWidget.item(tableWidget.currentRow(), 0);
        Mailbox mailbox = (Mailbox) item.data(Qt.ItemDataRole.UserRole);
        FormMailbox form = new FormMailbox(this, mailbox);
        form.show();
    }
}
