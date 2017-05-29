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

import com.axiastudio.pypapi.Application;
import com.axiastudio.pypapi.Register;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.plugins.cmis.DocerPlugin;
import it.tn.rivadelgarda.comune.gda.docer.DocerHelper;
import it.tn.rivadelgarda.comune.gda.docer.keys.MetadatiUtente;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Michela Piva - Comune di Riva del Garda
 */
public class UtenteListener {

    @PostPersist
    void postPersist(Object object) {
/*
 ***   creazione utente su Doc/ER
 */
        Application app = Application.getApplicationInstance();
        DocerHelper docerHelper = new DocerHelper((String)app.getConfigItem("docer.url"), (String) app.getConfigItem("docer.username"),
                (String) app.getConfigItem("docer.password"));
        Utente utente = (Utente) object;

		try {
			boolean res = docerHelper.createUser(utente.getLogin(), utente.getLogin()+"1", "", "", utente.getNome(),
                    utente.getEmail());
            if ( !res ) {
                System.err.print("Creazione dell'utente in Doc/ER non completata.");
            }
		} catch (Exception ex) {
            System.err.print("Exception nella creazione dell'utente in Doc/ER." +
                    "" + ex.toString());
		}
    }

    @PostUpdate
    void postUpdate(Object object) {

        Utente utente = (Utente) object;

        if ( utente.hasChanged() ) {
//            DocerPlugin docerPlugin = (DocerPlugin) Register.queryPlugin(Utente.class, "DocER");
//            DocerHelper docerHelper = docerPlugin.createDocerHelper(object);
            Application app = Application.getApplicationInstance();
            DocerHelper docerHelper = new DocerHelper((String)app.getConfigItem("docer.url"), (String) app.getConfigItem("docer.username"),
                    (String) app.getConfigItem("docer.password"));

        /* aggiornamento abilitazione, nomecompleto e email utente (la login non è modificabile!!) */
            try {
                Map<MetadatiUtente, String> metadati = new HashMap<MetadatiUtente, String>();
                metadati.put(MetadatiUtente.USER_ENABLED, ((Boolean)!utente.getDisabilitato()).toString());
                metadati.put(MetadatiUtente.FULL_NAME, utente.getNome());
                metadati.put(MetadatiUtente.EMAIL_ADDRESS, utente.getEmail());
                boolean res = docerHelper.updateUser(utente.getLogin(), metadati);
                if (!res) {
                    System.err.print("Modifica dell'utente in Doc/ER non completata.");
                }
            } catch (Exception ex) {
                System.err.print("Exception nella modifica del nome dell'utente in Doc/ER." +
                        "" + ex.toString());
            }
            utente.reset();
        }
    }
}
