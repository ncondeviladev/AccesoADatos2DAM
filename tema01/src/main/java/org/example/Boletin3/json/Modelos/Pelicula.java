package org.example.Boletin3.json.Modelos;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class Pelicula {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Pelicula pelicula = (Pelicula) o;
        return estreno == pelicula.estreno && duracion == pelicula.duracion && Objects.equals(id, pelicula.id) && Objects.equals(titulo, pelicula.titulo) && Objects.equals(director, pelicula.director) && Objects.deepEquals(generos, pelicula.generos) && Objects.equals(puntuaciones, pelicula.puntuaciones);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo, director, estreno, duracion, Arrays.hashCode(generos), puntuaciones);
    }

    private String id;
    private String titulo;
    private String director;
    private int estreno;

    public Double getNotaMedia() {
        return notaMedia;
    }

    public void setNotaMedia(Double notaMedia) {
        this.notaMedia = notaMedia;
    }

    private int duracion;
    private String[] generos;
    private Map<String, Double> puntuaciones;
    private Double notaMedia;

    public Pelicula(String id, String titulo, String director, int estreno, int duracion, String[] generos, Map<String, Double> puntuaciones){
        this.id = id;
        this.titulo = titulo;
        this.director = director;
        this.estreno = estreno;
        this.duracion = duracion;
        this.generos = generos;
        this.puntuaciones = puntuaciones;
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

    public int getDuracion() {
        return duracion;
    }

    public String[] getGeneros() {
        return generos;
    }

    public Map<String, Double> getPuntuaciones() {
        return puntuaciones;
    }
}
