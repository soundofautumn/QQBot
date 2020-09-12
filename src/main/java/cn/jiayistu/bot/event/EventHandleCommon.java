package cn.jiayistu.bot.event;

import cn.jiayistu.database.DBUtil;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.Events;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.message.MessageEvent;
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

            event.getSender().sendMessage("输入格式为(name,singer,introduce)");
            Events.registerEvents(bot, new SimpleListenerHost() {
                @EventHandler
                public ListeningStatus UploadMusic(MessageEvent musicEvent) {

                    //输入格式为 (name,singer,introduce)
                    String musicString = musicEvent.getMessage().contentToString();

                    String[] strings = musicString.split(",");

                    if (strings.length!=3) return ListeningStatus.LISTENING;

                    Connection conn = null;
                    PreparedStatement ps = null;
                    try {
                        conn = DBUtil.getConnection();
                        //insert into music(music_name,music_singer,introduce) values(name,singer,introduce);
                        String sql = "INSERT INTO music" +
                                "(music_name,music_singer,introduce) " +
                                "VALUES" +
                                "(?,?,?)";
                        ps = conn.prepareStatement(sql);
                        ps.setString(1, strings[0]);
                        ps.setString(2,strings[1]);
                        ps.setString(3,strings[2]);

                        ps.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        DBUtil.close(conn, ps);
                    }
                    return ListeningStatus.STOPPED;
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
