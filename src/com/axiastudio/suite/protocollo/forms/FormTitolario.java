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
package com.axiastudio.suite.protocollo.forms;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.suite.protocollo.entities.Fascicolo;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class FormTitolario extends QDialog {
    private final QTreeWidget tree;
    private final QComboBox filtro;

    public FormTitolario(){
        this(null);
    }
        
    public FormTitolario(QWidget parent){
        super(parent);
        QVBoxLayout layout = new QVBoxLayout();

        QHBoxLayout filterLayout = new QHBoxLayout();
        QLabel label = new QLabel("Titolario del: ");
        filterLayout.addWidget(label);
        filtro = new QComboBox();
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        Query q=em.createNativeQuery("select distinct dal from protocollo.fascicolo order by dal desc");
        List <Date> dataDal = q.getResultList();
        for ( Date data: dataDal) {
            filtro.addItem((new SimpleDateFormat("dd/MM/yyyy")).format(data));
        }
        filterLayout.addWidget(filtro);
        filtro.currentIndexChanged.connect(this, "aggiornaTitolario()");
        filterLayout.addSpacerItem(new QSpacerItem(10, 10, QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Minimum));
        layout.addLayout(filterLayout);

        this.tree = new QTreeWidget();
        this.tree.header().hide();
        this.tree.doubleClicked.connect(this, "accept()");
        layout.addWidget(this.tree);

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
        this.popola(this.tree);
    }
    
    public Fascicolo getSelection() {
        QTreeWidgetItem currentItem = this.tree.currentItem();
        return (Fascicolo) currentItem.data(0, Qt.ItemDataRole.UserRole);
    }

    private void aggiornaTitolario() {
        this.popola(this.tree);
    }

    private void popola(QTreeWidget tree){
        tree.clear();

        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManagerFactory emf = db.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(Fascicolo.class);
        Root root = cq.from(Fascicolo.class);
        try {
            cq.where(cb.equal(root.get("dal"), (new SimpleDateFormat("dd/MM/yyyy")).parse(filtro.currentText())));
        }
        catch (Exception e) {
            System.out.println("Errore di interpretazione della data " + filtro.currentText());
            return;
        }
        cq.orderBy(cb.asc(root.get("categoria")), cb.asc(root.get("classe")), cb.asc(root.get("fascicolo")));
        Query q = em.createQuery(cq);
        List store = q.getResultList();
        QTreeWidgetItem itemCategoria=null;
        QTreeWidgetItem itemClasse=null;
        QTreeWidgetItem itemFascicolo;
        for (Object aStore : store) {
            Fascicolo fascicolo = (Fascicolo) aStore;
            if (fascicolo.getFascicolo() == 0) {
                if (fascicolo.getClasse() == 0) {
                    itemCategoria = new QTreeWidgetItem();
                    String descrizione = "(" + fascicolo.getCategoria() +
                            ") " + fascicolo.getDescrizione();
                    itemCategoria.setText(0, descrizione);
                    itemCategoria.setData(0, Qt.ItemDataRole.UserRole, fascicolo);
                    tree.addTopLevelItem(itemCategoria);
                } else {
                    itemClasse = new QTreeWidgetItem();
                    String descrizione = "(" + fascicolo.getCategoria() + "-" +
                            fascicolo.getClasse() + ") " + fascicolo.getDescrizione();
                    itemClasse.setText(0, descrizione);
                    if (!"".equals(fascicolo.getNote())) {
                        itemClasse.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/note.png"));
                        itemClasse.setToolTip(0, "<FONT COLOR=black>" + fascicolo.getNote() + "</FONT>");
                        itemClasse.setData(0, Qt.ItemDataRole.UserRole, fascicolo);
                    }
                    itemCategoria.addChild(itemClasse);
                }
            } else {
                itemFascicolo = new QTreeWidgetItem();
                String descrizione = "(" + fascicolo.getCategoria() + "-" +
                        fascicolo.getClasse() + "-" + fascicolo.getFascicolo() +
                        ") " + fascicolo.getDescrizione();
                itemFascicolo.setText(0, descrizione);
                if (!"".equals(fascicolo.getNote())) {
                    itemFascicolo.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/note.png"));
                    itemFascicolo.setToolTip(0, "<FONT COLOR=black>" + fascicolo.getNote() + "</FONT>");
                    itemFascicolo.setData(0, Qt.ItemDataRole.UserRole, fascicolo);
                }
                itemClasse.addChild(itemFascicolo);
            }
        }
    }
}
