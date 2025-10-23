package org.example.Boletin3.xml.Loaders;

import org.example.Boletin3.xml.Modelos.Autor;
import org.example.Boletin3.xml.Modelos.Libro;
import org.example.myUtils.ParseUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class BiblioLoader {

    public static List<Libro> capturarLibros() throws IOException, ParserConfigurationException, SAXException, NullPointerException {
        List<Libro> libros = new ArrayList<>();

        try (InputStream inputStream = EmpleadoLoader.class.getResourceAsStream("/Boletin3/biblioteca.xml")) {

            if (inputStream == null) {
                System.err.println("No se pudo encontrar el archivo libros.xml. Asegúrate de que está en la carpeta de recursos correcta.");
                return libros; // Devuelve la lista vacía
            }

            Document dom = ParseUtils.parseXML(inputStream);
            NodeList items = dom.getElementsByTagName("libro");

            for (int i = 0; i < items.getLength(); i++) {
                Element librosElement = (Element) items.item(i);

                String isbn = librosElement.getAttribute("isbn");

                //Capturamos el titulo del libro comprobando si no esta vacio, si no me daba error..
                NodeList tituloNodes = librosElement.getElementsByTagName("titulo");
                String titulo = ""; // Asigna un valor por defecto por si no lo encuentra
                if (tituloNodes.getLength() > 0) {
                    titulo = tituloNodes.item(0).getTextContent();
                }

                //Capturamos elemento autor, extraemos sus etiquetas nombre y nacimineto y creamos instancia Autor
                Element autorElement = (Element) librosElement.getElementsByTagName("autor").item(0);
                String nombreAutor = autorElement.getElementsByTagName("nombre").item(0).getTextContent();
                String nacimientoAutor = autorElement.getElementsByTagName("nacimiento").item(0).getTextContent();
                Autor autor = new Autor(nombreAutor, nacimientoAutor);

                int anio = Integer.parseInt(librosElement.getElementsByTagName("anio").item(0).getTextContent());

                //Capturamos los generos y los añadimos a una lista
                Element generosElement = (Element) librosElement.getElementsByTagName("generos").item(0);
                NodeList listaGeneros = generosElement.getElementsByTagName("genero");
                List<String> generos = new ArrayList<>();
                for(int j = 0; j < listaGeneros.getLength(); j++) {
                    String genero = listaGeneros.item(j).getTextContent();
                    generos.add(genero);
                }

                boolean disponible = Boolean.parseBoolean(librosElement.getElementsByTagName("disponible").item(0).getTextContent());

                //Creamos el libro con los datos capturados
                Libro libro = new Libro(isbn, titulo, autor, anio, generos, disponible);
                libros.add(libro);

            }
            return libros;


        } catch (Exception e) {
            System.err.println("Ocurrió un error al parsear el XML:");
            e.printStackTrace();
        }
        return libros; // Devuelve la lista (llena o vacía si hubo error)
    }
}
