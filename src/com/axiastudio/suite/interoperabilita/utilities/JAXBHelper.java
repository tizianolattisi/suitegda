package com.axiastudio.suite.interoperabilita.utilities;

import com.axiastudio.suite.SuiteUtil;
import com.axiastudio.suite.anagrafiche.entities.Soggetto;
import com.axiastudio.suite.interoperabilita.entities.*;
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
        CodiceAmministrazione codiceAmministrazione1 = new CodiceAmministrazione();
        codiceAmministrazione1.setContent(SuiteUtil.trovaCostante("CODICE_AMMINISTRAZIONE").getValore());
        identificatore.setCodiceAmministrazione(codiceAmministrazione1);
        CodiceAOO codiceAOO1 = new CodiceAOO();
        codiceAOO1.setContent(SuiteUtil.trovaCostante("CODICE_AOO").getValore());
        identificatore.setCodiceAOO(codiceAOO1);
        NumeroRegistrazione numeroRegistrazione1 = new NumeroRegistrazione();
        numeroRegistrazione1.setContent(protocollo.getIddocumento().substring(4));
        identificatore.setNumeroRegistrazione(numeroRegistrazione1);
        DataRegistrazione dataRegistrazione1 = new DataRegistrazione();
        dataRegistrazione1.setContent(protocollo.getDataprotocollo().toString());
        identificatore.setDataRegistrazione(dataRegistrazione1);
        CodiceRegistro codiceRegistro = new CodiceRegistro();
        codiceRegistro.setContent("--");
        identificatore.setCodiceRegistro(codiceRegistro);
        intestazione.setIdentificatore(identificatore);
        Oggetto oggetto = new Oggetto();
        oggetto.setContent(protocollo.getOggetto());
        intestazione.setOggetto(oggetto);

        Origine origine = new Origine();
        intestazione.setOrigine(origine);

        IndirizzoTelematico indirizzoTelematico = new IndirizzoTelematico();
        indirizzoTelematico.setTipo("smtp");
        indirizzoTelematico.setContent(protocollo.getUfficioProtocolloCollection().iterator().next().getUfficio().getPec());
        origine.setIndirizzoTelematico(indirizzoTelematico);

        Mittente mittente = new Mittente();
        origine.setMittente(mittente);
        Amministrazione amministrazione = new Amministrazione();
        mittente.setAmministrazione(amministrazione);
        Denominazione denominazione = new Denominazione();
        denominazione.setContent(SuiteUtil.trovaCostante("DENOMINAZIONE").getValore());
        amministrazione.setDenominazione(denominazione);

        UnitaOrganizzativa unitaOrganizzativa = new UnitaOrganizzativa();
        unitaOrganizzativa.setTipo("permanente");
        Denominazione denominazioneUnitaOrganizzativa = new Denominazione();
        denominazioneUnitaOrganizzativa.setContent(SuiteUtil.trovaCostante("DENOMINAZIONE").getValore());
        unitaOrganizzativa.setDenominazione(denominazioneUnitaOrganizzativa);
        IndirizzoPostale indirizzoPostaleUnitaOrganizzativa = new IndirizzoPostale();
        indirizzoPostaleUnitaOrganizzativa.setDenominazione(new Denominazione());
        unitaOrganizzativa.setIndirizzoPostale(indirizzoPostaleUnitaOrganizzativa);
        amministrazione.setUnitaOrganizzativa(unitaOrganizzativa);

        AOO aoo = new AOO();
        mittente.setAOO(aoo);
        Denominazione denominazioneAoo = new Denominazione();
        aoo.setDenominazione(denominazioneAoo);
        denominazioneAoo.setContent(SuiteUtil.trovaCostante("CODICE_AOO").getValore());

        Destinazione destinazione = new Destinazione();
        intestazione.getDestinazione().add(destinazione);
        destinazione.setConfermaRicezione("no");
        IndirizzoTelematico indirizzoTelematicoDestinazione = new IndirizzoTelematico();
        indirizzoTelematicoDestinazione.setTipo("smtp");
        destinazione.setIndirizzoTelematico(indirizzoTelematicoDestinazione);
        indirizzoTelematicoDestinazione.setContent(pecDestinatario);

        // documento principale o corpo del messaggio
        Descrizione descrizione = new Descrizione();
        segnatura.setDescrizione(descrizione);
        Integer allegatiDa = 0;
        if( protocollo.getPecBody()==null || protocollo.getPecBody().equals("") ) {
            Documento documento = new Documento();
            descrizione.setDocumento(documento);
            documento.setNome(fileAllegati.get(0));
            documento.setTipoRiferimento("MIME"); // fisso
            Oggetto oggetto1 = new Oggetto();
            oggetto1.setContent(fileAllegati.get(0));
            documento.setOggetto(oggetto1);
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
            descrizione.setDocumento(documento);
            documento.setNome(fileAllegati.get(i));
            documento.setTipoRiferimento("MIME"); // fisso
            Oggetto oggetto1 = new Oggetto();
            oggetto1.setContent(fileAllegati.get(i));
            documento.setOggetto(oggetto1);
            allegati.getDocumentoOrFascicolo().add(documento);
        }
        if( allegati.getDocumentoOrFascicolo().size()>0 ){
            descrizione.setAllegati(allegati);
        }

        segnatura.setDescrizione(descrizione);

        return segnatura;

    }

}
