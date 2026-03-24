package dao;

import model.User;
import utils.DBConnection;

import java.sql.*;

public class UserDAO {

    public User findByUsername(String username) throws Exception {
        Connection conn = DBConnection.getConnection();

        String sql = "SELECT * FROM users WHERE username=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            return user;
        }

        return null;
    }
}