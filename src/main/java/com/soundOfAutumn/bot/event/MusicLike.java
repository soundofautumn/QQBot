package com.soundOfAutumn.bot.event;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.soundOfAutumn.utils.AccountUtils;
import com.soundOfAutumn.utils.MusicDao;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.util.Map;

/**
 * @author SoundOfAutumn
 */
public class MusicLike {
    public synchronized static MessageChain giveLike(long musicId, long qq) {
        int userId = AccountUtils.qq2UserId(qq);

        MessageChainBuilder mcb = new MessageChainBuilder();
        Map<Long, JSONObject> musicList = MusicDao.getMusicLikes();
        if (!musicList.containsKey(musicId)) {
            return mcb.append("未找到此歌曲").asMessageChain();
        }
        JSONObject jsonObject = musicList.get(musicId);
        JSONArray likeUsers = jsonObject.getJSONArray("like_users");

            if (likeUsers.contains(userId)) {
                mcb.append("您已点赞过这首歌了");
            } else {
                likeUsers.add(userId);
                jsonObject.replace("like_users", likeUsers);
                MusicDao.updateMusicLikes(jsonObject, musicId);
                mcb.append("点赞成功");
            }


        return mcb.asMessageChain();
    }
}
