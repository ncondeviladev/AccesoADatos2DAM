package com.germangascon.tema01.ejercicio04;

/**
 * Item
 * License: 🅮 Public Domain
 * Created on: 2025-10-08
 *
 * @author Germán Gascón <ggascon@gmail.com>
 * @version 0.0.1
 * @since 0.0.1
 **/
public class Item {
    private final String sku;
    private final String descripcion;
    private final int cantidad;
    private final float precioUnitario;

    public Item(String sku, String descripcion, int cantidad, float precioUnitario) {
        this.sku = sku;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
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

    public float getPrecioUnitario() {
        return precioUnitario;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Item item)) return false;

        return sku.equals(item.sku);
    }

    @Override
    public int hashCode() {
        return sku.hashCode();
    }

    @Override
    public String toString() {
        return "Item{" +
                "sku='" + sku + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                '}';
    }
}
