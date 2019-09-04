package com.cold.util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * @Auther: ohj
 * @Date: 2019/5/9 11:33
 * @Description:
 */
public class Base64Utils {

    public static String encode(String content){
        try {
            content = Base64.getEncoder().encodeToString(content.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static String decode(String content){
        try {
            content = new String(Base64.getDecoder().decode(content.getBytes()), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return content;
    }
}