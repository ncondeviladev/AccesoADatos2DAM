package com.germangascon.tema01.ejercicio02;

import com.germangascon.tema01.lib.TextTable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p><strong>Ejercicio02</strong></p>
 * <p>Objetos y estadísticas por departamento</p>
 * <p>Mapear cada elemento <empleado> del dataset empleados.xml, a una clase Empleado y calcular el
 * salario medio por departamento.<br />
 * Salida: mostrar en 3 columnas<br />
 * Departamento | Nº empleados | Salario medio. <br /></p>
 * License: 🅮 Public Domain<br />
 * Created on: 2025-10-08<br />
 *
 * @author Germán Gascón <ggascon@gmail.com>
 * @version 0.0.1
 * @since 0.0.1
 **/
public class Ejercicio02 {
    public Ejercicio02() throws IOException, ParserConfigurationException, SAXException {
        TextTable textTable = new TextTable("Departamento", "N° Empleados", "Salario medio");
        textTable.setAlign("N° Empleados", TextTable.Align.RIGHT);
        textTable.setAlign("Salario medio", TextTable.Align.RIGHT);
        Map<String, List<Empleado>> empleados = new HashMap<>();
        try (InputStream is = getClass().getResourceAsStream("/xml/empleados.xml")) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom = builder.parse(is);
            dom.getDocumentElement().normalize();
            DateTimeFormatter dte = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            NodeList items = dom.getElementsByTagName("empleado");
            for (int i = 0; i < items.getLength();i++) {
                Element element = (Element) items.item(i);
                String id = element.getAttribute("id");
                String nombre = element.getElementsByTagName("nombre").item(0).getTextContent();
                String departamento = element.getElementsByTagName("departamento").item(0).getTextContent();
                Node itemSalario = element.getElementsByTagName("salario").item(0);
                double salario = Double.parseDouble(itemSalario.getTextContent());
                String moneda = itemSalario.getAttributes().getNamedItem("moneda").getTextContent();
                String fechaAltaStr = element.getElementsByTagName("fechaAlta").item(0).getTextContent();
                LocalDate fechaAlta = LocalDate.parse(fechaAltaStr, dte);
                Empleado empleado = new Empleado(id, nombre, departamento, salario, moneda, fechaAlta);
                List<Empleado> listaEmpleados = empleados.get(departamento);
                if (listaEmpleados == null) {
                    listaEmpleados = new ArrayList<>();
                    empleados.put(departamento, listaEmpleados);
                }
                listaEmpleados.add(empleado);
            }
        }
        for (Map.Entry<String, List<Empleado>> entryEmpleado : empleados.entrySet()) {
            String departamento = entryEmpleado.getKey();
            int cantidadEmpleados = entryEmpleado.getValue().size();
            double sumaSalarios = 0;
            for (Empleado empleado : entryEmpleado.getValue()) {
                sumaSalarios += empleado.getSalario();
            }
            textTable.addRow(departamento, String.valueOf(cantidadEmpleados), String.valueOf(sumaSalarios));
        }
        System.out.println(textTable);
    }
}
