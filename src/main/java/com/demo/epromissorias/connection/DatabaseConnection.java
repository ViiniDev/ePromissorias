package com.demo.epromissorias.connection;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/ePromissorias";
    private static final String USER = "postgres"; // Substitua pelo seu usuário
    private static final String PASSWORD = "admin"; // Substitua pela sua senha

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
