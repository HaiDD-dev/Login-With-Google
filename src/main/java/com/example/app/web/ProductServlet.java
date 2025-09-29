package com.example.app.web;

import com.example.app.config.DBUtil;
import com.example.app.dao.ProductDAO;
import com.example.app.model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ProductServlet", urlPatterns = {"/products"})
public class ProductServlet extends HttpServlet {
    @Override
    public void init() {
        DBUtil.init(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("new".equals(action)) {
                req.getRequestDispatcher("/product-form.jsp").forward(req, resp);
                return;
            }
            if ("edit".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                Product p = new ProductDAO().findById(id);
                req.setAttribute("product", p);
                req.getRequestDispatcher("/product-form.jsp").forward(req, resp);
                return;
            }

            List<Product> list = new ProductDAO().findAll();
            req.setAttribute("items", list);
            req.getRequestDispatcher("/products.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        ProductDAO dao = new ProductDAO();
        try {
            if ("create".equals(action)) {
                Product p = parse(req);
                dao.insert(p);
                resp.sendRedirect(req.getContextPath() + "/products");
                return;
            } else if ("update".equals(action)) {
                Product p = parse(req);
                p.setId(Integer.parseInt(req.getParameter("id")));
                dao.update(p);
                resp.sendRedirect(req.getContextPath() + "/products");
                return;
            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                dao.delete(id);
                resp.sendRedirect(req.getContextPath() + "/products");
                return;
            }
            resp.sendError(400);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private Product parse(HttpServletRequest req) {
        Product p = new Product();
        p.setName(req.getParameter("name"));
        p.setDescription(req.getParameter("description"));
        p.setPrice(new BigDecimal(req.getParameter("price")));
        p.setStock(Integer.parseInt(req.getParameter("stock")));
        return p;
    }
}