package util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseSetup {
    public static void main(String[] args) {
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement()) {

            // Enable foreign key support
            st.execute("PRAGMA foreign_keys = ON;");

            // Users table (login system)
            st.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT UNIQUE NOT NULL, " +
                    "password TEXT NOT NULL, " +
                    "role TEXT NOT NULL" +
                    ");");

            // Members table (profile info)
            st.execute("CREATE TABLE IF NOT EXISTS members (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id INTEGER UNIQUE, " +
                    "name TEXT NOT NULL, " +
                    "email TEXT, " +
                    "phone TEXT, " +
                    "join_date TEXT, " +
                    "FOREIGN KEY(user_id) REFERENCES users(id)" +
                    ");");

            // Attendance table
            st.execute("CREATE TABLE IF NOT EXISTS attendance (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "member_id INTEGER, " +
                    "date TEXT, " +
                    "status TEXT, " +
                    "FOREIGN KEY(member_id) REFERENCES members(id)" +
                    ");");

            // Payments table
            st.execute("CREATE TABLE IF NOT EXISTS payments (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "member_id INTEGER, " +
                    "amount INTEGER, " +
                    "paid_date TEXT, " +
                    "FOREIGN KEY(member_id) REFERENCES members(id)" +
                    ");");

            // Workout table
            st.execute("CREATE TABLE IF NOT EXISTS workout (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "member_id INTEGER, " +
                    "exercise TEXT, " +
                    "sets INTEGER, " +
                    "reps INTEGER, " +
                    "day TEXT, " +
                    "duration INTEGER, " +
                    "FOREIGN KEY(member_id) REFERENCES members(id)" +
                    ");");

            // Diet table
            st.execute("CREATE TABLE IF NOT EXISTS diet (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "member_id INTEGER, " +
                    "meal TEXT, " +
                    "food TEXT, " +
                    "calories INTEGER, " +
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

            // Insert default manager
            st.execute("INSERT OR IGNORE INTO users(username,password,role) VALUES('admin','1234','manager');");

            // Insert sample users and members
            st.execute("INSERT OR IGNORE INTO users(id,username,password,role) VALUES(2,'john','123','user');");
            st.execute("INSERT OR IGNORE INTO members(user_id,name,email,phone,join_date) " +
                    "VALUES(2,'John Doe','john@example.com','0123456789','2026-01-01');");

            st.execute("INSERT OR IGNORE INTO users(id,username,password,role) VALUES(3,'emma','456','user');");
            st.execute("INSERT OR IGNORE INTO members(user_id,name,email,phone,join_date) " +
                    "VALUES(3,'Emma Smith','emma@example.com','0987654321','2026-01-01');");

            System.out.println("✅ Database created and sample data added successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

