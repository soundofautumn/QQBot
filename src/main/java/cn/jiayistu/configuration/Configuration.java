package cn.jiayistu.configuration;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Enumeration;
import java.util.Properties;

public class Configuration {

    final private static String DEFAULT_PATH = "default.properties";

    public static void GetAllProperties(String filePath) {
        filePath = "conf\\" + filePath;


        Properties pps = new Properties();
        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(filePath));
            pps.load(is);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Enumeration en = pps.propertyNames(); //得到配置文件的名字

        while (en.hasMoreElements()) {
            String strKey = (String) en.nextElement();
            String strValue = pps.getProperty(strKey);
            System.out.println(strKey + "=" + strValue);
        }

    }

    public static void GetAllProperties() {
        GetAllProperties(DEFAULT_PATH);
    }

    @NotNull
    public static String readConfig(String key, String filePath) {
        filePath = "conf\\" + filePath;


        Properties properties = new Properties();

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
            properties.load(fis);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("未找到相关配置文件");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return properties.getProperty(key);

    }

    public static String readConfig(String key) {
        return readConfig(DEFAULT_PATH, key);
    }

    public static void writeConfig(String key, String value, String filePath) {
        filePath = "conf\\" + filePath;
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Properties properties = new Properties();

        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            fis = new FileInputStream(filePath);
            properties.load(fis);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        try {

            fos = new FileOutputStream(filePath);
            properties.setProperty(key, value);
            properties.store(fos, "Update '" + key + "' value");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public static void writeConfig(String key, String value) {
        writeConfig(DEFAULT_PATH, key, value);
    }

}
