package com.axiastudio.suite.pratiche.forms;

import com.axiastudio.pypapi.ui.IForm;
import com.axiastudio.pypapi.ui.Util;
import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.suite.pratiche.IDettaglio;
import com.axiastudio.suite.pratiche.entities.Pratica;
import com.trolltech.qt.gui.QMainWindow;
import com.trolltech.qt.gui.QMdiArea;

/**
 * User: tiziano
 * Date: 12/12/13
 * Time: 12:40
 */
public class FormDettaglio extends Window {

    public FormDettaglio(String uiFile, Class entityClass, String title) {
        super(uiFile, entityClass, title);
        DettaglioToolbar dettaglioToolbar = new DettaglioToolbar("Richiesta", this);
        addToolBar(dettaglioToolbar);
    }

    private void apriPratica(){
        IDettaglio dettaglio = (IDettaglio) this.getContext().getCurrentEntity();
        Pratica pratica = dettaglio.getPratica();
        IForm form = Util.formFromEntity(pratica);
        QMdiArea workspace = Util.findParentMdiArea(this);
        if( workspace != null ){
            workspace.addSubWindow((QMainWindow) form);
        }
        form.show();
    }

}
