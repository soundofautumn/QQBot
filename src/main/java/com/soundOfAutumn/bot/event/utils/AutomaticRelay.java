package com.soundOfAutumn.bot.event.utils;

import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.message.GroupMessageEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author SoundOfAutumn
 * @date 2020/9/30 22:31
 */
public class AutomaticRelay extends SimpleListenerHost {
    private final Set<Integer> set = new TreeSet<>();
    private final Bot bot;

    public AutomaticRelay(Bot bot) {
        this.bot = bot;
    }


    @EventHandler(concurrency = Listener.ConcurrencyKind.LOCKED)
    public ListeningStatus onGroup(GroupMessageEvent event){

        String msgString = event.getMessage().contentToString();
        if ("清空".equals(msgString)) {
            set.clear();
            event.getGroup().sendMessage("已清空");
        }
        String pattern = "^[1-9]\\d*$";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(msgString);
        if (m.matches()) {
            set.add(Integer.parseInt(msgString));
            StringBuilder sb = new StringBuilder("已完成:");
            for (int i :
                    set) {
                sb.append(i).append("、");
            }
            sb.deleteCharAt(sb.length()-1);
            event.getGroup().sendMessage(sb.toString());
            bot.recall(event.getMessage());

        }


        return ListeningStatus.LISTENING;
    }

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        throw new RuntimeException("在程序运行过程中出现异常", exception);
    }
}
