package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSetup {

    public static void main(String[] args) {
        String dbPath = System.getProperty("user.dir") + "/database/gym.db";
        System.out.println("🧹 Cleaning database at: " + dbPath);

        try (Connection con = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement st = con.createStatement()) {

            // Enable foreign key support
            st.execute("PRAGMA foreign_keys = ON;");

            // 🔥 ONE-TIME CLEANUP: Delete ALL sample data first
            cleanupSampleData(st);

            // ✅ Create tables (safe - IF NOT EXISTS)
            createAllTables(st);

            // ✅ Insert ONLY admin (real users created by app)
            insertAdminOnly(st);

            System.out.println("🎉 CLEAN DATABASE READY!");
            System.out.println("✅ Admin login: admin/1234");
            System.out.println("✅ Real users SAFE forever!");
            System.out.println("✅ Dashboard: 0 members, $0");

        } catch (Exception e) {
            System.err.println("❌ Database setup failed:");
            e.printStackTrace();
        }
    }

    // 🔥 ONE-TIME: Delete ALL sample data
    private static void cleanupSampleData(Statement st) throws SQLException {
        System.out.println("🗑️ Deleting ALL sample data...");

        // Delete in correct order (children first)
        st.execute("DELETE FROM weight;");
        st.execute("DELETE FROM diet;");
        st.execute("DELETE FROM workout;");
        st.execute("DELETE FROM attendance;");
        st.execute("DELETE FROM payments;");
        st.execute("DELETE FROM members;");
        st.execute("DELETE FROM users;");
        st.execute("DELETE FROM sqlite_sequence;"); // Reset auto-increment IDs

        System.out.println("✅ Sample data completely removed!");
    }

    // ✅ Create all tables (safe with IF NOT EXISTS)
    private static void createAllTables(Statement st) throws SQLException {
        System.out.println("📋 Creating tables...");

        // Users table
        st.execute("CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE NOT NULL, " +
                "password TEXT NOT NULL, " +
                "role TEXT NOT NULL" +
                ");");

        // Members table
        st.execute("CREATE TABLE IF NOT EXISTS members (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER UNIQUE, " +
                "name TEXT NOT NULL, " +
                "email TEXT, " +
                "phone TEXT, " +
                "join_date TEXT, " +
                "FOREIGN KEY(user_id) REFERENCES users(id)" +
                ");");

        // Payments table
        st.execute("CREATE TABLE IF NOT EXISTS payments (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "member_id INTEGER, " +
                "amount REAL, " +
                "paid_date TEXT, " +
                "FOREIGN KEY(member_id) REFERENCES members(id)" +
                ");");

        // Attendance table
        st.execute("CREATE TABLE IF NOT EXISTS attendance (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "member_id INTEGER, " +
                "date TEXT, " +
                "status TEXT, " +
                "FOREIGN KEY(member_id) REFERENCES members(id)" +
                ");");

        // Workout table
        st.execute("CREATE TABLE IF NOT EXISTS workout (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "member_id INTEGER, " +
                "exercise TEXT, " +
                "reps INTEGER, " +
                "day TEXT, " +
                "FOREIGN KEY(member_id) REFERENCES members(id)" +
                ");");

        // Diet table
        st.execute("CREATE TABLE IF NOT EXISTS diet (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "member_id INTEGER, " +
                "meal TEXT, " +
                "calories INTEGER, " +
                "date TEXT, " +
                "FOREIGN KEY(member_id) REFERENCES members(id)" +
                ");");

        // Weight table
        st.execute("CREATE TABLE IF NOT EXISTS weight (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "member_id INTEGER, " +
                "weight REAL, " +
                "record_date TEXT, " +
                "FOREIGN KEY(member_id) REFERENCES members(id)" +
                ");");
    }

    // ✅ Insert ONLY admin user
    private static void insertAdminOnly(Statement st) throws SQLException {
        System.out.println("👑 Inserting admin user...");
        st.execute("INSERT OR IGNORE INTO users(id, username, password, role) VALUES(1, 'admin', '1234', 'manager');");
        System.out.println("✅ Admin created: admin/1234");
    }
}
