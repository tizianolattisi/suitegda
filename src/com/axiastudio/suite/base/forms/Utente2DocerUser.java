package com.axiastudio.suite.base.forms;

import com.axiastudio.pypapi.Application;
import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.suite.base.entities.UfficioUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.base.entities.Utente_;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.*;
import it.tn.rivadelgarda.comune.gda.docer.DocerHelper;
import it.tn.rivadelgarda.comune.gda.docer.KeyValuePairFactory;
import it.tn.rivadelgarda.comune.gda.docer.keys.MetadatiGruppi;
import it.tn.rivadelgarda.comune.gda.docer.keys.MetadatiUtente;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.trolltech.qt.core.Qt.AlignmentFlag.AlignHCenter;

/**
 * User: Michela Piva - Comune di Riva del Garda
 */

public class Utente2DocerUser extends QDialog {

    private QLabel labelDal;
    private QLabel labelAl;
    private QLabel labelPwd;
    private QSpinBox spinUtenteDal;
    private QSpinBox spinUtenteAl;
    private QLineEdit editPwd;
    private QToolButton buttonOk;
    private QToolButton buttonResetPwd;
    private QCheckBox checkInserisciUtenti;

    public Utente2DocerUser() {

        super(null, new Qt.WindowFlags(Qt.WindowType.Tool, Qt.WindowType.WindowStaysOnTopHint));

         // geometry
        setGeometry(200, 200, 200, 100);
        setWindowTitle("Export utenti in Doc/ER");

        QHBoxLayout hBox = new QHBoxLayout();
        hBox.setMargin(0);

        QVBoxLayout vBox = new QVBoxLayout();
        vBox.setMargin(4);

        labelDal = new QLabel();
        labelDal.setText("Utenti: dal numero");
        hBox.addWidget(labelDal);

        spinUtenteDal = new QSpinBox();
        spinUtenteDal.setRange(1, 1000);
        hBox.addWidget(spinUtenteDal);

        labelAl = new QLabel();
        labelAl.setText("al numero");
        hBox.addWidget(labelAl);

        spinUtenteAl = new QSpinBox();
        spinUtenteAl.setRange(0, 1000);
        hBox.addWidget(spinUtenteAl);
        vBox.addLayout(hBox);

        checkInserisciUtenti = new QCheckBox("Inserire anche uffici");
        vBox.addWidget(checkInserisciUtenti, 0, AlignHCenter);

        buttonOk = new QToolButton();
        buttonOk.setText("Esporta");
        buttonOk.clicked.connect(this, "exportUtenti()");
        vBox.addWidget(buttonOk, 0, AlignHCenter);

        vBox.addSpacing(10);

        QHBoxLayout hBoxPwd = new QHBoxLayout();
        hBoxPwd.setMargin(0);

        labelPwd = new QLabel();
        labelPwd.setText("Password");
        hBoxPwd.addWidget(labelPwd);

        editPwd = new QLineEdit();
        hBoxPwd.addWidget(editPwd);
        vBox.addLayout(hBoxPwd);

        buttonResetPwd = new QToolButton();
        buttonResetPwd.setText("Reset password");
        buttonResetPwd.clicked.connect(this, "resetPassword()");
        vBox.addWidget(buttonResetPwd, 0, AlignHCenter);

        setLayout(vBox);
    }

    private void exportUtenti(){
        int utenteDal= spinUtenteDal.value();
        int utenteAl= spinUtenteAl.value();
        if ( utenteAl<utenteDal) {
            utenteAl=utenteDal;
        }

        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Utente> cq = cb.createQuery(Utente.class);
        Root<Utente> root = cq.from(Utente.class);
        cq=cq.select(root);
        cq = cq.where(cb.and(cb.between(root.get(Utente_.id), Long.valueOf(utenteDal), Long.valueOf(utenteAl)),
                cb.isFalse(root.get(Utente_.disabilitato))));
        TypedQuery<Utente> query = em.createQuery(cq);
        List<Utente> utenti=query.getResultList();
        if ( utenti.size()>0 ) {
            Application app = Application.getApplicationInstance();
            DocerHelper docerHelper = new DocerHelper((String)app.getConfigItem("docer.url"), (String) app.getConfigItem("docer.username"),
                    (String) app.getConfigItem("docer.password"));
            boolean res=false;

            for ( Utente utente: utenti ) {
        /* Utente normale */
                try {
                    if (docerHelper.searchUsers(utente.getLogin()).size() == 0) {
                        try {
                            res = docerHelper.createUser(utente.getLogin(), utente.getLogin()+"1", "", "", utente.getNome(),
                                    utente.getEmail());
                            if (!res) {
                                QMessageBox.critical(this, "Errore",
                                        "Creazione dell'utente " + utente.getLogin() + " in Doc/ER non completata.");
                            }
                        } catch (Exception ex) {
                            QMessageBox.critical(this, "Errore",
                                    "Exception nella creazione dell'utente " + utente.getLogin() + " in Doc/ER.\n" + ex.toString());
                        }
                    } else {
                        QMessageBox.information(this, "Utente esiste", "Utente " + utente.getNome() + " già presente in Doc/ER.");
                    }
                } catch (Exception ex) {
                    QMessageBox.critical(this, "Errore",
                            "Exception nella creazione dell'utente " + utente.getLogin() + " in Doc/ER.\n" + ex.toString());
                }

                if ( checkInserisciUtenti.checkState()== Qt.CheckState.Checked ) {
                    List<String> gruppi = new ArrayList<String>();
                    /* utente ricercatore e/o supervisore */
                    if ( utente.getRicercatoreprotocollo() ) {
                        gruppi.add("0");
                    }
                    if ( utente.getSupervisoreprotocollo() ) {
                        gruppi.add("0");
                        gruppi.add("0R");
                    }
                    /* uffici utenti */
                    for (UfficioUtente ufficioutente : utente.getUfficioUtenteCollection()) {
                        if (ufficioutente.getVisualizza()) {
                            try {
                                KeyValuePairFactory criteri =
                                        new KeyValuePairFactory<MetadatiGruppi>().add(MetadatiGruppi.GROUP_ID, ufficioutente.getUfficio().getId().toString());
                                if (docerHelper.searchGroups(criteri).size() == 1) {
                            /* Ufficio normale */
                                    gruppi.add(ufficioutente.getUfficio().getId().toString());
                                    if (ufficioutente.getRiservato()) {
                                /* Ufficio riservato */
                                        gruppi.add(ufficioutente.getUfficio().getId().toString() + "R");
                                    }
                                }
                            } catch (Exception ex) {
                                QMessageBox.critical(this, "Errore",
                                        "Exception su inserimento utente in ufficio in Doc/ER.\n" + ex.toString());
                            }
                        }
                    }
                    if (gruppi.size()>0) {
                        try {
                            res = docerHelper.updateGroupsOfUser(utente.getLogin(), gruppi, null);
                            if (!res) {
                                QMessageBox.critical(this, "Errore", "Inserimento utente in ufficio in Doc/ER non completata.");
                            }
                        } catch (Exception ex) {
                            QMessageBox.critical(this, "Errore",
                                    "Exception su inserimento utente in ufficio in Doc/ER.\n" + ex.toString());
                        }
                    }
                }
                QMessageBox.information(this, "Tutto ok", "Utente " + utente.getNome() + " inserito.");
            }
            QMessageBox.information(this, "Tutto ok", "Completata importazione dell'utente/degli utenti.");
        } else {
            QMessageBox.information(this, "Nessun utente trovato", "Non esistono utenti abilitati nell'intervallo indicato.");
        }
    }

    private void resetPassword() {
        Application app = Application.getApplicationInstance();
        String pwd = editPwd.text();
        if ( pwd==null || pwd.length()==0 ) {
            pwd="7mqeR+fe$3uQjom1";
//            pwd=(String) app.getConfigItem("docer.userpassword");
        }

        int utenteDal= spinUtenteDal.value();
        int utenteAl= spinUtenteAl.value();
        if ( utenteAl<utenteDal) {
            utenteAl=utenteDal;
        }

        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Utente> cq = cb.createQuery(Utente.class);
        Root<Utente> root = cq.from(Utente.class);
        cq=cq.select(root);
        cq = cq.where(cb.and(cb.between(root.get(Utente_.id), Long.valueOf(utenteDal), Long.valueOf(utenteAl)),
                cb.isFalse(root.get(Utente_.disabilitato))));
        TypedQuery<Utente> query = em.createQuery(cq);
        List<Utente> utenti=query.getResultList();
        if ( utenti.size()>0 ) {
            DocerHelper docerHelper = new DocerHelper((String) app.getConfigItem("docer.url"), (String) app.getConfigItem("docer.username"),
                    (String) app.getConfigItem("docer.password"));
            boolean res = false;

            for (Utente utente : utenti) {
                try {
                    if (docerHelper.searchUsers(utente.getLogin()).size() == 1) {
                        try {
                            Map<MetadatiUtente, String> metadati = new HashMap<MetadatiUtente, String>();
                            metadati.put(MetadatiUtente.USER_PASSWORD, pwd);
                            res = docerHelper.updateUser(utente.getLogin(), metadati);
                            if (!res) {
                                System.err.print("Modifica dell'utente in Doc/ER non completata.");
                            }
                        } catch (Exception ex) {
                            QMessageBox.critical(this, "Errore",
                                    "Exception nella modifica della password dell'utente " + utente.getLogin() + " in Doc/ER.\n" + ex.toString());
                        }
                    } else {
                        QMessageBox.information(this, "Utente non esiste", "Utente " + utente.getNome() + " non presente in Doc/ER.");
                    }
                } catch (Exception ex) {
                    QMessageBox.critical(this, "Errore",
                            "Exception nella ricerca dell'utente " + utente.getLogin() + " in Doc/ER.\n" + ex.toString());
                }
            }
        }
    }
}
