package cn.jiayistu.bot.event;

import cn.jiayistu.utils.DBUtil;
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

    private JSONObject music_share;//分享的音乐链接
    private String introduce;//对音乐的介绍
    private final long QQId;

    public UpLoad(long id) {
        QQId = id;
    }


    @EventHandler(concurrency = Listener.ConcurrencyKind.LOCKED, priority = Listener.EventPriority.HIGH)
    public ListeningStatus Update(MessageEvent updateEvent) {
        if (updateEvent.getSender().getId() != QQId) return ListeningStatus.LISTENING;

        //如果不是上传命令,则继续监听
        if (!updateEvent.getMessage().contentToString().equals("上传")) return ListeningStatus.LISTENING;
        //如果是退出指令,则停止监听
        if (updateEvent.getMessage().contentToString().equals("退出")) return ListeningStatus.STOPPED;
        //判断歌曲信息是否完整,并发送提示信息
        if (introduce == null && music_share == null) {
            updateEvent.getSubject().sendMessage("缺少歌曲介绍或音乐链接");
            return ListeningStatus.LISTENING;
        }

        updateEvent.getSubject().sendMessage("正在上传...");
        //将歌曲信息储存到数据库
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            //sql语句
            String sql = "INSERT INTO music (music_name,music_singer,introduce,music_share) VALUES (?,?,?,?)";
            ps = conn.prepareStatement(sql);

            //获取歌名和歌手
            String music_name = music_share.getJSONObject("meta").getJSONObject("music").getString("title");
            String music_singer = music_share.getJSONObject("meta").getJSONObject("music").getString("desc");

            updateEvent.getSubject().sendMessage("歌名为:" + music_name);
            updateEvent.getSubject().sendMessage("歌手为:" + music_singer);

            ps.setString(1, music_name);
            ps.setString(2, music_singer);
            ps.setString(3, introduce);
            ps.setString(4, music_share.toString());

            ps.executeUpdate();
            //上传成功
            updateEvent.getSubject().sendMessage("上传成功");
        } catch (Exception e) {
            updateEvent.getSubject().sendMessage("上传过程中出错,以自动退出");
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps);//关闭连接
        }


        return ListeningStatus.STOPPED;//停止监听
    }

    @EventHandler(priority = Listener.EventPriority.HIGH)
    public ListeningStatus OnMusicShare(MessageEvent shareEvent) {
        if (shareEvent.getSender().getId() != QQId) return ListeningStatus.LISTENING;


        //如果发送的消息不是分享链接,则保持监听
        if (shareEvent.getMessage().get(1) instanceof LightApp) {
            music_share = JSONObject.parseObject(shareEvent.getMessage().get(1).contentToString());//如果为链接则转化为json


        }
        return ListeningStatus.LISTENING;
    }

    @EventHandler(priority = Listener.EventPriority.HIGH)
    public ListeningStatus UploadMusic(MessageEvent musicEvent) {

        if (musicEvent.getSender().getId() != QQId) return ListeningStatus.LISTENING;


        //介绍输入格式为 介绍:xxx
        String musicString = musicEvent.getMessage().contentToString();

        String[] strings = musicString.split(":");//分割字符串
        //判断是否符合要求
        if (strings.length != 2 || !strings[0].equals("介绍")) return ListeningStatus.LISTENING;
        introduce = strings[1];//将歌曲介绍保存
        return ListeningStatus.LISTENING;
    }

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        throw new RuntimeException("在上传歌曲中出现异常", exception);
    }

}

