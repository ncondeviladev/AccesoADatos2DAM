package org.example.Boletin3.xml;

import org.example.Boletin3.xml.Loaders.BiblioLoader;
import org.example.Boletin3.xml.Modelos.Libro;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class B3E3 {

    public static void mostrarTituloLibros() throws IOException, ParserConfigurationException, SAXException {

        List<Libro> libros = BiblioLoader.capturarLibros();
        System.out.println("Libros disponibles: ");
        for(Libro l : libros) {
            System.out.println(l.getTitulo());
        }
    }

    public static void contrarLibrosGenero() {

        System.out.println("Libros disponibles: ");
        try{
            //Cargamos la lista de libros
            List<Libro> libros = BiblioLoader.capturarLibros();
            //Map para guardar el conteo genero -cantidad
            Map<String, Integer> conteoLibrosGenero = new HashMap<>();
            //Recorremos los libros, y en cada uno recorremos los generos
            for(Libro l : libros) {
                for(String g : l.getGeneros()) {
                    if(conteoLibrosGenero.containsKey(g)) {
                        conteoLibrosGenero.put(g, conteoLibrosGenero.get(g) + 1);
                    } else {
                        conteoLibrosGenero.put(g, 1);
                    }
                }
            }
            //Recorremos el Map creado y mostramos
            for(Map.Entry<String, Integer> entry : conteoLibrosGenero.entrySet()) {
                System.out.println(entry.getKey() + " - " + entry.getValue() + " libros");
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
