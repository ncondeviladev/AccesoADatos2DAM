package com.germangascon.tema01.ejercicio03;

import com.germangascon.tema01.lib.TextTable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * <p><strong>Ejercicio03</strong></p>
 * <p>Biblioteca. Consultas y filtrado</p>
 * <p>A partir del dataset biblioteca.xml mostrar:<br />
 * a) El título de todos los libros<br />
 * b) Contar cuantos libros hay de cada género</p>
 * License: 🅮 Public Domain<br />
 * Created on: 2025-10-08<br />
 *
 * @author Germán Gascón <ggascon@gmail.com>
 * @version 0.0.1
 * @since 0.0.1
 **/
public class Ejercicio03 {
    public Ejercicio03() throws IOException, ParserConfigurationException, SAXException {
        TextTable textTable = new TextTable("Genero", "Cantidad");
        textTable.setAlign("Cantidad", TextTable.Align.RIGHT);
        Map<String, Integer> mapGeneros = new HashMap<>();
        try (InputStream is = getClass().getResourceAsStream("/xml/biblioteca.xml")) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom = builder.parse(is);
            dom.getDocumentElement().normalize();
            NodeList items = dom.getElementsByTagName("libro");
            for (int i = 0; i < items.getLength();i++) {
                Element element = (Element) items.item(i);
                String titulo = element.getElementsByTagName("titulo").item(0).getTextContent();
                System.out.println(titulo);
                NodeList nodeListGenero = element.getElementsByTagName("genero");
                for (int j = 0; j < nodeListGenero.getLength(); j++) {
                    String genero = nodeListGenero.item(j).getTextContent();
                    Integer cantidad = mapGeneros.get(genero);
                    if (cantidad == null) {
                        cantidad = 1;
                    } else {
                        cantidad++;
                    }
                    mapGeneros.put(genero, cantidad);
                }
            }
        }
        for (Map.Entry<String, Integer> entryGenero : mapGeneros.entrySet()) {
            String genero = entryGenero.getKey();
            int cantidadLibros = entryGenero.getValue();
            textTable.addRow(genero, String.valueOf(cantidadLibros));
        }
        System.out.println(textTable);
    }
}
