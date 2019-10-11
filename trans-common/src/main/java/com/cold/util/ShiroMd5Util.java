package com.cold.util;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * @Auther: ohj
 * @Date: 2019/7/4 16:18
 * @Description:
 */
public class ShiroMd5Util {
    //添加user的密码加密方法
    public static String  SysMd5(String username,String password) {
        String hashAlgorithmName = "MD5";//加密方式

        //Object crdentials =userVo.getPassword();//密码原值

        ByteSource salt = ByteSource.Util.bytes(username);//以账号作为盐值

        int hashIterations = 1024;//加密1024次

        SimpleHash hash = new SimpleHash(hashAlgorithmName,password,salt,hashIterations);

        return hash.toString();
    }

    public static void main(String[] args) {
        System.out.println(SysMd5("sale","111111"));
        System.out.println(SysMd5("pm","111111"));
        System.out.println(SysMd5("admin","111111"));
    }
}