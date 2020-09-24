package cn.jiayistu.bot.event;

import cn.jiayistu.utils.DataBaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Formatter;
import java.util.Locale;

/**
 * @author QJMing
 */
public class MusicList {

    public synchronized static String printBrief() {
        StringBuffer sb = new StringBuffer();
        Formatter formatter = new Formatter(sb, Locale.CHINA);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DataBaseUtils.getConnection();
            String sql = "SELECT id, music_name, music_singer FROM music";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                formatter.format("%s.歌曲名称:%s 歌手名称:%s\n", rs.getString(1), rs.getString(2), rs.getString(3));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseUtils.close(conn, ps, rs);
        }

        return sb.toString();
    }


}
