package com.example.app.config;

import jakarta.servlet.ServletContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static String url;
    private static String user;
    private static String pass;

    public static void init(ServletContext ctx) {
        url = ctx.getInitParameter("DB_URL");
        user = ctx.getInitParameter("DB_USER");
        pass = ctx.getInitParameter("DB_PASSWORD");
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }
}