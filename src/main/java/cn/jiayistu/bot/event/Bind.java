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
public class Bind extends SimpleListenerHost {
    private final long qq;

    public Bind(long id) {
        this.qq = id;
    }


    /**
     * 绑定qq号
     *
     * @param event 事件
     * @return 是否监听
     */
    @EventHandler(priority = Listener.EventPriority.HIGH, concurrency = Listener.ConcurrencyKind.LOCKED)
    public ListeningStatus binding(MessageEvent event) {

        String studentId = event.getMessage().contentToString();
        //判断是否是自己发出来的
        if (event.getSender().getId() != qq) {
            return ListeningStatus.LISTENING;
        }
        //是否是8位数字
        if (!isNumeric(studentId)) {
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
            //情况1:未找到学号 rs.next() = false
            //情况2:找到学号,但QQ号是空的,即 rs.wasNull = true
            //情况3:找到学号,QQ号也有,所以 rs.next()=true, rs.wasNull = false
            //如果数据库里这个学号已经注册了
            if (rs.next()) {
                rs.getString(1);
                if (rs.wasNull()) {
                    //注册这个学号
                    event.getSubject().sendMessage("请输入您的姓名以验证");
                    //注册
                    Events.registerEvents(new NewAccount(event.getSender().getId(), id));
                } else {
                    event.getSubject().sendMessage("此学号已注册,绑定失败,请重新绑定");
                }
                return ListeningStatus.STOPPED;
            } else {
                event.getSubject().sendMessage("未找到此学号,请重新输入");
                return ListeningStatus.LISTENING;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseUtils.close(conn, ps, rs);
        }


        return ListeningStatus.STOPPED;
    }


    public boolean isNumeric(String str) {
        String pattern = "^\\d{8}$";
        return Pattern.matches(pattern, str);
    }


    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        throw new RuntimeException("在事件处理过程中出现异常", exception);
    }
}
