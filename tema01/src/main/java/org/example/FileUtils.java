package org.example;

import java.io.*;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


/**
 * FileUtils
 * License: 🅮 Public Domain
 * Created on: 2024-09-11
 *
 * @author Germán Gascón <ggascon@gmail.com>
 * @version 0.0.1
 * @since 0.0.1
 **/
public class FileUtils {

    // Evitamos que se creen instancias de FileUtils
    private FileUtils() {
    }




    public static File fileDirectorio() {
        final String rutaDir = System.getProperty("user.dir");
        File file = new File(rutaDir);

        if (file.exists() && file.isDirectory()) {
            return file;

        } else {
            System.out.println("Error al buscar el directorio principal");
            return null;
        }

    }


    /**
     * Muestra los archivos rebidos como parámetro con el siguiente formato:
     * trwx usuario tamanyo fechaModificacion nombreDelArchivo
     * donde:
     * t es el tipo. Tipos soportados son d: directorio, f: fichero. Cualquier otro tipo se muestra ?
     * r indica si se puede leer, de lo contrario muestra -
     * w indica si se puede escribir, de lo contrario muestra -
     * x indica si se puede ejecutar, de lo contrario muestra -
     *
     * @param files Array con los File a mostrar
     * @return Cadena de texto formateado según los criterios definidos anteriormente
     */
    public static String filesToString(File[] files) {
        StringBuilder sb = new StringBuilder();
        for (File f : files) {
            // Para cada fichero mostramos sus propiedades aprovechando nuestro método
            sb.append(fileToString(f));
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Muestra el archivo rebido como parámetro con el siguiente formato:
     * trwx usuario tamanyo fechaModificacion nombreDelArchivo
     * donde:
     * t es el tipo. Tipos soportados son d: directorio, f: fichero. Cualquier otro tipo se muestra ?
     * r indica si se puede leer, de lo contrario muestra -
     * w indica si se puede escribir, de lo contrario muestra -
     * x indica si se puede ejecutar, de lo contrario muestra -
     *
     * @param file fichero a mostrar
     * @return Cadena de texto formateado según los criterios definidos anteriormente
     */
    public static String fileToString(File file) {
        StringBuilder sb = new StringBuilder();
        if (file.exists()) {
            // Primero mostramos el tipo:
            // - d Directorio
            // - f Fichero
            sb.append(file.isDirectory() ? "d" : (file.isFile() ? "f" : "?"));
            // Luego permisos lectura, escritura, ejecución
            sb.append(file.canRead() ? "r" : "-");
            sb.append(file.canWrite() ? "w" : "-");
            sb.append(file.canExecute() ? "x" : "-");
            sb.append(" ");
            // Propietario
            try {
                sb.append(Files.getOwner(file.toPath()));
            } catch (IOException ioe) {
                sb.append("unknown");
            }
            sb.append(" ");
            // Tamaño en la unidad más conveniente
            sb.append(String.format("%8s", getReadableFileSize(file)));
            sb.append(" ");
            // Fecha última modificación
            sb.append(getLastModified(file, "dd/MM/yyyy HH:mm"));
            sb.append(" ");
            // Nombre del archivo
            sb.append(file.getName());
        }
        return sb.toString();
    }

    /**
     * Obtiene la última fecha de modificación del archivo recibido como parámetro
     * en el formato indicado en format
     *
     * @param file   Fichero
     * @param format Formato de representación de la fecha. Los formatos aceptados son
     *               todos los que acepta DateTimeFormater
     * @return String con la última fecha de modificación formateada
     */
    public static String getLastModified(File file, String format) {
        long lastModified = file.lastModified();
        LocalDateTime localDateTime = Instant.ofEpochMilli(lastModified)
                .atZone(ZoneId.systemDefault()) // usa la zona horaria local
                .toLocalDateTime();
        DateTimeFormatter dte = DateTimeFormatter.ofPattern(format);
        return localDateTime.format(dte);
    }

    /**
     * Obtiene el tamaño del fichero recibido como parámetro en formato legible
     *
     * @param file Fichero
     * @return Tamaño formateado a la unidad de medida más cercano en rangos de valores [0 - 1023]
     */
    public static String getReadableFileSize(File file) {
        String[] units = {"", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"};
        int unitIndex = 0;

        double readableSize = file.length();
        while (readableSize >= 1024 && unitIndex < units.length - 1) {
            readableSize /= 1024.0;
            unitIndex++;
        }
        String format = String.format("%.1f", readableSize);
        if (file.isDirectory() || file.length() < 1000) {
            format = String.format("%.0f", readableSize);
        }
        return format + units[unitIndex];
    }

    /**
     * Devuelve como String el archivo recibido como parámetro
     *
     * @param textFile Fichero
     * @return String con el contenido del fichero
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static String readTextFile(File textFile) throws FileNotFoundException, IOException {
        if (!textFile.exists()) {
            throw new FileNotFoundException("El archivo " + textFile.getAbsolutePath() + " no existe");
        }
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(textFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Devuelve como array de bytes el archivo recibido como parámetro
     *
     * @param file Fichero
     * @return contenido del fichero como un array de bytes
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static byte[] readFile(File file) throws FileNotFoundException, IOException {
        if (!file.exists()) {
            throw new FileNotFoundException("El archivo " + file.getAbsolutePath() + " no existe");
        }
        byte[] data = new byte[(int) file.length()];
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            int bytesRead = 0;
            int offset = 0;
            while (offset < data.length && (bytesRead = bis.read(data, offset, data.length - offset)) != -1) {
                offset += bytesRead;
            }
        }
        return data;
    }


    /**
     * Comprueba si los archivos indicados por fichero1 y fichero2 son iguales
     *
     * @param file1 Primer fichero a comparar
     * @param file2 Segundo fichero a comparar
     * @return true si son iguales, false en caso contrario
     */
    public static boolean compare(File file1, File file2) {
        // Si alguno de los archivo no existe devolvemos false
        if (!file1.exists() || !file1.exists()) {
            return false;
        }
        // Si las longitudes no coinciden, son distintos
        if (file1.length() != file2.length()) {
            return false;
        }
        boolean iguales = true;
        try (BufferedInputStream bis1 = new BufferedInputStream(new FileInputStream(file1));
             BufferedInputStream bis2 = new BufferedInputStream(new FileInputStream(file2))) {
            int byte1, byte2;
            do {
                byte1 = bis1.read();
                byte2 = bis2.read();
                if (byte1 != byte2) {
                    return false;
                }
            } while (byte1 != -1 && byte2 != -1);
        } catch (IOException ioe) {
            iguales = false;
            System.out.println("Error al leer los archivos");
        }
        return iguales;
    }

    /**
     * Obtiene el nombre del archivo recibido como parámetro pero sin la extensión
     *
     * @param file Fichero
     * @return nombre del archivo sin la extensión
     */
    public static String getFileNameWithoutExtension(File file) {
        String name = file.getName();
        int lastDot = name.lastIndexOf('.');
        if (lastDot == -1) {
            return name; // no tiene extensión
        }
        return name.substring(0, lastDot);
    }
}
