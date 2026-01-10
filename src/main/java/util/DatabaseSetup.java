package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseSetup {

    public static void main(String[] args) {
        String dbPath = System.getProperty("user.dir") + "/database/gym.db";
        System.out.println("Database will be created at: " + dbPath);

        try (Connection con = DriverManager.getConnection("jdbc:sqlite:"+dbPath);
             Statement st = con.createStatement()) {

            // Enable foreign key support
            st.execute("PRAGMA foreign_keys = ON;");

            // ---------------- Users table ----------------
            st.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT UNIQUE NOT NULL, " +
                    "password TEXT NOT NULL, " +
                    "role TEXT NOT NULL" +
                    ");");

            // ---------------- Members table ----------------
            st.execute("CREATE TABLE IF NOT EXISTS members (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id INTEGER UNIQUE, " +
                    "name TEXT NOT NULL, " +
                    "email TEXT, " +
                    "phone TEXT, " +
                    "join_date TEXT, " +
                    "FOREIGN KEY(user_id) REFERENCES users(id)" +
                    ");");

            // ---------------- Payments table ----------------
            st.execute("CREATE TABLE IF NOT EXISTS payments (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "member_id INTEGER, " +
                    "amount REAL, " +
                    "paid_date TEXT, " +
                    "FOREIGN KEY(member_id) REFERENCES members(id)" +
                    ");");

            // ---------------- Attendance table ----------------
            st.execute("CREATE TABLE IF NOT EXISTS attendance (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "member_id INTEGER, " +
                    "date TEXT, " +
                    "status TEXT, " +
                    "FOREIGN KEY(member_id) REFERENCES members(id)" +
                    ");");

            // ---------------- Workout table ----------------
            st.execute("CREATE TABLE IF NOT EXISTS workout (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "member_id INTEGER, " +
                    "exercise TEXT, " +
                    "reps INTEGER, " +
                    "day TEXT, " +
                    "FOREIGN KEY(member_id) REFERENCES members(id)" +
                    ");");

            // ---------------- Diet table ----------------
            st.execute("CREATE TABLE IF NOT EXISTS diet (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "member_id INTEGER, " +
                    "meal TEXT, " +
                    "calories INTEGER, " +
                    "date TEXT, " +
                    "FOREIGN KEY(member_id) REFERENCES members(id)" +
                    ");");

            // ---------------- Weight table ----------------
            st.execute("CREATE TABLE IF NOT EXISTS weight (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "member_id INTEGER, " +
                    "weight REAL, " +
                    "record_date TEXT, " +
                    "FOREIGN KEY(member_id) REFERENCES members(id)" +
                    ");");

            // ---------------- Insert default users and members ----------------
            st.execute("INSERT OR IGNORE INTO users(id, username, password, role) VALUES" +
                    "(1, 'admin', '1234', 'manager')," +
                    "(2, 'john', '123', 'user')," +
                    "(3, 'emma', '456', 'user');");

            st.execute("INSERT OR IGNORE INTO members(user_id, name, email, phone, join_date) VALUES" +
                    "(2, 'John Doe', 'john@example.com', '0123456789', '2026-01-01')," +
                    "(3, 'Emma Smith', 'emma@example.com', '0987654321', '2026-01-01');");

            // ---------------- Insert sample payments ----------------
            st.execute("INSERT OR IGNORE INTO payments(member_id, amount, paid_date) VALUES" +
                    "(1, 500, '2026-01-01')," +
                    "(1, 600, '2026-01-10')," +
                    "(2, 400, '2026-01-05');");

            // ---------------- Insert sample attendance ----------------
            st.execute("INSERT OR IGNORE INTO attendance(member_id, date, status) VALUES" +
                    "(1, '2026-01-01', 'Present')," +
                    "(1, '2026-01-02', 'Absent')," +
                    "(2, '2026-01-01', 'Present');");

            // ---------------- Insert sample workouts ----------------
            st.execute("INSERT OR IGNORE INTO workout(member_id, exercise, reps, day) VALUES" +
                    "(1, 'Push Ups', 20, '2026-01-01')," +
                    "(1, 'Squats', 15, '2026-01-02')," +
                    "(2, 'Sit Ups', 30, '2026-01-01');");

            // ---------------- Insert sample diet ----------------
            st.execute("INSERT OR IGNORE INTO diet(member_id, meal, calories, date) VALUES" +
                    "(1, 'Breakfast', 500, '2026-01-01')," +
                    "(1, 'Lunch', 700, '2026-01-01')," +
                    "(2, 'Dinner', 600, '2026-01-01');");

            // ---------------- Insert sample weight ----------------
            st.execute("INSERT OR IGNORE INTO weight(member_id, weight, record_date) VALUES" +
                    "(1, 70.5, '2026-01-01')," +
                    "(1, 71.0, '2026-01-10')," +
                    "(2, 65.0, '2026-01-01');");

            System.out.println("✅ Database created and sample data added successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
