package util;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ViewDatabase {

    public static void main(String[] args) {
        String dbPath = System.getProperty("user.dir") + "/database/gym.db";

        try (Connection con = DriverManager.getConnection("jdbc:sqlite:" + dbPath)) {

            System.out.println("✅ Connected to gym.db successfully!");

            String[] tables = {"users", "members", "payments", "attendance", "workout", "diet", "weight"};

            for (String table : tables) {
                System.out.println("\n--- " + table.toUpperCase() + " ---");

                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM " + table + ";");

                int columnCount = rs.getMetaData().getColumnCount();

                while (rs.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        System.out.print(rs.getString(i) + " | ");
                    }
                    System.out.println();
                }

                rs.close();
                st.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

