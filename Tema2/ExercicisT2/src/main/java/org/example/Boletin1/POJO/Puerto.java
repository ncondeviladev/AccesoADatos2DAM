package org.example.Boletin1.POJO;

import java.util.Objects;

public class Puerto {

    private final int idPuerto;
    private final int idEtapa;
    private final String nombre;
    private final double km;
    private final String categoria;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Puerto puerto = (Puerto) o;
        return idPuerto == puerto.idPuerto && idEtapa == puerto.idEtapa && Double.compare(km, puerto.km) == 0 && Objects.equals(nombre, puerto.nombre) && Objects.equals(categoria, puerto.categoria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPuerto, idEtapa, nombre, km, categoria);
    }

    public int getIdPuerto() {
        return idPuerto;
    }

    public int getIdEtapa() {
        return idEtapa;
    }

    public String getNombre() {
        return nombre;
    }

    public double getKm() {
        return km;
    }

    public String getCategoria() {
        return categoria;
    }

    public Puerto(int idPuerto, int idEtapa, String nombre, double km, String categoria) {

        this.idPuerto = idPuerto;
        this.idEtapa = idEtapa;
        this.nombre = nombre;
        this.km = km;
        this.categoria = categoria;
    }
}
