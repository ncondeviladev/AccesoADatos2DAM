package org.example.Boletin2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GestionAlumnos {

    File listaAlumnos;

    public GestionAlumnos(String ruta)throws IOException{

        listaAlumnos = new File("src/main/resources/Boletin2/ListaAlumnos.txt");
        if(!listaAlumnos.exists()) {
            listaAlumnos.createNewFile();
        }
    }

    public void insertarAlumnos(Alumno[] alumnos) throws IOException {

        try(BufferedWriter wr = new BufferedWriter(new FileWriter(listaAlumnos))){
            for(Alumno a : alumnos){

            }
        }
    }
}

