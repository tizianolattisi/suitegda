package com.axiastudio.suite.interoperabilita;

/**
 * User: tiziano
 * Date: 05/02/14
 * Time: 09:56
 */
public class Destinatario {
    private String indirizzoTelematico;
    private Boolean confermaRicezione=false;
    private Boolean perConoscenza=false;
    private TipoDestinatario tipoDestinatario = TipoDestinatario.AMMINISTRAZIONE;

    // AMMINISTRAZIONE
    private String denominazione=null;
    private String denominazioneUnitaOrganizzativa=null;

    public Destinatario() {
    }

    public void setIndirizzoTelematico(String indirizzoTelematico) {
        this.indirizzoTelematico = indirizzoTelematico;
    }

    public String getIndirizzoTelematico() {
        return indirizzoTelematico;
    }

    public Boolean getConfermaRicezione() {
        return confermaRicezione;
    }

    public void setConfermaRicezione(Boolean confermaRicezione) {
        this.confermaRicezione = confermaRicezione;
    }

    public Boolean getPerConoscenza() {
        return perConoscenza;
    }

    public void setPerConoscenza(Boolean perConoscenza) {
        this.perConoscenza = perConoscenza;
    }

    public TipoDestinatario getTipoDestinatario() {
        return tipoDestinatario;
    }

    public void setTipoDestinatario(TipoDestinatario tipoDestinatario) {
        this.tipoDestinatario = tipoDestinatario;
    }

    public String getDenominazione() {
        return denominazione;
    }

    public void setDenominazione(String denominazione) {
        this.denominazione = denominazione;
    }

    public String getDenominazioneUnitaOrganizzativa() {
        return denominazioneUnitaOrganizzativa;
    }

    public void setDenominazioneUnitaOrganizzativa(String denominazioneUnitaOrganizzativa) {
        this.denominazioneUnitaOrganizzativa = denominazioneUnitaOrganizzativa;
    }
}
