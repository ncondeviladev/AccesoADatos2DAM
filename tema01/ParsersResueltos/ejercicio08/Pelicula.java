package com.germangascon.tema01.ejercicio08;

import java.util.List;

/**
 * Pelicula
 * License: 🅮 Public Domain
 * Created on: 2025-10-17
 *
 * @author Germán Gascón <ggascon@gmail.com>
 * @version 0.0.1
 * @since 0.0.1
 **/
public class Pelicula {
    private final String id;
    private final String titulo;
    private final String director;
    private final int estreno;
    private final int duracionMin;
    private final List<String> generos;
    private final Puntuacion puntuacion;

    public Pelicula(String id, String titulo, String director, int estreno, int duracionMin, List<String> generos, Puntuacion puntuacion) {
        this.id = id;
        this.titulo = titulo;
        this.director = director;
        this.estreno = estreno;
        this.duracionMin = duracionMin;
        this.generos = generos;
        this.puntuacion = puntuacion;
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDirector() {
        return director;
    }

    public int getEstreno() {
        return estreno;
    }

    public int getDuracionMin() {
        return duracionMin;
    }

    public List<String> getGeneros() {
        return generos;
    }

    public Puntuacion getPuntuacion() {
        return puntuacion;
    }

    public float getPuntuacionMedia() {
        return puntuacion.getImdb() + puntuacion.getRt() / 10f;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Pelicula pelicula)) return false;

        return id.equals(pelicula.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Pelicula{" +
                "id='" + id + '\'' +
                ", titulo='" + titulo + '\'' +
                ", director='" + director + '\'' +
                ", estreno=" + estreno +
                ", duracionMin=" + duracionMin +
                ", generos=" + generos +
                ", puntuacion=" + puntuacion +
                '}';
    }
}
