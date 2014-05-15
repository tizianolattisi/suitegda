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
package com.axiastudio.suite.protocollo.forms;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.suite.SuiteUtil;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.UfficioUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.generale.entities.Costante;
import com.axiastudio.suite.protocollo.entities.AnnullamentoProtocollo;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QComboBox;


/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class FormAnnullamentoProtocollo extends Window {
    
    public FormAnnullamentoProtocollo(String uiFile, Class entityClass, String title){
        super(uiFile, entityClass, title);
    }

    @Override
    protected void indexChanged(int row) {
        updatePermission();
    }

    private void updatePermission() {
        /* permesso di confermare o respingere */
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        Costante costanteUfficioAnnullati = SuiteUtil.trovaCostante("UFFICIO_ANNULLATI");
        Long idUfficioAnnullati = Long.parseLong(costanteUfficioAnnullati.getValore());
        Boolean inUfficioAnnullati = Boolean.FALSE;     // TODO: conferma annullamento da rifare tramite finestra 'scollegata' da form Protocollo (e dalla logica della callback)
        for( UfficioUtente ufficio: autenticato.getUfficioUtenteCollection()) {
            if (ufficio.getUfficio().getId().equals(idUfficioAnnullati) && ufficio.getRicerca()) {
                inUfficioAnnullati = Boolean.TRUE;
            }
        }
        AnnullamentoProtocollo annullamento = (AnnullamentoProtocollo) this.getContext().getCurrentEntity();
        QCheckBox checkBox_autorizzato = (QCheckBox) this.findChild(QCheckBox.class, "checkBox_autorizzato");
        QCheckBox checkBox_respinto = (QCheckBox) this.findChild(QCheckBox.class, "checkBox_respinto");
        Boolean modifica = annullamento.getId()!=null && !annullamento.getRespinto() && !annullamento.getAutorizzato()
                            && autenticato.getAttributoreprotocollo() && autenticato.getSupervisorepratiche() && inUfficioAnnullati;
        checkBox_autorizzato.setEnabled( modifica );
        checkBox_respinto.setEnabled( modifica );
        QComboBox comboBox_motivazione = (QComboBox) this.findChild(QComboBox.class, "comboBox_motivazione");
        comboBox_motivazione.setEnabled( modifica || !(annullamento.getAutorizzato() || annullamento.getRespinto()) );
    }

}
