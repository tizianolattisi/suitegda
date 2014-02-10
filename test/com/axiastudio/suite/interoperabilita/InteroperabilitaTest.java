package com.axiastudio.suite.interoperabilita;

import org.junit.*;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

/**
 * User: tiziano
 * Date: 04/02/14
 * Time: 16:12
 */
public class InteroperabilitaTest {

    public static final String DENOMINAZIONE_DESTINATARIO = "Provincia Autonoma di Trento";
    public static final String DENOMINAZIONE_UNITA_ORGANIZZATIVA_DESTINATARIO = "Servizio supporto amministrativo e informatica";
    String CODICE_AMMINISTRAZIONE = "r_h330";
    String CODICE_AOO = "RSERVIZI";
    String NUMERO_REGISTRAZIONE = "0000001";
    String DATA_REGISTRAZIONE = "2014-01-01"; // aaaa-mm-gg
    String INDIRIZZO_TELEMATICO = "info@comune.rivadelgarda.tn.it";
    String DENOMINAZIONE = "Comune di Riva del Garda";
    String TOPONIMO = "piazza";
    String INDIRIZZO_POSTALE = "III Novembre";
    String CIVICO = "5";
    String CAP = "38066";
    String COMUNE = "Riva del Garda";
    String PROVINCIA = "TN";
    String TELEFONO = "0464 573888";
    String FAX = "0464 552410";
    String INDIRIZZO_TELEMATICO_DESTINATARIO = "serv.supportoeinformatica@pec.provincia.tn.it";
    String INDIRIZZO_TELEMATICO_PERCONOSCENZA = "serv.supportoeinformatica@pec.provincia.tn.it";
    String OGGETTO = "Invio di prova per test Segnatura.xml";

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
    public void creaSegnatura() {

        Segnatura segnatura = new Segnatura();
        segnatura.setCodiceAmministrazione(CODICE_AMMINISTRAZIONE);
        segnatura.setCodiceAOO(CODICE_AOO);
        segnatura.setNumeroRegistrazione(NUMERO_REGISTRAZIONE);
        segnatura.setDataRegistrazione(DATA_REGISTRAZIONE);
        segnatura.setIndirizzoTelematico(INDIRIZZO_TELEMATICO);
        segnatura.setDenominazione(DENOMINAZIONE);
        segnatura.setToponimo(TOPONIMO);
        segnatura.setIndirizzoPostale(INDIRIZZO_POSTALE);
        segnatura.setCivico(CIVICO);
        segnatura.setCap(CAP);
        segnatura.setComune(COMUNE);
        segnatura.setProvincia(PROVINCIA);
        segnatura.setTelefono(TELEFONO);
        segnatura.setFax(FAX);
        segnatura.setOggetto(OGGETTO);

        // destinatario
        Destinatario destinatario = new Destinatario();
        destinatario.setIndirizzoTelematico(INDIRIZZO_TELEMATICO_DESTINATARIO);
        destinatario.setConfermaRicezione(true);
        destinatario.setDenominazione(DENOMINAZIONE_DESTINATARIO);
        destinatario.setDenominazioneUnitaOrganizzativa(DENOMINAZIONE_UNITA_ORGANIZZATIVA_DESTINATARIO);
        segnatura.addDestinatario(destinatario);

        // per conoscenza
        Destinatario destinatario2 = new Destinatario();
        destinatario2.setIndirizzoTelematico(INDIRIZZO_TELEMATICO_PERCONOSCENZA);
        destinatario2.setConfermaRicezione(false);
        destinatario2.setPerConoscenza(true);
        segnatura.addDestinatario(destinatario2);

        // documento
        Documento documento = new Documento();
        documento.setNome("documento.pdf");
        documento.setOggetto("File documento come test.");
        segnatura.setDocumento(documento);

        // allegati
        Documento allegato1 = new Documento();
        allegato1.setNome("allegato1.pdf");
        allegato1.setOggetto("File allegato come test.");
        segnatura.addAllegato(allegato1);

        String xml = segnatura.toXml();

        System.out.println(xml);

        validatore(xml);

    }

    @Test
    public void leggiSegnatura() throws IOException {

        String xml = null;

        BufferedReader br = new BufferedReader(new FileReader("testSegnatura.xml"));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            xml = sb.toString();
        } finally {
            br.close();
        }

        Segnatura segnatura = new Segnatura(xml);

        System.out.println(segnatura.getIndirizzoTelematico());


    }

    private Boolean validatore(String xml) {
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
