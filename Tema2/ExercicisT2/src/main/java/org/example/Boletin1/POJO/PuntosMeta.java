package org.example.Boletin1.POJO;

import java.util.Objects;

public class PuntosMeta {

    private final int idEtapa;
    private final int idCiclista;
    private final int posicion;
    private final int puntos;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PuntosMeta that = (PuntosMeta) o;
        return idEtapa == that.idEtapa && idCiclista == that.idCiclista && posicion == that.posicion && puntos == that.puntos;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEtapa, idCiclista, posicion, puntos);
    }

    public PuntosMeta(int idEtapa, int idCiclista, int posicion, int puntos) {
        this.idEtapa = idEtapa;
        this.idCiclista = idCiclista;
        this.posicion = posicion;
        this.puntos = puntos;
    }

    public int getIdEtapa() {
        return idEtapa;
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
