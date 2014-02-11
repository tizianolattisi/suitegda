package com.axiastudio.suite.interoperabilita.utilities;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * User: tiziano
 * Date: 11/02/14
 * Time: 17:14
 */
public class StampGenericXML {
    static public void staticStampGenericXML(Object objectJAXB, String
            context){
        try {
            JAXBContext jaxbLocalContext = JAXBContext.newInstance
                    (context);
            Marshaller marshaller =
                    jaxbLocalContext.createMarshaller();
            marshaller.setProperty(marshaller.JAXB_FORMATTED_OUTPUT,
                    true);
            marshaller.marshal(objectJAXB, System.out);
        } catch (JAXBException e1) {
            e1.printStackTrace();
        }
    }
}
