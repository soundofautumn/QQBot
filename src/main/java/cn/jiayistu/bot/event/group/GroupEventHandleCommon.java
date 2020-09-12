package cn.jiayistu.bot.event.group;

import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.LightApp;
import net.mamoe.mirai.message.data.Message;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@Deprecated
public class GroupEventHandleCommon extends SimpleListenerHost {
    final private Bot bot;

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

        }else if (msgString.equals("测试1")) {
            BufferedReader bufferedReader = null;
            String json = null;

            try {
                bufferedReader = new BufferedReader(new FileReader("src\\main\\resources\\LightApp\\1.json"));
                json = bufferedReader.readLine();
                System.out.println(json);
                event.getGroup().sendMessage(new LightApp(json));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        System.out.println("=================begin=======================");
        for (Message m:event.getMessage()
        ) {
            System.out.println(m);
            System.out.println("===========================================");

        }
        System.out.println("=================end=======================");
//        else if (msgString.equals("测试1")) {
//            event.getGroup().setName(new LightApp(""));
//        }
//        } else if (msgString.equals("数据库查询")) {
//            event.getGroup().sendMessage("请输入查询的学号");
//            Events.registerEvents(bot,
//                    new BotDatabaseQuery(bot, event.getSender().getId(), event.getGroup().getId()));
//        }
        return ListeningStatus.LISTENING;
    }


    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        throw new RuntimeException("在事件处理中发生异常", exception);
    }
}
