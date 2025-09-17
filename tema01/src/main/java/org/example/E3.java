package org.example;

import java.io.*;

import org.example.*;

public class E3 {


    public static void infoDir() {


        File file = Util.fileDirectorio();

        System.out.println("Nombre carpeta - " + file.getName());
        System.out.println("Ruta - " + file.getAbsolutePath());
        System.out.println("Se puede leer?" + file.canRead());
        System.out.println("Se puede escribir?" + file.canWrite());
        System.out.println("");
    }
}
