package com.axiastudio.suite.interoperabilita.utilities;

import com.sun.corba.se.spi.activation._ServerManagerStub;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

/**
 * User: tiziano
 * Date: 11/02/14
 * Time: 17:15
 */
public class StringMarshalling {

    static public String getXMLString(String context, Object objectJAXB){
        return getXMLString(context, objectJAXB, false);
    }
    static public String getXMLString(String context, Object objectJAXB, Boolean pretty){

        StringWriter xml = new StringWriter();
        String xmlString = new String();

        try {
            JAXBContext jaxbLocalContext = JAXBContext.newInstance(context);
            Marshaller marshaller = jaxbLocalContext.createMarshaller();
            if( pretty ){
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            }
            marshaller.setProperty("jaxb.encoding", "ISO-8859-1");
            marshaller.setProperty("com.sun.xml.bind.xmlHeaders", "<!DOCTYPE Segnatura SYSTEM \"Segnatura.dtd\">");
            marshaller.marshal(objectJAXB, xml);
            xmlString = xml.toString();
        } catch (JAXBException e1) {
            e1.printStackTrace();
        }
        return xmlString;
    }

}
