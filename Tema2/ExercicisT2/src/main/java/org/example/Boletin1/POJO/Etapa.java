package org.example.Boletin1.POJO;

import java.util.Objects;

public class Etapa {

    private final int idEtapa;
    private final int idCarrera;
    private final int numEtapa;
    private final String fecha;
    private final String salida;
    private final String llegada;
    private final double distanciaKM;

    public Etapa(int idEtapa, int idCarrera, int numEtapa, String fecha, String salida, String llegada, double distanciaKM) {
        this.idEtapa = idEtapa;
        this.idCarrera = idCarrera;
        this.numEtapa = numEtapa;
        this.fecha = fecha;
        this.salida = salida;
        this.llegada = llegada;
        this.distanciaKM = distanciaKM;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Etapa etapa = (Etapa) o;
        return idEtapa == etapa.idEtapa && idCarrera == etapa.idCarrera && numEtapa == etapa.numEtapa && Double.compare(distanciaKM, etapa.distanciaKM) == 0 && Objects.equals(fecha, etapa.fecha) && Objects.equals(salida, etapa.salida) && Objects.equals(llegada, etapa.llegada);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEtapa, idCarrera, numEtapa, fecha, salida, llegada, distanciaKM);
    }

    public int getIdEtapa() {
        return idEtapa;
    }

    public int getIdCarrera() {
        return idCarrera;
    }

    public int getNumEtapa() {
        return numEtapa;
    }

    public String getFecha() {
        return fecha;
    }

    public String getSalida() {
        return salida;
    }

    public String getLlegada() {
        return llegada;
    }

    public double getDistanciaKM() {
        return distanciaKM;
    }
}
