package org.example.Boletin1.BDConect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase DataSource para establecer la conexión con la base de datos
 */
public class DataSource {
    private final String url;
    private final String user;
    private final String password;
    public enum Driver {
        MYSQL, POSTGRESQL;
        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
    /**
     * Constructor
     *
     **/
    public DataSource(Driver driver, String host, int port, String database, String user, String password) {
        this.url = "jdbc:" + driver + "://" + host + ":" + port + "/" + database;
        this.user = user;
        this.password = password;
    }

    /**
     * @return Connection
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
