package com.axiastudio.suite.richieste.forms;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Store;
import com.axiastudio.pypapi.ui.Dialog;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.UfficioUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.richieste.entities.DestinatarioUfficio;


public class FormDestinatarioUfficio extends Dialog {

    public FormDestinatarioUfficio(String uiFile, Class entityClass, String title) {
        super(uiFile, entityClass, title);
    }

    @Override
    public void init(Store store) {
        super.init(store);
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        DestinatarioUfficio destRichiesta=(DestinatarioUfficio) this.getContext().getCurrentEntity();
        Boolean enabled = false;

        for (UfficioUtente uu: destRichiesta.getDestinatario().getUfficioUtenteCollection()) {
            if ( autenticato.equals(uu.getUtente()) && !uu.getOspite() && uu.getVisualizza() ) {
                enabled = true;
                break;
            }
        }
        this.setEnabled(enabled);
    }
}
