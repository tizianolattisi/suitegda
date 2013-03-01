/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.deliberedetermine.forms;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.deliberedetermine.entities.Determina;
import com.trolltech.qt.gui.QPushButton;
import java.util.Date;

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
    
    protected Boolean vistoResponsabile() {
        Determina determina = (Determina) this.getContext().getCurrentEntity();
        Utente utente = (Utente) Register.queryUtility(IUtente.class);
        determina.setVistoResponsabile(Boolean.TRUE);
        determina.setDataVistoResponsabile(new Date());
        determina.setUtenteVistoResponsabile(utente);
        return true;
    }

    protected Boolean vistoBilancio() {
        Determina determina = (Determina) this.getContext().getCurrentEntity();
        Utente utente = (Utente) Register.queryUtility(IUtente.class);
        determina.setVistoBilancio(Boolean.TRUE);
        determina.setDataVistoBilancio(new Date());
        determina.setUtenteVistoBilancio(utente);
        return true;
    }

    protected Boolean vistoNegato() {
        Determina determina = (Determina) this.getContext().getCurrentEntity();
        Utente utente = (Utente) Register.queryUtility(IUtente.class);
        determina.setVistoNegato(Boolean.TRUE);
        determina.setDataVistoNegato(new Date());
        determina.setUtenteVistoNegato(utente);
        return true;
        
    }
    
    /*
     * Verifica delle condizioni di abilitazione alla firma del responsabile
     * del servizio.
     */
    protected Boolean checkResponsabile() {
        return false;
    }

    protected Boolean checkBilancio() {
        return true;
    }

    @Override
    protected void indexChanged(int row) {
        super.indexChanged(row);
        Determina d = (Determina) this.getContext().getCurrentEntity();
        Boolean vResp = !d.getVistoResponsabile() && this.checkResponsabile();
        Boolean vBil = d.getVistoResponsabile() && !d.getVistoBilancio() && this.checkBilancio();
        Boolean vNeg = vBil;
        this.pushButtonResponsabile.setEnabled(vResp);
        this.pushButtonBilancio.setEnabled(vBil);
        this.pushButtonVistoNegato.setEnabled(vNeg);        
    }


}
