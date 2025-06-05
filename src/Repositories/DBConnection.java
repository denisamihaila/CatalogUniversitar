package Repositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public final class DBConnection {
    // Clasă Singleton
    private static final String URL  = "jdbc:mysql://localhost:3306/proiect_schema";
    private static final String USER = "root";
    private static final String PASS = "deni12345";

    private static DBConnection instance;
    private Connection connection;

    private DBConnection() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            throw new RuntimeException("Nu s-a putut realiza conexiunea la baza de date!", e);
        }
    }

    public static DBConnection getInstance() {
        if (instance == null) instance = new DBConnection();
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
