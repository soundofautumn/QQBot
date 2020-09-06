package cn.jiayistu.bot.event;

import cn.jiayistu.database.DBHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataBase {
    public static String Query(String msgString) {
        //判断是否是8位数字(是否是学号)
        String pattern = "^\\d{8}$";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(msgString);
        //判断是否符合
        if (m.matches()) {
            //如果符合则转化为long类型
            long username = Long.parseLong(msgString);
            //生成sql语句
            String sql = "SELECT username,grade FROM users WHERE username= " + username;
            //连接数据库
            DBHelper db = null;
            ResultSet rs = null;
            //查询数据库
            try {
                db = new DBHelper(sql);
                rs = db.pst.executeQuery();
                rs.next();
                return rs.getString("grade");
            } catch (SQLException se) {
                se.printStackTrace();
                return "出现未知错误,数据库查询已停止";
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException se) {
                        se.printStackTrace();
                    }
                }
                if (db != null) {
                    try {
                        db.close();
                    } catch (SQLException se2) {
                        se2.printStackTrace();
                    }
                }
            }

        } else {
            return "学号输入有误,数据库查询已停止";
        }
    }

}
