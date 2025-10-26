package com.germangascon.tema01.ejercicio08;

import com.germangascon.tema01.lib.TextTable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * <p><strong>Ejercicio08</strong></p>
 * <p>Peliculas</p>
 * <p>a) Calcular la puntuación media de cada película. Para ello hay que normalizar primero la
 * puntuación rt que está en escala 1-100 y pasarla a escala 1-10 que es la que utiliza imbd.
 * Después mostrar todas las películas ordenadas por media en orden descendiente.<br />
 * b) Mostrar todos los géneros distintos de películas ordenados alfabéticamente.</p>
 * License: 🅮 Public Domain<br />
 * Created on: 2025-10-17<br />
 *
 * @author Germán Gascón <ggascon@gmail.com>
 * @version 0.0.1
 * @since 0.0.1
 **/
public class Ejercicio08 {

    public Ejercicio08() throws IOException, JSONException {
        StringBuilder sb = new StringBuilder();
        try (InputStream is = getClass().getResourceAsStream("/json/peliculas.json")) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            }
        }
        List<Pelicula> peliculas = new ArrayList<>();
        JSONTokener tokener = new JSONTokener(sb.toString());
        JSONObject jsonObject = new JSONObject(tokener);
        JSONArray peliculasArray = jsonObject.getJSONArray("peliculas");
        for (int i = 0; i < peliculasArray.length(); i++) {
            JSONObject peliculaObject = peliculasArray.getJSONObject(i);
            String id = peliculaObject.getString("id");
            String titulo = peliculaObject.getString("titulo");
            String director = peliculaObject.getString("director");
            int estreno = peliculaObject.getInt("estreno");
            int duracionMin = peliculaObject.getInt("duracionMin");
            JSONArray generosArray = peliculaObject.getJSONArray("generos");
            List<String> generos = new ArrayList<>();
            for (int j = 0; j < generosArray.length(); j++) {
                generos.add(generosArray.getString(j));
            }
            JSONObject puntuacionesObject = peliculaObject.getJSONObject("puntuaciones");
            float imdb = puntuacionesObject.getFloat("imdb");
            int rt = puntuacionesObject.getInt("rt");
            Puntuacion puntuacion = new Puntuacion(imdb, rt);
            Pelicula pelicula = new Pelicula(id, titulo, director, estreno, duracionMin, generos, puntuacion);
            peliculas.add(pelicula);
        }
        peliculas.sort(Comparator.comparing(Pelicula::getPuntuacionMedia, Comparator.reverseOrder()));
        TextTable peliculasTable = new TextTable("Id", "Título", "Director", "Estreno", "Duración", "Géneros", "Media");
        peliculasTable.setAlign("Estreno", TextTable.Align.RIGHT);
        peliculasTable.setAlign("Duración", TextTable.Align.RIGHT);
        peliculasTable.setAlign("Media", TextTable.Align.RIGHT);
        for (Pelicula pelicula : peliculas) {
            peliculasTable.addRow(pelicula.getId(), pelicula.getTitulo(), pelicula.getDirector(), String.valueOf(pelicula.getEstreno()), String.valueOf(pelicula.getDuracionMin()), pelicula.getGeneros().toString(), String.format("%.2f", pelicula.getPuntuacionMedia()));
        }
        System.out.println(peliculasTable);


        // Utilizamos un TreeSet para evitar duplicados y que se muestren ordenados
        peliculasTable.reset("Géneros");
        Set<String> generos = new TreeSet<>();
        for (Pelicula pelicula : peliculas) {
            generos.addAll(pelicula.getGeneros());
        }
        for (String genero : generos) {
            peliculasTable.addRow(genero);
        }
        System.out.println("Generos de películas ordenados alfabéticamente: ");
        System.out.println(peliculasTable);
    }
}
