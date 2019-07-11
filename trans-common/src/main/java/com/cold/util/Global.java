package com.cold.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Auther: ohj
 * @Date: 2018/8/9 14:10
 * @Description:
 */
public class Global {
    private static Map<String, String> map = new HashMap();
    private static Properties property = PropertiesUtils
            .getProperties("properties/config.properties");
    public static String getConfig(String key) {
        String value = map.get(key);
        if (value == null){
            value = property.getProperty(key);
            map.put(key, value);
        }
        return value;
    }
}