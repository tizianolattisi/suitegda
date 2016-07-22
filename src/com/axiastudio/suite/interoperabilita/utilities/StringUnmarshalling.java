package com.axiastudio.suite.interoperabilita.utilities;

import com.axiastudio.suite.interoperabilita.entities.xsd.Segnatura;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.sax.SAXSource;
import java.io.StringReader;

/**
 * User: tiziano Date: 11/02/14 Time: 17:15
 */
public class StringUnmarshalling {

	String text;
	String context;
	JAXBContext jaxbContext;

	public static Object getObject(String context, String stringToConvert) throws Exception {
		Unmarshaller unmarshaller = null;
		Object objectJAXB = null;

		// ClassLoader classLoader = Segnatura.class.getClassLoader();

		// SchemaFactory schemaFactory =
		// SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		// File f = (dtd) ? new
		// File(classLoader.getResource("Segnatura.dtd").getFile() ) : new
		// File(classLoader.getResource("Segnatura.xsd").getFile());
		// Schema schema = schemaFactory.newSchema(f);

		// try {
		JAXBContext jaxbContext = JAXBContext.newInstance(context);
		unmarshaller = jaxbContext.createUnmarshaller();
		// unmarshaller.setSchema(schema);

		boolean xsd = stringToConvert.indexOf("http://www.digitPa.gov.it/protocollo/") > -1;

		StringBuffer xmlString = new StringBuffer(stringToConvert);

		if (xsd)
			objectJAXB = ((JAXBElement) unmarshaller.unmarshal(new StringReader(xmlString.toString()))).getValue();
		else {
			unmarshaller.setSchema(null);
			objectJAXB = unmarshaller.unmarshal(new StringReader(xmlString.toString()));
		}

		// } catch (JAXBException e) {
		// e.printStackTrace();
		// throw new PecException(e);
		// }
		return objectJAXB;
	}

	public static Object getObjectXSD(String stringToConvert) throws Exception {
		Unmarshaller unmarshaller = null;
		Object objectJAXB = null;

		// ClassLoader classLoader = Segnatura.class.getClassLoader();

		// SchemaFactory schemaFactory =
		// SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		// File f = (dtd) ? new
		// File(classLoader.getResource("Segnatura.dtd").getFile() ) : new
		// File(classLoader.getResource("Segnatura.xsd").getFile());
		// Schema schema = schemaFactory.newSchema(f);

		// try {
		JAXBContext jaxbContext = JAXBContext.newInstance(com.axiastudio.suite.interoperabilita.entities.xsd.Segnatura.class);
		unmarshaller = jaxbContext.createUnmarshaller();
		// unmarshaller.setSchema(schema);

		StringBuffer xmlString = new StringBuffer(stringToConvert);
		objectJAXB = ((JAXBElement) unmarshaller.unmarshal(new StringReader(xmlString.toString()))).getValue();
		// objectJAXB = unmarshaller.unmarshal(new
		// StringReader(xmlString.toString()));
		// } catch (JAXBException e) {
		// e.printStackTrace();
		// throw new PecException(e);
		// }
		return objectJAXB;
	}

	public static Object getObjectDTD(String stringToConvert) throws Exception {
		Unmarshaller unmarshaller = null;
		Object objectJAXB = null;

		ClassLoader classLoader = Segnatura.class.getClassLoader();

//		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//		File f = new File(classLoader.getResource("Segnatura.dtd").getFile());
//		Schema schema = schemaFactory.newSchema(f);

		XMLReader xmlReader = XMLReaderFactory.createXMLReader();
		xmlReader.setEntityResolver(new SegnaturaDtdEntityResolver());
		
		// xmlReader.setContentHandler(new YourContentHandler());
		// xmlReader.parse(stringToConvert);
		
//		 SAXParserFactory spf = SAXParserFactory.newInstance();
//	        spf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
//	        XMLReader xmlReader = spf.newSAXParser().getXMLReader();
//	        InputSource inputSource = new InputSource(new FileReader("input.xml"));
//	        SAXSource source = new SAXSource(xmlReader, stringToConvert);
	        
		// try {
		JAXBContext jaxbContext = JAXBContext.newInstance(com.axiastudio.suite.interoperabilita.entities.dtd.Segnatura.class);
		unmarshaller = jaxbContext.createUnmarshaller();
//		unmarshaller.setSchema(schema);

		StringBuffer xmlString = new StringBuffer(stringToConvert);
		// objectJAXB = ((JAXBElement) unmarshaller.unmarshal(new
		// StringReader(xmlString.toString()))).getValue();
		
        InputSource inputSource = new InputSource(new StringReader(xmlString.toString()));
        SAXSource source = new SAXSource(xmlReader, inputSource);		
		// objectJAXB = unmarshaller.unmarshal(new StringReader(xmlString.toString()));
		objectJAXB = unmarshaller.unmarshal(source);
		
		// } catch (JAXBException e) {
		// e.printStackTrace();
		// throw new PecException(e);
		// }
		return objectJAXB;
	}

}
