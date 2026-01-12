package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    private static final String URL = "jdbc:sqlite:" + System.getProperty("user.dir") + "/database/gym.db";

    public static Connection getConnection() {
        try {
            Connection con = DriverManager.getConnection(URL);

            // ✅ CRITICAL FIXES: Enable WAL + Foreign Keys + Auto-commit
            Statement stmt = con.createStatement();
            stmt.execute("PRAGMA journal_mode = WAL;");
            stmt.execute("PRAGMA synchronous = NORMAL;");
            stmt.execute("PRAGMA foreign_keys = ON;");
            stmt.execute("PRAGMA cache_size = 10000;");
            stmt.close();

            con.setAutoCommit(true); // Force commits
            return con;
        } catch (Exception e) {
            System.err.println("❌ DB Connection failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
