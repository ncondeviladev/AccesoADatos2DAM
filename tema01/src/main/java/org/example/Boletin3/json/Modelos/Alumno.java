package org.example.Boletin3.json.Modelos;

import java.util.List;
import java.util.Objects;

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

    @Override
    public String toString() {
        return "Alumno{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", matriculado="
                + matriculado +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                ", notas=" + notas +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alumno alumno = (Alumno) o;
        return matriculado == alumno.matriculado && Objects.equals(id, alumno.id) && Objects.equals(nombre, alumno.nombre) && Objects.equals(fechaNacimiento, alumno.fechaNacimiento) && Objects.equals(notas, alumno.notas);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, matriculado, fechaNacimiento, notas);
    }

}
