package org.example.Boletin1.POJO;

import java.util.Objects;

public class Equipo {

    private final int idEquipo;
    private final String nombre;
    private final String pais;

    public Equipo(int idEquipo, String nombre, String pais) {
        this.idEquipo = idEquipo;
        this.nombre = nombre;
        this.pais = pais;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Equipo equipos = (Equipo) o;
        return idEquipo == equipos.idEquipo && Objects.equals(nombre, equipos.nombre) && Objects.equals(pais, equipos.pais);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEquipo, nombre, pais);
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
}
