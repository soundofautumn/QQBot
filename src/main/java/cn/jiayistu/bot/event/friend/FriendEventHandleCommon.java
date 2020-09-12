package cn.jiayistu.bot.event.friend;

import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.message.FriendMessageEvent;
import net.mamoe.mirai.message.data.Message;
import org.jetbrains.annotations.NotNull;

public class FriendEventHandleCommon extends SimpleListenerHost {
    final private Bot bot;

    public FriendEventHandleCommon(Bot bot) {
        this.bot = bot;
    }


    @EventHandler
    //处理好友的事件
    public ListeningStatus onFriendMessage(FriendMessageEvent event) {
        String msgString = event.getMessage().contentToString();

        if (msgString.equals("你好")) {
            event.getSender().sendMessage("你好!");
        }
//        else if (msgString.equals("测试1")) {
//            //event.getSender().sendMessage(new LightApp("{\"app\":\"com.tencent.structmsg\",\"config\":{\"autosize\":true,\"ctime\":1599910841,\"forward\":true,\"token\":\"daa26142c3e20c954eb9c1b932f4c1fe\",\"type\":\"normal\"},\"desc\":\"音乐\",\"extra\":{\"app_type\":1,\"appid\":100495085,\"msg_seq\":6871564734753538191},\"meta\":{\"music\":{\"action\":\"\",\"android_pkg_name\":\"\",\"app_type\":1,\"appid\":100495085,\"desc\":\"Glimmer of Blooms\",\"jumpUrl\":\"https://y.music.163.com/m/song/1326862369/?userid=377287271\",\"musicUrl\":\"http://music.163.com/song/media/outer/url?id=1326862369&userid=377287271\",\"preview\":\"http://p1.music.126.net/XA6bKZlV9bp1H8-5YbrUtA==/109951163676928586.jpg\",\"sourceMsgId\":\"0\",\"source_icon\":\"\",\"source_url\":\"\",\"tag\":\"网易云音乐\",\"title\":\"Can't Get You out of My Head\"}},\"prompt\":\"[分享]Can't Get You out of My Head\",\"ver\":\"0.0.0.1\",\"view\":\"music\"}]"));
//
//        }
//        if (event.getMessage().get(2) instanceof RichMessage) {
//            event.getSender().sendMessage(event.getMessage().get(2));
//        }
        System.out.println("=================begin=======================");
        for (Message m:event.getMessage()
             ) {
            System.out.println(m);
            System.out.println("===========================================");

        }
        System.out.println("=================end=======================");

//         else if (msgString.equals("数据库查询")) {
//            event.getSender().sendMessage("请输入查询的学号");
//            Events.registerEvents(bot,
//                    new BotDatabaseQuery(bot, event.getSender().getId()));
//        }


        return ListeningStatus.LISTENING;//继续监听
    }


    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        throw new RuntimeException("在事件处理中发生异常", exception);
    }
}
