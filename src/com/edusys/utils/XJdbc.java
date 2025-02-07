package com.edusys.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class XJdbc {
    private static String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static String dburl = "jdbc:sqlserver://localhost;databaseName=EduSys;encrypt=true;trustServerCertificate=true";

    private static String username = "SA";
    private static String password = "Aa@123456";

    // Nạp driver
    static {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Không tìm thấy driver JDBC", ex);
        }
    }

    // Xây dựng PreparedStatement
    public static PreparedStatement getStmt(String sql, Object... args) throws SQLException {
        Connection connection = DriverManager.getConnection(dburl, username, password);
        PreparedStatement pstmt = null;
        if (sql.trim().startsWith("{")) {
            pstmt = connection.prepareCall(sql);
        } else {
            pstmt = connection.prepareStatement(sql);
        }
        for (int i = 0; i < args.length; i++) {
            pstmt.setObject(i + 1, args[i]);
        }
        return pstmt;
    }

    // Thực hiện câu lệnh SQL thao tác (INSERT, UPDATE, DELETE)
    public static void update(String sql, Object... args) {
        try (Connection conn = DriverManager.getConnection(dburl, username, password);
             PreparedStatement stmt = getStmt(sql, args)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi thực thi câu lệnh SQL: " + sql, e);
        }
    }

    // Thực hiện câu lệnh SQL truy vấn (SELECT)
   public static ResultSet query(String sql, Object... args) {
    try {
        PreparedStatement stmt = getStmt(sql, args);
        return stmt.executeQuery();
    } catch (SQLException e) {
        System.out.println("Lỗi khi thực thi câu lệnh SQL: " + sql);
        e.printStackTrace();
        throw new RuntimeException(e);
    }
}


    // Lấy giá trị từ câu lệnh SELECT
    public static Object value(String sql, Object... args) {
        try (ResultSet rs = query(sql, args)) {
            if (rs.next()) {
                return rs.getObject(1); // Lấy giá trị từ cột đầu tiên
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy giá trị từ câu lệnh SQL: " + sql, e);
        }
        return null;
    }
}
