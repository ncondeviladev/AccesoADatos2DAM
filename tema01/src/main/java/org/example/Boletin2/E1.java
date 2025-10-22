package org.example.Boletin2;

import org.example.myUtils.FileUtils;

import java.io.*;

public class E1 {

    /**
     * Método para crear un dni en base a una serie de dígitos aunque no tengan los 8 correspondientes
     * @param f
     * @throws IOException
     */
    public static void crearDni(File f) throws IOException {

        File fileDni, listaDni;
        fileDni = new File("src/main/resources/Boletin2/dni.txt");
        String nombreNuevoArchivo = FileUtils.getFileNameWithoutExtension(fileDni);
        listaDni = new File("src/main/resources/Boletin2/" + nombreNuevoArchivo + "_conLetras.txt");
        listaDni.createNewFile();

        try (BufferedReader reader = new BufferedReader(new FileReader(f));
             BufferedWriter writer = new BufferedWriter(new FileWriter(listaDni))) {

            String linea;
            String dniCompleto = null;
            while ((linea = reader.readLine()) != null) {

                String dniNumero = comprobarLong(linea);

                dniCompleto = letraDni(dniNumero);

                writer.write(dniCompleto);
                writer.newLine();
            }


        } catch (IOException e) {
            System.out.println("Error al procesar el archivo: " + e.getMessage());
        }
    }

    /**
     * Submétodo de crearDni para comprobalr la longitud del dni sin letra y en caso de no llegar a 8 dígitos se le añaden 0's a la zquierda
     * @param linea
     * @return
     */
    public static String comprobarLong(String linea) {
        while (linea.length() < 8) {
            linea = "0" + linea;
        }
        return linea;
    }

    /**
     * Submétodo de crearDni para calcular la letra del dni a partir de su número
     * @param dni
     * @return
     */
    public static String letraDni(String dni) {

        String listaLetras = "TRWAGMYFPDXBNJZSQVHLCKE";

        int dniNumero = Integer.parseInt(dni);
        char letraDni = listaLetras.charAt(dniNumero % 23);

        return dni + letraDni;

    }

}
