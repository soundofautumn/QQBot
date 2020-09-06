package cn.jiayistu.bot.event.group;

import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.Events;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import org.jetbrains.annotations.NotNull;

public class GroupEventHandleCommon extends SimpleListenerHost {
    final Bot bot;

    public GroupEventHandleCommon(Bot bot) {
        this.bot = bot;
    }

    @EventHandler
    public ListeningStatus OnGroupMessage(GroupMessageEvent event) {
        String msgString = event.getMessage().contentToString();
        if (msgString.equals("你好")) {
            event.getGroup().sendMessage("你好!");

        } else if (msgString.contains("@" + bot.getNick())) {
            event.getGroup().sendMessage(new At(event.getSender()).plus("干啥?"));

        } else if (msgString.equals("数据库查询")) {
            event.getGroup().sendMessage("请输入查询的学号");
            Events.registerEvents(bot,
                    new BotDatabaseQuery(bot, event.getSender().getId(), event.getGroup().getId()));
        }
        return ListeningStatus.LISTENING;
    }


    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        throw new RuntimeException("在事件处理中发生异常", exception);
    }
}
