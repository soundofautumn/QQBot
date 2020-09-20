package cn.jiayistu.bot.event;

import cn.jiayistu.utils.DataBaseUtils;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.*;
import net.mamoe.mirai.message.MessageEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

/**
 * @author QJMing
 */
public class Account extends SimpleListenerHost {
    private final long qq;

    public Account(long id) {
        this.qq = id;
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
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DataBaseUtils.close(conn, ps, rs);
        }
    }

    /**
     * 绑定qq号
     *
     * @param event 事件
     * @return 是否监听
     */
    @EventHandler(priority = Listener.EventPriority.HIGH)
    public ListeningStatus binding(MessageEvent event) {
        String studentId = event.getMessage().contentToString();
        //判断是否是自己发出来的和是否是纯数字
        if (event.getSender().getId() != qq || !isNumeric(studentId)) {
            return ListeningStatus.LISTENING;
        }
        //转成数字
        int id = Integer.parseInt(studentId);
        //连接数据库
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DataBaseUtils.getConnection();
            //判断数据库里这个学号是否注册
            String sql = "SELECT qq FROM users where user_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            //如果数据库里这个学号已经注册了
            if (rs.next() && rs.wasNull()) {
                event.getSubject().sendMessage("此学号已注册,绑定失败,请重新绑定");
                return ListeningStatus.STOPPED;
            } else {
                //否则注册这个学号
                event.getSubject().sendMessage("请输入您的姓名以验证");
                //注册
                Events.registerEvents(new NewAccount(event.getSender().getId(), id));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseUtils.close(conn, ps, rs);
        }



        return ListeningStatus.STOPPED;
    }


    public boolean isNumeric(String str) {
        String pattern = "[0-9]*";
        return Pattern.matches(pattern, str);
    }


    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        throw new RuntimeException("在事件处理过程中出现异常", exception);
    }
}
