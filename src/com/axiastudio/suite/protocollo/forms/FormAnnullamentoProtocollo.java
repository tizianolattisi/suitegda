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

import com.axiastudio.pypapi.ui.Window;
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
        this.formShown.connect(this, "updatePermission()");
    }

    @Override
    protected void indexChanged(int row) {
        super.indexChanged(row);
        updatePermission();
    }

    private void updatePermission() {
        AnnullamentoProtocollo annullamento = new AnnullamentoProtocollo();
        if ( this.getContext()!=null ) {
            annullamento = (AnnullamentoProtocollo) this.getContext().getCurrentEntity();
        } else {
//            this.setVisible(Boolean.FALSE);
            return;
        }
         /* permesso di confermare o respingere */
        if( getParentForm() != null ){
            this.setEnabled(false);
/*            if ( annullamento.getId()==null ) {
                this.setVisible(Boolean.FALSE);
                this.close();
            }*/
            return;
        }
        QCheckBox checkBox_autorizzato = (QCheckBox) this.findChild(QCheckBox.class, "checkBox_autorizzato");
        QCheckBox checkBox_respinto = (QCheckBox) this.findChild(QCheckBox.class, "checkBox_respinto");
        QComboBox comboBox_motivazione = (QComboBox) this.findChild(QComboBox.class, "comboBox_motivazione");
        Boolean modifica = annullamento.getId()!=null && !annullamento.getRespinto() && !annullamento.getAutorizzato();
        checkBox_autorizzato.setEnabled( modifica );
        checkBox_respinto.setEnabled( modifica );
        comboBox_motivazione.setEnabled( modifica );
    }

}
