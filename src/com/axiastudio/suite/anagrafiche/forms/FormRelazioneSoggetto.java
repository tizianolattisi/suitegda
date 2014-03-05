/*
 * Copyright (C) 2013 AXIA Studio (http://www.axiastudio.com)
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
 * You should have received a copy of the GNU Afffero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axiastudio.suite.anagrafiche.forms;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Controller;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.pypapi.db.Store;
import com.axiastudio.pypapi.ui.Context;
import com.axiastudio.pypapi.ui.Dialog;
import com.axiastudio.pypapi.ui.widgets.PyPaPiComboBox;
import com.axiastudio.suite.anagrafiche.entities.Relazione;
import com.axiastudio.suite.anagrafiche.entities.RelazioneSoggetto;
import com.axiastudio.suite.anagrafiche.entities.Soggetto;
import com.axiastudio.suite.anagrafiche.entities.TipoSoggetto;
import com.trolltech.qt.core.Qt.CheckState;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QShowEvent;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class FormRelazioneSoggetto extends Dialog {
    private String abilitataSx="";
    private String abilitataDx="";
    private Boolean checkboxAttivabile=Boolean.FALSE;
    private Controller controller;
    private EntityManager em;
    private QCheckBox cbInverti;
    private PyPaPiComboBox cmbRelazione;

    public FormRelazioneSoggetto(String uiFile, Class entityClass, String title){
        super(uiFile, entityClass, title);
        cbInverti = (QCheckBox) this.findChild(QCheckBox.class, "checkBox_inverti");
        cbInverti.stateChanged.connect(this, "aggiornaInvertita()");
        cmbRelazione = (PyPaPiComboBox) this.findChild(PyPaPiComboBox.class, "comboBox_relazione");
        cmbRelazione.currentIndexChanged.connect(this, "aggiornaRelazione()");
        Database db = (Database) Register.queryUtility(IDatabase.class);
        controller = db.createController(Relazione.class);
        em = controller.getEntityManager();
    }

    @Override
    protected void showEvent(QShowEvent arg__1) {
        storeInizializza();
        super.showEvent(arg__1);
    }

    private void aggiornaRelazione(){

        if ( cmbRelazione.currentIndex()<0 ) {
            return;
        }

        aggiornaPredicato();

        if ( checkboxAttivabile ) {
            if ( cmbRelazione.getCurrentText().equals(cmbRelazione.ND) ) {
                cbInverti.setEnabled(true);
            } else {
                cbInverti.setEnabled(false);
                if ( abilitataSx!="" ) {
                    Relazione rel = (Relazione) cmbRelazione.getCurrentEntity();
                    String query = "SELECT r FROM Relazione r ";
                    if ( cbInverti.checkState().equals(CheckState.Unchecked) ) {
                        query = query + "WHERE r." + abilitataDx + "sx = TRUE AND r." + abilitataSx + "dx = TRUE ";
                    } else {
                        query = query + "WHERE r." + abilitataSx + "sx = TRUE AND r." + abilitataDx + "dx = TRUE ";
                    }
                    query = query + "AND r.id = " + rel.getId().toString();
                    if ( em.createQuery(query, Relazione.class).getResultList().size()> 0 ) {
                        cbInverti.setEnabled(true);
                    }
                }
            }
        }
    }

    private void aggiornaInvertita(){

        aggiornaPredicato();

        Relazione rel = (Relazione) cmbRelazione.getCurrentEntity();
        Store store = storeRelazione();
        cmbRelazione.setLookupStore(store);
        this.getColumn("Relazione").setLookupStore(store);
        cmbRelazione.select(rel);
    }


    private void aggiornaPredicato(){

        QLabel predicato = (QLabel) this.findChild(QLabel.class, "label_predicato");
        Boolean inverti = cbInverti.checkState().equals(CheckState.Checked);
        Context context = this.getContext();
        if( context == null ){
            return;
        }
        RelazioneSoggetto rs = (RelazioneSoggetto) this.getContext().getModel().getStore().get(0); // XXX: perché non currentEntity??
        if( rs == null ){
            return;
        }
        Relazione rel = (Relazione) cmbRelazione.getCurrentEntity();
        predicato.setText(rs.getPredicato(rel, inverti));
    }

    public void storeInizializza() {

        RelazioneSoggetto rs = (RelazioneSoggetto) this.getContext().getModel().getStore().get(0); // XXX: perché non currentEntity??
        if( rs == null || rs.getId()==null ) {
            cbInverti.setEnabled(false);
        } else {
            checkboxAttivabile = Boolean.TRUE;
        }

        Store store = storeRelazione();
        cmbRelazione.setLookupStore(store);
        this.getColumn("Relazione").setLookupStore(store);
        cmbRelazione.select(rs.getRelazione());
    }

/*
 * Uno store contenente gli oggetti ordinati x descrizione
 */
    public Store storeRelazione() {
        Store storeRelazione = controller.createFullStore();
        RelazioneSoggetto rs = (RelazioneSoggetto) this.getContext().getModel().getStore().get(0); // XXX: perché non currentEntity??
        if( rs == null ){
            return storeRelazione;
        }

        Soggetto soggetto = new Soggetto();
        if ( rs.getSoggetto() == null ) {
            soggetto = (Soggetto) this.getParentForm().getContext().getCurrentEntity();
        } else {
            soggetto = rs.getSoggetto();
        }

        if (soggetto.getTipo().equals((TipoSoggetto.PERSONA))) {
            abilitataSx="p";
        } else if (soggetto.getTipo().equals((TipoSoggetto.AZIENDA))) {
            abilitataSx="a";
        } else if (soggetto.getTipo().equals((TipoSoggetto.ENTE))) {
            abilitataSx="e";
        }
        if (rs.getRelazionato().getTipo().equals((TipoSoggetto.PERSONA))) {
            abilitataDx="p";
        } else if (rs.getRelazionato().getTipo().equals((TipoSoggetto.AZIENDA))) {
            abilitataDx="a";
        } else if (rs.getRelazionato().getTipo().equals((TipoSoggetto.ENTE))) {
            abilitataDx="e";
        }

        String query = "SELECT r FROM Relazione r ";
        if ( cbInverti.checkState().equals(CheckState.Unchecked) ) {
            query = query + "WHERE r." + abilitataSx + "sx = TRUE AND r." + abilitataDx + "dx = TRUE ";
        } else {
            query = query + "WHERE r." + abilitataDx + "sx = TRUE AND r." + abilitataSx + "dx = TRUE ";
        }
        List<Relazione> relazioni = em.createQuery(query, Relazione.class).getResultList();

        Collections.sort(relazioni, Relazione.Comparators.DESCRIZIONE);
        return new Store(relazioni);
     }

}
