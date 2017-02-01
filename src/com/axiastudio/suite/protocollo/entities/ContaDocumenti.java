package com.axiastudio.suite.protocollo.entities;

import com.axiastudio.suite.base.entities.Ufficio;

import java.util.Date;

/**
 * Created by Comune di Riva del Garda
 */
public class ContaDocumenti {

    private Ufficio ufficio;
    private Protocollo protocollo;
    private Long numDocumenti;

    public Ufficio getUfficio() {
        return ufficio;
    }

    public void setUfficio(Ufficio ufficio) {
        this.ufficio = ufficio;
    }

    public Protocollo getProtocollo() {
        return protocollo;
    }

    public void setProtocollo(Protocollo protocollo) {
        this.protocollo = protocollo;
    }

    public Long getNumDocumenti() {
        return numDocumenti;
    }

    public void setNumDocumenti(Long numDocumenti) {
        this.numDocumenti = numDocumenti;
    }

    public String getIddocumento(){
        return this.protocollo.getIddocumento();
    }

    public void setIddocumento(String iddocumento){

    }

    public TipoProtocollo getTipoprotocollo(){
        return this.protocollo.getTipo();
    }

    public void setTipoprotocollo(TipoProtocollo tipoProtocollo){

    }

    public Date getDataprotocollo(){
        return this.protocollo.getDataprotocollo();
    }

    public void setDataprotocollo(Date dataProtocollo){

    }

    public String getOggetto(){
        return this.protocollo.getOggetto();
    }

    public void setOggetto(String oggetto){

    }

}
