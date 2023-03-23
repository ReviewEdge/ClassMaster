package edu.gcc.comp350.frg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    public static Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:C:\\Users\\RICHARDSONLG20\\IdeaProjects\\ClassMaster\\src\\main\\java\\edu\\gcc\\comp350\\frg\\db.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}
