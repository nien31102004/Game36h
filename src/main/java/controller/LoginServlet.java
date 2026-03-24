package controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import service.UserService;
import model.User;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserService userService = new UserService();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try {
            User user = userService.login(username, password);

            if (user != null) {
                req.getSession().setAttribute("user", user);
                resp.getWriter().write("Login success");
            } else {
                resp.getWriter().write("Login failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}