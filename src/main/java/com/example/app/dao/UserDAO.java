package com.example.app.dao;

import com.example.app.config.DBUtil;
import com.example.app.model.User;

import java.sql.*;

public class UserDAO {
    public User findByEmailOrUsername(String key) throws SQLException {
        String sql = "SELECT id,email,username,full_name,role,is_verified " +
                "FROM users WHERE email = ? OR username = ?";
        try (Connection cn = DBUtil.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, key);
            ps.setString(2, key);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public String getPasswordHashByUserId(int id) throws SQLException {
        String sql = "SELECT password_hash FROM users WHERE id=?";
        try (Connection cn = DBUtil.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        }
    }

    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT id,email,username,full_name,role,is_verified FROM users WHERE email = ?";
        try (Connection cn = DBUtil.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT id,email,username,full_name,role,is_verified FROM users WHERE username=?";
        try (Connection cn = DBUtil.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public User createLocal(String email, String username, String fullName, String passwordHash) throws SQLException {
        String sql = "INSERT INTO users(email,username,full_name,password_hash,is_verified) " +
                "VALUES(?,?,?,?,1); SELECT SCOPE_IDENTITY();";
        try (Connection cn = DBUtil.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            if (email != null) ps.setString(1, email);
            else ps.setNull(1, Types.VARCHAR);
            if (username != null) ps.setString(2, username);
            else ps.setNull(2, Types.VARCHAR);
            ps.setString(3, fullName);
            ps.setString(4, passwordHash);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getBigDecimal(1).intValue();
                    return getById(id);
                }
            }
        }
        return null;
    }

    public User getById(int id) throws SQLException {
        String sql = "SELECT id,email,username,full_name,role,is_verified FROM users WHERE id=?";
        try (Connection cn = DBUtil.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public int upsertGoogleAccount(String email, String fullName, String providerAccountId, String accessToken)
            throws SQLException {
        try (Connection cn = DBUtil.getConnection()) {
            cn.setAutoCommit(false);
            try {
                Integer userId = null;

                try (PreparedStatement ps = cn.prepareStatement("SELECT user_id FROM oauth_accounts " +
                        "WHERE provider=? AND provider_account_id=?")) {
                    ps.setString(1, "google");
                    ps.setString(2, providerAccountId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) userId = rs.getInt(1);
                    }
                }

                if (userId == null && email != null) {
                    try (PreparedStatement ps = cn.prepareStatement("SELECT id FROM users WHERE email=?")) {
                        ps.setString(1, email);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) userId = rs.getInt(1);
                        }
                    }
                }

                if (userId == null) {
                    try (PreparedStatement ps = cn.prepareStatement("INSERT INTO users(email, username, full_name, is_verified) " +
                            "VALUES (?,?,?,1); SELECT SCOPE_IDENTITY();")) {
                        if (email != null) ps.setString(1, email);
                        else ps.setNull(1, Types.VARCHAR);
                        ps.setNull(2, Types.VARCHAR);
                        ps.setString(3, fullName);
                        try (ResultSet rs = ps.executeQuery()) {
                            rs.next();
                            userId = rs.getBigDecimal(1).intValue();
                        }
                    }
                }

                try (PreparedStatement ps = cn.prepareStatement("MERGE oauth_accounts AS t " +
                        "USING (SELECT ? AS provider, ? AS provider_account_id) AS s " +
                        "ON (t.provider = s.provider AND t.provider_account_id = s.provider_account_id) " +
                        "WHEN MATCHED THEN UPDATE SET t.user_id=?, t.access_token=?, t.token_expiry=DATEADD(minute,55,SYSDATETIME()) " +
                        "WHEN NOT MATCHED THEN INSERT(user_id, provider, provider_account_id, access_token, token_expiry) " +
                        "VALUES (?,?,?,?,DATEADD(minute,55,SYSDATETIME()));")) {
                    ps.setString(1, "google");
                    ps.setString(2, providerAccountId);
                    ps.setInt(3, userId);
                    ps.setString(4, accessToken);
                    ps.setInt(5, userId);
                    ps.setString(6, "google");
                    ps.setString(7, providerAccountId);
                    ps.setString(8, accessToken);
                    ps.executeUpdate();
                }

                cn.commit();
                return userId;
            } catch (Exception ex) {
                cn.rollback();
                throw ex;
            } finally {
                cn.setAutoCommit(true);
            }
        }
    }

    private User map(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setEmail(rs.getString("email"));
        u.setUsername(rs.getString("username"));
        u.setFullName(rs.getString("full_name"));
        u.setRole(rs.getString("role"));
        u.setVerified(rs.getBoolean("is_verified"));
        return u;
    }
}