package com.soundOfAutumn.bot.event;

import com.alibaba.fastjson.JSONObject;
import com.soundOfAutumn.utils.DataBaseUtils;
import net.mamoe.mirai.message.data.LightApp;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Formatter;
import java.util.Locale;

/**
 * @author SoundOfAutumn
 */
public class MusicList {

    public synchronized static MessageChain getMusicShare(long id) {
        MessageChainBuilder mcb = new MessageChainBuilder();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DataBaseUtils.getConnection();
            String sql = "SELECT music_share FROM music WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                mcb.append(new LightApp(rs.getString(1)));
            }


        } catch (SQLException e) {
            e.printStackTrace();
            return mcb.append("未查询到此歌曲").asMessageChain();

        } finally {
            DataBaseUtils.close(conn, ps, rs);
        }
        mcb.append("");


        return mcb.asMessageChain();
    }

    public synchronized static MessageChain getIntroduce(long musicId) {
        MessageChainBuilder mcb = new MessageChainBuilder();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DataBaseUtils.getConnection();
            String sql = "SELECT introduce FROM music WHERE id = ? ";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, musicId);
            rs = ps.executeQuery();

            if (rs.next()) {
                mcb.append(new PlainText(rs.getString(1)));
            }


        } catch (SQLException e) {
            e.printStackTrace();
            mcb.append("未查询到此歌曲");

        } finally {
            DataBaseUtils.close(conn, ps, rs);
        }

        return mcb.asMessageChain();
    }

    public synchronized static String getBrief() {
        StringBuffer sb = new StringBuffer();
        Formatter formatter = new Formatter(sb, Locale.CHINA);

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DataBaseUtils.getConnection();
            String sql = "SELECT id, music_name, music_singer, likes FROM music";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                formatter.format("%s.歌曲名称:%s 歌手名称:%s 点赞人数:%d\n",
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        JSONObject.parseObject(rs.getString(4)).getJSONArray("like_users").size()

                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseUtils.close(conn, ps, rs);
        }
        if ("".equals(sb.toString())) {
            return sb.append("未查询到歌曲").toString();
        }
        return sb.toString();
    }


}
