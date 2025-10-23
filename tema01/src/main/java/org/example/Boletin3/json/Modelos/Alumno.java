package org.example.Boletin3.json.Modelos;

import java.util.List;

public class Alumno {

    private final String id;
    private final String nombre;
    private final boolean matriculado;
    private final String fechaNacimiento;
    private final List<Nota> notas;

    public Alumno(String id, String nombre, boolean matriculado, String fechaNacimiento, List<Nota> notas) {
        this.id = id;
        this.nombre = nombre;
        this.matriculado = matriculado;
        this.fechaNacimiento = fechaNacimiento;
        this.notas = notas;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isMatriculado() {
        return matriculado;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public List<Nota> getNotas() {
        return notas;
    }
}
