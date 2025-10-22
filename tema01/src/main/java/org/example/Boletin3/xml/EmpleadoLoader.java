package org.example.Boletin3.xml;

import org.example.myUtils.ParseUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoLoader {

    public static List<Empleado> capturarEmpleados() {
        List<Empleado> empleados = new ArrayList<>();

        try (InputStream inputStream = EmpleadoLoader.class.getResourceAsStream("/Boletin3/empleados.xml")) {

            if (inputStream == null) {
                System.err.println("No se pudo encontrar el archivo empleados.xml. Asegúrate de que está en la carpeta de recursos correcta.");
                return empleados; // Devuelve la lista vacía
            }

            Document dom = ParseUtils.parseXML(inputStream);
            NodeList items = dom.getElementsByTagName("empleado");

            for (int i = 0; i < items.getLength(); i++) {
                Element empleadoElement = (Element) items.item(i);
                String idEmpleado = empleadoElement.getAttribute("id");
                String nombreEmpleado = empleadoElement.getElementsByTagName("nombre").item(0).getTextContent();
                String departamentoEmpleado = empleadoElement.getElementsByTagName("departamento").item(0).getTextContent();
                String salarioString = empleadoElement.getElementsByTagName("salario").item(0).getTextContent();
                Double salarioEmpleado = Double.parseDouble(salarioString);
                String fechaAltaEmpleado = empleadoElement.getElementsByTagName("fechaAlta").item(0).getTextContent();

                empleados.add(new Empleado(idEmpleado, nombreEmpleado, departamentoEmpleado, salarioEmpleado, fechaAltaEmpleado));
            }
        } catch (Exception e) {
            System.err.println("Ocurrió un error al parsear el XML:");
            e.printStackTrace();
        }
        return empleados; // Devuelve la lista (llena o vacía si hubo error)
    }
}
