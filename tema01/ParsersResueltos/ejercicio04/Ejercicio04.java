package com.germangascon.tema01.ejercicio04;

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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * <p><strong>Ejercicio04</strong></p>
 * <p>Pedidos. Consulta, validación y totales</p>
 * <p>Utilizando el dataset pedidos.xml:<br />
 * a) Mostrar por pantalla los items del pedido indicado por el usuario.<br />
 * b) Recalcular el total del pedido como Σ(cantidad × precioUnitario) y comprobar si coincide con el
 * valor que figura en el elemento <total></p>
 * License: 🅮 Public Domain<br />
 * Created on: 2025-10-08<br />
 *
 * @author Germán Gascón <ggascon@gmail.com>
 * @version 0.0.1
 * @since 0.0.1
 **/
public class Ejercicio04 {
    public Ejercicio04() throws IOException, ParserConfigurationException, SAXException {
        Map<String, Pedido> pedidoMap = new HashMap<>();
        try (InputStream is = getClass().getResourceAsStream("/xml/pedidos.xml")) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom = builder.parse(is);
            dom.getDocumentElement().normalize();
            NodeList nodeListPedido = dom.getElementsByTagName("pedido");
            for (int i = 0; i < nodeListPedido.getLength();i++) {
                Element pedidoElement = (Element) nodeListPedido.item(i);
                String idPedido = pedidoElement.getAttribute("id");
                Element clienteElement = (Element) pedidoElement.getElementsByTagName("cliente").item(0);
                String nombreCliente = clienteElement.getElementsByTagName("nombre").item(0).getTextContent();
                String emailCliente = clienteElement.getElementsByTagName("email").item(0).getTextContent();
                String fechaPedidoStr = pedidoElement.getElementsByTagName("fecha").item(0).getTextContent();
                LocalDate fechaPedido;
                try {
                    fechaPedido = LocalDate.parse(fechaPedidoStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                } catch (DateTimeParseException dtpe) {
                    throw new NumberFormatException("ERROR: Formato del documento erróneo. Se esperaba la fecha de pedido como una fecha en formato yyyy-MM-dd");
                }
                List<Item> items = new ArrayList<>();
                NodeList nodeListItemsPedido = pedidoElement.getElementsByTagName("item");
                for (int j = 0; j < nodeListItemsPedido.getLength(); j++) {
                    Element itemElement = (Element) nodeListItemsPedido.item(j);
                    String sku = itemElement.getAttribute("sku");
                    String descripcion = itemElement.getElementsByTagName("descripcion").item(0).getTextContent();
                    int cantidad;
                    try {
                        cantidad = Integer.parseInt(itemElement.getElementsByTagName("cantidad").item(0).getTextContent());
                    } catch (NumberFormatException nfe) {
                        throw new NumberFormatException("ERROR: Formato del documento erróneo. Se esperaba la cantidad de item como un valor numérico entero");
                    }
                    float precioUnitario;
                    try {
                        precioUnitario = Float.parseFloat(itemElement.getElementsByTagName("precioUnitario").item(0).getTextContent());
                    } catch (NumberFormatException nfe) {
                        throw new NumberFormatException("ERROR: Formato del documento erróneo. Se esperaba el precioUnitario de item como un valor numérico decimal");
                    }
                    items.add(new Item(sku, descripcion, cantidad, precioUnitario));
                }
                Element totalElement = (Element) pedidoElement.getElementsByTagName("total").item(0);
                float totalPedido;
                try {
                    totalPedido = Float.parseFloat(totalElement.getTextContent());
                } catch (NumberFormatException nfe) {
                    throw new NumberFormatException("ERROR: Formato del documento erróneo. Se esperaba el total de pedido como un valor numérico decimal");
                }
                String moneda = totalElement.getAttribute("moneda");
                pedidoMap.put(idPedido, new Pedido(idPedido, new Cliente(nombreCliente, emailCliente), fechaPedido, items, totalPedido, moneda));
            }
        }
        TextTable textTable = new TextTable("Sku", "Cantidad", "Descripción", "Precio");
        textTable.setAlign("Cantidad", TextTable.Align.RIGHT);
        textTable.setAlign("Precio", TextTable.Align.RIGHT);
        System.out.println("Indique el id del pedido: ");
        try (Scanner scanner = new Scanner(System.in)) {
            String idPedido = scanner.nextLine();
            Pedido pedido = pedidoMap.get(idPedido);
            if (pedido == null) {
                System.out.println("No se ha encontrado ningún pedido con ID = " + idPedido);
                System.exit(-1);
            }
            for (Item item : pedido.getItems()) {
                textTable.addRow(item.getSku(), String.valueOf(item.getCantidad()), item.getDescripcion(), String.valueOf(item.getPrecioUnitario()));
            }
            System.out.println(textTable);
            System.out.println();
            System.out.println("Total pedido: " + pedido.getTotal());
            System.out.println("Total calculado: " + pedido.getTotalCalculado());
            if (Math.abs(pedido.getTotal() - pedido.getTotalCalculado()) < 0.005) {
                System.out.println("El total del pedido y el calculado se pueden considerar iguales con el segudo decimal redondeado");
            } else {
                System.out.println("Los totales no coinciden");
            }
        }
    }
}
