package org.example.Boletin3.json.Modelos;

import java.util.Arrays;
import java.util.Objects;

public class Producto {

    private String id;
    private String nombre;
    private int stock;
    private double precio;
    private String[] tags;
    private Ubicacion ubicacion;

    public Producto(String id, String nombre, int stock, double precio, String[] tags, Ubicacion ubicacion){
        this.id = id;
        this.nombre = nombre;
        this.stock = stock;
        this.precio = precio;
        this.tags = tags;
        this.ubicacion = ubicacion;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return id == producto.id && stock == producto.stock && Double.compare(precio, producto.precio) == 0 && Objects.equals(nombre, producto.nombre) && Objects.deepEquals(tags, producto.tags) && Objects.equals(ubicacion, producto.ubicacion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, stock, precio, Arrays.hashCode(tags), ubicacion);
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

    public double getPrecio() {
        return precio;
    }

    public String[] getTags() {
        return tags;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void mostrarUbicacion(){
        System.out.println("ID: " + id +
                "\nNombre - " + nombre +
                "\nUbicación - " + ubicacion.getPasillo() + " - " + ubicacion.getEstante());
    }
}
