package com.germangascon.tema01.lib;

import java.util.*;

/**
 * TextTable
 * License: 🅮 Public Domain
 * Created on: 2025-10-16
 *
 * @author Germán Gascón <ggascon@gmail.com>
 * @version 0.0.1
 * @since 0.0.1
 **/
public class TextTable {
    public enum Align {LEFT, RIGHT}
    private int numRows;
    // LinkedHashMap para preservar el orden de las cabeceras
    private final LinkedHashMap<String, Column> columns;
    /** Espaciado entre columnas **/
    private int padding;

    /**
     * Crea una TextTable con el espaciado entre columnas y las cabeceras indicadas
     * @param padding Espaciado entre columnas
     * @param headers Texto de las cabeceras de la tabla
     */
    public TextTable(int padding, String...headers) {
        if (padding < 0) {
            throw new IllegalArgumentException("El espaciado entre columnas no puede ser negativo");
        }
        columns = new LinkedHashMap<>();
        reset(padding, headers);
    }

    /**
     * Crea una TextTable con el padding por defecto (1) y las cabeceras indicadas
     * @param headers Texto de las cabeceras de la tabla
     */
    public TextTable(String...headers) {
        this(1, headers);
    }

    /**
     * Restablece la TextTable a los valores iniciales y borra su contenido
     * @param padding Espaciado entre columnas
     * @param headers Texto de las cabeceras de la tabla
     */
    public void reset(int padding, String ...headers) {
        this.numRows = 0;
        this.padding = padding;
        this.columns.clear();
        for (String header : headers) {
            if (columns.get(header) != null) {
                throw new IllegalArgumentException("La cabecera " + header + " está duplicada");
            }
            columns.put(header, new Column(header));
        }
    }

    /**
     * Restablece la TextTable a los valores iniciales y borra su contenido
     * @param headers Texto de las cabeceras de la tabla
     */
    public void reset(String ...headers) {
        reset(1, headers);
    }

    /**
     * Establece la alineación de la columna indicada.
     * El nombre debe de coincidir con el indicado en el momento de la creación/reset de la TextTable
     * @param header Nombre de la columna sobre la que se quiere establecer la alineación
     * @param align Alineación elegida
     */
    public void setAlign(String header, Align align) {
        Column col = columns.get(header);
        Objects.requireNonNull(col, "Cabecera desconocida: " + header);
        col.setAlign(align);
    }

    /**
     * Añade una fila de valores a la TextTable
     * Si la cantidad de valores indicados no se corresponde con la cantidad de cabeceras establecidas,
     * las columnas restantes se rellenan con vacíos.
     * @param values Cadenas de texto separadas por comas
     */
    public void addRow(String... values) {
        int i = 0;
        for (var e : columns.entrySet()) {
            Column column = e.getValue();
            // Si una fila tiene menos columnas rellenamos con ""
            column.add((values != null && i < values.length && values[i] != null) ? values[i] : "");
            i++;
        }
        numRows++;
    }

    /**
     * Obtiene la representación en texto de la tabla tabulada con los anchos de columna ajustados
     * @return String con la tabla tabulada
     */
    @Override
    public String toString() {
        if (columns.isEmpty()) {
            return "";
        }

        final String sep = " ".repeat(padding);

        StringBuilder sb = new StringBuilder();

        // Línea separadora
        sb.append(rowSeparator());

        // Cabecera
        sb.append("|");
        for (var c : columns.values()) {
            sb.append(sep);
            sb.append(String.format("%-" + c.getMaxSize() + "s", c.getHeader()));
            sb.append(sep);
            sb.append("|");
        }
        sb.append(System.lineSeparator());

        // Línea separadora
        sb.append(rowSeparator());

        // Filas de datos
        for (int r = 0; r < numRows; r++) {
            sb.append("|");
            for (var c : columns.entrySet()) {
                sb.append(sep);
                Column col = c.getValue();
                String cell = col.getRows().get(r);
                String alignStr = "";
                if (col.getAlign() == Align.LEFT) {
                    alignStr = "-";
                }
                String format = "%" + alignStr + col.getMaxSize() + "s";
                sb.append(String.format(format, cell));
                sb.append(sep);
                sb.append("|");
            }
            sb.append(System.lineSeparator());
        }
        // Línea separadora
        sb.append(rowSeparator());

        return sb.toString();
    }

    private String rowSeparator() {
        StringBuilder sb = new StringBuilder();
        sb.append("|");
        for (var c : columns.values()) {
            sb.append("-".repeat(c.getMaxSize() +  2 * padding));
            sb.append("|");
        }
        sb.append(System.lineSeparator());
        return sb.toString();
    }

    private static class Column {
        private final String header;
        private Align align;
        private int maxSize;
        private final List<String> rows;

        public Column(String header, Align align) {
            this.header = header;
            this.align = align;
            maxSize = header.length();
            rows = new ArrayList<>();
        }

        public Column(String header) {
            this(header, Align.LEFT);
        }

        public String getHeader() {
            return header;
        }

        public Align getAlign() {
            return align;
        }

        public void setAlign(Align align) {
            this.align = align;
        }

        public int getMaxSize() {
            return maxSize;
        }

        public void add(String text) {
            rows.add(text);
            maxSize = Math.max(text.length(), maxSize);
        }

        public List<String> getRows() {
            return rows;
        }
    }
}
