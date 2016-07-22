package com.axiastudio.suite.interoperabilita.utilities;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class SegnaturaDtdEntityResolver implements EntityResolver {
	
	public InputSource resolveEntity(String publicID, String systemID) throws SAXException, FileNotFoundException {
		ClassLoader classLoader = this.getClass().getClassLoader();
		File f = new File(classLoader.getResource("Segnatura.dtd").getFile());
		return new InputSource(new FileReader(f));
	}
	
}