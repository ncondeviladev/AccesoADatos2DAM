package org.example.Boletin3.json;

import org.example.Boletin3.json.Loaders.AlumnoLoader;
import org.example.Boletin3.json.Modelos.Alumno;

import java.io.IOException;
import java.util.List;

public class B3E5 {

    /**
     * Carga los datos de alumnos desde un JSON y muestra la información requerida.
     */
    public static void mostrarAlumnosYFechas() {
        try {
            // 1. Llama al Loader. Este método hace todo el trabajo de encontrar
            //    el archivo, leerlo y usar Gson para convertir el JSON en una lista de objetos Alumno.
            List<Alumno> alumnos = AlumnoLoader.capturarAlumnos();

            // 2. Comprobación de seguridad. Nos aseguramos de que la lista no sea nula
            //    (lo que indicaría un error de parseo) y que no esté vacía.
            if (alumnos != null && !alumnos.isEmpty()) {
                System.out.println("--- Listado de Alumnos y Fechas de Nacimiento ---");

                // 3. Recorremos la lista de objetos Alumno.
                //    'for (Alumno alumno : alumnos)' es un bucle "for-each" que toma cada
                //    objeto de la lista, uno por uno, y lo asigna a la variable 'alumno'.
                for (Alumno alumno : alumnos) {
                    // 4. Para cada objeto 'alumno', usamos sus métodos 'get' para acceder a sus
                    //    datos (que ya fueron rellenados por Gson) y los imprimimos.
                    System.out.println(
                        "- Nombre: " + alumno.getNombre() +
                        ", Fecha de Nacimiento: " + alumno.getFechaNacimiento()
                    );
                }
            } else {
                // Se muestra este mensaje si AlumnoLoader devolvió null o una lista vacía.
                System.out.println("No se encontraron alumnos o hubo un error al cargar el archivo.");
            }
        } catch (IOException e) {
            // Este bloque se ejecuta si AlumnoLoader lanza una excepción, por ejemplo,
            // si no puede encontrar el archivo 'alumnos.json'.
            System.err.println("Error de lectura al intentar cargar el archivo de alumnos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
