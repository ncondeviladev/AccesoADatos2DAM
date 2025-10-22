package org.example.Boletin3.xml;

import org.example.myUtils.ParseUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class B3E1 {

    /**
     * Ejercicio 1, Capturar datos de un xml de empleados para añadirlos en una lista y mostrar sus datos
     *
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */


    public static void ejecutar() throws IOException, ParserConfigurationException, SAXException {

        List<Empleado> listaEmpleados = EmpleadoLoader.capturarEmpleados();
        for(int i = 0; i < listaEmpleados.size(); i++) {
            System.out.println(listaEmpleados.get(i));
            System.out.println("- - - -");
        }
    }

}

