package org.example.Boletin2;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Alumno implements Serializable {

    final private String nia, nombre, apellido1, apellido2;
    final private LocalDate fechaNacimiento;

    public Alumno(String nia, String nombre, String apellido1, String apellido2, LocalDate fechaNacimiento){
        this.nia = nia;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getNia() {
        return nia;
    }
    public String getNombre() {
        return nombre;
    }
    public String getApellido1() {
        return apellido1;
    }
    public String getApellido2() {
        return apellido2;
    }
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    @Override
    public String toString(){
        return "Alumno { " +
                "NIA: " + nia + " / " +
                "Nombre: " + nombre + " / " +
                "Apellido1: " + apellido1 + " / " +
                "Apellido2: " + apellido2 + " / " +
                "Fecha nacimiento: " + fechaNacimiento.toString() +
                " }";
    }

    /**
     * Método equals adaptado a Alumno
     * @param o   the reference object with which to compare.
     * @return
     */
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof Alumno)) return false;
        Alumno alumno = (Alumno) o;
        return Objects.equals(nia, alumno.nia);
    }
    /**
     * Método hashCode adaptado a Alumno
     * @return
     */
    @Override
    public int hashCode(){
        return Objects.hash(nia);
    }
}
