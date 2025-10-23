package org.example.Boletin3.json.Modelos;

public class Nota {

    private final String asignatura;
    private final Double nota;

    public Nota(String asignatura, Double nota){
        this.asignatura = asignatura;
        this.nota = nota;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public Double getNota() {
        return nota;
    }
}
