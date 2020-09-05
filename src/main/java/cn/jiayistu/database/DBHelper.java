package cn.jiayistu.database;

import cn.jiayistu.configuration.ReadProperties;

import java.sql.*;

/**
 *帮助连接数据库
 */
public class DBHelper {

    //获取连接数据库所需要的信息
    public static final String url = ReadProperties.getValue("DB_URL","database.properties");
    public static final String name = ReadProperties.getValue("JDBC_DRIVER","database.properties");
    public static final String user =ReadProperties.getValue("USER", "database.properties");
    public static final String password = ReadProperties.getValue("PASS", "database.properties");

    public Connection conn = null;
    public PreparedStatement pst = null;

    /***
     * 构造方法
     * @param sql 要执行的sql语句
     */
    public DBHelper(String sql) {
        try {
            Class.forName(name);//指定连接类型
            conn = DriverManager.getConnection(url, user, password);//获取连接
            pst = conn.prepareStatement(sql);//准备执行语句
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭连接
     * @throws SQLException 抛出SQL异常
     */
    public void close() throws SQLException{
            this.conn.close();
            this.pst.close();

    }
}
