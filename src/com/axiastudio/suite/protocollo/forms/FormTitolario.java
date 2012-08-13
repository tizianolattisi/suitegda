/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.protocollo.forms;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.suite.protocollo.entities.Fascicolo;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QTreeWidget;
import com.trolltech.qt.gui.QTreeWidgetItem;
import com.trolltech.qt.gui.QVBoxLayout;
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

    public FormTitolario(){
        QTreeWidget tree = new QTreeWidget();
        QVBoxLayout layout = new QVBoxLayout();
        layout.addWidget(tree);
        this.setLayout(layout);
        this.popola(tree);
    }
    
    public void popola(QTreeWidget tree){
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManagerFactory emf = db.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(Fascicolo.class);
        Root root = cq.from(Fascicolo.class);
        cq.orderBy(cb.asc(root.get("categoria")), cb.asc(root.get("categoria")), cb.asc(root.get("classe")), cb.asc(root.get("fascicolo")));
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
                    tree.addTopLevelItem(itemCategoria);
                } else {
                    itemClasse = new QTreeWidgetItem();
                    String descrizione = "(" + fascicolo.getCategoria() + "-" +
                            fascicolo.getClasse() + ") " + fascicolo.getDescrizione();
                    itemClasse.setText(0, descrizione);
                    itemCategoria.addChild(itemClasse);
                }
            } else {
                itemFascicolo = new QTreeWidgetItem();
                String descrizione = "(" + fascicolo.getCategoria() + "-" +
                        fascicolo.getClasse() + "-" + fascicolo.getFascicolo() +
                        ") " + fascicolo.getDescrizione();
                itemFascicolo.setText(0, descrizione);
                itemClasse.addChild(itemFascicolo);
                
            }
        }
    }
}
