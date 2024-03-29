package com.cold.controller;

import com.cold.Constants;
import com.cold.dto.TaskType;
import com.cold.entity.SysUser;
import com.cold.entity.TBOrder;
import com.cold.entity.TBOrderFile;
import com.cold.service.IOrderService;
import com.cold.service.IUserService;
import com.cold.util.*;
import com.cold.vo.UserVo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: ohj
 * @Date: 2019/7/2 08:29
 * @Description:
 */
@Slf4j
@Controller
@RequiresRoles("ROLE_PM")
@RequestMapping("order")
public class OrderController extends BaseController {
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IUserService userService;

    @RequestMapping("fill")
    public String order(Model model){
        List<UserVo> transUsers = userService.findUserByRole(2);
        List<UserVo> proofUsers = userService.findUserByRole(2);
        model.addAttribute("transUsers",transUsers);
        model.addAttribute("proofUsers",proofUsers);
        return "order";
    }

    @PostMapping(value = "submitOrder",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String,Object> submitOrder(@RequestBody TBOrder tbOrder){
        log.info("订单提交开始"+ JsonUtils.objectToJson(tbOrder));
//        String orderNum = OrderUtil.getOrderNoByAtomic("");
//        tbOrder.setOrderNum(orderNum);
//        tbOrder.setStatus(TaskType.INIT.value());
//        tbOrder.setProcessStatus(0);
//        tbOrder.setCreateUserId(ContextUtil.getUserId());
//        String parentPath = File.separator+orderNum+File.separator+"trans";
//        String path = parentPath+File.separator+orderNum+".zip";
//        tbOrder.setDownloadPath(path);
//        orderService.saveOrder(tbOrder);
//        String basePath = getBaseDir();
//        new Thread(()->{
//            String dir = basePath+parentPath;
//            FileUtil.mkDirs(dir);
//            String zipFilename = basePath+ path;
//            List<TBOrderFile> list = tbOrder.getTbOrderFiles();
//            List<String> zipFiles = Lists.newArrayList();
//            list.forEach(tbOrderFile -> {
//                String filename = basePath+tbOrderFile.getFilePath();
//                zipFiles.add(filename);
//            });
//            try {
//                ZipUtil.zip(zipFilename,zipFiles);
//                tbOrder.setStatus(TaskType.TRANS.value());
//                orderService.update(tbOrder);
//            } catch (Exception e) {
//                log.error("{}文件压缩失败",orderNum);
//                e.printStackTrace();
//            }
//        }).start();
        return jsonResult(true,"");
    }

    @RequestMapping(value = "/uploadTaskFile",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> uploadTaskFile(MultipartFile file, HttpServletRequest request,
                                           HttpServletResponse response) throws IOException {
//        Map<String, Object> json = new HashMap<String, Object>();
//        if(file==null){
//            json.put("success",false);
//            return json;
//        }
//        String basePath = getBaseDir();
//        String username = ContextUtil.getLoginUser().getUsername();
//        String originalFileName = file.getOriginalFilename();
//        String path = username+ File.separator+ Constants.TRANS_FILE+File.separator+ FileUtil.getDatePath(); //文件上传路径信息
//        String uuid = orderService.getUuid();
//        byte[] bytes = file.getBytes();//获取字节数组
//        String filename = uuid + "." + FileUtil.getFileType(originalFileName);
//        String filePath =  File.separator + path+File.separator+filename;
//        String dir = basePath+File.separator+path;
//        FileUtil.mkDirs(dir);
//        File saveFile = new File(basePath,filePath);
//        FileOutputStream fos = new FileOutputStream(saveFile);
//        fos.write(bytes);
//        fos.flush();
//        fos.close();
//        TBOrderFile orderFile = new TBOrderFile();
//        orderFile.setFilename(originalFileName);
//        orderFile.setFilePath(File.separator+path+File.separator+filename);
//        orderFile.setUuid(uuid);
//        orderFile.setFileType(1);
//        orderFile.setUploadTime(new Date());
//        return jsonResult(true,orderFile);
        return jsonResult(true,"");
    }

    @RequestMapping("users")
    @ResponseBody
    public Map<String,Object> users(Long roleId){
        List<UserVo> users = userService.findUserByRole(roleId);
        return jsonResult(true,users);
    }
}