package com.germangascon.tema01;

import com.germangascon.tema01.ejercicio01.Ejercicio01;
import com.germangascon.tema01.ejercicio02.Ejercicio02;
import com.germangascon.tema01.ejercicio03.Ejercicio03;
import com.germangascon.tema01.ejercicio04.Ejercicio04;
import com.germangascon.tema01.ejercicio05.Ejercicio05;
import com.germangascon.tema01.ejercicio06.Ejercicio06;
import com.germangascon.tema01.ejercicio07.Ejercicio07;
import com.germangascon.tema01.ejercicio08.Ejercicio08;
import org.json.JSONException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Arrays;

/**
 * <p><strong>Tema 1. Acceso a ficheros</strong></p>
 * <p><strong>Boletín 3: Parsers XML y JSON</strong></p>
 * <p>License: 🅮 Public Domain</p>
 * <p>Created on: 2025-10-08</p>
 *
 * @author Germán Gascón <ggascon@gmail.com>
 * @version 0.0.1
 * @since 0.0.1
 **/
public class Main {
    public static void main(String[] args) {
        try {
            // Bloque XML
            // new Ejercicio01();
            // new Ejercicio02();
            // new Ejercicio03();
            // new Ejercicio04();

            // Bloque JSON
            // new Ejercicio05();
            // new Ejercicio06();
            // new Ejercicio07();
            new Ejercicio08();
        } catch (Exception e) {
            if (e instanceof IOException) {
                System.out.println("Error al leer el archivo de datos");
            } else if (e instanceof ParserConfigurationException) {
                System.out.println("La estructura del documento no es correcta");
            } else if (e instanceof SAXException) {
                System.out.println("Error al parsear el archivo xml");
            } else if (e instanceof JSONException) {
                System.out.println("Error al parsear el archivo json");
            } else {
                System.out.println("Error inesperado: " + e.getMessage());
            }
        }
    }
}
