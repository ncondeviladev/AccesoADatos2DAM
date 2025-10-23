package org.example.Boletin3.xml.Modelos;

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
}
