package com.germangascon.tema01.ejercicio06;

/**
 * Nota
 * License: 🅮 Public Domain
 * Created on: 2025-10-17
 *
 * @author Germán Gascón <ggascon@gmail.com>
 * @version 0.0.1
 * @since 0.0.1
 **/
public class Nota {
    private final String asignatura;
    private final float nota;

    public Nota(String asignatura, float nota) {
        this.asignatura = asignatura;
        this.nota = nota;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public float getNota() {
        return nota;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Nota nota)) return false;

        return asignatura.equals(nota.asignatura);
    }

    @Override
    public int hashCode() {
        return asignatura.hashCode();
    }

    @Override
    public String toString() {
        return "Nota{" +
                "asignatura='" + asignatura + '\'' +
                ", nota=" + nota +
                '}';
    }
}
