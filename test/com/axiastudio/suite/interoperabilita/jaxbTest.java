package com.axiastudio.suite.interoperabilita;

import com.axiastudio.suite.interoperabilita.entities.*;
import com.axiastudio.suite.interoperabilita.utilities.StringMarshalling;
import com.axiastudio.suite.interoperabilita.utilities.StringUnmarshalling;
import com.axiastudio.suite.protocollo.entities.Oggetto;
import org.junit.*;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * User: tiziano
 * Date: 11/02/14
 * Time: 16:27
 */
public class jaxbTest {

    String CONTEXT = "com.axiastudio.suite.interoperabilita.entities";

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void leggi() throws JAXBException, IOException {
        String filePath = "testSegnatura.xml";

        FileInputStream fileInputStream = new FileInputStream(filePath);
        StringBuilder builder = new StringBuilder();
        int ch;
        while((ch = fileInputStream.read()) != -1){
            builder.append((char)ch);
        }

        String xml = builder.toString();

        Segnatura segnatura = (Segnatura) StringUnmarshalling.getObject(CONTEXT, xml);

        System.out.println(segnatura.getIntestazione().getOggetto());

    }

    @Test
    public void scrivi(){

        Segnatura segnatura = new Segnatura();
        segnatura.setVersione("aaaa-mm-gg");
        segnatura.setXmlLang("it");

        Intestazione intestazione = new Intestazione();
        segnatura.setIntestazione(intestazione);

        Identificatore identificatore = new Identificatore();
        identificatore.setCodiceAmministrazione("r_h330");
        identificatore.setCodiceAOO("RSERVIZI");
        identificatore.setNumeroRegistrazione("0000001");
        identificatore.setDataRegistrazione("2014-01-01");
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
        denominazione.setvalue("Comune di Riva del Garda");
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

        String xml = StringMarshalling.getXMLString(CONTEXT, segnatura, false);
        System.out.println(xml);



        valida(StringMarshalling.getXMLString(CONTEXT, segnatura, false));

    }


    /*
     * utilities
     */

    private Boolean valida(String xml) {
        DocumentBuilder docBuilder = getDocumentBuilder();
        Document validationDoc = null;
        try {
            validationDoc = docBuilder.parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));
            return true;
        } catch (SAXException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private DocumentBuilder getDocumentBuilder() {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setValidating(true);
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = docFactory.newDocumentBuilder();

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        ErrorHandler errorHandler = new ErrorHandler() {
            @Override
            public void error(SAXParseException exception) throws SAXException {
                // do something more useful in each of these handlers
                exception.printStackTrace();
            }
            @Override
            public void fatalError(SAXParseException exception) throws SAXException {
                exception.printStackTrace();
            }
            @Override
            public void warning(SAXParseException exception) throws SAXException {
                exception.printStackTrace();
            }
        };
        docBuilder.setErrorHandler(errorHandler);
        return docBuilder;
    }

}
