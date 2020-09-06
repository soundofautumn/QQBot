package cn.jiayistu.bot.event.friend;

import cn.jiayistu.database.DBHelper;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.message.FriendMessageEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 新建一个线程
 * 查询数据库
 */
public class BotDatabaseQuery extends SimpleListenerHost {
    private final Bot bot;
    private final long senderId;

    /**
     * 构造方法
     *
     * @param bot      机器人对象
     * @param senderId 发送者的qq号码
     */
    public BotDatabaseQuery(Bot bot, long senderId) {
        this.bot = bot;
        this.senderId = senderId;
    }


    @EventHandler
    public ListeningStatus DatabaseQuery(FriendMessageEvent event) {

        //判断是否是发起查询的人
        if (event.getSender().getId() == senderId) {

            String msgString = event.getMessage().contentToString();//将收到的信息转化为字符串

            //排除自己和指令信息,去掉会产生bug
            if ((!msgString.equals("数据库查询")) && event.getSender().getId() != bot.getId()) {
                //判断是否是8位数字(是否是学号)
                String pattern = "^\\d{8}$";

                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(msgString);
                //判断是否符合
                if (m.matches()) {
                    //如果符合则转化为long类型
                    long username = Long.parseLong(msgString);
                    //生成sql语句
                    String sql = "SELECT username,grade FROM users WHERE username= " + username;
                    //连接数据库
                    DBHelper db = null;
                    ResultSet rs = null;
                    //查询数据库
                    try {
                        db = new DBHelper(sql);
                        rs = db.pst.executeQuery();
                        rs.next();
                        sendMessage(event, rs.getString("grade"));
                    } catch (SQLException se) {
                        se.printStackTrace();
                        sendMessage(event, "出现未知错误,数据库查询已停止");
                    } finally {
                        if (rs != null) {
                            try {
                                rs.close();
                            } catch (SQLException se) {
                                se.printStackTrace();
                            }
                        }
                        if (db != null) {
                            try {
                                db.close();
                            } catch (SQLException se2) {
                                se2.printStackTrace();
                            }
                        }
                    }

                } else {
                    sendMessage(event, "学号输入有误,数据库查询已停止");
                }
                return ListeningStatus.STOPPED;
            }
        }
        return ListeningStatus.LISTENING;
    }


    private static void sendMessage(FriendMessageEvent event, String message) {
        event.getSender().sendMessage(message);
    }



}
