package org.example.Boletin1.POJO;

import java.util.Objects;

public class ResultadosPuerto {

    private final int idPuerto;
    private final int idCiclista;
    private final int posicion;
    private final int puntos;

    public ResultadosPuerto(int idPuerto, int idCiclista, int posicion, int puntos) {

        this.idPuerto = idPuerto;
        this.idCiclista = idCiclista;
        this.posicion = posicion;
        this.puntos = puntos;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ResultadosPuerto that = (ResultadosPuerto) o;
        return idPuerto == that.idPuerto && idCiclista == that.idCiclista && posicion == that.posicion && puntos == that.puntos;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPuerto, idCiclista, posicion, puntos);
    }

    public int getIdPuerto() {
        return idPuerto;
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
}
