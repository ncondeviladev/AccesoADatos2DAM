package org.example.Boletin1.POJO;

import java.util.Objects;

public class ResultadosSprint {

    private final int idSprint;
    private final int idCiclista;
    private final int posicion;
    private final int puntos;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ResultadosSprint that = (ResultadosSprint) o;
        return idSprint == that.idSprint && idCiclista == that.idCiclista && posicion == that.posicion && puntos == that.puntos;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSprint, idCiclista, posicion, puntos);
    }

    public int getIdSprint() {
        return idSprint;
    }

    public int getIdCiclista() {
        return idCiclista;
    }

    public int getPosicion() {
        return posicion;
    }

    public int getPuntos() {
        return puntos;
    }

    public ResultadosSprint(int idSprint, int idCiclista, int posicion, int puntos) {
        this.idSprint = idSprint;
        this.idCiclista = idCiclista;
        this.posicion = posicion;
        this.puntos = puntos;

    }
}
