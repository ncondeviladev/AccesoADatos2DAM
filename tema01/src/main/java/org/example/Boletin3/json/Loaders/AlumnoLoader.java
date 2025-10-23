package org.example.Boletin3.json.Loaders;

import org.example.Boletin3.json.Modelos.Alumno;
import org.example.myUtils.ParseUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class AlumnoLoader {


    public static List<Alumno> capturarAlumnos() throws IOException {
        InputStream is = AlumnoLoader.class.getResourceAsStream("/Boletin3/alumnos.json");

        if (is == null) {
            System.err.println("No se pudo encontrar el archivo alumnos.json. Asegúrate de que está en la carpeta de recursos correcta.");
            return null; // Devuelve la lista vacía si no la encuentra
        }

        Alumno[] listaAlumnos = ParseUtils.parseJson(is, Alumno[].class);

        if (listaAlumnos != null) {
            return Arrays.asList(listaAlumnos);
        } else {
            return null;
        }
    }
}
