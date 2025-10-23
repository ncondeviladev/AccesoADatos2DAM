package org.example.Boletin3.xml.Modelos;

import java.util.List;

public class Libro {

    private final String isbn;
    private final String titulo;
    private final Autor autor;
    private final int anio;
    private final List<String> generos;
    private boolean disponible;
    private final List<Prestamo> prestamos;

    public Libro(String isbn, String titulo, Autor autor, int anio, List<String> generos, boolean disponible) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.anio = anio;
        this.generos = generos;
        this.disponible = disponible;
        this.prestamos = null;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public int getAnio() {
        return anio;
    }

    public List<String> getGeneros() {
        return generos;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public List<Prestamo> getPrestamos() {
        return prestamos;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}
