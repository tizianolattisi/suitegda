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
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.plugins.cmis.DocerPlugin;
import it.tn.rivadelgarda.comune.gda.docer.DocerHelper;
import it.tn.rivadelgarda.comune.gda.docer.keys.MetadatiGruppi;

import javax.persistence.*;
import java.util.*;

/**
 * @author Michela Piva - Comune di Riva del Garda
 */
public class UfficioListener {

    @PostPersist
    void postPersist(Object object) {
/*
 ***   creazione ufficio su Doc/ER
 */
        DocerPlugin docerPlugin = (DocerPlugin) Register.queryPlugin(Ufficio.class, "DocER");
        DocerHelper docerHelper = docerPlugin.createDocerHelper(object);
        Ufficio ufficio = (Ufficio) object;
        boolean res=false;

        /* ufficio normale */
		try {
			res = docerHelper.createGroup(ufficio.getId().toString(), ufficio.getDenominazione(), "C_H330");
            if ( !res ) {
                System.err.print("Creazione dell'ufficio in Doc/ER non completata.");
            }
		} catch (Exception ex) {
            System.err.print("Exception nella creazione dell'ufficio in Doc/ER." +
                    "" + ex.toString());
		}
        /* ufficio riservato */
        try {
            res = docerHelper.createGroup(ufficio.getId().toString() + "R", ufficio.getDenominazione() + " - Riservato", "C_H330");
            if ( !res ) {
                System.err.print("Creazione dell'ufficio riservato in Doc/ER non completata.");
            }
        } catch (Exception ex) {
            System.err.print("Exception nella creazione dell'ufficio riservato in Doc/ER." +
                    "" + ex.toString());
        }
    }

    @PostUpdate
    void postUpdate(Object object) {
        Ufficio ufficio = (Ufficio) object;

        if ( ufficio.hasChanged() ) {
            DocerPlugin docerPlugin = (DocerPlugin) Register.queryPlugin(Ufficio.class, "DocER");
            DocerHelper docerHelper = docerPlugin.createDocerHelper(object);
            boolean res = false;

        /* aggiornamento denominazione ufficio */
            try {
                Map<MetadatiGruppi, String> metadati = new HashMap<MetadatiGruppi, String>();
                metadati.put(MetadatiGruppi.GROUP_NAME, ufficio.getDenominazione());
                res = docerHelper.updateGroup(ufficio.getId().toString(), metadati);
                if (!res) {
                    System.err.print("Modifica dell'ufficio in Doc/ER non completata.");
                }
            } catch (Exception ex) {
                System.err.print("Exception nella modifica della denominazione dell'ufficio in Doc/ER." +
                        "" + ex.toString());
            }
        /* aggiornamento denominazione ufficio riservato */
            try {
                Map<MetadatiGruppi, String> metadati = new HashMap<MetadatiGruppi, String>();
                metadati.put(MetadatiGruppi.GROUP_NAME, ufficio.getDenominazione() + " - Riservato");
                res = docerHelper.updateGroup(ufficio.getId().toString() + "R", metadati);
                if (!res) {
                    System.err.print("Modifica dell'ufficio riservato in Doc/ER non completata.");
                }
            } catch (Exception ex) {
                System.err.print("Exception nella modifica della denominazione dell'ufficio riservato in Doc/ER." +
                        "" + ex.toString());
            }
            ufficio.reset();
        }
    }
}
