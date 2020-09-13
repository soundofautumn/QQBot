package cn.jiayistu.bot.event;

import cn.jiayistu.utils.DBUtil;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.Events;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.message.MessageEvent;
import net.mamoe.mirai.message.data.LightApp;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 处理主要事件
 */
public class EventHandleCommon extends SimpleListenerHost {

    private final Bot bot;

    /**
     * 构造方法
     *
     * @param bot 机器人对象
     */
    public EventHandleCommon(Bot bot) {
        this.bot = bot;
    }

    /**
     * 处理主要事件
     *
     * @param event 事件
     * @return 是否继续监听
     */
    @EventHandler
    public ListeningStatus OnMessage(MessageEvent event) {
        String msgString = event.getMessage().contentToString();//将获取到的事件转为可阅读的字符串

        //判断接收到的信息
        if (msgString.equals("上传歌曲")) {
            event.getSubject().sendMessage("请发送歌曲介绍和歌曲链接,介绍输入格式为 \"介绍:xxx\"(注意为英文状态的冒号) ");//发送提示

            //注册新事件
            Events.registerEvents(bot, new SimpleListenerHost() {
                private String music_share;//分享的音乐链接
                private String introduce;//对音乐的介绍


                @EventHandler
                ListeningStatus Update(MessageEvent updateEvent) {
                    //如果不是上传命令,则继续监听
                    if (!updateEvent.getMessage().contentToString().equals("上传")) return ListeningStatus.LISTENING;
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
                        String sql = "INSERT INTO music (introduce,music_share) VALUES (?,?)";
                        ps = conn.prepareStatement(sql);
                        ps.setString(1, introduce);
                        ps.setString(2, music_share);

                        ps.executeUpdate();
                        //上传成功
                        updateEvent.getSubject().sendMessage("上传成功");
                    } catch (SQLException e) {
                        updateEvent.getSubject().sendMessage("上传过程中出错,以自动退出");
                        e.printStackTrace();
                    } finally {
                        DBUtil.close(conn, ps);//关闭连接
                    }


                    return ListeningStatus.STOPPED;//停止监听
                }

                @EventHandler
                public ListeningStatus OnMusicShare(MessageEvent shareEvent) {
                    //如果发送的消息不是分享链接,则保持监听
                    if (shareEvent.getMessage().get(1) instanceof LightApp) {
                        music_share = shareEvent.getMessage().get(1).contentToString();//如果为链接则转化为json
                    }
                    return ListeningStatus.LISTENING;
                }

                @EventHandler
                public ListeningStatus UploadMusic(MessageEvent musicEvent) {

                    //介绍输入格式为 介绍:xxx
                    String musicString = musicEvent.getMessage().contentToString();

                    String[] strings = musicString.split(":");//分割字符串
                    //判断是否符合要求
                    if (strings.length != 2 || strings[0].equals("介绍")) return ListeningStatus.LISTENING;
                    introduce = strings[1];//将歌曲介绍保存
                    return ListeningStatus.LISTENING;
                }

                @Override
                public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
                    throw new RuntimeException("在上传歌曲中出现异常", exception);
                }
            });


        }


        return ListeningStatus.LISTENING;
    }

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        throw new RuntimeException("在事件处理过程中出现异常", exception);
    }
}
