package cn.jiayistu.bot.event;

import cn.jiayistu.utils.DataBaseUtils;
import com.alibaba.fastjson.JSONObject;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.message.MessageEvent;
import net.mamoe.mirai.message.data.LightApp;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class UpLoad extends SimpleListenerHost {
    /**
     * 储存音乐的分享链接
     */
    private JSONObject musicShare;
    /**
     * 储存对音乐的介绍
     */
    private String introduce;
    private final long QQId;

    public UpLoad(long id) {
        QQId = id;
    }


    @EventHandler(concurrency = Listener.ConcurrencyKind.LOCKED, priority = Listener.EventPriority.HIGH)
    public ListeningStatus update(MessageEvent updateEvent) {
        if (updateEvent.getSender().getId() != QQId) {
            return ListeningStatus.LISTENING;
        }

        //如果不是上传命令,则继续监听
        if (!"上传".equals(updateEvent.getMessage().contentToString())) {
            return ListeningStatus.LISTENING;
        }
        //如果是退出指令,则停止监听
        if ("退出".equals(updateEvent.getMessage().contentToString())) {
            return ListeningStatus.STOPPED;
        }
        //判断歌曲信息是否完整,并发送提示信息
        if (introduce == null && musicShare == null) {
            updateEvent.getSubject().sendMessage("缺少歌曲介绍或音乐链接");
            return ListeningStatus.LISTENING;
        }

        updateEvent.getSubject().sendMessage("正在上传...");
        //将歌曲信息储存到数据库
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DataBaseUtils.getConnection();
            //sql语句
            String sql = "INSERT INTO music (music_name,music_singer,introduce,music_share) VALUES (?,?,?,?)";
            ps = conn.prepareStatement(sql);

            //获取歌名和歌手
            String musicName = musicShare.getJSONObject("meta").getJSONObject("music").getString("title");
            String musicSinger = musicShare.getJSONObject("meta").getJSONObject("music").getString("desc");

            updateEvent.getSubject().sendMessage("歌名为:" + musicName);
            updateEvent.getSubject().sendMessage("歌手为:" + musicSinger);

            ps.setString(1, musicName);
            ps.setString(2, musicSinger);
            ps.setString(3, introduce);
            ps.setString(4, musicShare.toString());

            ps.executeUpdate();
            //上传成功
            updateEvent.getSubject().sendMessage("上传成功");
        } catch (Exception e) {
            updateEvent.getSubject().sendMessage("上传过程中出错,以自动退出");
            e.printStackTrace();
        } finally {
            //关闭连接
            DataBaseUtils.close(conn, ps);
        }

        //停止监听
        return ListeningStatus.STOPPED;
    }

    @EventHandler(priority = Listener.EventPriority.HIGH)
    public ListeningStatus onMusicShare(MessageEvent shareEvent) {

        if (shareEvent.getSender().getId() != QQId) {
            return ListeningStatus.LISTENING;
        }


        //如果发送的消息不是分享链接,则保持监听
        if (shareEvent.getMessage().get(1) instanceof LightApp) {
            //如果为链接则转化为json
            musicShare = JSONObject.parseObject(shareEvent.getMessage().get(1).contentToString());


        }
        return ListeningStatus.LISTENING;
    }

    @EventHandler(priority = Listener.EventPriority.HIGH)
    public ListeningStatus uploadMusic(MessageEvent musicEvent) {

        if (musicEvent.getSender().getId() != QQId) {
            return ListeningStatus.LISTENING;
        }


        //介绍输入格式为 介绍:xxx
        String musicString = musicEvent.getMessage().contentToString();

        //分割字符串
        String[] strings = musicString.split(":");
        //判断是否符合要求
        if (strings.length != 2 || !"介绍".equals(strings[0])) {
            return ListeningStatus.LISTENING;
        }
        //将歌曲介绍保存
        introduce = strings[1];
        return ListeningStatus.LISTENING;
    }

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        throw new RuntimeException("在上传歌曲中出现异常", exception);
    }

}

