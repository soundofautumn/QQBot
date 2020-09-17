package cn.jiayistu.bot.event;

import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.Events;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.message.MessageEvent;
import org.jetbrains.annotations.NotNull;

/**
 * 处理主要事件
 *
 * @author QJMing
 */
public class EventHandleCommon extends SimpleListenerHost {

    private final Bot bot;
    String command1 = "点歌";
    String command2 = "绑定";

    /**
     * 构造方法
     *
     * @param bot 机器人对象
     */
    public EventHandleCommon(Bot bot) {
        this.bot = bot;
    }

    /**
     * 处理主要事件
     *
     * @param event 事件
     * @return 是否继续监听
     */
    @EventHandler
    public ListeningStatus onMessage(MessageEvent event) {
        //将获取到的事件转为可阅读的字符串
        String msgString = event.getMessage().contentToString();

        //判断接收到的信息
        //判断是否发送的是绑定命令
        if (command1.equals(msgString)) {
            if (!Account.isBind(event.getSender().getId())) {
                event.getSubject().sendMessage("QQ号未绑定,请先绑定");
                return ListeningStatus.LISTENING;
            }
            //发送提示
            event.getSubject().sendMessage("请发送歌曲介绍和歌曲链接,介绍输入格式为 \"介绍:xxx\"(注意为英文状态的冒号) ");
            //注册新事件
            Events.registerEvents(bot, new UpLoad(event.getSender().getId()));
            //判断是否发送的绑定命令
        } else if (command2.equals(msgString)) {
            if (Account.isBind(event.getSender().getId())) {
                event.getSubject().sendMessage("QQ号已绑定");
            } else {
                event.getSubject().sendMessage("准备绑定....请注意一个QQ号只能绑定一个账号");

                Events.registerEvents(bot, new Account(event.getSender().getId()));

            }

        } else if (msgString.equals("@" + event.getBot().getNick())) {
            event.getSubject().sendMessage("?");
        }


        return ListeningStatus.LISTENING;
    }

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        throw new RuntimeException("在事件处理过程中出现异常", exception);
    }
}
