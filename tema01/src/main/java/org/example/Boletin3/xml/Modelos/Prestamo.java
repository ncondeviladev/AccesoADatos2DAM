package org.example.Boletin3.xml.Modelos;

public class Prestamo {

    private String usuario;
    private String fecha;

    public Prestamo(String usuario, String fecha) {
        this.usuario = usuario;
        this.fecha = fecha;
    }

    public String getUsuario() {
        return usuario;
        }

    public String getFecha() {
        return fecha;
    }
}
