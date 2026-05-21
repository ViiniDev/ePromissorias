package com.demo.epromissorias.connection;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = getEnvOrDefault("EPROMISSORIAS_DB_URL", "jdbc:postgresql://localhost:5432/ePromissorias");
    private static final String USER = getEnvOrDefault("EPROMISSORIAS_DB_USER", "postgres");
    private static final String PASSWORD = getEnvOrDefault("EPROMISSORIAS_DB_PASSWORD", "");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return value == null || value.isBlank() ? defaultValue : value;
    }
}
