package cn.jiayistu.bot;

import cn.jiayistu.database.DBHelper;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.Events;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.message.FriendMessageEvent;
import net.mamoe.mirai.message.GroupMessageEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 新建一个线程
 * 查询数据库
 */
public class BotDatabaseQuery implements Runnable {
    private final Bot bot;
    private final long groupId;
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
        this.groupId = 0L;
    }

    /**
     * 构造方法
     *
     * @param bot      机器人对象
     * @param senderId 发送者的qq号码
     * @param groupId  发送者所在的群号码
     */
    public BotDatabaseQuery(Bot bot, long senderId, long groupId) {
        this.bot = bot;
        this.groupId = groupId;
        this.senderId = senderId;
    }

    /***
     * 重写run方法
     */
    @Override
    public void run() {
        Events.registerEvents(bot, new SimpleListenerHost() {

            /**
             * 内部类用于监听查询事件
             * 处理群信息
             * @param event 得到的事件
             * @return 是否继续监听
             */
            @EventHandler
            public ListeningStatus DatabaseQuery(GroupMessageEvent event) {

                //判断是否是发起查询的人
                if (event.getGroup().getId() == groupId && event.getSender().getId() == senderId) {

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
                            String sql = "SELECT username,grade FROM users WHERE username= " + msgString;
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
                            } finally {
                                if (rs != null) {
                                    try {
                                        rs.close();
                                    } catch (SQLException se) {
                                        se.printStackTrace();
                                        sendMessage(event, "出现未知错误,数据库查询已停止");
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

            /**
             * 内部类用于监听查询事件
             * 处理私聊信息
             * @param event 得到的事件
             * @return 是否继续监听
             */
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
                            } finally {
                                if (rs != null) {
                                    try {
                                        rs.close();
                                    } catch (SQLException se) {
                                        se.printStackTrace();
                                        sendMessage(event, "出现未知错误,数据库查询已停止");
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


            @Override
            public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
                throw new RuntimeException("在事件处理中发生异常", exception);
            }
        });
    }

    private static void sendMessage(FriendMessageEvent event, String message) {
        event.getSender().sendMessage(message);
    }

    private static void sendMessage(GroupMessageEvent event, String message) {
        event.getGroup().sendMessage(message);
    }

}
