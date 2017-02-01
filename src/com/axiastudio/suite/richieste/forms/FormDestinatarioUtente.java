package com.axiastudio.suite.richieste.forms;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Store;
import com.axiastudio.pypapi.ui.Dialog;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.richieste.entities.DestinatarioUtente;


public class FormDestinatarioUtente extends Dialog {

    public FormDestinatarioUtente(String uiFile, Class entityClass, String title) {
        super(uiFile, entityClass, title);
    }

    @Override
    public void init(Store store) {
        super.init(store);
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        DestinatarioUtente destRichiesta=(DestinatarioUtente) this.getContext().getCurrentEntity();
        this.setEnabled(destRichiesta.getDestinatario().equals(autenticato));
    }
}
