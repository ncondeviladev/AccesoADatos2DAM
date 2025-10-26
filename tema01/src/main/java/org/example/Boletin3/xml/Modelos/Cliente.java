package org.example.Boletin3.xml.Modelos;

import java.util.Objects;

public class Cliente {

    private final String nombre;
    private final String email;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(nombre, cliente.nombre) && Objects.equals(email, cliente.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, email);
    }
}
