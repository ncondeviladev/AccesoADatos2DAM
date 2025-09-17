package org.example;

import java.io.*;


public class E1 {
    public E1() {
        if(Util.fileDirectorio().exists() && Util.fileDirectorio().isDirectory()){
            System.out.println("El directorio existe");
        } else System.out.println("Error! El directorio NO existe...");

        System.out.println("");
    }

    public static void existe() {

        if(Util.fileDirectorio().exists() && Util.fileDirectorio().isDirectory()){
            System.out.println("El directorio existe");
        } else System.out.println("Error! El directorio NO existe...");

        System.out.println("");
    }

}
