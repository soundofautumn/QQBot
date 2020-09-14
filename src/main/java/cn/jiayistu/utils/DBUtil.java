package cn.jiayistu.utils;

import java.sql.*;
import java.util.ResourceBundle;

public final class DBUtil {
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("database");


    //获取连接数据库所需要的信息
    private static final String DB_URL = resourceBundle.getString("DB_URL");
    private static final String JDBC_DRIVER = resourceBundle.getString("JDBC_DRIVER");
    private static final String USER = resourceBundle.getString("USER");
    private static final String PASSWORD = resourceBundle.getString("PASSWORD");

    static {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private DBUtil() {

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
