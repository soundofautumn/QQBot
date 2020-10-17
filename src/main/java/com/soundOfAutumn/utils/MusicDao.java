package com.soundOfAutumn.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author SoundOfAutumn
 * @date 2020/10/17 23:00
 */
public class MusicDao {
    private MusicDao() {

    }

    public static Map<Long, JSONObject> getMusicLikes() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<Long, JSONObject> result = new HashMap<>();
        try {
            conn = DataBaseUtils.getConnection();
            String sql = "SELECT id, likes FROM music";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                long id = rs.getLong(1);
                String music = rs.getString(2);
                JSONObject jsonObject = JSON.parseObject(music);
                result.put(id, jsonObject);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseUtils.close(conn, ps, rs);
        }
        return result;
    }

    public static void updateMusicLikes(JSONObject jsonObject, long musicId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DataBaseUtils.getConnection();
            String sql="UPDATE music SET likes = ? WHERE id = ?";
            ps = conn.prepareStatement(sql);

            ps.setString(1, jsonObject.toJSONString());
            ps.setLong(2, musicId);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
