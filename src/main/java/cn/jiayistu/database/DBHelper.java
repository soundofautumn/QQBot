package cn.jiayistu.database;

import cn.jiayistu.conf.Configuration;

import java.sql.*;

public class DBHelper {
    public static final String url = Configuration.readConfig("DB_URL");
    public static final String name = Configuration.readConfig("JDBC_DRIVER");
    public static final String user = Configuration.readConfig("USER", "database.properties");
    public static final String password = Configuration.readConfig("PASS", "database.properties");

    public Connection conn = null;
    public PreparedStatement pst = null;

    public DBHelper(String sql) {
        try {
            Class.forName(name);//指定连接类型
            conn = DriverManager.getConnection(url, user, password);//获取连接
            pst = conn.prepareStatement(sql);//准备执行语句
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            this.conn.close();
            this.pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
