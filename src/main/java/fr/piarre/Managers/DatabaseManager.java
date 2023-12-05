package fr.piarre.Managers;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private Connection conn;

    public DatabaseManager() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Error loading the driver: " + e.getMessage());
        }
    }

    public Statement connect() {
        String url = "jdbc:mysql://localhost:3306/users";
        String username = "root";
        String passwd = "";

        try {
            conn = DriverManager.getConnection(url, username, passwd);

            return conn.createStatement();
        } catch (CommunicationsException e) {
            System.out.println("Error connecting to the database. Please check your connection and try again.");
            System.exit(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
