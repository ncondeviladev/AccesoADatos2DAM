package org.example.Boletin3.json;

import org.example.Boletin3.json.Loaders.PelisLoader;
import org.example.Boletin3.json.Modelos.Pelicula;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class B3E8 {

    public B3E8() throws IOException {
        //a)
        List<Pelicula> peliculas = PelisLoader.capturarPeliculas();
        for (Pelicula p : peliculas) {
            double imdb = p.getPuntuaciones().get("imdb");
            double rt = p.getPuntuaciones().get("rt") / 10.0;
            double media = (imdb + rt) / 2.0;
            p.setNotaMedia(media);
        }
        //Ordenar por nota
        peliculas.sort((a, b) -> Double.compare(a.getNotaMedia(), b.getNotaMedia()));

        for(Pelicula p : peliculas){
            System.out.println(p.getTitulo() + " - Nota media - " + p.getNotaMedia());
        }

        //b)
        Set<String> generos = new TreeSet<>();
        for(Pelicula p : peliculas){
            for(String genero : p.getGeneros()){
                generos.add(genero);
            }
        }
        for(String g : generos){
            System.out.println(g);
        }


    }

}
