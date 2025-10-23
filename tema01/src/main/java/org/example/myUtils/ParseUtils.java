package org.example.myUtils;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

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

    /**
     * Parsea un stream JSON a un array de objetos usando Gson.
     * @param is
     * @param classOfT
     * @return
     */
    public static <T> T parseJson(InputStream is, Class<T> classOfT) {
        try (Reader reader = new InputStreamReader(is)) {
            Gson gson = new Gson();
            return gson.fromJson(reader, classOfT);
        } catch (JsonSyntaxException | JsonIOException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
