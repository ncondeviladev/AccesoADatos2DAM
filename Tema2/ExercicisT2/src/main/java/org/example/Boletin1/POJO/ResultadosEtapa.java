package org.example.Boletin1.POJO;

import java.util.Objects;

public class ResultadosEtapa {

    private final int idEtapa;
    private final int idCiclista;
    private final int posicion;
    private final double tiempo;
    private final String estado;

    public ResultadosEtapa(int idEtapa, int idCiclista, int posicion, double tiempo, String estado) {

        this.idEtapa = idEtapa;
        this.idCiclista = idCiclista;
        this.posicion = posicion;
        this.tiempo = tiempo;
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ResultadosEtapa that = (ResultadosEtapa) o;
        return idEtapa == that.idEtapa && idCiclista == that.idCiclista && posicion == that.posicion && Double.compare(tiempo, that.tiempo) == 0 && Objects.equals(estado, that.estado);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEtapa, idCiclista, posicion, tiempo, estado);
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

    public double getTiempo() {
        return tiempo;
    }

    public String getEstado() {
        return estado;
    }
}
