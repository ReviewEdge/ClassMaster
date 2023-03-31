package edu.gcc.comp350.frg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    public static Connection connect() {
        // SQLite connection string
        String dbFilePath = "src\\main\\java\\edu\\gcc\\comp350\\frg\\db.db";
//        System.out.println("[DatabaseConnector] EVERYONE NEEDS TO GET THEIR DB FILE'S ABSOLUTE PATH AND PUT IT HERE"); //delete me in the future
//        System.out.println("It should look something like this: " + dbFilePath);

        String url = "jdbc:sqlite:" + dbFilePath;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}
