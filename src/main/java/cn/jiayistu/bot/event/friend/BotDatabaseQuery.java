package cn.jiayistu.bot.event.friend;

import cn.jiayistu.bot.event.DataBase;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.message.FriendMessageEvent;

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
                sendMessage(event, DataBase.Query(msgString));
                return ListeningStatus.STOPPED;
            }
        }
        return ListeningStatus.LISTENING;
    }


    private static void sendMessage(FriendMessageEvent event, String message) {
        event.getSender().sendMessage(message);
    }



}
