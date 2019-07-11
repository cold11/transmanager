package com.cold.util;



import java.io.File;

/**
 * @Auther: ohj
 * @Date: 2018/8/9 14:15
 * @Description:
 */
public class FileUtil {

    /**
     * 日期目录
     * @return
     */
    public static String getDatePath() {
        String now = DateUtil.getDay();
        return now;
        //return File.separator + now.split("-")[0] + File.separator + now.split("-")[1] + File.separator + now.split("-")[2] + File.separator;
    }

    public static void mkDirs(String path){
        File file = new File(path);
        if(!file.exists())file.mkdirs();
    }



    /**
     * 获取文件名后缀
     * @param fileName
     * @return
     */
    public static String getFileType(String fileName){
        if(null != fileName && !"".equals(fileName)) {
            int index = 0;
            if((index = fileName.lastIndexOf(".")) != -1) {
                return fileName.substring(index + 1).toLowerCase();
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    /**
     * 获取文件名
     * @param fileName
     * @return
     */
    public static String getFileName(String fileName) {
        if(null != fileName && !"".equals(fileName)) {
            int index = 0;
            if((index = fileName.lastIndexOf(".")) != -1) {
                return fileName.substring(0, index);
            }else{
                return null;
            }
        }else{
            return null;
        }
    }


    /**
     * 文件后缀判断
     * @param contentType
     * @param allowTypes
     * @return
     */
    public static boolean isValid(String contentType, String... allowTypes) {
        if (null == contentType || "".equals(contentType)) {
            return false;
        }
        for (String type : allowTypes) {
            if (contentType.endsWith(type)) {
                return true;
            }
        }
        return false;
    }

}