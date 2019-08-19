package com.tedu.base.common.utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.Set;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2018/3/24
 */
public class PropertiesUtil {
    /** 配置文件属性 */
    private Properties properties = null;

    /** 私有化构造方法 */
    private PropertiesUtil() {
    }

    /** 获取实例方法 */
    public static PropertiesUtil getInstance() {
        return PropertiesUtilLoader.instance;
    }

    /** 加载配置文件 */
    public void loadProperties(String fileName) {
        //获取文件位置
        //String path = this.getClass().getResource("").getPath()+fileName;
        fileName = fileName + ".properties";
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);

        try {
            properties = new Properties();
            properties.load(new InputStreamReader(is,"UTF-8"));
        } catch (Exception e) {
        } finally {
        }
    }

    public void loadProperties(String path,String fileName) {
        //获取文件位置
        //String path = this.getClass().getResource("").getPath()+fileName;
        fileName = path+"/"+fileName + ".properties";
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);

        try {
            properties = new Properties();
            properties.load(new InputStreamReader(is,"UTF-8"));
        } catch (Exception e) {
        } finally {
        }
    }

    public String getProperties(String key, String encoding) {
        if (properties == null) {
            return null;
        }

        Object o = properties.get(key);

        if (o == null) {
        }

        String value = null;
        try {
            value = new String(o.toString().getBytes("ISO8859-1"), encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return value;
    }

    public String getProperties(String key) {
        if (properties == null) {
            return null;
        }

        Object o = properties.get(key);

        if (o == null) {
        }

        return o.toString();
    }

    public Set<Object> getKeys() {
        return properties.keySet();

    }


    /** 内部类实现懒汉式单例 */
    private static class PropertiesUtilLoader {

        // 单例变量
        private static PropertiesUtil instance = new PropertiesUtil();
    }

}
