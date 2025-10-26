package org.example.Boletin3.json;

import org.example.Boletin3.json.Loaders.AlumnoLoader;
import org.example.Boletin3.json.Modelos.Alumno;
import org.example.Boletin3.json.Modelos.Nota;

import java.io.IOException;
import java.util.List;

public class B3E6 {

    Alumno mejorAlumno = null;
    Nota notaMax = null;
    Double notaMedia = null;

    public B3E6() throws IOException {

        List<Alumno> listaAlumnos = AlumnoLoader.capturarAlumnos();
        //a)
        for(Alumno a : listaAlumnos){
            for(Nota n : a.getNotas()){
                if(notaMax == null || n.getNota() > notaMax.getNota()){
                    notaMax = n;
                    mejorAlumno = a;
                }
            }
        }
        System.out.println("La nota mas alta es un " + notaMax.getNota() + " de " + mejorAlumno.getNombre() + " en la asignatura " + notaMax.getAsignatura());

        //b)
        Alumno alumnoMejorMedia = null;
        double mediaMax = -1;

        for(Alumno a : listaAlumnos){
            List<Nota> notas = a.getNotas();
            if(notas == null || notas.isEmpty()) continue;

            double suma = 0;
            for(Nota n : notas){
                suma += n.getNota();
            }
            double media = suma / notas.size();
            if(media > mediaMax){
                mediaMax = media;
                alumnoMejorMedia = a;
            }
        }
        System.out.println("El mejor alumno es " + alumnoMejorMedia.getNombre() + " con una media de " + mediaMax);

    }

}
