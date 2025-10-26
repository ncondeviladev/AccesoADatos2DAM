package com.germangascon.tema01.ejercicio06;

import com.germangascon.tema01.lib.TextTable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p><strong>Ejercicio06</strong></p>
 * <p>Objetos y estadísticas</p>
 * <p>Mapear los datos de los alumnos a objetos Alumno, para ello también deberás mapear los notas a
 * un array de objetos Nota. Después muestra la siguiente información:<br />
 * a) Alumno y la asignatura con la nota más alta<br />
 * b) Alumno con la nota media más alta</p>
 * License: 🅮 Public Domain<br />
 * Created on: 2025-10-17<br />
 *
 * @author Germán Gascón <ggascon@gmail.com>
 * @version 0.0.1
 * @since 0.0.1
 **/
public class Ejercicio06 {
    public Ejercicio06() throws IOException, JSONException {
        StringBuilder sb = new StringBuilder();
        try (InputStream is = getClass().getResourceAsStream("/json/alumnos.json")) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            }
        }
        List<Alumno> alumnos = new ArrayList<>();
        JSONTokener tokener = new JSONTokener(sb.toString());
        JSONArray jsonArray = new JSONArray(tokener);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String id = jsonObject.getString("id");
            String nombre = jsonObject.getString("nombre");
            boolean matriculado = jsonObject.getBoolean("matriculado");
            String fechaNacimientoStr = jsonObject.getString("fechaNacimiento");
            LocalDate fechaNacimiento;
            try {
                fechaNacimiento = LocalDate.parse(fechaNacimientoStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (DateTimeParseException dtpe) {
                throw new NumberFormatException("ERROR: Formato del documento erróneo. Se esperaba la fecha de pedido como una fecha en formato yyyy-MM-dd");
            }
            JSONArray arrayNotas = jsonObject.getJSONArray("notas");
            List<Nota> notas = new ArrayList<>();
            for (int j = 0; j < arrayNotas.length(); j++) {
                JSONObject notaObject = arrayNotas.getJSONObject(j);
                String asignatura = notaObject.getString("asignatura");
                float nota = notaObject.getFloat("nota");
                notas.add(new Nota(asignatura, nota));
            }
            alumnos.add(new Alumno(id, nombre, matriculado, fechaNacimiento, notas));
        }
        imprimirStats(alumnos);
    }

    private void imprimirStats(List<Alumno> alumnos) {
        if (alumnos == null || alumnos.isEmpty()) {
            return;
        }

        Alumno alumnoConNotaMasAlta = alumnos.getFirst();
        Nota notaMasAlta = alumnoConNotaMasAlta.getNotas().getFirst();
        Alumno alumnoConNotaMediaMasAlta = alumnos.getFirst();
        float notaMediaMasAlta = 0;

        for (Alumno alumno : alumnos) {
            float sumaNotasAlumno = 0;
            for (Nota nota : alumno.getNotas()) {
                sumaNotasAlumno += nota.getNota();
                if (nota.getNota() > notaMasAlta.getNota()) {
                    notaMasAlta = nota;
                    alumnoConNotaMasAlta = alumno;
                }
            }
            float notaMediaAlumno = sumaNotasAlumno / alumno.getNotas().size();
            if (notaMediaAlumno > notaMediaMasAlta) {
                notaMediaMasAlta = notaMediaAlumno;
                alumnoConNotaMediaMasAlta = alumno;
            }
        }
        System.out.println("Alumno con la nota más alta: ");
        TextTable textTable1 = new TextTable("Alumno", "Asignatura", "Nota");
        textTable1.setAlign("Nota", TextTable.Align.RIGHT);
        textTable1.addRow(alumnoConNotaMasAlta.getNombre(), notaMasAlta.getAsignatura(), String.format("%.2f", notaMasAlta.getNota()));;
        System.out.println(textTable1);

        System.out.println();
        System.out.println("Alumno con la nota media más alta: ");
        TextTable textTable2 = new TextTable("Alumno", "NotaMedia");
        textTable2.setAlign("NotaMedia", TextTable.Align.RIGHT);
        textTable2.addRow(alumnoConNotaMediaMasAlta.getNombre(), String.format("%.2f", notaMediaMasAlta));;
        System.out.println(textTable2);
    }
}
