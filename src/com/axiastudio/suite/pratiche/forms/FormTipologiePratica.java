/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.pratiche.forms;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.suite.pratiche.entities.TipologiaPratica;
import com.trolltech.qt.gui.QDialog;
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
public class FormTipologiePratica extends QDialog {
    
    public FormTipologiePratica(){
        this(null);
    }
        
    public FormTipologiePratica(QWidget parent){
        super(parent);
        QTreeWidget tree = new QTreeWidget();
        QVBoxLayout layout = new QVBoxLayout();
        layout.addWidget(tree);
        this.setLayout(layout);
        this.popola(tree, null, null);
    }
    
    private List<TipologiaPratica> children(EntityManager em, TipologiaPratica parent){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(TipologiaPratica.class);
        Root root = cq.from(TipologiaPratica.class);
        if( parent == null ){
            cq.where(cb.isNull(root.get("tipologiapadre")));
        } else {
            cq.where(cb.equal(root.get("tipologiapadre"), parent));
        }
        Query q = em.createQuery(cq);
        return q.getResultList();
    }
    
    
    public void popola(QTreeWidget tree, QTreeWidgetItem parentItem, TipologiaPratica parent){
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManagerFactory emf = db.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        List<TipologiaPratica> children = this.children(em, parent);
        for( int i=0; i<children.size(); i++ ){
            TipologiaPratica tipologiaPratica = (TipologiaPratica) children.get(i);
            QTreeWidgetItem item = new QTreeWidgetItem();
            item.setText(0, tipologiaPratica.getCodice());
            item.setToolTip(0, "<FONT COLOR=black>" + tipologiaPratica.getDescrizione() + "</FONT>");
            if( parentItem == null ){
                tree.addTopLevelItem(item);
            } else {
                parentItem.addChild(item);
            }
            this.popola(tree, item, tipologiaPratica);
        }
    }
}
