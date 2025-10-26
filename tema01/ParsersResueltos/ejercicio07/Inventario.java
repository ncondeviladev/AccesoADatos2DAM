package com.germangascon.tema01.ejercicio07;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Inventario
 * License: 🅮 Public Domain
 * Created on: 2025-10-17
 *
 * @author Germán Gascón <ggascon@gmail.com>
 * @version 0.0.1
 * @since 0.0.1
 **/
public class Inventario {
    private final String almacen;
    private final LocalDateTime actualizado;
    private final Map<String, Producto> productos;

    public Inventario(String almacen, LocalDateTime actualizado, Map<String, Producto> productos) {
        this.almacen = almacen;
        this.actualizado = actualizado;
        this.productos = productos;
    }

    public String getAlmacen() {
        return almacen;
    }

    public LocalDateTime getActualizado() {
        return actualizado;
    }

    public Ubicacion getUbicacion(String idProducto) {
        Producto producto = productos.get(idProducto);
        if (producto == null) {
            return null;
        }
        return producto.getUbicacion();
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Inventario that)) return false;

        return almacen.equals(that.almacen);
    }

    @Override
    public int hashCode() {
        return almacen.hashCode();
    }

    @Override
    public String toString() {
        return "Inventario{" +
                "almacen='" + almacen + '\'' +
                ", actualizado=" + actualizado +
                ", productos=" + productos +
                '}';
    }
}
