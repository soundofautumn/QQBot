package com.soundOfAutumn.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author SoundOfAutumn
 */
public class AccountUtils {

    private AccountUtils() {

    }

    /**
     * 判断一个账号是否绑定
     * @param qq QQ号码
     * @return 是否绑定
     */
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

    /**
     * 将QQ号码转为用户的Id
     * @param qq QQ号码
     * @return 用户Id
     */
    public synchronized static long qq2UserId(long qq) {

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
