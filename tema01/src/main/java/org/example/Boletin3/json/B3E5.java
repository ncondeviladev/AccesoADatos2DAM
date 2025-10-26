package org.example.Boletin3.json;

import org.example.Boletin3.json.Loaders.AlumnoLoader;
import org.example.Boletin3.json.Modelos.Alumno;

import java.io.IOException;
import java.util.List;

public class B3E5 {


    public B3E5() throws  IOException {
        try {
            List<Alumno> alumnos = AlumnoLoader.capturarAlumnos();

            if (alumnos != null && !alumnos.isEmpty()) {
                System.out.println("--- Listado de Alumnos y Fechas de Nacimiento ---");
                for (Alumno alumno : alumnos) {

                    System.out.println(
                        "- Nombre: " + alumno.getNombre() +
                        ", Fecha de Nacimiento: " + alumno.getFechaNacimiento()
                    );
                }
            } else {
                System.out.println("No se encontraron alumnos o hubo un error al cargar el archivo.");
            }
        } catch (IOException e) {
            System.err.println("Error de lectura al intentar cargar el archivo de alumnos: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
