package com.axiastudio.suite.interoperabilita;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * User: tiziano
 * Date: 04/02/14
 * Time: 14:16
 */
public class Segnatura {

    private String codiceAmministrazione;
    private String CodiceAOO;
    private String codiceRegistro;
    private String numeroRegistrazione;
    private String dataRegistrazione;
    private String indirizzoTelematico;
    private String denominazione;
    private String toponimo;
    private String indirizzoPostale;
    private String civico;
    private String cap;
    private String comune;
    private String provincia;
    private String telefono;
    private String fax;
    private String oggetto;

    private List<Destinatario> destinatari = new ArrayList<Destinatario>();
    private Documento documento;
    private List<Documento> allegati = new ArrayList<Documento>();

    public Segnatura() {

    }

    public Segnatura(String xml) {
        parse(xml);
    }

    private void parse(String xml) {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = docFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        InputSource is = new InputSource(new StringReader(xml));
        Document document = null;
        try {
            document = docBuilder.parse(is);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element documentElement = document.getDocumentElement();
        System.out.println(documentElement);

    }

    public String toXml() {
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

        Document doc = docBuilder.newDocument();

        // Segnatura
        Element segnatura = doc.createElement("Segnatura");
        //segnatura.setAttribute("versione", "2009-12-03");  // fisso, può essere omesso
        segnatura.setAttribute("xml:lang", "it"); // fisso
        doc.appendChild(segnatura);

        // Intestazione
        Element intestazione = doc.createElement("Intestazione");
        segnatura.appendChild(intestazione);

        // Identificatore
        Element identificatore = doc.createElement("Identificatore");
        intestazione.appendChild(identificatore);

        // CodiceAmministrazione
        Element codiceAmministrazione = doc.createElement("CodiceAmministrazione");
        identificatore.appendChild(codiceAmministrazione);
        codiceAmministrazione.appendChild(doc.createTextNode(getCodiceAmministrazione()));

        // CodiceAOO
        Element codiceAOO = doc.createElement("CodiceAOO");
        identificatore.appendChild(codiceAOO);
        codiceAOO.appendChild(doc.createTextNode(getCodiceAOO()));

        // CodiceRegistro
        Element codiceRegistro = doc.createElement("CodiceRegistro");
        identificatore.appendChild(codiceRegistro);
        codiceRegistro.appendChild(doc.createTextNode("--"));

        // NumeroRegistrazione
        Element numeroRegistrazione = doc.createElement("NumeroRegistrazione");
        identificatore.appendChild(numeroRegistrazione);
        numeroRegistrazione.appendChild(doc.createTextNode(getNumeroRegistrazione()));

        // DataRegistrazione
        Element dataRegistrazione = doc.createElement("DataRegistrazione");
        identificatore.appendChild(dataRegistrazione);
        dataRegistrazione.appendChild(doc.createTextNode(getDataRegistrazione()));

        // </Identificatore>

        // Origine
        Element origine = doc.createElement("Origine");
        intestazione.appendChild(origine);

        // IndirizzoTelematico
        Element indirizzoTelematico = doc.createElement("IndirizzoTelematico");
        indirizzoTelematico.setAttribute("tipo", "smtp");
        indirizzoTelematico.appendChild(doc.createTextNode(getIndirizzoTelematico()));
        origine.appendChild(indirizzoTelematico);

        // Mittente
        Element mittente = doc.createElement("Mittente");
        origine.appendChild(mittente);

        // Amministrazione
        Element amministrazione = doc.createElement("Amministrazione");
        mittente.appendChild(amministrazione);

        // Denominazione
        Element denominazione = doc.createElement("Denominazione");
        denominazione.appendChild(doc.createTextNode(getDenominazione()));
        amministrazione.appendChild(denominazione);


        // CodiceAmministrazione
        /*
        Element codiceAmministrazione2 = doc.createElement("CodiceAmministrazione");
        codiceAmministrazione2.appendChild(doc.createTextNode(getCodiceAmministrazione()));
        amministrazione.appendChild(codiceAmministrazione2);*/

        // UnitaOrganizzativa
        Element unitaOrganizzativa = doc.createElement("UnitaOrganizzativa");
        amministrazione.appendChild(unitaOrganizzativa);

        // Denominazione
        Element denominazione2 = doc.createElement("Denominazione");
        denominazione2.appendChild(doc.createTextNode(getDenominazione()));
        unitaOrganizzativa.appendChild(denominazione2);

        // IndirizzoPostale
        Element indirizzoPostale = doc.createElement("IndirizzoPostale");
        unitaOrganizzativa.appendChild(indirizzoPostale);

        // Toponimo
        Element toponimo = doc.createElement("Toponimo");
        toponimo.setAttribute("dug", getToponimo());
        toponimo.appendChild(doc.createTextNode(getIndirizzoPostale()));
        indirizzoPostale.appendChild(toponimo);

        // Civico
        Element civico = doc.createElement("Civico");
        civico.appendChild(doc.createTextNode(getCivico()));
        indirizzoPostale.appendChild(civico);

        // Cap
        Element cap = doc.createElement("CAP");
        cap.appendChild(doc.createTextNode(getCap()));
        indirizzoPostale.appendChild(cap);

        // Comune
        Element comune = doc.createElement("Comune");
        comune.appendChild(doc.createTextNode(getComune()));
        indirizzoPostale.appendChild(comune);

        // Provincia
        Element provincia = doc.createElement("Provincia");
        provincia.appendChild(doc.createTextNode(getProvincia()));
        indirizzoPostale.appendChild(provincia);

        // </IndirizzoPostale>

        // IndirizzoTelematico
        Element indirizzoTelematico2 = doc.createElement("IndirizzoTelematico");
        indirizzoTelematico2.setAttribute("tipo", "smtp");
        indirizzoTelematico2.appendChild(doc.createTextNode(getIndirizzoTelematico()));
        unitaOrganizzativa.appendChild(indirizzoTelematico2);

        // Telefono
        Element telefono = doc.createElement("Telefono");
        telefono.appendChild(doc.createTextNode(getTelefono()));
        unitaOrganizzativa.appendChild(telefono);

        // Fax
        Element fax = doc.createElement("Fax");
        fax.appendChild(doc.createTextNode(getFax()));
        unitaOrganizzativa.appendChild(fax);

        // </UnitaOrganizzativa>

        // </Amministrazione>

        // AOO
        Element AOO = doc.createElement("AOO");
        mittente.appendChild(AOO);

        // Denominazione
        Element denominazione3 = doc.createElement("Denominazione");
        denominazione3.appendChild(doc.createTextNode(getCodiceAOO()));
        AOO.appendChild(denominazione3);


        // </Mittente>

        // </Origine>

        for( Destinatario destinatarioObj: getDestinatari()){

            // Destinazione
            Element destinazione;
            if( destinatarioObj.getPerConoscenza() ){
                destinazione = doc.createElement("PerConoscenza");
            } else {
                destinazione = doc.createElement("Destinazione");
            }
            if( destinatarioObj.getConfermaRicezione() ){
                destinazione.setAttribute("confermaRicezione", "si");
            } else {
                destinazione.setAttribute("confermaRicezione", "no");
            }
            intestazione.appendChild(destinazione);

            // IndirizzoTelematico
            Element indirizzoTelematico3 = doc.createElement("IndirizzoTelematico");
            indirizzoTelematico3.setAttribute("tipo", "smtp");
            indirizzoTelematico3.appendChild(doc.createTextNode(destinatarioObj.getIndirizzoTelematico()));
            destinazione.appendChild(indirizzoTelematico3);

            if( destinatarioObj.getTipoDestinatario().equals(TipoDestinatario.AMMINISTRAZIONE) ){
                if( destinatarioObj.getDenominazione() != null ){
                    Element destinatario = doc.createElement("Destinatario");
                    Element amministrazioneDestinatario = doc.createElement("Amministrazione");
                    Element denominazioneDestinatario = doc.createElement("Denominazione");
                    denominazioneDestinatario.appendChild(doc.createTextNode(destinatarioObj.getDenominazione()));
                    amministrazioneDestinatario.appendChild(denominazioneDestinatario);

                    if( destinatarioObj.getDenominazioneUnitaOrganizzativa() != null ){
                        Element unitaOrganizzativaDestinatario = doc.createElement("UnitaOrganizzativa");
                        Element denominazioneUnitaOrganizzativaDestinatario = doc.createElement("Denominazione");
                        denominazioneUnitaOrganizzativaDestinatario.appendChild(doc.createTextNode(destinatarioObj.getDenominazioneUnitaOrganizzativa()));
                        Element indirizzoPostaleUnitaOrganizzativaDestinatario = doc.createElement("IndirizzoPostale");
                        Element denominazioneIndirizzoPostaleUnitaOrganizzativaDestinatario = doc.createElement("Denominazione");
                        //denominazioneIndirizzoPostaleUnitaOrganizzativaDestinatario.appendChild(doc.createTextNode("")); // TODO: completare, anche se formalmente corretto.
                        indirizzoPostaleUnitaOrganizzativaDestinatario.appendChild(denominazioneIndirizzoPostaleUnitaOrganizzativaDestinatario);

                        unitaOrganizzativaDestinatario.appendChild(denominazioneUnitaOrganizzativaDestinatario);
                        unitaOrganizzativaDestinatario.appendChild(indirizzoPostaleUnitaOrganizzativaDestinatario);
                        amministrazioneDestinatario.appendChild(unitaOrganizzativaDestinatario);
                    }

                    destinatario.appendChild(amministrazioneDestinatario);
                    destinazione.appendChild(destinatario);
                }
            }

            // </Destinazione>

        }

        // Oggetto

        Element oggetto = doc.createElement("Oggetto");
        oggetto.appendChild(doc.createTextNode(getOggetto()));
        intestazione.appendChild(oggetto);

        // </Oggetto>

        // </Intestazione>

        // <Riferimenti></Riferimenti>

        // <Descrizione>

        Element descrizione = doc.createElement("Descrizione");
        Element documento = doc.createElement("Documento");
        documento.setAttribute("nome", getDocumento().getNome());
        documento.setAttribute("tipoRiferimento", getDocumento().getTipoRiferimento());
        Element oggettoDocumento = doc.createElement("Oggetto");
        oggettoDocumento.appendChild(doc.createTextNode(getDocumento().getOggetto()));
        documento.appendChild(oggettoDocumento);
        descrizione.appendChild(documento);

        if( getAllegati().size()>0 ){
            Element allegati = doc.createElement("Allegati");
            for( Documento allegato: getAllegati() ){
                Element documentoAllegato = doc.createElement("Documento");
                documentoAllegato.setAttribute("nome", allegato.getNome());
                documentoAllegato.setAttribute("tipoRiferimento", allegato.getTipoRiferimento());
                Element oggettoDocumentoAllegato = doc.createElement("Oggetto");
                oggettoDocumentoAllegato.appendChild(doc.createTextNode(allegato.getOggetto()));
                documentoAllegato.appendChild(oggettoDocumentoAllegato);
                allegati.appendChild(documentoAllegato);
            }

            descrizione.appendChild(allegati);
        }

        segnatura.appendChild(descrizione);

        // </Descrizione>

        // </Segnatura>

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "no");
        transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "Segnatura.dtd");
        DOMSource source = new DOMSource(doc);

        StreamResult result = new StreamResult(new StringWriter());
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        String xmlString = result.getWriter().toString();
        return xmlString;
    }

    /* accessori e mutatori */

    public String getCodiceAmministrazione() {
        return codiceAmministrazione;
    }

    public void setCodiceAmministrazione(String codiceAmministrazione) {
        this.codiceAmministrazione = codiceAmministrazione;
    }

    public String getCodiceAOO() {
        return CodiceAOO;
    }

    public void setCodiceAOO(String codiceAOO) {
        CodiceAOO = codiceAOO;
    }

    public String getCodiceRegistro() {
        return codiceRegistro;
    }

    public void setCodiceRegistro(String codiceRegistro) {
        this.codiceRegistro = codiceRegistro;
    }

    public String getNumeroRegistrazione() {
        return numeroRegistrazione;
    }

    public void setNumeroRegistrazione(String numeroRegistrazione) {
        this.numeroRegistrazione = numeroRegistrazione;
    }

    public String getDataRegistrazione() {
        return dataRegistrazione;
    }

    public void setDataRegistrazione(String dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }

    public String getIndirizzoTelematico() {
        return indirizzoTelematico;
    }

    public void setIndirizzoTelematico(String indirizzoTelematico) {
        this.indirizzoTelematico = indirizzoTelematico;
    }

    public String getDenominazione() {
        return denominazione;
    }

    public void setDenominazione(String denominazione) {
        this.denominazione = denominazione;
    }

    public String getToponimo() {
        return toponimo;
    }

    public void setToponimo(String toponimo) {
        this.toponimo = toponimo;
    }

    public String getIndirizzoPostale() {
        return indirizzoPostale;
    }

    public void setIndirizzoPostale(String indirizzoPostale) {
        this.indirizzoPostale = indirizzoPostale;
    }

    public String getCivico() {
        return civico;
    }

    public void setCivico(String civico) {
        this.civico = civico;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getComune() {
        return comune;
    }

    public void setComune(String comune) {
        this.comune = comune;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public void addDestinatario(Destinatario destinatario){
        destinatari.add(destinatario);
    }

    public List<Destinatario> getDestinatari() {
        return destinatari;
    }

    public void setDestinatari(List<Destinatario> destinatari) {
        this.destinatari = destinatari;
    }

    public String getOggetto() {
        return oggetto;
    }

    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    public void addAllegato(Documento documento){
        allegati.add(documento);
    }

    public List<Documento> getAllegati() {
        return allegati;
    }

    public void setAllegati(List<Documento> allegati) {
        this.allegati = allegati;
    }
}
