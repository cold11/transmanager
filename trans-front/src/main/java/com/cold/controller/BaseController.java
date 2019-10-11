package com.cold.controller;

import com.cold.cache.CacheService;
import com.cold.dto.TaskStatus;
import com.cold.entity.TBLanguage;
import com.cold.page.Pager;
import com.cold.util.Global;
import com.cold.vo.BaseVo;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: ohj
 * @Date: 2019/6/17 10:15
 * @Description:
 */
@Slf4j
@Controller
public class BaseController {

    @Autowired
    protected CacheService cacheService;

    /**
     * 返回字符串
     * @param response
     * @param json
     */
    public void outJsonString(HttpServletResponse response, Object json) {
        response.setContentType("text/javascript;charset=UTF-8");
        response.setHeader("Cache-Control","no-store max-age=0 no-cache must-revalidate");
        response.addHeader("Cache-Control","post-check=0 pre-check=0");
        response.setHeader("Pragma","no-cache");

        try {
            PrintWriter out = response.getWriter();
            out.print(json);
            out.flush();
            out.close();
        } catch (IOException e) {
            log.error("json返回字符串错误", e);
        }
    }
    /**
     * 返回json的数据
     * @param success
     * @param msg
     * @return
     */
    public Map<String, Object> jsonResult(boolean success, Object msg) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("success", success);
        data.put("msg", msg);
        return data;
    }

    /**
     * 获取默认翻译语言
     * @return
     */
    public List<TBLanguage> getDefaultLanguage () {
        return (List<TBLanguage>) cacheService.getPermanentCache("languages");
    }


    /**
     * 文件下载
     * @param response
     * @param file
     * @param fileName
     */
    public void downloadFile(HttpServletResponse response, File file, String fileName) {
        InputStream in = null;
        OutputStream out = null;
        try {
            response.reset(); //清空response
            response.setContentType("multipart/form-data");
            //response.setContentType("application/x-msdownload");
            response.setHeader("Connection", "close");   // 表示不能用浏览器直接打开
            response.setHeader("Accept-Ranges", "bytes");// 告诉客户端允许断点续传多线程连接下载
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1"));
            //response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "UTF-8"));
            response.setHeader("Content-Length", "" + file.length());
            response.setCharacterEncoding("UTF-8");
            in = new FileInputStream(file);
            out = response.getOutputStream();
            IOUtils.copy(in, out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(in,out);
        }
    }

    public void showImage(HttpServletResponse response, String filePath){
        InputStream in = null;
        OutputStream out = null;
        try {
            response.reset(); //清空response
            response.setContentType("image/jpeg");
            in = new FileInputStream(filePath);
            out = response.getOutputStream();
            IOUtils.copy(in, out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(in,out);
        }
    }

    private void close(InputStream in, OutputStream out){
        try {
            if(null != out) {
                out.close();
            }
            if(null != in) {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    @InitBinder
//    public void initBinder(WebDataBinder binder) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        dateFormat.setLenient(false);
//        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
//    }


    protected Pager getPager(BaseVo vo){
        Pager pager = new Pager();
        pager.setPageNo(vo.getPageNo());
        if(vo.getPageSize()!=null)pager.setPageSize(vo.getPageSize());
        pager.setCondition(vo);
        return pager;
    }


    protected String getBaseDir(){
        return Global.getConfig("upload.path");
    }

}