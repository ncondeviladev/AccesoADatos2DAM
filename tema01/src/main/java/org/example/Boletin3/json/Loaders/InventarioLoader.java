package org.example.Boletin3.json.Loaders;

import org.example.Boletin3.json.Modelos.Producto;
import org.example.Boletin3.json.Modelos.Ubicacion;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class InventarioLoader {

    public static List<Producto> capturarProductos() throws IOException {

        InputStream is = InventarioLoader.class.getResourceAsStream("/Boletin3/inventario.json");

        if(is == null){
            System.err.println("No se pudo encontrar el archivo inventario.json. Asegúrate de que está en la carpeta de recursos correcta.");
            return null; // Devuelve la lista vacía si no la encuentra
        }
        //Leemos el archivo json en sb
        StringBuilder sb = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String linea;
            while((linea = br.readLine()) != null) {
                sb.append(linea);
            }
        }
        //Creamos el tokener y array json
        JSONTokener tokener = new JSONTokener(sb.toString());
        JSONObject json = new JSONObject(tokener);
        //El json tien atributos ademas de productos, Obtenemos el array de productos
        JSONArray jsonArray = json.getJSONArray("productos");

        //Creamos una lista y la recorremos el jsonarray para llenarla
        List<Producto> listaProductos = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonProducto = jsonArray.getJSONObject(i); //JSONObject

            String id = jsonProducto.getString("id");
            String nombre = jsonProducto.getString("nombre");
            int stock = jsonProducto.getInt("stock");
            double precio = jsonProducto.getDouble("precio");

            JSONArray jsonTags = jsonProducto.getJSONArray("tags");
            String[] tags = new String[jsonTags.length()];
            for(int j = 0; j < jsonTags.length(); j++){
                tags[j] = jsonTags.getString(j);
            }

            JSONObject jsonUbicacion = jsonProducto.getJSONObject("ubicacion");
            int pasillo = jsonUbicacion.getInt("pasillo");
            String estante = jsonUbicacion.getString("estante");
            Ubicacion ubicacion = new Ubicacion(pasillo, estante);

            //Creamos el producto y añadimos a la lista
            Producto producto = new Producto(id, nombre, stock, precio, tags, ubicacion);
            listaProductos.add(producto);
            }
        return listaProductos;

    }



































}
