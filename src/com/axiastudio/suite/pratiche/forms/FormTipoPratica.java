package com.axiastudio.suite.pratiche.forms;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.*;
import com.axiastudio.suite.SuiteBaseForm;
import com.axiastudio.suite.pratiche.entities.TipoPratica;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QLineEdit;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by Comune di Riva del Garda on 10/11/2014.
 */

public class FormTipoPratica  extends SuiteBaseForm {

    public FormTipoPratica(String uiFile, Class entityClass, String title) {
        super(uiFile, entityClass, title);
        try {
            Method storeFactory = this.getClass().getMethod("storeTipo");
            Register.registerUtility(storeFactory, IStoreFactory.class, "Tipopadre");
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(FormPratica.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(FormPratica.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

/*
 * Uno store contenente gli oggetti ordinati x descrizione; solo tipologie 'foglie' e non obsolete
 */
    public Store storeTipo(){
        Database db = (Database) Register.queryUtility(IDatabase.class);
        Controller controller = db.createController(TipoPratica.class);
        Store storeTipo = controller.createFullStore();
        List<TipoPratica> oggetti = new ArrayList<TipoPratica>();
        for(Object ogg: storeTipo){
            if ( ((TipoPratica) ogg).getObsoleta().equals(Boolean.FALSE) && ((TipoPratica) ogg).getFoglia().equals(Boolean.FALSE) ) {
                oggetti.add((TipoPratica) ogg);
            }
        }
        return new Store(oggetti);
    }

    @Override
    protected void indexChanged(int row) {
        super.indexChanged(row);
        TipoPratica tipopratica = (TipoPratica) this.getContext().getCurrentEntity();

        ((QComboBox) this.findChild(QComboBox.class, "comboBox_tipopadre")).setEnabled(tipopratica.getId() == null);
        ((QLineEdit) this.findChild(QLineEdit.class, "lineEdit_codice")).setEnabled(tipopratica.getId() == null);
    }
}
