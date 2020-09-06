package cn.jiayistu.bot.event.group;

import cn.jiayistu.bot.event.DataBase;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.message.GroupMessageEvent;

public class BotDatabaseQuery extends SimpleListenerHost {

    private final Bot bot;
    private final long groupId;
    private final long senderId;

    public BotDatabaseQuery(Bot bot, long groupId, long senderId) {
        this.bot = bot;
        this.groupId = groupId;
        this.senderId = senderId;
    }

    @EventHandler
    public ListeningStatus DatabaseQuery(GroupMessageEvent event) {

        //判断是否是发起查询的人
        if (event.getGroup().getId() == groupId && event.getSender().getId() == senderId) {

            String msgString = event.getMessage().contentToString();//将收到的信息转化为字符串

            //排除自己和指令信息,去掉会产生bug
            if ((!msgString.equals("数据库查询")) && event.getSender().getId() != bot.getId()) {
                sendMessage(event,DataBase.Query(msgString));
                return ListeningStatus.STOPPED;
            }
        }
        return ListeningStatus.LISTENING;
    }

    private static void sendMessage(GroupMessageEvent event, String message) {
        event.getGroup().sendMessage(message);
    }

}
