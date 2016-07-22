package com.axiastudio.suite.interoperabilita;

import com.axiastudio.suite.interoperabilita.entities.xsd.*;
import com.axiastudio.suite.interoperabilita.utilities.StringMarshalling;
import com.axiastudio.suite.interoperabilita.utilities.StringUnmarshalling;
import org.junit.*;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

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

    String CONTEXT = "com.axiastudio.suite.interoperabilita.entities.xsd";

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
    public void leggi() throws Exception {
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
    public void scrivi() throws Exception {

        Segnatura segnatura = new Segnatura();
        segnatura.setVersione("aaaa-mm-gg");
        segnatura.setXmlLang("it");

        Intestazione intestazione = new Intestazione();
        segnatura.setIntestazione(intestazione);

        Identificatore identificatore = new Identificatore();
        CodiceAmministrazione codiceAmministrazione1 = new CodiceAmministrazione();
        codiceAmministrazione1.setContent("r_h330");
        identificatore.setCodiceAmministrazione(codiceAmministrazione1);
        CodiceAOO codiceAOO1 = new CodiceAOO();
        codiceAOO1.setContent("RSERVIZI");
        identificatore.setCodiceAOO(codiceAOO1);
        NumeroRegistrazione numeroRegistrazione1 = new NumeroRegistrazione();
        numeroRegistrazione1.setContent("0000001");
        identificatore.setNumeroRegistrazione(numeroRegistrazione1);
        DataRegistrazione dataRegistrazione1 = new DataRegistrazione();
        dataRegistrazione1.setContent("2014-01-01");
        identificatore.setDataRegistrazione(dataRegistrazione1);
        CodiceRegistro codiceRegistro = new CodiceRegistro();
        codiceRegistro.setContent("--");
        identificatore.setCodiceRegistro(codiceRegistro);
        intestazione.setIdentificatore(identificatore);

        Origine origine = new Origine();
        intestazione.setOrigine(origine);

        IndirizzoTelematico indirizzoTelematico = new IndirizzoTelematico();
        indirizzoTelematico.setTipo("smtp");
        indirizzoTelematico.setContent("info@comune.rivadelgarda.tn.it");
        origine.setIndirizzoTelematico(indirizzoTelematico);

        Mittente mittente = new Mittente();
        origine.setMittente(mittente);
        Amministrazione amministrazione = new Amministrazione();
        mittente.setAmministrazione(amministrazione);
        Denominazione denominazione = new Denominazione();
        denominazione.setContent("Comune di Riva del Garda");
        amministrazione.setDenominazione(denominazione);

        UnitaOrganizzativa unitaOrganizzativa = new UnitaOrganizzativa();
        unitaOrganizzativa.setTipo("permanente");
        Denominazione denominazioneUnitaOrganizzativa = new Denominazione();
        denominazioneUnitaOrganizzativa.setContent("Sistema informativo comunale");
        unitaOrganizzativa.setDenominazione(denominazioneUnitaOrganizzativa);
        IndirizzoPostale indirizzoPostaleUnitaOrganizzativa = new IndirizzoPostale();
        Denominazione denominazione1 = new Denominazione();
        denominazione1.setContent("Piazza II Novembre, 5 - 38062 Riva del Garda (TN)");
        indirizzoPostaleUnitaOrganizzativa.setDenominazione(denominazione1);
        unitaOrganizzativa.setIndirizzoPostale(indirizzoPostaleUnitaOrganizzativa);
        amministrazione.setUnitaOrganizzativa(unitaOrganizzativa);

        AOO aoo = new AOO();
        mittente.setAOO(aoo);
        Denominazione denominazioneAoo = new Denominazione();
        denominazioneAoo.setContent("RSERVIZI");
        aoo.setDenominazione(denominazioneAoo);

        Destinazione destinazione = new Destinazione();
        intestazione.getDestinazione().add(destinazione);
        destinazione.setConfermaRicezione("no");
        IndirizzoTelematico indirizzoTelematicoDestinazione = new IndirizzoTelematico();
        indirizzoTelematicoDestinazione.setTipo("smtp");
        destinazione.setIndirizzoTelematico(indirizzoTelematicoDestinazione);
        indirizzoTelematicoDestinazione.setContent("serv.supportoeinformatica@pec.provincia.tn.it");
        Destinatario destinatario = new Destinatario();
        destinazione.getDestinatario().add(destinatario);
        Amministrazione amministrazioneDestinatario = new Amministrazione();
        //destinatario.getAmministrazioneOrAOOOrDenominazioneOrPersona().add(amministrazioneDestinatario);
        Denominazione denominazioneAministrazioneDestinatario = new Denominazione();
        denominazioneAministrazioneDestinatario.setContent("Provincia Autonoma di Trento");
        amministrazioneDestinatario.setDenominazione(denominazioneAministrazioneDestinatario);
        UnitaOrganizzativa unitaOrganizzativaAmministrazioneDestinatario = new UnitaOrganizzativa();
        unitaOrganizzativaAmministrazioneDestinatario.setTipo("permanente");
        amministrazioneDestinatario.setUnitaOrganizzativa(unitaOrganizzativaAmministrazioneDestinatario);
        Denominazione denominazioneUnitaOrganizzativaDestinatario = new Denominazione();
        denominazioneUnitaOrganizzativaDestinatario.setContent("Direzione Generale della Provincia");
        unitaOrganizzativaAmministrazioneDestinatario.setDenominazione(denominazioneUnitaOrganizzativaDestinatario);
        UnitaOrganizzativa unitaOrganizzativaAmministrazioneDestinatario2 = new UnitaOrganizzativa();
        unitaOrganizzativaAmministrazioneDestinatario2.setTipo("permanente");
        unitaOrganizzativaAmministrazioneDestinatario.setUnitaOrganizzativa(unitaOrganizzativaAmministrazioneDestinatario2);
        Denominazione denominazioneUnitaOrganizzativaDestinatario2 = new Denominazione();
        denominazioneUnitaOrganizzativaDestinatario2.setContent("Servizio supporto amministrativo e informatica");
        unitaOrganizzativaAmministrazioneDestinatario2.setDenominazione(denominazioneUnitaOrganizzativaDestinatario2);
        IndirizzoPostale indirizzoPostale = new IndirizzoPostale();
        unitaOrganizzativaAmministrazioneDestinatario2.setIndirizzoPostale(indirizzoPostale);
        Denominazione denominazioneIndirizzoPostale = new Denominazione();
        denominazioneIndirizzoPostale.setContent("Via...");
        indirizzoPostale.setDenominazione(denominazioneIndirizzoPostale);
        Oggetto oggetto = new Oggetto();
        oggetto.setContent("Risposta");
        intestazione.setOggetto(oggetto);

        //Riferimenti riferimenti = new Riferimenti();
        //segnatura.setRiferimenti(riferimenti);

        Descrizione descrizione = new Descrizione();
        segnatura.setDescrizione(descrizione);
        Documento documento = new Documento();
        descrizione.setDocumento(documento);
        documento.setNome("risposta.pdf");
        documento.setTipoRiferimento("MIME");
        Oggetto oggetto1 = new Oggetto();
        oggetto1.setContent("Descrizione del file risposta.pdf");
        documento.setOggetto(oggetto1);

        String xml = StringMarshalling.getXMLStringXSD(CONTEXT, segnatura, "Segnatura", true);
        System.out.println(xml);



        //valida(StringMarshalling.getXMLString(CONTEXT, segnatura, false));

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
