package cn.jiayistu.bot;

import cn.jiayistu.configuration.Configuration;
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
        final long qq = Long.parseLong(Configuration.readConfig("qq","bot.properties"));
        final String password = Configuration.readConfig("password","bot.properties");

        final Bot bot = BotFactoryJvm.newBot(qq, password, new BotConfiguration() {
            {
                fileBasedDeviceInfo("conf\\deviceInfo.json");
            }
        });

        bot.login();

        Events.registerEvents(bot, new SimpleListenerHost() {
            @EventHandler
            public ListeningStatus onGroupMessage(GroupMessageEvent event) {
                String msgString = event.getMessage().contentToString();
//                System.out.println(msgString);
                if (event.getGroup().getId() == 915775011L) {//测试群
                    if (msgString.equals("你好")) {
                        event.getGroup().sendMessage("你好!");
                    }
                    if (msgString.contains("@" + bot.getNick())) {
                        event.getGroup().sendMessage(new At(event.getSender()).plus("干啥?"));
                    }

                } else if (event.getGroup().getId() == 956830867L) {
                    if (msgString.contains("@" + bot.getNick())) {
                        event.getGroup().sendMessage(new At(event.getSender()).plus("干啥?"));
                    }
                }

                return ListeningStatus.LISTENING;
            }

            @EventHandler
            public ListeningStatus onSenderMessage(FriendMessageEvent event) {
                String msgString = event.getMessage().contentToString();

                if (event.getSender().getId() == 1298528323L) {
                    if (msgString.equals("你好")) {
                        event.getSender().sendMessage("你好!");
                    }else {
                        event.getSender().sendMessage("你好,我是机器人小懿~,请问需要什么帮助吗?");
                    }
                }


                return ListeningStatus.LISTENING;
            }


            @Override
            public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
                throw new RuntimeException("在事件处理中发生异常", exception);
            }
        });

        bot.join();


    }

}
