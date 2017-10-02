package com.axiastudio.suite.base.forms;

import com.axiastudio.pypapi.Application;
import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.base.entities.UfficioUtente;
import com.axiastudio.suite.base.entities.Ufficio_;
import com.axiastudio.suite.protocollo.entities.Attribuzione;
import com.axiastudio.suite.protocollo.entities.Attribuzione_;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.*;
import it.tn.rivadelgarda.comune.gda.docer.DocerHelper;
import it.tn.rivadelgarda.comune.gda.docer.KeyValuePairFactory;
import it.tn.rivadelgarda.comune.gda.docer.keys.MetadatiGruppi;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.trolltech.qt.core.Qt.AlignmentFlag.AlignHCenter;

/**
 * User: Michela Piva - Comune di Riva del Garda
 */

public class Ufficio2DocerGroup extends QDialog {

    private QLabel labelDal;
    private QLabel labelAl;
    private QSpinBox spinUfficioDal;
    private QSpinBox spinUfficioAl;
    private QToolButton buttonOk;
    private QCheckBox checkInserisciUtenti;

    public Ufficio2DocerGroup() {

        super(null, new Qt.WindowFlags(Qt.WindowType.Tool, Qt.WindowType.WindowStaysOnTopHint));

         // geometry
        setGeometry(200, 200, 200, 100);
        setWindowTitle("Export uffici in Doc/ER");

        QHBoxLayout hBox = new QHBoxLayout();
        hBox.setMargin(0);

        QVBoxLayout vBox = new QVBoxLayout();
        vBox.setMargin(0);

        labelDal = new QLabel();
        labelDal.setText("Uffici: dal numero");
        hBox.addWidget(labelDal);

        spinUfficioDal = new QSpinBox();
        spinUfficioDal.setRange(1, 1000);
        hBox.addWidget(spinUfficioDal);

        labelAl = new QLabel();
        labelAl.setText("al numero");
        hBox.addWidget(labelAl);

        spinUfficioAl = new QSpinBox();
        spinUfficioAl.setRange(0, 1000);
        hBox.addWidget(spinUfficioAl);
        vBox.addLayout(hBox);

        checkInserisciUtenti = new QCheckBox("Inserire anche utenti");
        vBox.addWidget(checkInserisciUtenti, 0, AlignHCenter);

        buttonOk = new QToolButton();
        buttonOk.setText("Esporta");
        buttonOk.clicked.connect(this, "exportUffici()");
        vBox.addWidget(buttonOk, 0, AlignHCenter);

        setLayout(vBox);
    }

    private void exportUffici(){
        int ufficioDal= spinUfficioDal.value();
        int ufficioAl= spinUfficioAl.value();
        if ( ufficioAl<ufficioDal) {
            ufficioAl=ufficioDal;
        }

        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Ufficio> cq = cb.createQuery(Ufficio.class);
        Root<Ufficio> root = cq.from(Ufficio.class);
        cq=cq.select(root);
        cq = cq.where(cb.between(root.get(Ufficio_.id), Long.valueOf(ufficioDal), Long.valueOf(ufficioAl)));
        TypedQuery<Ufficio> query = em.createQuery(cq);
        List<Ufficio> uffici=query.getResultList();
        if ( uffici.size()>0 ) {
            Application app = Application.getApplicationInstance();
            DocerHelper docerHelper = new DocerHelper((String)app.getConfigItem("docer.url"), (String) app.getConfigItem("docer.username"),
                    (String) app.getConfigItem("docer.password"));
            boolean res=false;

            for ( Ufficio ufficio: uffici ) {
        /* ufficio è attribuzione di qualche protocollo? */
                CriteriaQuery<Long> cqAttrib = cb.createQuery(Long.class);
                Root<Attribuzione> rootAttrib = cq.from(Attribuzione.class);
                cqAttrib=cqAttrib.select(cb.count(rootAttrib));
                cqAttrib = cqAttrib.where(cb.equal(rootAttrib.get(Attribuzione_.ufficio), ufficio));
                if ( em.createQuery(cqAttrib).getSingleResult()==0L ) {
                    QMessageBox.information(this, "Non attribuzione", "L'ufficio " + ufficio.getDescrizione() + " non ha protocolli associati.");
                    continue;
                }

        /* ufficio normale */
                try {
                    KeyValuePairFactory criteri =
                            new KeyValuePairFactory<MetadatiGruppi>().add(MetadatiGruppi.GROUP_ID, ufficio.getId().toString());
                    if (docerHelper.searchGroups(criteri).size() == 0) {
                        try {
                            res = docerHelper.createGroup(ufficio.getId().toString(), ufficio.getDenominazione(), "C_H330");
                            if (!res) {
                                QMessageBox.critical(this, "Errore",
                                        "Creazione dell'ufficio " + ufficio.getId().toString() + " in Doc/ER non completata.");
                            }
                        } catch (Exception ex) {
                            QMessageBox.critical(this, "Errore",
                                    "Exception nella creazione dell'ufficio " + ufficio.getId().toString() + " in Doc/ER.\n" + ex.toString());
                        }
                    } else {
                        QMessageBox.information(this, "Ufficio presente", "Ufficio " + ufficio.getDescrizione() + " già presente in Doc/ER");
                    }
                } catch (Exception ex) {
                    QMessageBox.critical(this, "Errore",
                            "Exception nella creazione dell'ufficio " + ufficio.getId().toString() + " in Doc/ER.\n" + ex.toString());
                }
        /* ufficio riservato */
                try {
                    KeyValuePairFactory criteri =
                            new KeyValuePairFactory<MetadatiGruppi>().add(MetadatiGruppi.GROUP_ID, ufficio.getId().toString() + "R");
                    if (docerHelper.searchGroups(criteri).size() == 0) {
                        try {
                            res = docerHelper.createGroup(ufficio.getId().toString() + "R", ufficio.getDenominazione() + " - Riservato", "C_H330");
                            if (!res) {
                                QMessageBox.critical(this, "Errore",
                                        "Creazione dell'ufficio riservato " + ufficio.getId().toString() + " in Doc/ER non completata.");
                            }
                        } catch (Exception ex) {
                            QMessageBox.critical(this, "Errore",
                                    "Exception nella creazione dell'ufficio riservato " + ufficio.getId().toString() + " in Doc/ER.\n" + ex.toString());
                        }
                    } else {
                        QMessageBox.information(this, "Ufficio presente", "Ufficio " + ufficio.getDescrizione() + "-Riservato già presente in Doc/ER");
                    }
                } catch (Exception ex) {
                    QMessageBox.critical(this, "Errore",
                            "Exception nella creazione dell'ufficio riservato " + ufficio.getId().toString() + " in Doc/ER.\n" + ex.toString());
                }

                if ( checkInserisciUtenti.checkState()== Qt.CheckState.Checked ) {
        /* utenti uffici */
                    for (UfficioUtente ufficioutente : ufficio.getUfficioUtenteCollection()) {
                        if (ufficioutente.getVisualizza()) {
            /* ufficio normale */
                            List<String> gruppi = new ArrayList<String>();
                            gruppi.add(ufficioutente.getUfficio().getId().toString());
                            if (ufficioutente.getRiservato()) {
                /* ufficio riservato */
                                gruppi.add(ufficioutente.getUfficio().getId().toString() + "R");
                            }
                            try {
                                res = docerHelper.updateGroupsOfUser(ufficioutente.getUtente().getLogin(), gruppi, null);
                                if (!res) {
                                    QMessageBox.critical(this, "Errore", "Inserimento utente in ufficio in Doc/ER non completata.");
                                }
                            } catch (Exception ex) {
                                QMessageBox.critical(this, "Errore",
                                        "Exception su inserimento utente in ufficio in Doc/ER.\n" + ex.toString());
                            }
                        }
                    }
                }
            }
            QMessageBox.information(this, "Tutto ok", "Completata importazione dell'ufficio/degli uffici.");
        } else {
            QMessageBox.information(this, "Nessun ufficio trovato", "Non esistono uffici nell'intervallo indicato.");
        }
    }

}
