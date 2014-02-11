package com.axiastudio.suite.interoperabilita.utilities;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

/**
 * User: tiziano
 * Date: 11/02/14
 * Time: 17:15
 */
public class StringUnmarshalling {

    String text;
    String context;
    JAXBContext jaxbContext;

    public static Object getObject(String context, String stringToConvert){
        Unmarshaller unmarshaller=null;
        Object objectJAXB=null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(context);
            unmarshaller = jaxbContext.createUnmarshaller();
            StringBuffer xmlString = new StringBuffer(stringToConvert);
            objectJAXB = unmarshaller.unmarshal(new StringReader(xmlString.toString()));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return objectJAXB;
    }

}
