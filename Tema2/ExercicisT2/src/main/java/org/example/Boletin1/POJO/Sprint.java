package org.example.Boletin1.POJO;

import java.util.Objects;

public class Sprint {

    private final int idSprint;
    private final int idEtapa;
    private final double km;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Sprint sprint = (Sprint) o;
        return idSprint == sprint.idSprint && idEtapa == sprint.idEtapa && Double.compare(km, sprint.km) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSprint, idEtapa, km);
    }

    public int getIdSprint() {
        return idSprint;
    }

    public int getIdEtapa() {
        return idEtapa;
    }

    public double getKm() {
        return km;
    }

    public Sprint(int idSprint, int idEtapa, double km) {
        this.idSprint = idSprint;
        this.idEtapa = idEtapa;
        this.km = km;

    }
}
