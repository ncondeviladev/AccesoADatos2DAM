package org.example.Boletin3.json.Modelos;

import java.util.Objects;

public class Ubicacion {

    private int pasillo;
    private String estante;

    public Ubicacion(int pasillo, String estante) {
        this.pasillo = pasillo;
        this.estante = estante;
    }

    public int getPasillo() {
        return pasillo;
    }

    public String getEstante() {
        return estante;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ubicacion ubicacion = (Ubicacion) o;
        return pasillo == ubicacion.pasillo && Objects.equals(estante, ubicacion.estante);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pasillo, estante);
    }
}
