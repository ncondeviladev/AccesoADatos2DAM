package com.germangascon.tema01.ejercicio04;

import java.time.LocalDate;
import java.util.List;

/**
 * Pedido
 * License: 🅮 Public Domain
 * Created on: 2025-10-08
 *
 * @author Germán Gascón <ggascon@gmail.com>
 * @version 0.0.1
 * @since 0.0.1
 **/
public class Pedido {
    private final String id;
    private final Cliente cliente;
    private final LocalDate fecha;
    private final List<Item> items;
    // total debería ser un campo calculado, es decir, un método que lo calcule.
    // Lo guardamos porque el ejercicio dice que hay que comprobar que el valor de total es correcto
    private final float total;
    private final String moneda;

    public Pedido(String id, Cliente cliente, LocalDate fecha, List<Item> items, float total, String moneda) {
        this.id = id;
        this.cliente = cliente;
        this.fecha = fecha;
        this.items = items;
        this.total = total;
        this.moneda = moneda;
    }

    public String getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public List<Item> getItems() {
        return items;
    }

    public float getTotal() {
        return total;
    }

    public float getTotalCalculado() {
        float totalPedido = 0;
        for (Item item : items) {
            totalPedido += item.getCantidad() * item.getPrecioUnitario();
        }
        return totalPedido;
    }

    public String getMoneda() {
        return moneda;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Pedido pedido)) return false;

        return id.equals(pedido.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id='" + id + '\'' +
                ", cliente=" + cliente +
                ", fecha=" + fecha +
                ", items=" + items +
                ", total=" + total +
                ", moneda='" + moneda + '\'' +
                '}';
    }
}
