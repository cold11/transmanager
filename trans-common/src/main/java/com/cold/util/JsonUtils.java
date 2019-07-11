package com.cold.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * @Auther: ohj
 * @Date: 2019/7/8 13:46
 * @Description:
 */
public class JsonUtils {
    // 定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 将对象转换成json字符串。
     */
    public static String objectToJson(Object data) {
        try {
//            SimpleDateFormat smt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            MAPPER.setDateFormat(smt);
//            MAPPER.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            String string = MAPPER.writeValueAsString(data);
            return string;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 将json结果集转化为对象
     */
    public static <T> T jsonToPoJo(String jsonData, Class<T> beanType) {
        try {
            T t = MAPPER.readValue(jsonData, beanType);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 将json数据转换成pojo对象list
     */
    public  static <T> T jsonToList(String jsonData, TypeReference<T> typeReference) {
        try {
            return MAPPER.readValue(jsonData, typeReference);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}