package com.axiastudio.suite.richieste.forms;

import com.axiastudio.pypapi.ui.IForm;
import com.axiastudio.pypapi.ui.Util;
import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.suite.richieste.entities.Richiesta;
import com.trolltech.qt.gui.QMainWindow;
import com.trolltech.qt.gui.QMdiArea;


public class FormRichiesta extends Window {

    public RichiestaToolbar richiestaToolbar;


    public FormRichiesta(String uiFile, Class entityClass, String title) {
        super(uiFile, entityClass, title);
        this.richiestaToolbar = new RichiestaToolbar("Richiesta", this);
        this.addToolBar(richiestaToolbar);

    }

    private void inoltraRichiesta(){
        // TODO: costruire l'oggetto richiesta inoltrata
    }

    private void rispondiRichiesta(){
        Richiesta richiesta = (Richiesta) getContext().getCurrentEntity();
        Richiesta risposta = new Richiesta();
        risposta.setRichiestaprecedente(richiesta);
        // TODO: completare le informazioni della richiesta
        IForm win = Util.formFromEntity(risposta);
        QMdiArea workspace = Util.findParentMdiArea(this);
        if( workspace != null ){
            workspace.addSubWindow((QMainWindow) win);
        }
        win.show();
    }

    private void richiestaPrecedente(){
        Richiesta richiesta = (Richiesta) getContext().getCurrentEntity();
        Richiesta precedente = richiesta.getRichiestaprecedente();
        if( precedente != null ){
            IForm win = Util.formFromEntity(precedente);
            QMdiArea workspace = Util.findParentMdiArea(this);
            if( workspace != null ){
                workspace.addSubWindow((QMainWindow) win);
            }
            win.show();
        }

    }

}
