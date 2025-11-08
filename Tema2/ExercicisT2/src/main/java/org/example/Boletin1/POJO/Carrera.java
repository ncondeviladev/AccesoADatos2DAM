package org.example.Boletin1.POJO;

import java.time.LocalDate;
import java.util.Objects;

public class Carrera {

    private final int idCarrera;
    private final String nombre;
    private final int anio;
    private final LocalDate fechaInicio;
    private final LocalDate fechaFin;

    public Carrera(int idCarrera, String nombre, int anio, LocalDate fechaInicio, LocalDate fechaFin) {
        this.idCarrera = idCarrera;
        this.nombre = nombre;
        this.anio = anio;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public int getIdCarrera() {
        return idCarrera;
    }

    public String getNombre() {
        return nombre;
    }

    public int getAnio() {
        return anio;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Carrera carrera = (Carrera) o;
        return idCarrera == carrera.idCarrera && anio == carrera.anio && Objects.equals(nombre, carrera.nombre) && Objects.equals(fechaInicio, carrera.fechaInicio) && Objects.equals(fechaFin, carrera.fechaFin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCarrera, nombre, anio, fechaInicio, fechaFin);
    }

    @Override
    public String toString() {
        return "Carrera{" +
                "idCarrera=" + idCarrera +
                ", nombre='" + nombre + "'" +
                ", anio=" + anio +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                '}';
    }
}
