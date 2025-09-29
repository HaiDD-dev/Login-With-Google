package com.example.app.web;

import com.example.app.config.DBUtil;
import com.example.app.dao.UserDAO;
import com.example.app.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Properties;

@WebServlet(name = "GoogleCallbackServlet", urlPatterns = "/auth/google/callback")
public class GoogleCallbackServlet extends HttpServlet {
    private String clientId, clientSecret, redirectUri;

    @Override
    public void init() throws ServletException {
        DBUtil.init(getServletContext());
        try {
            Properties p = new Properties();
            p.load(getServletContext().getResourceAsStream("/WEB-INF/classes/auth.properties"));
            clientId = p.getProperty("GOOGLE_CLIENT_ID");
            clientSecret = p.getProperty("GOOGLE_CLIENT_SECRET");
            redirectUri = p.getProperty("REDIRECT_URI");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String state = req.getParameter("state");
        String expected = (String) req.getSession().getAttribute("OAUTH_STATE");
        if (expected == null || !expected.equals(state)) {
            resp.sendError(400, "Invalid state");
            return;
        }
        String code = req.getParameter("code");
        try {
            // 1) Exchange code for token
            HttpClient client = HttpClient.newHttpClient();
            String body = "code=" + enc(code) +
                    "&client_id=" + enc(clientId) +
                    "&client_secret=" + enc(clientSecret) +
                    "&redirect_uri=" + enc(redirectUri) +
                    "&grant_type=authorization_code";
            HttpRequest tokenReq = HttpRequest.newBuilder(URI.create("https://oauth2.googleapis.com/token")).header("Content-Type", "application/x-www-form-urlencoded").POST(HttpRequest.BodyPublishers.ofString(body)).build();
            HttpResponse<String> tokenRes = client.send(tokenReq, HttpResponse.BodyHandlers.ofString());
            JSONObject tok = new JSONObject(tokenRes.body());
            String accessToken = tok.getString("access_token");

            // 2) Get userinfo
            HttpRequest uiReq = HttpRequest.newBuilder(URI.create("https://www.googleapis.com/oauth2/v3/userinfo")).header("Authorization", "Bearer " + accessToken).GET().build();
            HttpResponse<String> uiRes = client.send(uiReq, HttpResponse.BodyHandlers.ofString());
            JSONObject userinfo = new JSONObject(uiRes.body());
            String sub = userinfo.getString("sub");
            String email = userinfo.optString("email", null);
            String name = userinfo.optString("name", "");

            UserDAO dao = new UserDAO();
            int userId = dao.upsertGoogleAccount(email, name, sub, accessToken);
            User u = dao.getById(userId);
            req.getSession().setAttribute("user", u);
            resp.sendRedirect(req.getContextPath() + "/products");
        } catch (SQLException | InterruptedException e) {
            throw new ServletException(e);
        }
    }

    private static String enc(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
}