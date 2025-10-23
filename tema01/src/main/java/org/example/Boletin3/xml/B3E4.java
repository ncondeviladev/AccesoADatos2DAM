package org.example.Boletin3.xml;

import org.example.Boletin3.xml.Loaders.PedidoLoader;
import org.example.Boletin3.xml.Modelos.Item;
import org.example.Boletin3.xml.Modelos.Pedido;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class B3E4 {


    /**
     * Método para mostrar los pedidos de una lista extraida de un XML
     *
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public static void mostrarPedidos() throws IOException, ParserConfigurationException, SAXException, NullPointerException {
        List<Pedido> listaPedidos = PedidoLoader.capturarPedidos();

        if (listaPedidos == null) {
            System.err.println("Lista de pedidos nula");
            return;
        }
        System.out.println("Pedidos:");
        for (int i = 0; i < listaPedidos.size(); i++) {
            System.out.print("Pedido " + (i + 1) + ": ");
            System.out.println(listaPedidos.get(i).getId());
        }

    }

    /**
     * Método que muestra los items de un pedido concreto
     *
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public static void mostrarItems() throws IOException, ParserConfigurationException, SAXException {
        List<Pedido> listaPedidos = PedidoLoader.capturarPedidos();
        if (listaPedidos == null) {
            System.err.println("Lista de pedidos nula");
            return;
        }
        try {
            mostrarPedidos();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Introduce el nº de pedido para mostrar sus Items");
        Scanner sc = new Scanner(System.in);
        Integer numPedido = sc.nextInt();
        sc.nextLine();
        
        int pedidoIndex = numPedido - 1;
        if (pedidoIndex < 0 || pedidoIndex >= listaPedidos.size()) {
            System.err.println("Número de pedido no válido.");
            return;
        }
        
        Pedido p = listaPedidos.get(pedidoIndex);
        List<Item> itemsPedido = p.getItems();
        for (int i = 0; i < itemsPedido.size(); i++) {
            System.out.println(itemsPedido.get(i).getSku());
        }
        calcPrecioFinal(p);
    }

    /**
     * Método que compara el precio total con el calculo de los items y su cantidad
     * @param pedido
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public static void calcPrecioFinal(Pedido pedido) throws IOException, ParserConfigurationException, SAXException {
        List<Item> itemsPedido = pedido.getItems();
        Double totalItems = 0.0;
        for (int i = 0; i < itemsPedido.size(); i++) {
            totalItems += itemsPedido.get(i).getPrecioUnitario() * itemsPedido.get(i).getCantidad();
        }
        System.out.println("Precio final: " + totalItems);
        if(Math.abs(totalItems - pedido.getTotal()) < 0.001){
            System.out.println("El precio del total es igual a la suma de los items");
        } else {
            System.out.println("Los precios no coinciden, alguien roba dinero de la caja...");
        }
    }
}
