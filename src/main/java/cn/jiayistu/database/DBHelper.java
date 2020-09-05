package cn.jiayistu.database;


import cn.jiayistu.configuration.ReadProperties;

import java.sql.*;

public class DBHelper {
    public static final String url = ReadProperties.getValue("DB_URL","database.properties");
    public static final String name = ReadProperties.getValue("JDBC_DRIVER","database.properties");
    public static final String user =ReadProperties.getValue("USER", "database.properties");
    public static final String password = ReadProperties.getValue("PASS", "database.properties");

    public Connection conn = null;
    public PreparedStatement pst = null;

    public DBHelper(String sql) {
        try {
            Class.forName(name);//指定连接类型
            conn = DriverManager.getConnection(url, user, password);//获取连接
            pst = conn.prepareStatement(sql);//准备执行语句
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() throws SQLException{
            this.conn.close();
            this.pst.close();

    }
}
