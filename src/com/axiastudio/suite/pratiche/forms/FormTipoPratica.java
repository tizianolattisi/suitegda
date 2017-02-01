package com.axiastudio.suite.pratiche.forms;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.*;
import com.axiastudio.pypapi.ui.widgets.PyPaPiComboBox;
import com.axiastudio.suite.SuiteBaseForm;
import com.axiastudio.suite.pratiche.entities.TipoPratica;
import com.axiastudio.suite.protocollo.entities.Fascicolo;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QMessageBox;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by Comune di Riva del Garda on 10/11/2014.
 */

public class FormTipoPratica  extends SuiteBaseForm {

    public FormTipoPratica(String uiFile, Class entityClass, String title) {
        super(uiFile, entityClass, title);
        try {
            Method storeFactory = this.getClass().getMethod("storeTipo");
            Register.registerUtility(storeFactory, IStoreFactory.class, "Tipopadre");
            storeFactory = this.getClass().getMethod("storeFascicolo");
            Register.registerUtility(storeFactory, IStoreFactory.class, "Fascicolo");
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(FormPratica.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(FormPratica.class.getName()).log(Level.SEVERE, null, ex);
        }
        ((QComboBox) this.findChild(QComboBox.class, "comboBox_tipopadre")).currentIndexChanged.connect(this, "aggiornaCodicePadre()");
        ((QLineEdit) this.findChild(QLineEdit.class, "lineEdit_codicefiglio")).textChanged.connect(this, "aggiornaCodiceFiglio()");
    }

/*
 * Uno store contenente gli oggetti ordinati x descrizione; solo tipologie 'foglie' e non obsolete
 */
    public Store storeTipo(){
        Database db = (Database) Register.queryUtility(IDatabase.class);
        Controller controller = db.createController(TipoPratica.class);
        Store storeTipo = controller.createFullStore();
        List<TipoPratica> oggetti = new ArrayList<TipoPratica>();
        for(Object ogg: storeTipo){
            if ( ((TipoPratica) ogg).getObsoleta().equals(Boolean.FALSE) && ((TipoPratica) ogg).getFoglia().equals(Boolean.FALSE) ) {
                oggetti.add((TipoPratica) ogg);
            }
        }
        return new Store(oggetti);
    }

    public Store storeFascicolo() {
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Fascicolo> cq = cb.createQuery(Fascicolo.class);
        Root<Fascicolo> root = cq.from(Fascicolo.class);
        cq.select(root);
        cq.where(cb.isNull(root.get("al")));
        TypedQuery<Fascicolo> tq = em.createQuery(cq);
        List <Fascicolo> titolario=tq.getResultList();
        if( titolario.size() == 0 ){
            QMessageBox.warning(this, "Attenzione", "Titolario non trovato");
        }
        return new Store(titolario);
    }


    private String aggiornaCodicePadre() {
        String codpadre = "";
        if ( this.getContext()!=null && this.getContext().getCurrentEntity()!=null &&
                    ((TipoPratica) this.getContext().getCurrentEntity()).getId()==null ) {
            PyPaPiComboBox cmbCodice=((PyPaPiComboBox) this.findChild(PyPaPiComboBox.class, "comboBox_tipopadre"));
            QLineEdit codicePadre=((QLineEdit) this.findChild(QLineEdit.class, "lineEdit_codicepadre"));
            QLineEdit codiceFiglio=((QLineEdit) this.findChild(QLineEdit.class, "lineEdit_codicefiglio"));
            QLineEdit codice=((QLineEdit) this.findChild(QLineEdit.class, "lineEdit_codice"));
            if ( cmbCodice.getCurrentEntity()!=null ) {
                codpadre = ((TipoPratica) cmbCodice.getCurrentEntity()).getCodice();
            }
            codicePadre.setText(codpadre);
            codice.setText(codpadre + codiceFiglio.text().toUpperCase());
            if (codpadre.equals("")) {
                codiceFiglio.setMaxLength(3);
            } else {
                codiceFiglio.setMaxLength(2);
            }
        }
        return codpadre;
    }

    private void aggiornaCodiceFiglio() {
        QLineEdit codicePadre=((QLineEdit) this.findChild(QLineEdit.class, "lineEdit_codicepadre"));
        QLineEdit codiceFiglio=((QLineEdit) this.findChild(QLineEdit.class, "lineEdit_codicefiglio"));
        QLineEdit codice=((QLineEdit) this.findChild(QLineEdit.class, "lineEdit_codice"));

        codice.setText(codicePadre.text() + codiceFiglio.text().toUpperCase());
    }

    @Override
    protected void indexChanged(int row) {
        super.indexChanged(row);
        if ( this.getContext()!=null ) {
            TipoPratica tipopratica = (TipoPratica) this.getContext().getCurrentEntity();

            ((QComboBox) this.findChild(QComboBox.class, "comboBox_tipopadre")).setEnabled(tipopratica.getId() == null);
            ((QLineEdit) this.findChild(QLineEdit.class, "lineEdit_codice")).setEnabled(tipopratica.getId() == null);

            QLineEdit codicePadre=((QLineEdit) this.findChild(QLineEdit.class, "lineEdit_codicepadre"));
            QLineEdit codiceFiglio=((QLineEdit) this.findChild(QLineEdit.class, "lineEdit_codicefiglio"));

            if (tipopratica.getId() != null) {
                String codpadre = "";
                if (tipopratica.getTipopadre() != null) {
                    codpadre = tipopratica.getTipopadre().getCodice();
                }
                codicePadre.setText(codpadre);

                String codfiglio = tipopratica.getCodice().replaceFirst(codpadre, "");
                codiceFiglio.setText(codfiglio);
            } else {
                codicePadre.setText("");
                codiceFiglio.setText("");
            }
        }
    }
}
