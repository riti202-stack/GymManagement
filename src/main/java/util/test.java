package util;
import java.sql.Connection;

public class test {
    public static void main(String[] args) {
        Connection con = DBConnection.getConnection();
        System.out.println(con != null ? "SQLite Connected ✅" : "Failed ❌");
    }
}

