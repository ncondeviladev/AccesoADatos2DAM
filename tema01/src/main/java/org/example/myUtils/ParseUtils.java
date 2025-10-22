package org.example.myUtils;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

public class ParseUtils {

    /**
     * Recibe un IS de xml y lo devuelve como Document
     * @param is Input Stream de un archivo XML
     * @return
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static Document parseXML(InputStream is) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance(); //Constructor de parsers
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder(); //Crea objeto parseador
        Document doc = docBuilder.parse(is); //Parsea xml a Doc
        doc.getDocumentElement().normalize(); //Limpia y elimina espacios en blanco
        return doc;
    }
}
