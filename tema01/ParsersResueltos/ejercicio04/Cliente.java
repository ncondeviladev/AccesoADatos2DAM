package com.germangascon.tema01.ejercicio04;

/**
 * Cliente
 * License: 🅮 Public Domain
 * Created on: 2025-10-08
 *
 * @author Germán Gascón <ggascon@gmail.com>
 * @version 0.0.1
 * @since 0.0.1
 **/
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
    public final boolean equals(Object o) {
        if (!(o instanceof Cliente cliente)) return false;

        return nombre.equals(cliente.nombre) && email.equals(cliente.email);
    }

    @Override
    public int hashCode() {
        int result = nombre.hashCode();
        result = 31 * result + email.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
