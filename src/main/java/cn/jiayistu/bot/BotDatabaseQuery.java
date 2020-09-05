package cn.jiayistu.bot;

import cn.jiayistu.database.DBHelper;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.*;
import net.mamoe.mirai.message.FriendMessageEvent;
import net.mamoe.mirai.message.GroupMessageEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BotDatabaseQuery implements Runnable {
    private final Bot bot;
    private final long id;


    public BotDatabaseQuery(Bot bot, long id) {
        this.bot = bot;
        this.id = id;
    }

    @Override
    public void run() {
        Events.registerEvents(bot, new SimpleListenerHost() {

            @EventHandler
            public ListeningStatus DatabaseQuery(GroupMessageEvent event) {
//                System.out.println(event);

                if (event.getGroup().getId() == id) {

                    String msgString = event.getMessage().contentToString();

                    if ((!msgString.equals("数据库查询")) && event.getSender().getId() != bot.getId()) {
                        //判断是否是8位数字(是否是学号)
                        String pattern = "^\\d{8}$";

                        Pattern r = Pattern.compile(pattern);
                        Matcher m = r.matcher(msgString);
                        if (m.matches()) {
                            long username = Long.parseLong(msgString);
                            String sql = "SELECT username,grade FROM users WHERE username= " + username;
                            DBHelper db = null;
                            ResultSet rs = null;
                            try {
                                db = new DBHelper(sql);
                                rs = db.pst.executeQuery();
                                rs.next();
                                event.getGroup().sendMessage(rs.getString(2));
                            } catch (SQLException se) {
                                se.printStackTrace();
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
                            event.getGroup().sendMessage("数据库查询已停止");
                        } else {
                            event.getGroup().sendMessage("学号输入有误,数据库查询已停止");
                        }
                        return ListeningStatus.STOPPED;
                    }
                }
                return ListeningStatus.LISTENING;
            }


            @EventHandler
            public ListeningStatus DatabaseQuery(FriendMessageEvent event) {
//                System.out.println(event);

                if (event.getSender().getId() == id) {

                    String msgString = event.getMessage().contentToString();

                    if ((!msgString.equals("数据库查询")) && event.getSender().getId() != bot.getId()) {
                        //判断是否是8位数字(是否是学号)
                        String pattern = "^\\d{8}$";

                        Pattern r = Pattern.compile(pattern);
                        Matcher m = r.matcher(msgString);
                        if (m.matches()) {
                            long username = Long.parseLong(msgString);
                            String sql = "SELECT username,grade FROM users WHERE username= " + username;
                            DBHelper db = null;
                            ResultSet rs = null;
                            try {
                                db = new DBHelper(sql);
                                rs = db.pst.executeQuery();
                                rs.next();
                                event.getSender().sendMessage(rs.getString(2));
                            } catch (SQLException se) {
                                se.printStackTrace();
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
                            event.getSender().sendMessage("数据库查询已停止");
                        } else {
                            event.getSender().sendMessage("学号输入有误,数据库查询已停止");
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
}
