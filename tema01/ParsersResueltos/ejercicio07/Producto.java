package com.germangascon.tema01.ejercicio07;

import java.util.List;

/**
 * Producto
 * License: 🅮 Public Domain
 * Created on: 2025-10-17
 *
 * @author Germán Gascón <ggascon@gmail.com>
 * @version 0.0.1
 * @since 0.0.1
 **/
public class Producto {
    private final String id;
    private final String nombre;
    private final int stock;
    private final float precio;
    private final List<String> tags;
    private final Ubicacion ubicacion;

    public Producto(String id, String nombre, int stock, float precio, List<String> tags, Ubicacion ubicacion) {
        this.id = id;
        this.nombre = nombre;
        this.stock = stock;
        this.precio = precio;
        this.tags = tags;
        this.ubicacion = ubicacion;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getStock() {
        return stock;
    }

    public float getPrecio() {
        return precio;
    }

    public List<String> getTags() {
        return tags;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Producto producto)) return false;

        return id.equals(producto.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", stock=" + stock +
                ", precio=" + precio +
                ", tags=" + tags +
                ", ubicacion=" + ubicacion +
                '}';
    }
}
