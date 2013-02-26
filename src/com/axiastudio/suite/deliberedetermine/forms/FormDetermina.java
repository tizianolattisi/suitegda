/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.deliberedetermine.forms;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.suite.deliberedetermine.entities.Determina;
import com.axiastudio.suite.deliberedetermine.entities.ImpegnoDetermina;
import com.axiastudio.suite.finanziaria.entities.IFinanziaria;
import com.axiastudio.suite.procedimenti.IGestoreDeleghe;
import com.axiastudio.suite.procedimenti.entities.CodiceCarica;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QTabWidget;
import com.trolltech.qt.gui.QTableWidget;
import com.trolltech.qt.gui.QTableWidgetItem;
import java.util.List;

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
        // impegni da utilità finanziaria esterna?
        QTabWidget tabWidget = (QTabWidget) this.findChild(QTabWidget.class, "tabWidget");
        Object objFinanziariaUtil = Register.queryUtility(IFinanziaria.class);
        if( objFinanziariaUtil != null ){
            if( tabWidget.isTabEnabled(0) ){
                tabWidget.setTabEnabled(0, false);
            }
            IFinanziaria finanziariaUtil = (IFinanziaria) objFinanziariaUtil;
            List<ImpegnoDetermina> impegni = finanziariaUtil.getImpegniDetermina("A", "DT", "2009", "1150");
            QTableWidget tableWidget = (QTableWidget) this.findChild(QTableWidget.class, "tableWidgetImpegni");
            tableWidget.clear();
            tableWidget.setColumnCount(1);
            int i = 0;
            for( ImpegnoDetermina impegno: impegni ){
                tableWidget.insertRow(i);
                QTableWidgetItem itemImporto = new QTableWidgetItem(impegno.getImporto().toString());
                tableWidget.setItem(i, 0, itemImporto);
                i++;
            }
        } else {
            if( tabWidget.isTabEnabled(1) ){
                tabWidget.setTabEnabled(1, false);
            }
        }
        
        super.indexChanged(row);
    }


}
