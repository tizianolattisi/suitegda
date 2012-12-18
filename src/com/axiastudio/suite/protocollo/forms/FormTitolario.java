/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.protocollo.forms;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.suite.protocollo.entities.Fascicolo;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QDialogButtonBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QSpacerItem;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QTreeWidget;
import com.trolltech.qt.gui.QTreeWidgetItem;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class FormTitolario extends QDialog {
    private final QTreeWidget tree;

    public FormTitolario(){
        this(null);
    }
        
    public FormTitolario(QWidget parent){
        super(parent);
        this.tree = new QTreeWidget();
        this.tree.header().hide();
        this.tree.doubleClicked.connect(this, "accept()");
        QVBoxLayout layout = new QVBoxLayout();
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
        Fascicolo fascicolo = (Fascicolo) currentItem.data(0, Qt.ItemDataRole.UserRole);
        return fascicolo;
    }

    private void popola(QTreeWidget tree){
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManagerFactory emf = db.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(Fascicolo.class);
        Root root = cq.from(Fascicolo.class);
        cq.orderBy(cb.asc(root.get("categoria")), cb.asc(root.get("classe")), cb.asc(root.get("fascicolo")));
        Query q = em.createQuery(cq);
        List store = q.getResultList();
        QTreeWidgetItem itemCategoria=null;
        QTreeWidgetItem itemClasse=null;
        QTreeWidgetItem itemFascicolo;
        for( int i=0; i<store.size(); i++ ){
            Fascicolo fascicolo = (Fascicolo) store.get(i);
            if( fascicolo.getFascicolo() == 0){
                if( fascicolo.getClasse() == 0){
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
                    if( !"".equals(fascicolo.getNote()) ){
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
                if( !"".equals(fascicolo.getNote()) ){
                    itemFascicolo.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/note.png"));
                    itemFascicolo.setToolTip(0, "<FONT COLOR=black>" + fascicolo.getNote() + "</FONT>");
                    itemFascicolo.setData(0, Qt.ItemDataRole.UserRole, fascicolo);
                }
                itemClasse.addChild(itemFascicolo);
            }
        }
    }
}
