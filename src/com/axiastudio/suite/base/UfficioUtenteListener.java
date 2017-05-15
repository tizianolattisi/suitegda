/*
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
package com.axiastudio.suite.base;

import com.axiastudio.pypapi.Register;
import com.axiastudio.suite.base.entities.UfficioUtente;
import com.axiastudio.suite.plugins.cmis.DocerPlugin;
import it.tn.rivadelgarda.comune.gda.docer.DocerHelper;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import java.util.*;

/**
 * @author Michela Piva - Comune di Riva del Garda
 */
public class UfficioUtenteListener {

    @PostPersist
    void postPersist(Object object) {
/*
 ***   inserimento utente in ufficio su Doc/ER
 */
        DocerPlugin docerPlugin = (DocerPlugin) Register.queryPlugin(UfficioUtente.class, "DocER");
        DocerHelper docerHelper = docerPlugin.createDocerHelper(object);
        UfficioUtente ufficioutente = (UfficioUtente) object;
        boolean res=false;

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
                    System.err.print("Inserimento utente in ufficio in Doc/ER non completata.");
                }
            } catch (Exception ex) {
                System.err.print("Exception su inserimento utente in ufficio in Doc/ER." +
                        "" + ex.toString());
            }
        }
    }

    @PostUpdate
    void postUpdate(Object object) {
        UfficioUtente ufficioutente = (UfficioUtente) object;

        if ( ufficioutente.hasChanged() ) {
            DocerPlugin docerPlugin = (DocerPlugin) Register.queryPlugin(UfficioUtente.class, "DocER");
            DocerHelper docerHelper = docerPlugin.createDocerHelper(object);
            boolean res = false;

        /* aggiornamento utente-ufficio (inserimento/cancellazione) */
            List<String> gruppiIns = new ArrayList<String>();
            List<String> gruppiDel = new ArrayList<String>();
            if (ufficioutente.getVisualizza()) {
                gruppiIns.add(ufficioutente.getUfficio().getId().toString());
            } else {
                gruppiDel.add(ufficioutente.getUfficio().getId().toString());
                gruppiDel.add(ufficioutente.getUfficio().getId().toString() + "R");
            }
            if (ufficioutente.getRiservato()) {
                gruppiIns.add(ufficioutente.getUfficio().getId().toString() + "R");
            } else {
                gruppiDel.add(ufficioutente.getUfficio().getId().toString() + "R");
            }
            try {
                res = docerHelper.updateGroupsOfUser(ufficioutente.getUtente().getLogin(), gruppiIns, gruppiDel);
                if (!res) {
                    System.err.print("Inserimento utente in ufficio in Doc/ER non completata.");
                }
            } catch (Exception ex) {
                System.err.print("Exception su inserimento utente in ufficio in Doc/ER." +
                        "" + ex.toString());
            }
            ufficioutente.reset();
        }
    }

    @PostRemove
    void postRemove(Object object) {
/*
 ***   cancellazione utente in ufficio su Doc/ER
 */
        DocerPlugin docerPlugin = (DocerPlugin) Register.queryPlugin(UfficioUtente.class, "DocER");
        DocerHelper docerHelper = docerPlugin.createDocerHelper(object);
        UfficioUtente ufficioutente = (UfficioUtente) object;
        boolean res=false;

        if (ufficioutente.getVisualizza()) {
            /* cancellazione entrambi uffici */
            List<String> gruppi = Arrays.asList(ufficioutente.getUfficio().getId().toString(),
                    ufficioutente.getUfficio().getId().toString() + "R");
            try {
                res = docerHelper.updateGroupsOfUser(ufficioutente.getUtente().getLogin(), null, gruppi);
                if (!res) {
                    System.err.print("Cancellazione utente dall'ufficio in Doc/ER non completata.");
                }
            } catch (Exception ex) {
                System.err.print("Exception su cancellazione utente dall'ufficio in Doc/ER." +
                        "" + ex.toString());
            }
        }
    }

}
