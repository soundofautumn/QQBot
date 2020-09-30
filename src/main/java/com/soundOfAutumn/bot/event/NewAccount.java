package com.soundOfAutumn.bot.event;

import com.soundOfAutumn.utils.DataBaseUtils;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.*;
import net.mamoe.mirai.message.MessageEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author SoundOfAutumn
 */
public class NewAccount extends SimpleListenerHost {
    private final long senderQQ;
    private final int userId;

    public NewAccount(long sender, int userId) {
        this.userId = userId;
        this.senderQQ = sender;
    }


    @EventHandler(priority = Listener.EventPriority.HIGHEST, concurrency = Listener.ConcurrencyKind.LOCKED)
    public ListeningStatus registerAccount(MessageEvent event) {
        String msgString = event.getMessage().contentToString();
        if (senderQQ != event.getSender().getId()) {
            return ListeningStatus.LISTENING;
        }
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DataBaseUtils.getConnection();
            String sql = "SELECT real_name FROM users WHERE user_id = ? ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);

            rs = ps.executeQuery();

            if (rs.next() && rs.getString("real_name").equals(msgString)) {
                event.getSubject().sendMessage("验证成功");
                if (register()) {
                    event.getSubject().sendMessage("注册成功");
                } else {
                    event.getSubject().sendMessage("注册失败");
                }
            }else {
                event.getSubject().sendMessage("验证失败,请重新输入");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseUtils.close(conn, ps, rs);
        }

        return ListeningStatus.STOPPED;
    }




    private boolean register() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DataBaseUtils.getConnection();
            String sql = "UPDATE users SET qq = ? WHERE user_id = ? ";

            ps = conn.prepareStatement(sql);
            ps.setLong(1, senderQQ);
            ps.setInt(2, userId);

            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            DataBaseUtils.close(conn, ps);
        }
        return false;
    }

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        throw new RuntimeException("在事件处理过程中发生异常", exception);
    }


}
