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

@WebServlet(name = "LoginServlet", urlPatterns = "/auth/login")
public class LoginServlet extends HttpServlet {
    @Override
    public void init() {
        DBUtil.init(getServletContext());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String key = req.getParameter("login");
        String pass = req.getParameter("password");
        UserDAO dao = new UserDAO();
        try {
            User u = dao.findByEmailOrUsername(key);
            if (u != null) {
                String hash = dao.getPasswordHashByUserId(u.getId());
                if (hash != null && PasswordUtil.verify(pass, hash)) {
                    req.getSession().setAttribute("user", u);
                    resp.sendRedirect(req.getContextPath() + "/products");
                    return;
                }
            }
            req.setAttribute("error", "Invalid credentials");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}