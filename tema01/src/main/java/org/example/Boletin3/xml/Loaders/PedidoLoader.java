package org.example.Boletin3.xml.Loaders;

import org.example.Boletin3.xml.Modelos.Cliente;
import org.example.Boletin3.xml.Modelos.Item;
import org.example.Boletin3.xml.Modelos.Pedido;
import org.example.myUtils.ParseUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que carga una lista de pedidos extraida de un archivo XML.
 */
public class PedidoLoader {

    /**
     * @return Método que devuelve una lista de pedidos extraida de un XML usando otro metodo de clase para parsearlo
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws NullPointerException
     */
    public static List<Pedido> capturarPedidos() throws IOException, ParserConfigurationException, SAXException, NullPointerException {

        List<Pedido> listaPedidos = new ArrayList<>();

        try (InputStream inputStream = PedidoLoader.class.getResourceAsStream("/Boletin3/pedidos.xml")) {

            if (inputStream == null) {
                System.err.println("No se pudo encontrar el archivo pedidos.xml. Asegúrate de que está en la carpeta de recursos correcta.");
                return null; // Devuelve la lista vacía
            }

            Document dom = ParseUtils.parseXML(inputStream);
            NodeList nodeItems = dom.getElementsByTagName("pedido");

            for (int i = 0; i < nodeItems.getLength(); i++) {
                Element pedidosElement = (Element) nodeItems.item(i);

                String id = pedidosElement.getAttribute("id");

                //Capturamos el cliente con sus etiquetas interiores
                Element clienteElement = (Element) pedidosElement.getElementsByTagName("cliente").item(0);
                String nombreCliente = clienteElement.getElementsByTagName("nombre").item(0).getTextContent();
                String emailCliente = clienteElement.getElementsByTagName("email").item(0).getTextContent();
                Cliente cliente = new Cliente(nombreCliente, emailCliente);

                String fecha = pedidosElement.getElementsByTagName("fecha").item(0).getTextContent();

                //Capturamos cada item iterando sobre etiqueta itemS
                Element itemElement = (Element) pedidosElement.getElementsByTagName("items").item(0);
                NodeList listaItems = itemElement.getElementsByTagName("item");
                List<Item> itemsList = new ArrayList<>();
                for(int j = 0; j < listaItems.getLength(); j++) {
                    Element item = (Element) listaItems.item(j);
                    String sku = item.getAttribute("sku");
                    String descripcion = item.getElementsByTagName("descripcion").item(0).getTextContent();
                    int cantidad = Integer.parseInt(item.getElementsByTagName("cantidad").item(0).getTextContent());

                    Element precioElement = (Element) item.getElementsByTagName("precioUnitario").item(0);
                    Double precioUnitario = Double.parseDouble(precioElement.getTextContent());
                    String moneda = precioElement.getAttribute("moneda");

                    Item itemPedido = new Item(sku, descripcion, cantidad, precioUnitario, moneda);
                    itemsList.add(itemPedido);
                }

                double total = Double.parseDouble(pedidosElement.getElementsByTagName("total").item(0).getTextContent());
                String moneda = pedidosElement.getAttribute("moneda");

                //Creamos pedido y aladimos a la lista
                Pedido pedido = new Pedido(id, cliente, fecha, itemsList, total, moneda);
                listaPedidos.add(pedido);
            }
            return listaPedidos;
        } catch (Exception e) {
            System.err.println("Ocurrió un error al parsear el XML:");
            e.printStackTrace();
            return null; // Devuelve la lista (llena o vacía si hubo error)
        }

    }
}
