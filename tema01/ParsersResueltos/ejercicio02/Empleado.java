package com.germangascon.tema01.ejercicio02;

import java.time.LocalDate;

public class Empleado {
    private final String id;
    private final String nombre;
    private final String departamento;
    private final double salario;
    private final String moneda;
    private final LocalDate fechaAlta;

    public Empleado(String id, String nombre, String departamento, double salario, String moneda, LocalDate fechaAlta) {
        this.id = id;
        this.nombre = nombre;
        this.departamento = departamento;
        this.salario = salario;
        this.moneda = moneda;
        this.fechaAlta = fechaAlta;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDepartamento() {
        return departamento;
    }

    public double getSalario() {
        return salario;
    }

    public String getMoneda() {
        return moneda;
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Empleado empleado)) return false;

        return id.equals(empleado.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Empleado{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", departamento='" + departamento + '\'' +
                ", salario=" + salario +
                ", moneda='" + moneda + '\'' +
                ", fechaAlta='" + fechaAlta + '\'' +
                '}';
    }
}
