package com.soundOfAutumn.bot;

import com.soundOfAutumn.bot.event.EventHandleCommon;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactoryJvm;
import net.mamoe.mirai.event.Events;
import net.mamoe.mirai.utils.BotConfiguration;

import java.util.ResourceBundle;

/**
 * @author SoundOfAutumn
 */
public class Main {
    public static void main(String[] args) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("bot");
        //获取qq号和密码
        final long qq = Long.parseLong(resourceBundle.getString("qq"));
        final String password = resourceBundle.getString("password");

        //创建bot对象
        final Bot bot = BotFactoryJvm.newBot(qq, password, new BotConfiguration() {
            {
                fileBasedDeviceInfo("deviceInfo.json");//使用设备配置信息
            }
        });

        bot.login();//登录

        //注册监听事件
        Events.registerEvents(new EventHandleCommon(bot));

        bot.join();//阻塞进程


    }

}
