package controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import utils.DBConnection;

import java.io.IOException;
import java.sql.Connection;

@WebServlet("/db-test")
public class DBTestServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        try {
            Connection conn = DBConnection.getConnection();

            if (conn != null) {
                resp.getWriter().write("DB Connected!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("DB Error!");
        }
    }
}