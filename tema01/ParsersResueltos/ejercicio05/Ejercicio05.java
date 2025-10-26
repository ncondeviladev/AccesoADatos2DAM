package com.germangascon.tema01.ejercicio05;

import com.germangascon.tema01.lib.TextTable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


/**
 * <p><strong>Ejercicio05</strong></p>
 * <p>Listado de alumnos</p>
 * <p>Cargar el dataset alumnos.json y mostrar el nombre y la fecha de nacimiento de los alumnos.</p>
 * License: 🅮 Public Domain<br />
 * Created on: 2025-10-08<br />
 *
 * @author Germán Gascón <ggascon@gmail.com>
 * @version 0.0.1
 * @since 0.0.1
 **/
public class Ejercicio05 {
    public Ejercicio05() throws IOException, JSONException {
        StringBuilder sb = new StringBuilder();
        try (InputStream is = getClass().getResourceAsStream("/json/alumnos.json")) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            }
        }
        JSONTokener tokener = new JSONTokener(sb.toString());
        JSONArray jsonArray = new JSONArray(tokener);
        TextTable textTable = new TextTable("Alumno", "FechaNacimiento");
        textTable.setAlign("FechaNacimiento", TextTable.Align.RIGHT);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String nombre = jsonObject.getString("nombre");
            String fechaNacimientoStr = jsonObject.getString("fechaNacimiento");
            LocalDate fechaNacimiento;
            try {
                fechaNacimiento = LocalDate.parse(fechaNacimientoStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (DateTimeParseException dtpe) {
                throw new NumberFormatException("ERROR: Formato del documento erróneo. Se esperaba la fecha de pedido como una fecha en formato yyyy-MM-dd");
            }
            textTable.addRow(nombre, DateTimeFormatter.ofPattern("dd/MM/yyyy").format(fechaNacimiento));
        }
        System.out.println(textTable);
    }
}
