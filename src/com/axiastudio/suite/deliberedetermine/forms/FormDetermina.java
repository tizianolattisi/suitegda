/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.deliberedetermine.forms;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.suite.deliberedetermine.entities.Determina;
import com.axiastudio.suite.procedimenti.GestoreDeleghe;
import com.axiastudio.suite.procedimenti.IGestoreDeleghe;
import com.axiastudio.suite.procedimenti.entities.CodiceCarica;
import com.trolltech.qt.gui.QPushButton;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class FormDetermina extends Window {
    private QPushButton pushButtonResponsabile;
    private QPushButton pushButtonBilancio;
    private QPushButton pushButtonVistoNegato;
    
    public FormDetermina(String uiFile, Class entityClass, String title){
        super(uiFile, entityClass, title);
        pushButtonResponsabile = (QPushButton) this.findChild(QPushButton.class, "pushButtonResponsabile");
        pushButtonBilancio = (QPushButton) this.findChild(QPushButton.class, "pushButtonBilancio");
        pushButtonVistoNegato = (QPushButton) this.findChild(QPushButton.class, "pushButtonVistoNegato");
        pushButtonResponsabile.clicked.connect(this, "vistoResponsabile()");
        pushButtonBilancio.clicked.connect(this, "vistoBilancio()");
        pushButtonVistoNegato.clicked.connect(this, "vistoNegato()");
        
    }
    
    private void vistoResponsabile() {
        if( this.checkResponsabile() ){
            // TODO
            
        }
    }

    private void vistoBilancio() {
        
    }

    private void vistoNegato() {
        
    }
    
    private Boolean checkResponsabile() {
        IGestoreDeleghe gestore = (IGestoreDeleghe) Register.queryUtility(IGestoreDeleghe.class);
        /// TODO: da completare con il servizio
        return gestore.checkDelega(CodiceCarica.RESPONSABILE_DI_SERVIZIO);
    }

    private Boolean checkBilancio() {
        return true;
    }

    @Override
    protected void indexChanged(int row) {
        Determina d = (Determina) this.getContext().getCurrentEntity();
        Boolean vResp = !d.getVistoResponsabile() && this.checkResponsabile();
        Boolean vBil = d.getVistoResponsabile() && !d.getVistoBilancio() && this.checkBilancio();
        Boolean vNeg = vBil;
        this.pushButtonResponsabile.setEnabled(vResp);
        this.pushButtonBilancio.setEnabled(vBil);
        this.pushButtonVistoNegato.setEnabled(vNeg);
        super.indexChanged(row);
    }


}
