package com.soundOfAutumn.bot.event;

import com.soundOfAutumn.utils.AccountUtils;
import com.soundOfAutumn.utils.DataBaseUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author SoundOfAutumn
 */
public class MusicLike {

    private synchronized static boolean isLiked(String musicId, long userId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DataBaseUtils.getConnection();
            String sql = "SELECT likes FROM music WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, musicId);

            rs = ps.executeQuery();

            if (rs.next()) {
                JSONObject jsonObject = JSON.parseObject(rs.getString(1));
                JSONArray likeUsers = jsonObject.getJSONArray("like_users");
                for (Object likeUser :
                        likeUsers) {
                    long likeUserId = Long.parseLong(likeUser.toString());
                    if (likeUserId == userId) {
                        return true;
                    }

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseUtils.close(conn, ps, rs);
        }

        return false;
    }

    private synchronized static void newLike(String musicId, long userId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DataBaseUtils.getConnection();
            String sql = "SELECT likes FROM music WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, musicId);

            rs = ps.executeQuery();
            JSONObject jsonObject = null;
            if (rs.next()) {
                jsonObject = JSON.parseObject(rs.getString(1));
                JSONArray usersList = jsonObject.getJSONArray("like_users");
                if (!usersList.contains(userId)) {
                    usersList.add(userId);
                    jsonObject.put("total", jsonObject.getInteger("total") + 1);
                }
                jsonObject.put("like_users", usersList);
            }

            sql = "UPDATE music SET likes = ? WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, jsonObject.toJSONString());
            ps.setString(2, musicId);

            ps.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseUtils.close(conn, ps, rs);
        }
    }


    public synchronized static MessageChain giveLike(String musicId, long qq) {
        long userId = AccountUtils.qqToUserId(qq);

        MessageChainBuilder mcb = new MessageChainBuilder();
        if (isLiked(musicId, userId)) {
            mcb.append("您已点赞过这首歌了");
        } else {
            newLike(musicId, userId);
            mcb.append("点赞成功");
        }


        return mcb.asMessageChain();
    }

}
