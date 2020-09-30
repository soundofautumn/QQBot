package com.soundOfAutumn.utils;

import java.sql.*;
import java.util.ResourceBundle;

/**
 * @author SoundOfAutumn
 */
public final class DataBaseUtils {
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("database");


    /**
     * 数据库的URL
     */
    private static final String DB_URL = RESOURCE_BUNDLE.getString("DB_URL");
    /**
     * 数据库的驱动
     */
    private static final String JDBC_DRIVER = RESOURCE_BUNDLE.getString("JDBC_DRIVER");
    /**
     * 数据库的用户名
     */
    private static final String USER = RESOURCE_BUNDLE.getString("USER");
    /**
     * 数据库的密码
     */
    private static final String PASSWORD = RESOURCE_BUNDLE.getString("PASSWORD");

    static {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private DataBaseUtils() {

    }

    /**
     * 获取数据库连接对象
     * @return 连接对象
     * @throws SQLException 数据库连接异常
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

    /**
     * 关闭资源
     * @param conn 连接对象
     * @param ps 数据库操作对象
     * @param rs 结果集
     */
    public static void close(Connection conn, Statement ps, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static void close(Connection conn, Statement ps) {

        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
