package com.germangascon.tema01.ejercicio08;

/**
 * Puntuacion
 * License: 🅮 Public Domain
 * Created on: 2025-10-17
 *
 * @author Germán Gascón <ggascon@gmail.com>
 * @version 0.0.1
 * @since 0.0.1
 **/
public class Puntuacion {
    private final float imdb;
    private final int rt;

    public Puntuacion(float imdb, int rt) {
        this.imdb = imdb;
        this.rt = rt;
    }

    public float getImdb() {
        return imdb;
    }

    public int getRt() {
        return rt;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Puntuacion that)) return false;

        return Float.compare(imdb, that.imdb) == 0 && rt == that.rt;
    }

    @Override
    public int hashCode() {
        int result = Float.hashCode(imdb);
        result = 31 * result + rt;
        return result;
    }

    @Override
    public String toString() {
        return "Puntuacion{" +
                "imdb=" + imdb +
                ", rt=" + rt +
                '}';
    }
}
