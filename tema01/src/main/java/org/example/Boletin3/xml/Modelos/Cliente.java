package org.example.Boletin3.xml.Modelos;

public class Cliente {

    private String nombre;
    private String email;

    public Cliente(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
    }


    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }
}
