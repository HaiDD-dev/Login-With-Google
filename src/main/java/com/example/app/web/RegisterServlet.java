package com.example.app.web;

import com.example.app.config.DBUtil;
import com.example.app.dao.UserDAO;
import com.example.app.model.User;
import com.example.app.security.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "RegisterServlet", urlPatterns = "/auth/register")
public class RegisterServlet extends HttpServlet {
    @Override
    public void init() {
        DBUtil.init(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = trim(req.getParameter("email"));
        String username = trim(req.getParameter("username"));
        String fullName = trim(req.getParameter("fullName"));
        String password = req.getParameter("password");
        String confirm = req.getParameter("confirm");

        List<String> errors = new ArrayList<>();
        if (email == null || !email.contains("@")) errors.add("Invalid email");
        if (username == null || username.length() < 3) errors.add("Username least 3 characters");
        if (password == null || password.length() < 8) errors.add("Password least 8 characters");
        if (confirm == null || !confirm.equals(password)) errors.add("Password confirmation does not match");

        UserDAO dao = new UserDAO();
        try {
            if (email != null && dao.findByEmail(email) != null) errors.add("Email has been registered");
            if (username != null && dao.findByUsername(username) != null) errors.add("Username has been registered");
            if (!errors.isEmpty()) {
                req.setAttribute("errors", errors);
                req.setAttribute("prefill_email", email);
                req.setAttribute("prefill_username", username);
                req.setAttribute("prefill_fullName", fullName);
                req.getRequestDispatcher("/register.jsp").forward(req, resp);
                return;
            }
            String hash = PasswordUtil.hash(password);
            User u = dao.createLocal(email, username, fullName, hash);
            req.getSession().setAttribute("user", u);
            resp.sendRedirect(req.getContextPath() + "/profile");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private static String trim(String s) {
        return s == null ? null : s.trim();
    }
}