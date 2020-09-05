package cn.jiayistu.configuration;


import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

public class ReadProperties {


    public static void GetAllProperties(String filePath) {


    }

    @NotNull
    public static String getValue(String key, String filePath) {
        try {
            Properties prop = new Properties();
            ClassLoader classLoader = ReadProperties.class.getClassLoader();
            InputStream in = classLoader.getResourceAsStream(filePath);
            prop.load(in);
            for (String s : prop.stringPropertyNames()) {
                if (s.equals(key)) {
                    return prop.getProperty(key);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new ValueNotFoundException("未找到指定配置");
    }

    public static void writeValue(String key, String value, String filePath) {

    }
}
