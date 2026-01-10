package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    // Use the same file as DatabaseSetup
    private static final String URL = "jdbc:sqlite:database/gym.db";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
