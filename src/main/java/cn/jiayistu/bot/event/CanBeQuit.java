package cn.jiayistu.bot.event;

import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.message.MessageEvent;
import org.jetbrains.annotations.NotNull;

/**
 * @author QJMing
 */
public abstract class CanBeQuit extends SimpleListenerHost {

    @EventHandler(priority = Listener.EventPriority.HIGHEST)
    public ListeningStatus exit(MessageEvent event) {
        if ("退出".equals(event.getMessage().contentToString())) {
            event.getSubject().sendMessage("已退出");
            return ListeningStatus.STOPPED;
        } else {
            return ListeningStatus.LISTENING;
        }
    }


    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        throw new RuntimeException("在事件处理过程中发生异常", exception);
    }
}
