package com.axiastudio.suite.scanndo;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Controller;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.pypapi.ui.Util;
import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.suite.protocollo.entities.Protocollo;
import com.trolltech.qt.core.QByteArray;
import com.trolltech.qt.core.QFile;
import com.trolltech.qt.designer.QUiLoader;
import com.trolltech.qt.designer.QUiLoaderException;
import com.trolltech.qt.gui.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: tiziano
 * Date: 12/05/14
 * Time: 16:15
 */
public class ScanNDo extends QDialog {

    private final QLineEdit scansione;
    private final QSound beep;

    public ScanNDo() {
        this(null);
    }

    public ScanNDo(String preselezione) {
        QFile file = Util.ui2jui(new QFile("classpath:com/axiastudio/suite/scanndo/scanndo.ui"));
        loadUi(file);

        scansione = (QLineEdit) findChild(QLineEdit.class, "scansione");
        scansione.returnPressed.connect(this, "doSomething()");

        beep = new QSound(getClass().getResource("beep.wav").getPath());

        if( preselezione != null ){
            scansione.setText(preselezione);
        }

    }

    private void loadUi(QFile uiFile){
        QDialog dialog = null;
        try {
            dialog = (QDialog) QUiLoader.load(uiFile);
        } catch (QUiLoaderException ex) {
            Logger.getLogger(ScanNDo.class.getName()).log(Level.SEVERE, null, ex);
        }
        for( QByteArray name: dialog.dynamicPropertyNames()){
            this.setProperty(name.toString(), dialog.property(name.toString()));
        }
        setLayout(dialog.layout());
        setModal(true);
        setWindowTitle(dialog.windowTitle());
    }

    private void doSomething(){
        String text = scansione.text();

        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Protocollo> cq = cb.createQuery(Protocollo.class);
        Root from = cq.from(Protocollo.class);
        Predicate predicate = cb.equal(from.get("iddocumento"), text);
        cq.select(from);
        cq.where(predicate);
        TypedQuery<Protocollo> tq = em.createQuery(cq);
        if ( tq.getResultList().size() == 0 ) {
            QMessageBox.warning(this, "Attenzione", "Protocollo non trovato.");
            return;
        }
        Protocollo protocollo = tq.getSingleResult();
        if( !protocollo.getSpedito() ) {
            protocollo.setSpedito(true);
            // utilizzo direttamente l'entity manager così non passo per le callback
            em.getTransaction().begin();
            em.persist(protocollo);
            em.getTransaction().commit();
            beep.play();
        } else {
            QMessageBox.warning(this, "Attenzione", "Protocollo già spedito.");
        }
    }

}
