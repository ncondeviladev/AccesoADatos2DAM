package org.example;

import java.io.*;


public class E2 {

    public static void listar() {
        File file = Util.fileDirectorio();
        File[] lista = file.listFiles();

        if (lista != null) {
            for(File f : lista){
                if(f.isDirectory()){
                    System.out.println("Directorio - " + f.getName());
                } else if (f.isFile()) {
                    System.out.println("Archivo - " + f.getName());
                }
            }
            System.out.println("");
        }

    }

}
