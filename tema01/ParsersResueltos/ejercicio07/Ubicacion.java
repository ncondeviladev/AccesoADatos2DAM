package com.germangascon.tema01.ejercicio07;

/**
 * Ubicacion
 * License: 🅮 Public Domain
 * Created on: 2025-10-17
 *
 * @author Germán Gascón <ggascon@gmail.com>
 * @version 0.0.1
 * @since 0.0.1
 **/
public class Ubicacion {
    private final int pasillo;
    private final String estante;

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
    public final boolean equals(Object o) {
        if (!(o instanceof Ubicacion ubicacion)) return false;

        return pasillo == ubicacion.pasillo && estante.equals(ubicacion.estante);
    }

    @Override
    public int hashCode() {
        int result = pasillo;
        result = 31 * result + estante.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Ubicacion{" +
                "pasillo=" + pasillo +
                ", estante='" + estante + '\'' +
                '}';
    }
}
