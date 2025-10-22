package org.example.Boletin1;


import org.example.myUtils.FileUtils;


public class E1 {
    public E1() {
        if(FileUtils.fileDirectorio().exists() && FileUtils.fileDirectorio().isDirectory()){
            System.out.println("El directorio existe");
        } else System.out.println("Error! El directorio NO existe...");

        System.out.println("");
    }

    public static void existe() {

        if(FileUtils.fileDirectorio().exists() && FileUtils.fileDirectorio().isDirectory()){
            System.out.println("El directorio existe");
        } else System.out.println("Error! El directorio NO existe...");

        System.out.println("");
    }

}
