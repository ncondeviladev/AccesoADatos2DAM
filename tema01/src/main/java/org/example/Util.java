package org.example;

import java.io.*;


public class Util {

    public static File fileDirectorio(){
        final String rutaDir = System.getProperty("user.dir");
        File file = new File(rutaDir);

        if(file.exists() && file.isDirectory()){
            return file;

        } else {
            System.out.println("Error al buscar el directorio principal");
            return null;
        }

    }
}
