package com.axiastudio.suite.interoperabilita.utilities;

import com.axiastudio.suite.SuiteUtil;
import com.axiastudio.suite.anagrafiche.entities.Soggetto;
import com.axiastudio.suite.interoperabilita.entities.xsd.*;
import com.axiastudio.suite.protocollo.entities.Protocollo;

import java.util.List;

/**
 * User: tiziano Date: 11/02/14 Time: 17:11
 */
public class JAXBHelper {

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
	private static String CONTEXT = "com.axiastudio.suite.interoperabilita.entities.xsd";

	private static String CONTEXT_DTD = "com.axiastudio.suite.interoperabilita.entities.dtd";

	public static Segnatura leggiSegnatura(String xml) throws Exception {
		String context = CONTEXT;
		Segnatura segnatura = (Segnatura) StringUnmarshalling.getObject(context, xml);
		return segnatura;
	}

	public static Object leggiSegnaturaDtdOrXsd(String xml) throws Exception {
		Object segnatura = null;
		/*
		 * meglio fare riferimento alla presenza del namespace, altrimenti un
		 * xml che non ha presente segnatura.dtd si tenta di interpretarlo come
		 * xsd, ma senza namespace da in errore
		 */
		// boolean dtd = xml.indexOf("Segnatura.dtd") > -1;
		boolean xsd = xml.indexOf("http://www.digitPa.gov.it/protocollo/") > -1;
		if (!xsd)
			segnatura = (com.axiastudio.suite.interoperabilita.entities.dtd.Segnatura) StringUnmarshalling.getObjectDTD(xml);
		else
			segnatura = (com.axiastudio.suite.interoperabilita.entities.xsd.Segnatura) StringUnmarshalling.getObjectXSD(xml);
		return segnatura;
	}

	public static String scriviSegnatura(Segnatura segnatura) throws Exception {
		String xml = StringMarshalling.getXMLStringXSD(CONTEXT, segnatura, "Segnatura", false);
		return xml;
	}

	public static ConfermaRicezione leggiConfermaRicezione(String xml) throws Exception {
		ConfermaRicezione confermaRicezione = (ConfermaRicezione) StringUnmarshalling.getObject(CONTEXT, xml);
		return confermaRicezione;
	}

	public static String scriviConfermaRicezioneXSD(ConfermaRicezione confermaRicezione) throws Exception {
		String xml = StringMarshalling.getXMLStringXSD(CONTEXT, confermaRicezione, "ConfermaRicezione", false);
		return xml;
	}

	public static String scriviConfermaRicezioneDTD(com.axiastudio.suite.interoperabilita.entities.dtd.ConfermaRicezione confermaRicezione) throws Exception {
		String xml = StringMarshalling.getXMLStringDTD(CONTEXT_DTD, confermaRicezione, "ConfermaRicezione", false);
		return xml;
	}

	/*
	 * Con "mittente" ci si riferisce all'ente che ha richiesto la conferma di
	 * ricezione
	 */
	public static com.axiastudio.suite.interoperabilita.entities.dtd.ConfermaRicezione generaConfermaRicezioneDTD(String codiceAmministrazione, String codiceAOO, String numeroRegistrazione, String dataRegistrazione, String codiceAmministrazioneMittente, String codiceAOOMittente, String numeroRegistrazioneMittente, String dataRegistrazioneMittente) {
		com.axiastudio.suite.interoperabilita.entities.dtd.ConfermaRicezione confermaRicezione = new com.axiastudio.suite.interoperabilita.entities.dtd.ConfermaRicezione();

		// l'ente che ha protocollato in entrata (noi)
		com.axiastudio.suite.interoperabilita.entities.dtd.Identificatore identificatore = new com.axiastudio.suite.interoperabilita.entities.dtd.Identificatore();
		// CodiceAmministrazione codiceAmministrazione1 = new
		// CodiceAmministrazione();
		// codiceAmministrazione1.setContent(codiceAmministrazione);
		identificatore.setCodiceAmministrazione(codiceAmministrazione);
		// CodiceAOO codiceAOO1 = new CodiceAOO();
		// codiceAOO1.setContent(codiceAOO);
		identificatore.setCodiceAOO(codiceAOO);
		// NumeroRegistrazione numeroRegistrazione1 = new NumeroRegistrazione();
		// numeroRegistrazione1.setContent(numeroRegistrazione);
		identificatore.setNumeroRegistrazione(numeroRegistrazione);
		// DataRegistrazione dataRegistrazione1 = new DataRegistrazione();
		// dataRegistrazione1.setContent(dataRegistrazione);
		identificatore.setDataRegistrazione(dataRegistrazione);
		confermaRicezione.setIdentificatore(identificatore);

		// mittente: l'ente che ha richiesto conferma di ricezione
		com.axiastudio.suite.interoperabilita.entities.dtd.Identificatore identificatoreMittente = new com.axiastudio.suite.interoperabilita.entities.dtd.Identificatore();
		// CodiceAmministrazione codiceAmministrazione2 = new
		// CodiceAmministrazione();
		// codiceAmministrazione2.setContent(codiceAmministrazioneMittente);
		identificatoreMittente.setCodiceAmministrazione(codiceAmministrazioneMittente);
		// CodiceAOO codiceAOO2 = new CodiceAOO();
		// codiceAOO2.setContent(codiceAOOMittente);
		identificatoreMittente.setCodiceAOO(codiceAOOMittente);
		// NumeroRegistrazione numeroRegistrazione2 = new NumeroRegistrazione();
		// numeroRegistrazione2.setContent(numeroRegistrazioneMittente);
		identificatoreMittente.setNumeroRegistrazione(numeroRegistrazioneMittente);
		// DataRegistrazione dataRegistrazione2 = new DataRegistrazione();
		// dataRegistrazione2.setContent(dataRegistrazioneMittente);
		identificatoreMittente.setDataRegistrazione(dataRegistrazioneMittente);
		com.axiastudio.suite.interoperabilita.entities.dtd.MessaggioRicevuto messaggioRicevuto = new com.axiastudio.suite.interoperabilita.entities.dtd.MessaggioRicevuto();
		// messaggioRicevuto.setIdentificatore(identificatoreMittente)
		messaggioRicevuto.getIdentificatoreOrPrimaRegistrazioneOrDescrizioneMessaggio().add(identificatoreMittente);
		confermaRicezione.setMessaggioRicevuto(messaggioRicevuto);

		return confermaRicezione;

	}

	public static ConfermaRicezione generaConfermaRicezioneXSD(String codiceAmministrazione, String codiceAOO, String numeroRegistrazione, String dataRegistrazione, String codiceAmministrazioneMittente, String codiceAOOMittente, String numeroRegistrazioneMittente, String dataRegistrazioneMittente) {
		ConfermaRicezione confermaRicezione = new ConfermaRicezione();

		// l'ente che ha protocollato in entrata (noi)
		Identificatore identificatore = new Identificatore();
		CodiceAmministrazione codiceAmministrazione1 = new CodiceAmministrazione();
		codiceAmministrazione1.setContent(codiceAmministrazione);
		identificatore.setCodiceAmministrazione(codiceAmministrazione1);
		CodiceAOO codiceAOO1 = new CodiceAOO();
		codiceAOO1.setContent(codiceAOO);
		identificatore.setCodiceAOO(codiceAOO1);
		NumeroRegistrazione numeroRegistrazione1 = new NumeroRegistrazione();
		numeroRegistrazione1.setContent(numeroRegistrazione);
		identificatore.setNumeroRegistrazione(numeroRegistrazione1);
		DataRegistrazione dataRegistrazione1 = new DataRegistrazione();
		dataRegistrazione1.setContent(dataRegistrazione);
		identificatore.setDataRegistrazione(dataRegistrazione1);
		confermaRicezione.setIdentificatore(identificatore);

		// mittente: l'ente che ha richiesto conferma di ricezione
		Identificatore identificatoreMittente = new Identificatore();
		CodiceAmministrazione codiceAmministrazione2 = new CodiceAmministrazione();
		codiceAmministrazione2.setContent(codiceAmministrazioneMittente);
		identificatoreMittente.setCodiceAmministrazione(codiceAmministrazione2);
		CodiceAOO codiceAOO2 = new CodiceAOO();
		codiceAOO2.setContent(codiceAOOMittente);
		identificatoreMittente.setCodiceAOO(codiceAOO2);
		NumeroRegistrazione numeroRegistrazione2 = new NumeroRegistrazione();
		numeroRegistrazione2.setContent(numeroRegistrazioneMittente);
		identificatoreMittente.setNumeroRegistrazione(numeroRegistrazione2);
		DataRegistrazione dataRegistrazione2 = new DataRegistrazione();
		dataRegistrazione2.setContent(dataRegistrazioneMittente);
		identificatoreMittente.setDataRegistrazione(dataRegistrazione2);
		MessaggioRicevuto messaggioRicevuto = new MessaggioRicevuto();
		messaggioRicevuto.setIdentificatore(identificatoreMittente);
		confermaRicezione.setMessaggioRicevuto(messaggioRicevuto);

		return confermaRicezione;

	}

}
