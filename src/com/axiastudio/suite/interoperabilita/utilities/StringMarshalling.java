package com.axiastudio.suite.interoperabilita.utilities;

import com.axiastudio.suite.interoperabilita.entities.xsd.Segnatura;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

/**
 * User: tiziano Date: 11/02/14 Time: 17:15
 */
public class StringMarshalling {

	static public String getXMLString(String context, Object objectJAXB, String docType) throws Exception {
		return getXMLStringDTD(context, objectJAXB, docType, false);
	}

	static public String getXMLStringXSD(String context, Object objectJAXB, String docType, Boolean pretty) throws Exception {

		StringWriter xml = new StringWriter();
		String xmlString = new String();

		// try {
		JAXBContext jaxbLocalContext = JAXBContext.newInstance(Segnatura.class);
		Marshaller marshaller = jaxbLocalContext.createMarshaller();
		if (pretty) {
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		}
//		marshaller.setProperty("jaxb.encoding", "ISO-8859-1");
//		marshaller.setProperty("com.sun.xml.bind.xmlHeaders", "<!DOCTYPE " + docType + " SYSTEM \"Segnatura.dtd\">");
		marshaller.marshal(objectJAXB, xml);
		xmlString = xml.toString();
		// } catch (JAXBException e1) {
		// e1.printStackTrace();
		// }
		return xmlString;
	}

	static public String getXMLStringDTD(String context, Object objectJAXB, String docType, Boolean pretty) throws Exception {

		StringWriter xml = new StringWriter();
		String xmlString = new String();

		// try {
		JAXBContext jaxbLocalContext = JAXBContext.newInstance(context);
		Marshaller marshaller = jaxbLocalContext.createMarshaller();
		if (pretty) {
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		}
		marshaller.setProperty("jaxb.encoding", "ISO-8859-1");
		marshaller.setProperty("com.sun.xml.bind.xmlHeaders", "<!DOCTYPE " + docType + " SYSTEM \"Segnatura.dtd\">");
		marshaller.marshal(objectJAXB, xml);
		xmlString = xml.toString();
		// } catch (JAXBException e1) {
		// e1.printStackTrace();
		// }
		return xmlString;
	}

}
