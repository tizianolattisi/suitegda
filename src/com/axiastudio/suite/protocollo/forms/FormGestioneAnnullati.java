package com.axiastudio.suite.protocollo.forms;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.pypapi.db.Store;
import com.axiastudio.pypapi.ui.Column;
import com.axiastudio.pypapi.ui.TableModel;
import com.axiastudio.pypapi.ui.Util;
import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.suite.protocollo.entities.AnnullamentoProtocollo;
import com.trolltech.qt.core.QByteArray;
import com.trolltech.qt.core.QFile;
import com.trolltech.qt.designer.QUiLoader;
import com.trolltech.qt.designer.QUiLoaderException;
import com.trolltech.qt.gui.QHeaderView;
import com.trolltech.qt.gui.QItemSelectionModel;
import com.trolltech.qt.gui.QMainWindow;
import com.trolltech.qt.gui.QTableView;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: tiziano
 * Date: 13/05/14
 * Time: 14:55
 */
public class FormGestioneAnnullati extends QMainWindow {

    private final QTableView annullati;

    public FormGestioneAnnullati() {
        QFile file = Util.ui2jui(new QFile("classpath:com/axiastudio/suite/protocollo/forms/gestioneannullati.ui"));
        loadUi(file);
        annullati = (QTableView) this.findChild(QTableView.class, "annullati");
        popolaAnnullati();
    }

    private void loadUi(QFile uiFile){
        QMainWindow window = null;
        try {
            window = (QMainWindow) QUiLoader.load(uiFile);
        } catch (QUiLoaderException ex) {
            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
        }
        for( QByteArray name: window.dynamicPropertyNames()){
            this.setProperty(name.toString(), window.property(name.toString()));
        }
        this.setCentralWidget(window.centralWidget());
        this.setWindowTitle(window.windowTitle());
    }

    private void popolaAnnullati() {
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        List<AnnullamentoProtocollo> annullamentiRichiesti =
                em.createNamedQuery("annullamentiRichiesti", AnnullamentoProtocollo.class).getResultList();

        List<Column> colonne = new ArrayList();
        colonne.add(new Column("datarichiesta", "Data", "Data di richiesta"));
        colonne.add(new Column("esecutorerichiesta", "Richiedente", "Esecutore della richiesta di annullamento"));
        colonne.add(new Column("motivazioneannullamento", "Motivazione", "Motivazione della richiesta di annullamento"));
        TableModel model = new TableModel(new Store(annullamentiRichiesti), colonne);
        annullati.clearSelection();
        model.setEditable(false);
        annullati.setModel(model);
        QItemSelectionModel selectionModel = new QItemSelectionModel(model);
        annullati.setSelectionModel(selectionModel);
        selectionModel.selectionChanged.connect(this, "selectRows(QItemSelection, QItemSelection)");
        annullati.horizontalHeader().setResizeMode(0, QHeaderView.ResizeMode.ResizeToContents);
        annullati.horizontalHeader().setResizeMode(1, QHeaderView.ResizeMode.ResizeToContents);
        annullati.horizontalHeader().setResizeMode(1, QHeaderView.ResizeMode.Stretch);
    }

}
