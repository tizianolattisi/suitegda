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
import com.axiastudio.pypapi.db.*;
import com.axiastudio.pypapi.ui.Dialog;
import com.axiastudio.pypapi.ui.widgets.PyPaPiComboBox;
import com.axiastudio.pypapi.ui.widgets.PyPaPiDateEdit;
import com.axiastudio.suite.anagrafiche.entities.*;
import com.axiastudio.suite.protocollo.ISoggettoProtocollo;
import com.axiastudio.suite.protocollo.entities.Protocollo;
import com.axiastudio.suite.protocollo.entities.SoggettoProtocollo;
import com.axiastudio.suite.protocollo.entities.Titolo;
import com.trolltech.qt.core.QDate;
import com.trolltech.qt.gui.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class FormSoggettoProtocollo extends Dialog {
    private PyPaPiComboBox cmbReferenti;
    private QComboBox cmbIndirizzi;
    private QLabel indirizzo;
    private ISoggettoProtocollo sp;
    private Protocollo protocollo;

    public FormSoggettoProtocollo(String uiFile, Class entityClass, String title){
        super(uiFile, entityClass, title);
        cmbReferenti = (PyPaPiComboBox) this.findChild(PyPaPiComboBox.class, "comboBox_referente");
        cmbReferenti.currentIndexChanged.connect(this, "aggiornaReferente()");
        cmbIndirizzi = (QComboBox) this.findChild(QComboBox.class, "comboBox_indirizzo");
        cmbIndirizzi.activated.connect(this, "aggiornaIndirizzo()");
        indirizzo= (QLabel) this.findChild(QLabel.class, "label_indirizzo");

        QPushButton clearButton=(QPushButton) this.findChild(QPushButton.class, "clearButton");
        clearButton.setIcon(new QIcon("classpath:com/axiastudio/suite/resources/cancel.png"));
        clearButton.clicked.connect(this, "clearReferente()");
        QPushButton clearButtonIndirizzo=(QPushButton) this.findChild(QPushButton.class, "clearButtonIndirizzo");
        clearButtonIndirizzo.setIcon(new QIcon("classpath:com/axiastudio/suite/resources/cancel.png"));
        clearButtonIndirizzo.clicked.connect(this, "clearIndirizzo()");

        try {
            Method storeFactory = this.getClass().getMethod("storeTitolo");
            Register.registerUtility(storeFactory, IStoreFactory.class, "Titolo");
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(FormSoggettoProtocollo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(FormSoggettoProtocollo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    protected void showEvent(QShowEvent arg__1) {

        sp = (ISoggettoProtocollo) this.getContext().getCurrentEntity();
        if ( sp!=null ) {
            protocollo = sp.getProtocollo();
            if (protocollo == null) {
                protocollo = (Protocollo) this.getParentForm().getContext().getCurrentEntity();
            }
            if ( sp.getDatainizio()==null ){
                Calendar calendar = Calendar.getInstance();
                if (  protocollo != null && protocollo.getDataprotocollo()!=null ) {
                    calendar.setTime(protocollo.getDataprotocollo());
                }
                QDate datainizio = new QDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH));
                ((QDateEdit) this.findChild(PyPaPiDateEdit.class, "dateEdit_datainizio")).setDate(datainizio);
                sp.setDatainizio(calendar.getTime());      // TODO: aggiornamento framework x gestione modifiche di valori da codice
            }
        }

        // indirizzo
        Store storeIndirizzo=storeIndirizzi();
        cmbIndirizzi.addItems(storeIndirizzo);
        cmbIndirizzi.setCurrentIndex(cmbIndirizzi.findText(""));
        indirizzo.setText(sp.getIndirizzo());

        // referente
        Store store = storeReferenti();
        cmbReferenti.setLookupStore(store);
        this.getColumn("ReferenteRelazione").setLookupStore(store);
        cmbReferenti.select(null);

        super.showEvent(arg__1);
    }

    public void aggiornaReferente(){

        if (sp != null ) {
            QLabel predicato = (QLabel) this.findChild(QLabel.class, "label_predicato");
            predicato.setText(sp.getPredicato());
        }

    }

    public void clearReferente() {
        sp.setSoggettoReferente(null);      // TODO: aggiornamento framework x gestione modifiche di valori da codice
        aggiornaReferente();
    }

    public void aggiornaIndirizzo(){
        if (sp != null ) {
            if ( !cmbIndirizzi.currentText().equals(sp.getIndirizzo()) ) {
                sp.setIndirizzo(cmbIndirizzi.currentText());
                indirizzo.setText(sp.getIndirizzo());
                this.getContext().getDirty();
            }
        }
    }

    public void clearIndirizzo() {
        sp.setIndirizzo(null);
        indirizzo.setText(sp.getIndirizzo());
        this.getContext().getDirty();
    }

    /*
 * Uno store contenente gli oggetti ordinati x descrizione
 */
    public Store storeTitolo(){
        Database db = (Database) Register.queryUtility(IDatabase.class);
        Controller controller = db.createController(Titolo.class);
        Store storeTitolo = controller.createFullStore();
        List<Titolo> titoli = new ArrayList<Titolo>();
        for(Object ogg: storeTitolo){
            titoli.add((Titolo) ogg);
        }
        Collections.sort(titoli, Titolo.Comparators.DESCRIZIONE);
        return new Store(titoli);
    }

    /*
     * Uno store contenente i possibili referenti
     */
    public Store storeReferenti(){

        List<RelazioneSoggetto> referenti = new ArrayList<RelazioneSoggetto>();

        if( protocollo == null || sp == null || sp.getSoggetto() == null ||
                            sp.getSoggetto().getTipo()== TipoSoggetto.PERSONA){
            return new Store(referenti);
        }

        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        cmbReferenti.setCurrentIndex(0);

        Date dataProtocollo=protocollo.getDataprotocollo();
        if (dataProtocollo == null) {
            Calendar calendar = Calendar.getInstance();
            dataProtocollo = calendar.getTime();
        }

        referenti = em.createNamedQuery("trovaReferenteSoggetto", RelazioneSoggetto.class)
                .setParameter("id", sp.getSoggetto().getId())
                .setParameter("tipo", TipoSoggetto.PERSONA)
                .setParameter("data", dataProtocollo)
                .getResultList();

//        Collections.sort(referenti, RelazioneSoggetto.Comparators.DESCRIZIONE);
        
        return new Store(referenti);
    }

    /*
     * Uno store contenente i possibili indirizzi
     */
    public Store storeIndirizzi() {
        List<String> indirizzoList = new ArrayList<String>();

        if (protocollo == null || sp == null || sp.getSoggetto() == null) {
            return new Store(indirizzoList);
        }
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Indirizzo> cq = cb.createQuery(Indirizzo.class);
        Root<Indirizzo> root = cq.from(Indirizzo.class);
        cq=cq.select(root);
        cq=cq.where(cb.equal(root.get(Indirizzo_.soggetto), sp.getSoggetto()));
        TypedQuery<Indirizzo> query = em.createQuery(cq);
        for ( Indirizzo indirizzo: query.getResultList()) {
            indirizzoList.add(indirizzo.toString());
        }
        indirizzoList.add("");
        return new Store(indirizzoList);
    }
}
