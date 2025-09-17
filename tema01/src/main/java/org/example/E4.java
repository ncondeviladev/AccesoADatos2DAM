package org.example;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class E4 {


    public static File buscarArchivo(File directorio, String nombreArchivo) {
        File[] listaArchivos = directorio.listFiles();

        for (File f : listaArchivos) {
            if (f.isDirectory()) {
                File carpeta = buscarArchivo(f, nombreArchivo);
                if (carpeta != null) {
                    return carpeta;
                }
            } else if (f.getName().equals(nombreArchivo)) {
                return f;
            }
        }
        return null;
    }

    public static void mostrarArchivo() {

        File directorio = Util.fileDirectorio();
        File f = buscarArchivo(directorio, "Util.java");
        boolean encontrado = false;
        if (f != null) encontrado = true;


        System.out.println("Nombre del archivo: " + f.getName());
        System.out.println("Ruta absoluta: " + f.getAbsolutePath());
        System.out.println("Está oculto? " + f.isHidden());
        System.out.println("Se puede leer? " + f.canRead());
        System.out.println("Se puede escribir? " + f.canWrite());
        System.out.println("tamaño del archivo: " + f.length());

        Date fechaArchivo = new Date(f.lastModified());
        System.out.println("Última modificación: " + fechaArchivo.toString());
        f.setLastModified(System.currentTimeMillis());
        Date fechaActualizada = new Date(f.lastModified());
        System.out.println("Fecha modificación actualizada: " + fechaActualizada.toString());


        if (!encontrado) System.out.println("No se ha encontrado el archivo");
        System.out.println("");
    }
}
