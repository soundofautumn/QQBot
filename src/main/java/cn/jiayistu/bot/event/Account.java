package cn.jiayistu.bot.event;

import cn.jiayistu.utils.DataBaseUtils;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.message.MessageEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author QJMing
 */
public class Account extends SimpleListenerHost {
    private long qq;

    public Account(long id) {
        qq = id;
    }

    public static boolean isBind(long qq) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DataBaseUtils.getConnection();

            String sql = "SELECT qq FROM users WHERE qq = ? ";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, qq);
            rs = ps.executeQuery();
            rs.next();
            int i = rs.getInt(1);
            return i != 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DataBaseUtils.close(conn, ps, rs);
        }
    }

    @EventHandler(priority = Listener.EventPriority.HIGH)
    public static ListeningStatus binding(MessageEvent event) {
        event.getSubject().sendMessage("请输入您的学号");
        //TODO 判断是否被绑定&&绑定


        return ListeningStatus.STOPPED;
    }

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        throw new RuntimeException("在事件处理过程中出现异常", exception);
    }
}
