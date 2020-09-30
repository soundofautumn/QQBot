package com.soundOfAutumn.bot.event;

import com.soundOfAutumn.utils.AccountUtils;
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
 * @author SoundOfAutumn
 */
public class EventHandleCommon extends SimpleListenerHost {

    private final Bot bot;

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
        if ("点歌".equals(msgString)) {
            if (!AccountUtils.isBind(event.getSender().getId())) {
                event.getSubject().sendMessage("QQ号未绑定,请先绑定");
                return ListeningStatus.LISTENING;
            }
            //发送提示
            event.getSubject().sendMessage("请发送歌曲介绍和歌曲链接,介绍输入格式为 \"介绍:xxx\" ,最后输入 \"上传\" 来确认");
            event.getSubject().sendMessage("如需更改上传的内容,直接重新发送即可");

            //注册新事件
            Events.registerEvents(bot, new MusicUpLoad(event.getSender().getId()));
            //判断是否发送的绑定命令
        } else if ("绑定".equals(msgString)) {
            //判断是否已经绑定
            if (AccountUtils.isBind(event.getSender().getId())) {
                event.getSubject().sendMessage("QQ号已绑定");
            } else {
                //绑定
                event.getSubject().sendMessage("准备绑定....请注意一个QQ号只能绑定一个账号");
                event.getSubject().sendMessage("请输入您的学号");

                Events.registerEvents(bot, new Bind(event.getSender().getId()));

            }
        } else if (msgString.equals("@" + event.getBot().getNick())) {
            //显示提示信息
            event.getSubject().sendMessage("现支持的命令如下:\n" +
                    "1. \"点歌\" :进行上传歌曲等操作\n" +
                    "2. \"绑定\" :进行QQ号与学号的绑定(如需解绑,请联系管理员)\n" +
                    "3. \"显示歌曲列表\" :显示简要的歌曲信息" +
                    "4. \"显示歌曲详细信息:X\" :X为歌曲序号,显示歌曲的简介和分享链接");
        } else if ("显示歌曲列表".equals(msgString)) {
            event.getSubject().sendMessage(MusicList.getBrief());
        } else if (msgString.contains("显示歌曲详细信息") && msgString.contains(":")) {
            String s = msgString.split(":")[1];
            event.getSubject().sendMessage(MusicList.getIntroduce(s));
            event.getSubject().sendMessage(MusicList.getMusicShare(s));
        } else if (msgString.contains("点赞") && msgString.contains(":")) {
            String musicId = msgString.split(":")[1];
            event.getSubject().sendMessage(MusicLike.giveLike(musicId, event.getSender().getId()));

        }


        return ListeningStatus.LISTENING;
    }

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        throw new RuntimeException("在事件处理过程中出现异常", exception);
    }
}
