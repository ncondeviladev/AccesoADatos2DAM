package org.example.Boletin3.xml.Modelos;

public class Empleado {

    private final String id;
    private final String nombre;
    private final String departamento;
    private final Double salario;
    private final String fechaAlta;

    public Empleado(String id, String nombre, String departamento, Double salario, String fechaAlta) {
        this.id = id;
        this.nombre = nombre;
        this.departamento = departamento;
        this.salario = salario;
        this.fechaAlta = fechaAlta;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Double getSalario() {
        return salario;
    }

    public String getDepartamento() {
        return departamento;
    }

    public String getFechaAlta() {
        return fechaAlta;
    }

    @Override
    public String toString(){
        return "Nombre: " + this.nombre + "\n" +
                "Departamento: " + this.departamento + "\n" +
                "Salario: " + this.salario + "\n" +
                "Fecha de alta: " + this.fechaAlta;
    }
}
