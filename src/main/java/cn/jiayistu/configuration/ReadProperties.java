package cn.jiayistu.configuration;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 读取配置文件
 */
public class ReadProperties {

    /**
     * 根据关键词和配置文件路径获取配置信息
     * @param key 关键词
     * @param filePath 配置文件路径
     * @return 得到的配置信息
     */
    @NotNull
    public static String getValue(String key, String filePath) {
        try {
            Properties prop = new Properties();
            ClassLoader classLoader = ReadProperties.class.getClassLoader();
            InputStream in = classLoader.getResourceAsStream(filePath);
            prop.load(in);
            //遍历
            for (String s : prop.stringPropertyNames()) {
                //判断关键词是否相同
                if (s.equals(key)) {
                    return prop.getProperty(key);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new ValueNotFoundException("未找到指定配置");
    }

}
