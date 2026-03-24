package utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() throws Exception {

        String url = "jdbc:mysql://localhost:3306/gameweb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String user = "root";
        String password = "nien20043110";

        Class.forName("com.mysql.cj.jdbc.Driver");

        return DriverManager.getConnection(url, user, password);
    }
}