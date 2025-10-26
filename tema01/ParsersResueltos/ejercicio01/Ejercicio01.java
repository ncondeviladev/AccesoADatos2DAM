package com.germangascon.tema01.ejercicio01;

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

/**
 * <p><strong>Ejercicio01</strong></p>
 * <p>Listado simple de empleados</p>
 * <p>Cargar el dataset empleados.xml con DOM y mostrar el id, nombre y salario de cada uno de los
 * empleados.</p>
 * License: 🅮 Public Domain<br />
 * Created on: 2025-10-08<br />
 *
 * @author Germán Gascón <ggascon@gmail.com>
 * @version 0.0.1
 * @since 0.0.1
 **/
public class Ejercicio01 {
    public Ejercicio01() throws IOException, ParserConfigurationException, SAXException {
        TextTable textTable = new TextTable("Id", "Nombre", "Salario");
        try (InputStream  is = getClass().getResourceAsStream("/xml/empleados.xml")) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom = builder.parse(is);
            dom.getDocumentElement().normalize();
            NodeList items = dom.getElementsByTagName("empleado");
            for (int i = 0; i < items.getLength();i++) {
                Element element = (Element) items.item(i);
                String id = element.getAttribute("id");
                String nombre = element.getElementsByTagName("nombre").item(0).getTextContent();
                String salario = element.getElementsByTagName("salario").item(0).getTextContent();
                textTable.addRow(id, nombre, salario);
            }
            System.out.println(textTable);
        }
    }
}
