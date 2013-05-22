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
import com.axiastudio.pypapi.db.Controller;
import com.axiastudio.pypapi.db.IController;
import com.axiastudio.pypapi.ui.Dialog;
import com.axiastudio.suite.SuiteUtil;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.generale.entities.Costante;
import com.axiastudio.suite.pratiche.entities.Pratica;
import com.axiastudio.suite.protocollo.entities.AnnullamentoProtocollo;
import com.axiastudio.suite.protocollo.entities.Attribuzione;
import com.axiastudio.suite.protocollo.entities.PraticaProtocollo;
import com.axiastudio.suite.protocollo.entities.Protocollo;
import com.trolltech.qt.gui.QCheckBox;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class FormAnnullamentoProtocollo extends Dialog {
    
    public FormAnnullamentoProtocollo(String uiFile, Class entityClass, String title){
        super(uiFile, entityClass, title);
        this.storeInitialized.connect(this, "updatePermission()");        
    }

    private void updatePermission() {
        /* permesso di confermare o respingere */
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        AnnullamentoProtocollo annullamento = (AnnullamentoProtocollo) this.getContext().getCurrentEntity();
        QCheckBox checkBox_autorizzato = (QCheckBox) this.findChild(QCheckBox.class, "checkBox_autorizzato");
        QCheckBox checkBox_respinto = (QCheckBox) this.findChild(QCheckBox.class, "checkBox_respinto");
        Boolean modifica = !annullamento.getRespinto() && !annullamento.getAutorizzato() && autenticato.getAttributoreprotocollo();
        checkBox_autorizzato.setEnabled( modifica );
        checkBox_respinto.setEnabled( modifica );
    }

    @Override
    public void accept() {
        AnnullamentoProtocollo annullamento = (AnnullamentoProtocollo) this.getContext().getCurrentEntity();
        if( annullamento.getId() != null && (annullamento.getAutorizzato() || annullamento.getRespinto()) ){
            
            /* devo registrare indipendentemente dal protocollo */
            Calendar calendar = Calendar.getInstance();
            Date today = calendar.getTime();
            Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
            annullamento.setEsecutoreautorizzazione(autenticato.getLogin());
            annullamento.setDataautorizzazione(today);
            
            /* se è un annullamento:
             *  - aggiungo l'ufficio protocollo come unica attribuzione
             *  - inserisco nella pratica (unica) dei protocolli annullati
             *  - consolido il protocollo
             *  - marco il protocollo come annullato
             */
            if( annullamento.getAutorizzato() ){
                Costante costantePraticaAnnullati = SuiteUtil.trovaCostante("PRATICA_ANNULLATI");
                Long idPraticaAnnullati = Long.parseLong(costantePraticaAnnullati.getValore());
                Controller controllerPratica = (Controller) Register.queryUtility(IController.class, Pratica.class.getName());
                Pratica praticaAnnullati = (Pratica) controllerPratica.get(idPraticaAnnullati);
                Costante costanteUfficioAnnullati = SuiteUtil.trovaCostante("UFFICIO_ANNULLATI");
                Long idUfficioAnnullati = Long.parseLong(costanteUfficioAnnullati.getValore());
                Controller controllerUfficio = (Controller) Register.queryUtility(IController.class, Ufficio.class.getName());
                Ufficio ufficioAnnullati = (Ufficio) controllerUfficio.get(idUfficioAnnullati);
                Protocollo protocollo = annullamento.getProtocollo();
                protocollo.setAttribuzioneCollection(null);
                protocollo.setPraticaProtocolloCollection(null);
                List<Attribuzione> attribuzioni = new ArrayList();
                Attribuzione attribuzione = new Attribuzione();
                attribuzione.setUfficio(ufficioAnnullati);
                attribuzione.setPrincipale(Boolean.TRUE);
                attribuzioni.add(attribuzione);
                protocollo.setAttribuzioneCollection(attribuzioni);
                List<PraticaProtocollo> praticheProtocollo = new ArrayList();
                PraticaProtocollo praticaProtocollo = new PraticaProtocollo();
                praticaProtocollo.setPratica(praticaAnnullati);
                praticaProtocollo.setOriginale(Boolean.TRUE);
                praticheProtocollo.add(praticaProtocollo);
                protocollo.setPraticaProtocolloCollection(praticheProtocollo);
                protocollo.setConvalidaattribuzioni(Boolean.TRUE);
                protocollo.setConvalidaprotocollo(Boolean.TRUE);
                protocollo.setConsolidadocumenti(Boolean.TRUE);
                Controller controllerProtocollo = (Controller) Register.queryUtility(IController.class, Protocollo.class.getName());
                controllerProtocollo.commit(protocollo);
            } else {
                Controller controllerAnnullamento = (Controller) Register.queryUtility(IController.class, annullamento.getClass().getName());
                controllerAnnullamento.commit(annullamento);
            }
        }
        super.accept();
    }
    
    
    
}
