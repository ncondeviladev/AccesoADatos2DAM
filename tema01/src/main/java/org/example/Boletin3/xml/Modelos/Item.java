package org.example.Boletin3.xml.Modelos;

import java.util.Objects;

public class Item {

    private final String sku;
    private final String descripcion;
    private final int cantidad;
    private final Double precioUnitario;
    private final String moneda;

    public Item(String sku, String descripcion, int cantidad, Double precioUnitario, String moneda) {
        this.sku = sku;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.moneda = moneda;

    }

    public String getSku() {
        return sku;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }
    public String getMoneda() {
        return moneda;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return cantidad == item.cantidad && Objects.equals(sku, item.sku) && Objects.equals(descripcion, item.descripcion) && Objects.equals(precioUnitario, item.precioUnitario) && Objects.equals(moneda, item.moneda);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sku, descripcion, cantidad, precioUnitario, moneda);
    }
}
