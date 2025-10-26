package org.example.Boletin3.json.Modelos;

import java.util.Objects;

public class Nota {

    private final String asignatura;
    private final Double nota;

    public Nota(String asignatura, Double nota) {
        this.asignatura = asignatura;
        this.nota = nota;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public Double getNota() {
        return nota;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nota nota1 = (Nota) o;
        return Objects.equals(asignatura, nota1.asignatura);
    }
    @Override
    public int hashCode() {
        return Objects.hash(asignatura, nota);
    }


}

