package com.axiastudio.suite.interoperabilita.utilities;

import com.axiastudio.suite.SuiteUtil;
import com.axiastudio.suite.interoperabilita.entities.*;
import com.axiastudio.suite.interoperabilita.utilities.StringMarshalling;
import com.axiastudio.suite.interoperabilita.utilities.StringUnmarshalling;
import com.axiastudio.suite.protocollo.entities.Oggetto;
import com.axiastudio.suite.protocollo.entities.Protocollo;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

/**
 * User: tiziano
 * Date: 11/02/14
 * Time: 17:11
 */
public class JAXBHelper {

    private static String CONTEXT = "com.axiastudio.suite.interoperabilita.entities";

    public static Segnatura leggiSegnatura(String xml){
        Segnatura segnatura = (Segnatura) StringUnmarshalling.getObject(CONTEXT, xml);
        return segnatura;
    }

    public static String scriviSegnatura(Segnatura segnatura){
        String xml = StringMarshalling.getXMLString(CONTEXT, segnatura, false);
        return xml;
    }

    public static Segnatura segnaturaDaProtocollo(Protocollo protocollo){

        Segnatura segnatura = new Segnatura();
        segnatura.setVersione("aaaa-mm-gg");
        segnatura.setXmlLang("it");

        Intestazione intestazione = new Intestazione();
        segnatura.setIntestazione(intestazione);

        Identificatore identificatore = new Identificatore();
        identificatore.setCodiceAmministrazione(SuiteUtil.trovaCostante("CODICE_AMMINISTRAZIONE").getValore());
        identificatore.setCodiceAOO(SuiteUtil.trovaCostante("CODICE_AOO").getValore());
        identificatore.setNumeroRegistrazione(protocollo.getIddocumento());
        identificatore.setDataRegistrazione(protocollo.getDataprotocollo().toString());
        identificatore.setCodiceRegistro("--");
        intestazione.setIdentificatore(identificatore);

        Origine origine = new Origine();
        intestazione.setOrigine(origine);

        IndirizzoTelematico indirizzoTelematico = new IndirizzoTelematico();
        indirizzoTelematico.setTipo("smtp");
        indirizzoTelematico.setvalue("info@comune.rivadelgarda.tn.it");
        origine.setIndirizzoTelematico(indirizzoTelematico);

        Mittente mittente = new Mittente();
        origine.setMittente(mittente);
        Amministrazione amministrazione = new Amministrazione();
        mittente.setAmministrazione(amministrazione);
        Denominazione denominazione = new Denominazione();
        denominazione.setvalue(SuiteUtil.trovaCostante("DENOMINAZIONE").getValore());
        amministrazione.setDenominazione(denominazione);

        UnitaOrganizzativa unitaOrganizzativa = new UnitaOrganizzativa();
        unitaOrganizzativa.setTipo("permanente");
        Denominazione denominazioneUnitaOrganizzativa = new Denominazione();
        denominazioneUnitaOrganizzativa.setvalue("Sistema informativo comunale");
        unitaOrganizzativa.setDenominazione(denominazioneUnitaOrganizzativa);
        IndirizzoPostale indirizzoPostaleUnitaOrganizzativa = new IndirizzoPostale();
        indirizzoPostaleUnitaOrganizzativa.getDenominazioneOrToponimoOrCivicoOrCAPOrComuneOrProvinciaOrNazione()
                .add(new Denominazione());
        unitaOrganizzativa.getUnitaOrganizzativaOrRuoloOrPersonaOrIndirizzoPostaleOrIndirizzoTelematicoOrTelefonoOrFax()
                .add(indirizzoPostaleUnitaOrganizzativa);
        amministrazione.getUnitaOrganizzativaOrRuoloOrPersonaOrIndirizzoPostaleOrIndirizzoTelematicoOrTelefonoOrFax()
                .add(unitaOrganizzativa);

        AOO aoo = new AOO();
        mittente.setAOO(aoo);
        Denominazione denominazioneAoo = new Denominazione();
        aoo.setDenominazione(denominazioneAoo);
        denominazioneAoo.setvalue("RSERVIZI");

        Destinazione destinazione = new Destinazione();
        intestazione.getDestinazione().add(destinazione);
        destinazione.setConfermaRicezione("no");
        IndirizzoTelematico indirizzoTelematicoDestinazione = new IndirizzoTelematico();
        indirizzoTelematicoDestinazione.setTipo("smtp");
        destinazione.setIndirizzoTelematico(indirizzoTelematicoDestinazione);
        indirizzoTelematicoDestinazione.setvalue("serv.supportoeinformatica@pec.provincia.tn.it");
        Destinatario destinatario = new Destinatario();
        destinazione.getDestinatario().add(destinatario);
        Amministrazione amministrazioneDestinatario = new Amministrazione();
        destinatario.getAmministrazioneOrAOOOrDenominazioneOrPersona().add(amministrazioneDestinatario);
        Denominazione denominazioneAministrazioneDestinatario = new Denominazione();
        amministrazioneDestinatario.setDenominazione(denominazioneAministrazioneDestinatario);
        denominazioneAministrazioneDestinatario.setvalue("Provincia Autonoma di Trento");
        UnitaOrganizzativa unitaOrganizzativaAmministrazioneDestinatario = new UnitaOrganizzativa();
        unitaOrganizzativaAmministrazioneDestinatario.setTipo("permanente");
        amministrazioneDestinatario.getUnitaOrganizzativaOrRuoloOrPersonaOrIndirizzoPostaleOrIndirizzoTelematicoOrTelefonoOrFax()
                .add(unitaOrganizzativaAmministrazioneDestinatario);
        Denominazione denominazioneUnitaOrganizzativaDestinatario = new Denominazione();
        unitaOrganizzativaAmministrazioneDestinatario.setDenominazione(denominazioneUnitaOrganizzativaDestinatario);
        denominazioneUnitaOrganizzativaDestinatario.setvalue("Direzione Generale della Provincia");
        UnitaOrganizzativa unitaOrganizzativaAmministrazioneDestinatario2 = new UnitaOrganizzativa();
        unitaOrganizzativaAmministrazioneDestinatario2.setTipo("permanente");
        unitaOrganizzativaAmministrazioneDestinatario.getUnitaOrganizzativaOrRuoloOrPersonaOrIndirizzoPostaleOrIndirizzoTelematicoOrTelefonoOrFax()
                .add(unitaOrganizzativaAmministrazioneDestinatario2);
        Denominazione denominazioneUnitaOrganizzativaDestinatario2 = new Denominazione();
        unitaOrganizzativaAmministrazioneDestinatario2.setDenominazione(denominazioneUnitaOrganizzativaDestinatario2);
        denominazioneUnitaOrganizzativaDestinatario2.setvalue("Servizio supporto amministrativo e informatica");
        IndirizzoPostale indirizzoPostale = new IndirizzoPostale();
        unitaOrganizzativaAmministrazioneDestinatario2.getUnitaOrganizzativaOrRuoloOrPersonaOrIndirizzoPostaleOrIndirizzoTelematicoOrTelefonoOrFax()
                .add(indirizzoPostale);
        Denominazione denominazioneIndirizzoPostale = new Denominazione();
        indirizzoPostale.getDenominazioneOrToponimoOrCivicoOrCAPOrComuneOrProvinciaOrNazione()
                .add(denominazioneIndirizzoPostale);
        denominazioneIndirizzoPostale.setvalue("");

        intestazione.setOggetto("Risposta");

        //Riferimenti riferimenti = new Riferimenti();
        //segnatura.setRiferimenti(riferimenti);

        Descrizione descrizione = new Descrizione();
        segnatura.setDescrizione(descrizione);
        Documento documento = new Documento();
        descrizione.getDocumentoOrTestoDelMessaggio().add(documento);
        documento.setNome("risposta.pdf");
        documento.setTipoRiferimento("MIME");
        Oggetto oggetto = new Oggetto();
        documento.setOggetto("Descrizione del file risposta.pdf");

        return segnatura;

    }

}
