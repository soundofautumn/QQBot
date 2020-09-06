package cn.jiayistu.bot.event.friend;

import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.Events;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.message.FriendMessageEvent;
import org.jetbrains.annotations.NotNull;

public class FriendEventHandleCommon extends SimpleListenerHost {
    final Bot bot;

    public FriendEventHandleCommon(Bot bot) {
        this.bot = bot;
    }


    @EventHandler
    //处理好友的事件
    public ListeningStatus onFriendMessage(FriendMessageEvent event) {
        String msgString = event.getMessage().contentToString();

        if (msgString.equals("你好")) {
            event.getSender().sendMessage("你好!");

        } else if (msgString.equals("数据库查询")) {
            event.getSender().sendMessage("请输入查询的学号");
            Events.registerEvents(bot,
                    new BotDatabaseQuery(bot, event.getSender().getId()));
        }
        //event.getSender().sendMessage("你好,我是机器人小懿~,请问需要什么帮助吗?");
        //TODO bug here


        return ListeningStatus.LISTENING;//继续监听
    }


    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        throw new RuntimeException("在事件处理中发生异常", exception);
    }
}
