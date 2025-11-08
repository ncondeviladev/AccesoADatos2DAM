package org.example.myUtils;

import java.io.*;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class myUtils {

    // -------------------
    // ARCHIVOS Y DIRECTORIOS
    // -------------------

    /**
     * Crea el archivo si no existe
     */
    public static void crearArchivoSiNoExiste(String ruta) throws IOException {
        File f = new File(ruta);
        if (!f.exists()) {
            f.getParentFile().mkdirs();
            f.createNewFile();
        }
    }

    /**
     * Comprueba si un archivo existe
     */
    public static boolean existeArchivo(String ruta) {
        return new File(ruta).exists();
    }

    /**
     * Comprueba si la ruta es un directorio
     */
    public static boolean esDirectorio(String ruta) {
        return new File(ruta).isDirectory();
    }

    // -------------------
    // TEXTO
    // -------------------

    /**
     * Lee todo el contenido de un archivo de texto
     */
    public static String leerArchivoTexto(String ruta) throws IOException {
        File f = new File(ruta);
        if (!f.exists() || !f.isFile()) throw new FileNotFoundException();
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                sb.append(linea).append("\n");
            }
            return sb.toString();
        }
    }

    /**
     * Escribe contenido en un archivo de texto (append=true para añadir)
     */
    public static void escribirArchivoTexto(String ruta, String contenido, boolean append) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta, append))) {
            bw.write(contenido);
        }
    }

    /**
     * Compara dos archivos de texto línea a línea
     */
    public static boolean compararArchivosTexto(String ruta1, String ruta2) throws IOException {
        List<String> f1 = Files.readAllLines(new File(ruta1).toPath());
        List<String> f2 = Files.readAllLines(new File(ruta2).toPath());
        return f1.equals(f2);
    }

    // -------------------
    // BINARIO / OBJETO SERIALIZADO
    // -------------------

    /**
     * Guarda un objeto Serializable en un archivo
     */
    public static void guardarObjeto(String ruta, Serializable obj) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ruta))) {
            oos.writeObject(obj);
        }
    }

    /**
     * Lee un objeto Serializable desde un archivo
     */
    public static Object leerObjeto(String ruta) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ruta))) {
            return ois.readObject();
        }
    }

    /**
     * Compara dos archivos binarios byte a byte
     */
    public static boolean comparaArchivosBinarios(String ruta1, String ruta2) throws IOException {
        try (FileInputStream fis1 = new FileInputStream(ruta1);
             FileInputStream fis2 = new FileInputStream(ruta2)) {
            int b1, b2;
            while ((b1 = fis1.read()) != -1 && (b2 = fis2.read()) != -1) {
                if (b1 != b2) return false;
            }
            return fis1.read() == -1 && fis2.read() == -1;
        }
    }

    // -------------------
    // HEXADECIMAL
    // -------------------

    /**
     * Muestra el contenido de un archivo en hexadecimal
     */
    public static void mostrarHex(String ruta) throws IOException {
        try (FileInputStream fis = new FileInputStream(ruta)) {
            int b, count = 0;
            while ((b = fis.read()) != -1) {
                System.out.printf("%02x ", b);
                if (++count % 12 == 0) System.out.println();
            }
            System.out.println();
        }
    }

    // -------------------
    // MATHS
    // -------------------

    /**
     * Comprueba si un número es primo.
     */
    public static boolean esPrimo(long n) {
        if (n < 2) return false;
        for (long i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    /**
     * Devuelve el siguiente número primo mayor que 'n'.
     */
    public static long siguientePrimo(long n) {
        long candidato = n + 1;
        while (!esPrimo(candidato)) candidato++;
        return candidato;
    }

    // -------------------
    // HASH
    // -------------------

    /**
     * Genera un hash del archivo usando el algoritmo indicado (MD5, SHA-1, SHA-256, etc.)
     */
    public static byte[] generarHash(String ruta, String algoritmo) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algoritmo);
        try (FileInputStream fis = new FileInputStream(ruta)) {
            byte[] buffer = new byte[1024];
            int n;
            while ((n = fis.read(buffer)) != -1) md.update(buffer, 0, n);
        }
        return md.digest();
    }

    /**
     * Convierte un array de bytes de hash a cadena hexadecimal
     */
    public static String hashToString(byte[] hash) {
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) sb.append(String.format("%02x", b));
        return sb.toString();
    }

    /**
     * Valida que el hash de un archivo coincida con el hash esperado
     */
    public static boolean validarHash(String ruta, String hashEsperado, String algoritmo) throws IOException, NoSuchAlgorithmException {
        byte[] hashActual = generarHash(ruta, algoritmo);
        return hashToString(hashActual).equalsIgnoreCase(hashEsperado);
    }

    // -------------------
    // CONCATENAR ARCHIVOS
    // -------------------

    /**
     * Concatena dos archivos binarios en un tercer archivo destino.
     * El contenido de ruta1 se escribe primero, luego el de ruta2.
     */
    public static void concatenarArchivosBinarios(String ruta1, String ruta2, String rutaDestino) throws IOException {
        File f1 = new File(ruta1);
        File f2 = new File(ruta2);
        File fDest = new File(rutaDestino);
        crearArchivoSiNoExiste(rutaDestino);

        try (FileInputStream fis1 = new FileInputStream(f1);
             FileInputStream fis2 = new FileInputStream(f2);
             FileOutputStream fos = new FileOutputStream(fDest)) {

            byte[] buffer = new byte[1024];
            int leido;
            while ((leido = fis1.read(buffer)) != -1) fos.write(buffer, 0, leido);
            while ((leido = fis2.read(buffer)) != -1) fos.write(buffer, 0, leido);
        }
    }

    /**
     * Concatena dos archivos de texto en un tercer archivo destino.
     * El contenido de ruta1 se escribe primero, luego el de ruta2.
     */
    public static void concatenarArchivosTexto(String ruta1, String ruta2, String rutaDestino, boolean append) throws IOException {
        File f1 = new File(ruta1);
        File f2 = new File(ruta2);
        File fDest = new File(rutaDestino);
        crearArchivoSiNoExiste(rutaDestino);

        try (BufferedReader br1 = new BufferedReader(new FileReader(f1));
             BufferedReader br2 = new BufferedReader(new FileReader(f2));
             BufferedWriter bw = new BufferedWriter(new FileWriter(fDest, append))) {

            String linea;
            while ((linea = br1.readLine()) != null) {
                bw.write(linea);
                bw.newLine();
            }
            while ((linea = br2.readLine()) != null) {
                bw.write(linea);
                bw.newLine();
            }
        }
    }

    /**
     * Concatena dos archivos de texto línea a línea.
     * Cada línea de ruta1 se combina con la línea correspondiente de ruta2.
     * Si un archivo tiene más líneas, se añaden al final.
     */
    public static void concatenarLineasArchivoTexto(String ruta1, String ruta2, String rutaDestino) throws IOException {
        File f1 = new File(ruta1);
        File f2 = new File(ruta2);
        File fDest = new File(rutaDestino);
        crearArchivoSiNoExiste(rutaDestino);

        try (BufferedReader br1 = new BufferedReader(new FileReader(f1));
             BufferedReader br2 = new BufferedReader(new FileReader(f2));
             BufferedWriter bw = new BufferedWriter(new FileWriter(fDest))) {

            String linea1, linea2;
            while ((linea1 = br1.readLine()) != null && (linea2 = br2.readLine()) != null) {
                bw.write(linea1 + linea2);
                bw.newLine();
            }
            while ((linea1 = br1.readLine()) != null) {
                bw.write(linea1);
                bw.newLine();
            }
            while ((linea2 = br2.readLine()) != null) {
                bw.write(linea2);
                bw.newLine();
            }
        }
    }

    // -------------------
    // FECHAS
    // -------------------

    /**
     * Devuelve la fecha de última modificación de un archivo formateada.
     */
    public static String obtenerFechaModificacion(String ruta, String formato) {
        File f = new File(ruta);
        if (!f.exists()) return null;
        Instant instant = Instant.ofEpochMilli(f.lastModified());
        LocalDateTime fecha = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return fecha.format(DateTimeFormatter.ofPattern(formato));
    }

    /**
     * Cambia la fecha de modificación de un archivo a la hora actual.
     */
    public static void actualizarFechaModificacion(String ruta) {
        File f = new File(ruta);
        if (f.exists()) f.setLastModified(System.currentTimeMillis());
    }




}
