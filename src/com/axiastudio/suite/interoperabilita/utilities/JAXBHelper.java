package com.axiastudio.suite.interoperabilita.utilities;

import com.axiastudio.suite.SuiteUtil;
import com.axiastudio.suite.anagrafiche.entities.Soggetto;
import com.axiastudio.suite.interoperabilita.entities.*;
import com.axiastudio.suite.protocollo.entities.Oggetto;
import com.axiastudio.suite.protocollo.entities.Protocollo;

import java.util.List;

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

    public static Segnatura segnaturaDaProtocollo(Protocollo protocollo, Soggetto soggetto, String pecDestinatario, List<String> fileAllegati){

        Segnatura segnatura = new Segnatura();
        segnatura.setVersione("aaaa-mm-gg");
        segnatura.setXmlLang("it");

        Intestazione intestazione = new Intestazione();
        segnatura.setIntestazione(intestazione);

        Identificatore identificatore = new Identificatore();
        identificatore.setCodiceAmministrazione(SuiteUtil.trovaCostante("CODICE_AMMINISTRAZIONE").getValore());
        identificatore.setCodiceAOO(SuiteUtil.trovaCostante("CODICE_AOO").getValore());
        identificatore.setNumeroRegistrazione(protocollo.getIddocumento().substring(4));
        identificatore.setDataRegistrazione(protocollo.getDataprotocollo().toString());
        identificatore.setCodiceRegistro("--");
        intestazione.setIdentificatore(identificatore);
        intestazione.setOggetto(protocollo.getOggetto());

        Origine origine = new Origine();
        intestazione.setOrigine(origine);

        IndirizzoTelematico indirizzoTelematico = new IndirizzoTelematico();
        indirizzoTelematico.setTipo("smtp");
        indirizzoTelematico.setvalue(protocollo.getUfficioProtocolloCollection().iterator().next().getUfficio().getPec());
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
        denominazioneUnitaOrganizzativa.setvalue(SuiteUtil.trovaCostante("DENOMINAZIONE").getValore());
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
        denominazioneAoo.setvalue(SuiteUtil.trovaCostante("CODICE_AOO").getValore());

        Destinazione destinazione = new Destinazione();
        intestazione.getDestinazione().add(destinazione);
        destinazione.setConfermaRicezione("no");
        IndirizzoTelematico indirizzoTelematicoDestinazione = new IndirizzoTelematico();
        indirizzoTelematicoDestinazione.setTipo("smtp");
        destinazione.setIndirizzoTelematico(indirizzoTelematicoDestinazione);
        indirizzoTelematicoDestinazione.setvalue(pecDestinatario);

        /* // opzionale
        Destinatario destinatario = new Destinatario();
        destinazione.getDestinatario().add(destinatario);
        Amministrazione amministrazioneDestinatario = new Amministrazione();
        destinatario.getAmministrazioneOrAOOOrDenominazioneOrPersona().add(amministrazioneDestinatario);
        Denominazione denominazioneAministrazioneDestinatario = new Denominazione();
        amministrazioneDestinatario.setDenominazione(denominazioneAministrazioneDestinatario);
        denominazioneAministrazioneDestinatario.setvalue(soggetto.getDenominazione());
        UnitaOrganizzativa unitaOrganizzativaAmministrazioneDestinatario = new UnitaOrganizzativa();
        unitaOrganizzativaAmministrazioneDestinatario.setTipo("permanente");
        amministrazioneDestinatario.getUnitaOrganizzativaOrRuoloOrPersonaOrIndirizzoPostaleOrIndirizzoTelematicoOrTelefonoOrFax()
                .add(unitaOrganizzativaAmministrazioneDestinatario);
        Denominazione denominazioneUnitaOrganizzativaDestinatario = new Denominazione();
        unitaOrganizzativaAmministrazioneDestinatario.setDenominazione(denominazioneUnitaOrganizzativaDestinatario);
        denominazioneUnitaOrganizzativaDestinatario.setvalue(soggetto.getDenominazione2());
        UnitaOrganizzativa unitaOrganizzativaAmministrazioneDestinatario2 = new UnitaOrganizzativa();
        unitaOrganizzativaAmministrazioneDestinatario2.setTipo("permanente");
        unitaOrganizzativaAmministrazioneDestinatario.getUnitaOrganizzativaOrRuoloOrPersonaOrIndirizzoPostaleOrIndirizzoTelematicoOrTelefonoOrFax()
                .add(unitaOrganizzativaAmministrazioneDestinatario2);
        Denominazione denominazioneUnitaOrganizzativaDestinatario2 = new Denominazione();
        unitaOrganizzativaAmministrazioneDestinatario2.setDenominazione(denominazioneUnitaOrganizzativaDestinatario2);
        denominazioneUnitaOrganizzativaDestinatario2.setvalue(soggetto.getDenominazione3());
        IndirizzoPostale indirizzoPostale = new IndirizzoPostale();
        unitaOrganizzativaAmministrazioneDestinatario2.getUnitaOrganizzativaOrRuoloOrPersonaOrIndirizzoPostaleOrIndirizzoTelematicoOrTelefonoOrFax()
                .add(indirizzoPostale);
        Denominazione denominazioneIndirizzoPostale = new Denominazione();
        indirizzoPostale.getDenominazioneOrToponimoOrCivicoOrCAPOrComuneOrProvinciaOrNazione()
                .add(denominazioneIndirizzoPostale);
        denominazioneIndirizzoPostale.setvalue("");
        */


        //Riferimenti riferimenti = new Riferimenti();
        //segnatura.setRiferimenti(riferimenti);

        // documento principale o corpo del messaggio
        Descrizione descrizione = new Descrizione();
        segnatura.setDescrizione(descrizione);
        Integer allegatiDa = 0;
        if( protocollo.getPecBody()==null || protocollo.getPecBody().equals("") ) {
            Documento documento = new Documento();
            descrizione.getDocumentoOrTestoDelMessaggio().add(documento);
            documento.setNome(fileAllegati.get(0));
            documento.setTipoRiferimento("MIME"); // fisso
            documento.setOggetto(fileAllegati.get(0));
            allegatiDa++;
        } else {
            TestoDelMessaggio testoDelMessaggio = new TestoDelMessaggio();
            testoDelMessaggio.setTipoMIME("text/plain");
            testoDelMessaggio.setTipoRiferimento("MIME");
            testoDelMessaggio.setId("body");
        }

        // allegati
        Allegati allegati = new Allegati();
        for( Integer i=allegatiDa; i<fileAllegati.size(); i++ ){
            Documento documento = new Documento();
            descrizione.getDocumentoOrTestoDelMessaggio().add(documento);
            documento.setNome(fileAllegati.get(i));
            documento.setTipoRiferimento("MIME"); // fisso
            documento.setOggetto(fileAllegati.get(i));
            allegati.getDocumentoOrFascicolo().add(documento);
        }
        if( allegati.getDocumentoOrFascicolo().size()>0 ){
            descrizione.setAllegati(allegati);
        }

        segnatura.setDescrizione(descrizione);

        return segnatura;

    }

}
