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

public class EventHandleCommon extends SimpleListenerHost {
    private final Bot bot;

    public EventHandleCommon(Bot bot) {
        this.bot = bot;
    }

    @EventHandler
    public ListeningStatus OnMessage(MessageEvent event) {
        String msgString = event.getMessage().contentToString();

        if (msgString.equals("上传歌曲")) {

            event.getSubject().sendMessage("输入格式为(introduce:xxx)");
            Events.registerEvents(bot, new SimpleListenerHost() {
                private String music_share;
                private String introduce;


                @EventHandler
                ListeningStatus Update(MessageEvent updateEvent) {
                    //如果不是上传命令,则继续监听
                    if (!updateEvent.getMessage().contentToString().equals("上传")) return ListeningStatus.LISTENING;
                    if (introduce == null && music_share == null) return ListeningStatus.LISTENING;

                    Connection conn = null;
                    PreparedStatement ps = null;
                    try {
                        conn = DBUtil.getConnection();
                        String sql = "INSERT INTO music (introduce,music_share) VALUES (?,?)";
                        ps = conn.prepareStatement(sql);
                        ps.setString(1, introduce);
                        ps.setString(2, music_share);

                        ps.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        DBUtil.close(conn, ps);
                    }


                    return ListeningStatus.STOPPED;
                }

                @EventHandler
                public ListeningStatus OnMusicShare(MessageEvent shareEvent) {
                    //如果发送的消息不是分享链接,则保持监听
                    if (shareEvent.getMessage().get(1) instanceof LightApp) {
                        music_share= shareEvent.getMessage().get(1).contentToString();
                    }
                    return ListeningStatus.LISTENING;
                }

                @EventHandler
                public ListeningStatus UploadMusic(MessageEvent musicEvent) {

                    //输入格式为 (name,singer,introduce)
                    String musicString = musicEvent.getMessage().contentToString();

                    String[] strings = musicString.split(":");
                    if (strings.length != 2) return ListeningStatus.LISTENING;
                    introduce = strings[1];
                    return ListeningStatus.LISTENING;
                }

                @Override
                public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
                    super.handleException(context, exception);
                }
            });


        }


        return ListeningStatus.LISTENING;
    }


    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        super.handleException(context, exception);
    }
}
