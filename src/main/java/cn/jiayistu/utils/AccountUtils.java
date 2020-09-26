package cn.jiayistu.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author QJMing
 */
public class AccountUtils {

    private AccountUtils() {

    }
    public synchronized static boolean isBind(long qq) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DataBaseUtils.getConnection();

            String sql = "SELECT qq FROM users WHERE qq = ? ";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, qq);
            rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DataBaseUtils.close(conn, ps, rs);
        }
    }

    public synchronized static long qqToUserId(long qq) {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DataBaseUtils.getConnection();
            String sql = "SELECT id FROM users WHERE qq = ?";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, qq);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DataBaseUtils.close(conn, ps, rs);
        }

        return -1L;
    }

}
