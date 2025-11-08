package org.example.Boletin1.DAOs;

import org.example.Boletin1.BDConect.DataSource;
import org.example.myUtils.TextTable;

import javax.sql.rowset.Joinable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import static java.lang.Double.parseDouble;


public class ciclismoDAO {

    private final DataSource ds;
    private Connection conn;

    //Constructor que recibe el DataSource y conecta con la BD
    public ciclismoDAO() {
        this.ds = new DataSource(
                DataSource.Driver.POSTGRESQL,
                "localhost",
                5432,
                "ciclismo",
                "user",
                "user"
        );
    }

    /**
     * Lista todos los equipos registrados en la base de datos.
     * Muestra el ID, nombre y país de cada equipo en formato de tabla.
     */
    public void listarEquipos() throws SQLException {

        String sql = "SELECT id_equipo, nombre, pais FROM equipos ORDER BY id_equipo";

        try (Connection conn = ds.getConnection();
             PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            System.out.println("- - - LISTA DE EQUIPOS - - -");
            String[] columnas = {"ID", "Nombre", "País"};
            TextTable tt = new TextTable(columnas);

            while (rs.next()) {
                tt.addRow(
                        rs.getString("id_equipo"),
                        rs.getString("nombre"),
                        rs.getString("pais")
                );
            }
            System.out.println(tt);
        }
    }


    /**
     * Lista los ciclistas de un equipo específico o todos los ciclistas agrupados por equipo.
     * Si el idEquipo es 0, muestra todos los ciclistas.
     *
     * @param idEquipo El ID del equipo a listar. Si es 0, lista todos los ciclistas.
     */
    public void ciclistasPorEquipo(int idEquipo) throws SQLException {

        TextTable tt;
        String sql;
        //Si selecciona 0 mostrar todos los ciclistas agrupados por equipo
        if (idEquipo == 0) {
            sql = "SELECT e.nombre AS nombre_equipo, c.nombre AS nombre_ciclista, c.pais " +
                    "FROM ciclistas c " +
                    "JOIN equipos e ON c.id_equipo = e.id_equipo " +
                    "ORDER BY e.nombre, c.nombre";
            String[] columnas = {"Equipo", "Nombre Ciclista", "País"};
            tt = new TextTable(columnas);

            try (Connection conn = ds.getConnection();
                 PreparedStatement st = conn.prepareStatement(sql);
                 ResultSet rs = st.executeQuery()) {

                System.out.println("- - LISTA DE CICLISTAS POR EQUIPO - - ");
                while (rs.next()) {
                    tt.addRow(
                            rs.getString("nombre_equipo"),
                            rs.getString("nombre_ciclista"),
                            rs.getString("pais")
                    );
                }
                System.out.println(tt);
            }

        } else {
            sql = "SELECT id_ciclista, nombre, pais FROM ciclistas WHERE id_equipo = ? ORDER BY nombre";
            String[] columnas = {"ID", "Nombre", "País"};
            tt = new TextTable(columnas);

            try (Connection conn = ds.getConnection();
                 PreparedStatement st = conn.prepareStatement(sql)) {
                st.setInt(1, idEquipo);

                try (ResultSet rs = st.executeQuery()) {
                    System.out.println("- - LISTA DE CICLISTAS POR ID " + idEquipo + " - - ");
                    while (rs.next()) {
                        tt.addRow(
                                rs.getString("id_ciclista"),
                                rs.getString("nombre"),
                                rs.getString("pais")
                        );
                    }
                    System.out.println(tt);
                }
            }
        }
    }

    /**
     * Lista todas las etapas registradas en la base de datos y, al final,
     * muestra un resumen de la cantidad de etapas y la suma de kilómetros
     * por cada tipo de etapa.
     */
    public void listarEtapas() throws SQLException {
        String sql = "SELECT id_etapa, tipo, fecha, salida, llegada, distancia_km FROM etapas ORDER BY id_etapa";

        try (Connection conn = ds.getConnection();
             PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            System.out.println("- - - LISTA DE ETAPAS - - -");
            String[] columnas = {"ID", "Tipo", "Fecha", "Salida", "Llegada", "Distancia (km)"};
            TextTable tt = new TextTable(columnas);

            while (rs.next()) {
                int id = rs.getInt("id_etapa");
                String tipo = rs.getString("tipo");
                String fecha = rs.getString("fecha");
                String salida = rs.getString("salida");
                String llegada = rs.getString("llegada");
                double distancia_km = rs.getDouble("distancia_km");
                tt.addRow(
                        String.valueOf(id),
                        tipo,
                        fecha,
                        salida,
                        llegada,
                        String.valueOf(distancia_km));
            }
            System.out.println(tt);

            //Resumen de etapas
            String sqlResumen = "SELECT tipo, COUNT(*) AS cantidad, SUM(distancia_km) AS total_km FROM etapas GROUP BY tipo ORDER BY tipo";

            try (Connection connR = ds.getConnection();
                 PreparedStatement stResumen = connR.prepareStatement(sqlResumen);
                 ResultSet rsResumen = stResumen.executeQuery()) {
                System.out.println("- - - RESUMEN POR ETAPA - - -");
                String[] columnasResumen = {"Tipo de etapa", "Cantidad", "Distancia total (km)"};
                TextTable ttResumen = new TextTable(columnasResumen);

                while (rsResumen.next()) {
                    String tipo = rsResumen.getString("tipo");
                    int cantidad = rsResumen.getInt("cantidad");
                    double totalKm = rsResumen.getDouble("total_km");

                    ttResumen.addRow(
                            tipo,
                            String.valueOf(cantidad),
                            String.valueOf(totalKm)
                    );
                }
                System.out.println(ttResumen);
            }
        }


    }

    /**
     * Calcula y muestra la velocidad media de un ciclista específico.
     * Suma la distancia total recorrida y el tiempo total empleado por el ciclista
     * en todas las etapas finalizadas, y luego calcula la velocidad media en km/h.
     *
     * @param idCiclista El ID del ciclista para el cual se calculará la velocidad media.
     */
    public void velocidadMediaCiclista(int idCiclista) throws SQLException {

        String sql = "SELECT c.nombre AS nombre_ciclista, " +
                "SUM(e.distancia_km) AS distancia_total, " +
                "SUM(EXTRACT(EPOCH FROM re.tiempo)) AS tiempo_total " +
                "FROM ciclistas c " +
                "JOIN resultados_etapa re ON c.id_ciclista = re.id_ciclista " +
                "JOIN etapas e ON re.id_etapa = e.id_etapa " +
                "WHERE c.id_ciclista = ? AND re.tiempo IS NOT NULL " +
                "GROUP BY c.nombre";


        try (Connection conn = ds.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, idCiclista);

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                String nombreCiclista = rs.getString("nombre_ciclista");
                double distanciaTotal = rs.getDouble("distancia_total");
                double tiempoTotal = rs.getDouble("tiempo_total");
                double velocidadMedia = distanciaTotal / (tiempoTotal / 3600);

                System.out.println("- - - VELOCIDAD MEDIA DEL CICLISTA - - -");
                System.out.println("Ciclista: " + nombreCiclista);
                System.out.println("Velocidad media " + velocidadMedia);
            }
        }
    }

    /**
     * Muestra la clasificación de una etapa específica.
     * Incluye la posición, nombre del ciclista, equipo del ciclista y tiempo,
     * ordenada por tiempo de menor a mayor.
     *
     * @param idEtapa El ID de la etapa para la cual se mostrará la clasificación.
     */
    public void clasificacionEtapa(int idEtapa) throws SQLException {

        String sql = """
                SELECT re.posicion,
                c.nombre AS nombre_ciclista, 
                eq.nombre AS nombre_equipo,
                re.tiempo,
                et.num_etapa,
                et.salida,
                et.llegada
                FROM resultados_etapa re
                JOIN ciclistas c ON re.id_ciclista = c.id_ciclista
                JOIN equipos eq ON c.id_equipo = eq.id_equipo
                JOIN etapas et ON re.id_etapa = et.id_etapa
                WHERE re.id_etapa = ? AND re.estado = 'FINALIZADO'
                ORDER BY re.tiempo
                """;

        try (Connection conn = ds.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, idEtapa);

            String[] columnas = {"Posición", "Ciclista", "Equipo", "Tiempo"};
            TextTable tt = new TextTable(columnas);

            ResultSet rs = st.executeQuery();
            System.out.println("- - - CLASIFICACIÓN DE ETAPA - - -");
            while (rs.next()) {
                tt.addRow(
                        rs.getString("posicion"),
                        rs.getString("nombre_ciclista"),
                        rs.getString("nombre_equipo"),
                        rs.getString("tiempo")
                );
            }
            System.out.println(tt);
        }
    }


    /**
     * Muestra la clasificación de la montaña (maillot de lunares).
     * Calcula los puntos totales obtenidos por cada ciclista en los puertos de montaña
     * y los muestra ordenados de mayor a menor puntuación.
     */
    public void clasificacionMontana() throws SQLException {

        String sql = """
                SELECT c.nombre AS nombre_ciclista,
                eq.nombre AS nombre_equipo,
                SUM(rp.puntos) AS puntos_total
                FROM resultados_puerto rp
                JOIN ciclistas c ON rp.id_ciclista = c.id_ciclista
                JOIN equipos eq ON c.id_equipo = eq.id_equipo
                GROUP BY c.id_ciclista, c.nombre, eq.nombre
                HAVING SUM(rp.puntos) > 0
                ORDER BY puntos_total DESC
                """;

        try (Connection conn = ds.getConnection();
             PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            System.out.println("- - - CLASIFICACIÓN DE MONTANA - - -");

            String[] columnas = {"Posición", "Ciclista", "Equipo", "Puntos"};
            TextTable tt = new TextTable(columnas);

            int posicion = 1;
            while (rs.next()) {
                tt.addRow(
                        String.valueOf(posicion++),
                        rs.getString("nombre_ciclista"),
                        rs.getString("nombre_equipo"),
                        rs.getString("puntos_total")
                );
            }
            System.out.println(tt);
        }

    }

    /**
     * Muestra la clasificación de la regularidad (maillot verde).
     * Calcula los puntos totales obtenidos por cada ciclista combinando
     * los puntos de sprints intermedios (resultados_sprint) y metas (puntos_meta).
     * La clasificación se ordena de mayor a menor puntuación.
     */
    public void clasificacionRegularidad() throws SQLException {

        String sql = """
                SELECT c.nombre AS nombre_ciclista,
                eq.nombre AS nombre_equipo,
                SUM(puntos_combinados.puntos) AS total_puntos
                FROM (
                    SELECT id_ciclista, puntos FROM resultados_sprint
                UNION ALL
                SELECT id_ciclista, puntos FROM puntos_meta
                    ) AS puntos_combinados
                JOIN ciclistas c ON puntos_combinados.id_ciclista = c.id_ciclista
                JOIN equipos eq ON c.id_equipo = eq.id_equipo
                GROUP BY c.id_ciclista, c.nombre, eq.nombre
                ORDER BY total_puntos DESC;
                
                """;

        try (Connection conn = ds.getConnection();
             PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            System.out.println("- - - CLASIFICACIÓN DE REGULARIDAD - - -");

            String[] columnas = {"Posición", "Ciclista", "Equipo", "Puntos"};
            TextTable tt = new TextTable(columnas);

            int posicion = 1;
            while (rs.next()) {
                tt.addRow(
                        String.valueOf(posicion++),
                        rs.getString("nombre_ciclista"),
                        rs.getString("nombre_equipo"),
                        rs.getString("total_puntos")
                );
            }
            System.out.println(tt);
        }
    }

    /**
     * Muestra la clasificación general (maillot amarillo).
     * Calcula el tiempo total acumulado por cada ciclista en todas las etapas finalizadas
     * y los muestra ordenados de menor a mayor tiempo.
     */
    public void clasificacionGeneral() throws SQLException {

        String sql = """
                SELECT c.nombre AS nombre_ciclista,
                eq.nombre AS nombre_equipo,
                SUM(re.tiempo) AS tiempo_total
                FROM resultados_etapa re
                JOIN ciclistas c ON re.id_ciclista = c.id_ciclista
                JOIN equipos eq On c.id_equipo = eq.id_equipo
                WHERE re.estado = 'FINALIZADO'
                GROUP BY c.id_ciclista, c.nombre, eq.nombre
                ORDER BY tiempo_total ASC
                """;
        try (Connection conn = ds.getConnection();
             PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            System.out.println("- - - CLASIFICACIÓN GENERAL - - -");

            String[] columnas = {"Posición", "Ciclista", "Equipo", "Tiempo"};
            TextTable tt = new TextTable(columnas);

            int posicion = 1;
            while (rs.next()) {
                tt.addRow(
                        String.valueOf(posicion++),
                        rs.getString("nombre_ciclista"),
                        rs.getString("nombre_equipo"),
                        rs.getString("tiempo_total")
                );
            }
            System.out.println(tt);
        }


    }

    /**
     * Muestra la clasificación general por equipos.
     * Calcula el tiempo total de cada equipo sumando los tiempos de sus tres mejores
     * ciclistas en cada etapa finalizada. La clasificación se ordena de menor a mayor tiempo.
     */
    public void clasificacionPorEquipo() throws SQLException {

        String sql = """
                WITH RankedTimes AS (
                    SELECT c.id_equipo,
                    re.tiempo,
                    ROW_NUMBER() OVER (PARTITION BY re.id_etapa, c.id_equipo ORDER BY re.tiempo ASC) AS team_rank_in_stage
                    FROM resultados_etapa re
                    JOIN ciclistas c ON re.id_ciclista = c.id_ciclista
                    WHERE re.estado = 'FINALIZADO' AND re.tiempo IS NOT NULL
                    )
                SELECT eq.nombre AS nombre_equipo,
                SUM(rt.tiempo) AS tiempo_total_eq
                FROM RankedTimes rt
                JOIN equipos eq ON rt.id_equipo = eq.id_equipo
                WHERE rt.team_rank_in_stage <= 3
                GROUP BY eq.id_equipo, eq.nombre
                ORDER BY tiempo_total_eq ASC;
                """;

        try (Connection conn = ds.getConnection();
             PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            System.out.println("- - - CLASIFICACIÓN POR EQUIPO - - -");

            String[] columnas = {"Posición", "Equipo", "Tiempo"};
            TextTable tt = new TextTable(columnas);

            int posicion = 1;
            while (rs.next()) {
                tt.addRow(
                        String.valueOf(posicion++),
                        rs.getString("nombre_equipo"),
                        rs.getString("tiempo_total_eq")
                );
            }
            System.out.println(tt);
        }
    }


}
