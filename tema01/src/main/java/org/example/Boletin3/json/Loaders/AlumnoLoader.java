package org.example.Boletin3.json.Loaders;

import org.example.Boletin3.json.Modelos.Alumno;
import org.example.Boletin3.json.Modelos.Nota;
import org.example.myUtils.ParseUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class AlumnoLoader {


    public static List<Alumno> capturarAlumnos() throws IOException {
        InputStream is = AlumnoLoader.class.getResourceAsStream("/Boletin3/alumnos.json");

        if (is == null) {
            System.err.println("No se pudo encontrar el archivo alumnos.json. Asegúrate de que está en la carpeta de recursos correcta.");
            return null; // Devuelve la lista vacía si no la encuentra
        }
        //Leemos el archivo json en sb
        StringBuilder sb = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String linea;
            while((linea = br.readLine()) != null) {
                sb.append(linea);
            }
        }
        //Creamos el tokener y array json
        JSONTokener tokener = new JSONTokener(sb.toString());
        JSONArray jsonArray = new JSONArray(tokener);
        
        //Creamos una lista y la recorremos el jsonarray para llenarla
        List<Alumno> listaAlumnos = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonAlumno = jsonArray.getJSONObject(i);

            String id = jsonAlumno.getString("id");
            String nombre = jsonAlumno.getString("nombre");
            boolean matriculado = jsonAlumno.getBoolean("matriculado");
            String fechaNacimiento = jsonAlumno.getString("fechaNacimiento");

            JSONArray jsonNotas = jsonAlumno.getJSONArray("notas");
            List<Nota> listaNotas = new ArrayList<>();
            for(int j = 0; j < jsonNotas.length(); j++){
                JSONObject jsonNota = jsonNotas.getJSONObject(j);
                String asignatura = jsonNota.getString("asignatura");
                Double nota = jsonNota.getDouble("nota");
                listaNotas.add(new Nota(asignatura, nota));
            }

            //Creamos el alumno y añadimos a la lista
            Alumno alumno = new Alumno(id, nombre, matriculado, fechaNacimiento, listaNotas);
            listaAlumnos.add(alumno);
        }
        return listaAlumnos;
    }
}
