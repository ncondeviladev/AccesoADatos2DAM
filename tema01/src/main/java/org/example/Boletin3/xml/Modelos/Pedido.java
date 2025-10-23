package org.example.Boletin3.xml.Modelos;

import java.util.List;

public class Pedido {

    private final String id;
    private final Cliente cliente;
    private final String fecha;
    private final List<Item> items;
    private final Double total;
    private final String moneda;

    public Pedido(String id, Cliente cliente, String fecha, List<Item> items, Double total, String modeda) {
        this.id = id;
        this.cliente = cliente;
        this.fecha = fecha;
        this.items = items;
        this.total = total;
        this.moneda = modeda;
    }

    public String getId() {
        return id;
    }
    public Cliente getCliente() {
        return cliente;
    }
    public String getFecha() {
        return fecha;
    }
    public List<Item> getItems() {
        return items;
    }
    public Double getTotal() {
        return total;
    }
    public String getMoneda() {
        return moneda;
    }
}
