package cn.jiayistu.bot;

import cn.jiayistu.bot.event.friend.FriendEventHandleCommon;
import cn.jiayistu.bot.event.group.GroupEventHandleCommon;
import cn.jiayistu.configuration.ReadProperties;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactoryJvm;
import net.mamoe.mirai.event.Events;
import net.mamoe.mirai.utils.BotConfiguration;

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
        Events.registerEvents(bot, new FriendEventHandleCommon(bot));
        Events.registerEvents(bot, new GroupEventHandleCommon(bot));


        bot.join();//阻塞进程


    }

}
