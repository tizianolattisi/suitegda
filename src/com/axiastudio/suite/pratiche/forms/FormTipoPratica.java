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
package com.axiastudio.suite.pratiche.forms;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.pypapi.ui.widgets.PyPaPiComboBox;
import com.axiastudio.suite.pratiche.entities.Pratica;
import com.axiastudio.suite.pratiche.entities.TipoPratica;
import com.axiastudio.suite.procedimenti.entities.TipoPraticaProcedimento;
import com.axiastudio.suite.protocollo.entities.Fascicolo;
import com.axiastudio.suite.protocollo.forms.FormTitolario;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QSpacerItem;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QTreeWidget;
import com.trolltech.qt.gui.QTreeWidgetItem;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Predicate;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class FormTipoPratica extends QDialog {
    private QTreeWidget tree;
    private Pratica pratica=null;
    private List ids;
    
    public FormTipoPratica(){
        this(null);
    }
        
    public FormTipoPratica(QWidget parent){
        this(parent, null);
    }

    public FormTipoPratica(QWidget parent, Pratica pratica){
        super(parent);
        this.pratica = pratica;
        tree = new QTreeWidget();
        this.tree.header().hide();
        this.tree.doubleClicked.connect(this, "accept()");
        /* fascicolazione */
        QToolButton toolButtonTitolario = (QToolButton) this.findChild(QToolButton.class, "toolButtonTitolario");
        toolButtonTitolario.setIcon(new QIcon("classpath:com/axiastudio/suite/resources/email_go.png"));
        toolButtonTitolario.clicked.connect(this, "apriTitolario()");
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
        this.popola(this.tree, null, null);
    }
    
    private List<TipoPratica> children(EntityManager em, TipoPratica parent){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(TipoPratica.class);
        Root root = cq.from(TipoPratica.class);
        
        List<Predicate> predicates = new ArrayList();

        // tipologie non obsolete
        predicates.add(cb.isFalse(root.get("obsoleta")));       
        
//        if( parent == null ){
//            cq.where(cb.isNull(root.get("tipopadre")));
//        } else {
//            cq.where(cb.equal(root.get("tipopadre"), parent));
//       }
        
        if( parent == null ){
            predicates.add(cb.isNull(root.get("tipopadre")));
        } else {
            predicates.add(cb.equal(root.get("tipopadre"), parent));
        }
        // where
        cq = cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        Query q = em.createQuery(cq);
        return q.getResultList();
    }
    
     public TipoPratica getSelection() {
        QTreeWidgetItem currentItem = this.tree.currentItem();
        TipoPratica tipo = (TipoPratica) currentItem.data(0, Qt.ItemDataRole.UserRole);
        return tipo;
    }
   
    public void popola(QTreeWidget tree, QTreeWidgetItem parentItem, TipoPratica parent){
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManagerFactory emf = db.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        
        // cerco gli id di tipo pratica validi
        if( this.pratica != null ){
            ids = em.createNamedQuery("trovaTipiPraticaPermessiDaAttribuzioni", TipoPraticaProcedimento.class)
                                          .setParameter("id", this.pratica.getAttribuzione().getId())
                                          .getResultList();
        } else {
            ids = new ArrayList();
        }
        makeTree(em, parent, parentItem, tree);        
    }

// by FormProtocollo
    private void apriTitolario() {
        FormTitolario titolario = new FormTitolario();
        int exec = titolario.exec();
        if( exec == 1 ){
            Fascicolo selection = titolario.getSelection();
            PyPaPiComboBox comboBoxTitolario = (PyPaPiComboBox) this.findChild(PyPaPiComboBox.class, "comboBoxTitolario");
            comboBoxTitolario.select(selection);
//            this.getContext().getDirty();
        }
    }
    
    private void makeTree(EntityManager em, TipoPratica parent, QTreeWidgetItem parentItem, QTreeWidget tree) {
        // costruisco il tree
        List<TipoPratica> children = this.children(em, parent);
        for( int i=0; i<children.size(); i++ ){
            TipoPratica tipoPratica = (TipoPratica) children.get(i);
            QTreeWidgetItem item = new QTreeWidgetItem();
            item.setText(0, tipoPratica.getCodice());
            item.setToolTip(0, "<FONT COLOR=black>" + tipoPratica.getDescrizione() + "</FONT>");
            if( ids.contains(tipoPratica.getId()) ){
                // tipologia selezionabile
                item.setIcon(0, new QIcon("classpath:com/axiastudio/pypapi/ui/resources/toolbar/accept.png"));
                item.setData(0, Qt.ItemDataRole.UserRole, tipoPratica);
            }
            if( parentItem == null ){
                tree.addTopLevelItem(item);
            } else {
                parentItem.addChild(item);
            }
            if (tipoPratica.getFoglia().booleanValue() == Boolean.FALSE) {
                this.makeTree(em, tipoPratica, item, tree);
            }
        }
    }
    
}
