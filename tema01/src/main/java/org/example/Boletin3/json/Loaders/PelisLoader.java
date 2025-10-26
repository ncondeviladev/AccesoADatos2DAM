package org.example.Boletin3.json.Loaders;

import org.example.Boletin3.json.Modelos.Pelicula;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;



public class PelisLoader {

    public static List<Pelicula> capturarPeliculas() throws IOException {

        InputStream is = InventarioLoader.class.getResourceAsStream("/Boletin3/peliculas.json");

        if (is == null) {
            System.err.println("No se pudo encontrar el archivo peliculas.json. Asegúrate de que está en la carpeta de recursos correcta.");
            return null; // Devuelve la lista vacía si no la encuentra
        }
        //Leemos el archivo json en sb
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                sb.append(linea);
            }
        }
        //Creamos el tokener y array json
        JSONTokener tokener = new JSONTokener(sb.toString());
        JSONObject json = new JSONObject(tokener);
        //El json tien atributos ademas de productos, Obtenemos el array de productos
        JSONArray jsonArray = json.getJSONArray("peliculas");

        //Creamos una lista y la recorremos el jsonarray para llenarla
        List<Pelicula> listaPeliculas = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonPelicula = jsonArray.getJSONObject(i); //JSONObject

            String id = jsonPelicula.getString("id");
            String titulo = jsonPelicula.getString("titulo");
            String director = jsonPelicula.getString("director");
            int estreno = jsonPelicula.getInt("estreno");
            int duracionMin = jsonPelicula.getInt("duracionMin");

            JSONArray jsonGeneros = jsonPelicula.getJSONArray("generos");
            String[] generos = new String[jsonGeneros.length()];
            for (int j = 0; j < jsonGeneros.length(); j++) {
                generos[j] = jsonGeneros.getString(j);
            }

            JSONObject jsonPuntuaciones = jsonPelicula.getJSONObject("puntuaciones");
            Map<String, Double> puntuaciones = new HashMap<>();
            for (String key : jsonPuntuaciones.keySet()) {
                puntuaciones.put(key, jsonPuntuaciones.getDouble(key));
            }

            //Creamos el producto y añadimos a la lista
            Pelicula pelicula = new Pelicula(id, titulo, director, estreno, duracionMin, generos, puntuaciones);
            listaPeliculas.add(pelicula);
        }
        return listaPeliculas;

    }

}
