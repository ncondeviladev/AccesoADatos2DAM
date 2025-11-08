package org.example.Boletin1.POJO;

import java.util.Objects;

public class Ciclista {

    private final int idCiclista;
    private final int idEquipo;
    private final String nombre;
    private final String pais;
    private final String fechaNac;


    public Ciclista(int idCiclista, int idEquipo, String nombre, String pais, String fechaNac) {

        this.idCiclista = idCiclista;
        this.idEquipo = idEquipo;
        this.nombre = nombre;
        this.pais = pais;
        this.fechaNac = fechaNac;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ciclista ciclista = (Ciclista) o;
        return idCiclista == ciclista.idCiclista && idEquipo == ciclista.idEquipo && Objects.equals(nombre, ciclista.nombre) && Objects.equals(pais, ciclista.pais) && Objects.equals(fechaNac, ciclista.fechaNac);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCiclista, idEquipo, nombre, pais, fechaNac);
    }

    public int getIdCiclista() {
        return idCiclista;
    }

    public int getIdEquipo() {
        return idEquipo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPais() {
        return pais;
    }

    public String getFechaNac() {
        return fechaNac;
    }
}
