package cn.jiayistu.bot;

import cn.jiayistu.configuration.ReadProperties;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.*;
import net.mamoe.mirai.event.*;
import net.mamoe.mirai.message.FriendMessageEvent;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.utils.*;
import org.jetbrains.annotations.NotNull;

public class Main {
    public static void main(String[] args) {
        //获取qq号和密码
        final long qq = Long.parseLong(ReadProperties.getValue("qq", "bot.properties"));
        final String password = ReadProperties.getValue("password", "bot.properties");

        //创建bot对象
        final Bot bot = BotFactoryJvm.newBot(qq, password, new BotConfiguration() {
            {
                fileBasedDeviceInfo("/deviceInfo.json");//使用设备配置信息
            }
        });

        bot.login();//登录

        //注册监听事件
        Events.registerEvents(bot, new SimpleListenerHost() {
            @EventHandler
            //处理群中的事件
            public ListeningStatus onGroupMessage(GroupMessageEvent event) {
                String msgString = event.getMessage().contentToString();
                if (msgString.equals("你好")) {
                    event.getGroup().sendMessage("你好!");

                } else if (msgString.contains("@" + bot.getNick())) {
                    event.getGroup().sendMessage(new At(event.getSender()).plus("干啥?"));

                } else if (msgString.equals("数据库查询")) {
                    event.getGroup().sendMessage("请输入查询的学号");
                    new BotDatabaseQuery(bot, event.getSender().getId(), event.getGroup().getId()).run();
                }


                return ListeningStatus.LISTENING;//保持监听
            }

            @EventHandler
            //处理好友的事件
            public ListeningStatus onFriendMessage(FriendMessageEvent event) {
                String msgString = event.getMessage().contentToString();

                if (msgString.equals("你好")) {
                    event.getSender().sendMessage("你好!");

                } else if (msgString.equals("数据库查询")) {
                    event.getSender().sendMessage("请输入查询的学号");
                    new BotDatabaseQuery(bot, event.getSender().getId()).run();
                }
                //event.getSender().sendMessage("你好,我是机器人小懿~,请问需要什么帮助吗?");
                //TODO bug here


                return ListeningStatus.LISTENING;//继续监听
            }


            @Override
            //处理异常
            public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
                throw new RuntimeException("在事件处理中发生异常", exception);
            }
        });

        bot.join();//阻塞进程


    }

}
