package com.germangascon.tema01.ejercicio07;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * <p><strong>Ejercicio07</strong></p>
 * <p>Consulta de inventario</p>
 * <p>Cargar el dataset inventario.json y mostrar la ubicación del producto indicado por el usuario.</p>
 * License: 🅮 Public Domain<br />
 * Created on: 2025-10-17<br />
 *
 * @author Germán Gascón <ggascon@gmail.com>
 * @version 0.0.1
 * @since 0.0.1
 **/
public class Ejercicio07 {

    public Ejercicio07() throws IOException, JSONException {
        StringBuilder sb = new StringBuilder();
        try (InputStream is = getClass().getResourceAsStream("/json/inventario.json")) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            }
        }
        JSONTokener tokener = new JSONTokener(sb.toString());
        JSONObject jsonObject = new JSONObject(tokener);
        String almacen = jsonObject.getString("almacen");
        String actualizadoStr = jsonObject.getString("actualizado");
        LocalDateTime actualizado;
        try {
            actualizado = LocalDateTime.parse(actualizadoStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (DateTimeParseException dtpe) {
            throw new NumberFormatException("ERROR: Formato del documento erróneo. Se esperaba actualizado como una FechaHora en formato yyyy-MM-ddTHH:mm:ss");
        }
        Map<String, Producto> productos = new HashMap<>();
        JSONArray productosArray = jsonObject.getJSONArray("productos");
        for (int i = 0; i < productosArray.length(); i++) {
            JSONObject productoObjecct = productosArray.getJSONObject(i);
            String idProducto = productoObjecct.getString("id");
            String nombreProducto = productoObjecct.getString("nombre");
            int stockProducto = productoObjecct.getInt("stock");
            float precioProducto = productoObjecct.getFloat("precio");
            JSONArray tagsArray = productoObjecct.getJSONArray("tags");
            List<String> tags = new ArrayList<>();
            for (int j = 0; j < tagsArray.length(); j++) {
                tags.add(tagsArray.getString(j));
            }
            JSONObject ubicacionObject = productoObjecct.getJSONObject("ubicacion");
            int pasillo = ubicacionObject.getInt("pasillo");
            String estante = ubicacionObject.getString("estante");
            Ubicacion ubicacion = new Ubicacion(pasillo, estante);
            productos.put(idProducto, new Producto(idProducto, nombreProducto, stockProducto, precioProducto, tags, ubicacion));
        }
        Inventario inventario = new Inventario(almacen, actualizado, productos);

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Indique el id del producto: ");
            String idProducto = scanner.nextLine();
            System.out.println(inventario.getUbicacion(idProducto));
        }
    }
}
