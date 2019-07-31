package com.cold.controller;

import com.cold.Constants;
import com.cold.annotation.Token;
import com.cold.dto.OrderFileStatus;
import com.cold.dto.OrderStatus;
import com.cold.dto.TaskFileType;
import com.cold.dto.TaskType;
import com.cold.entity.TBCustomer;
import com.cold.entity.TBOrder;
import com.cold.entity.TBOrderFile;
import com.cold.service.ICustomerService;
import com.cold.service.IOrderService;
import com.cold.util.*;
import com.cold.vo.UserVo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
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
 * @Date: 2019/7/15 09:26
 * @Description:
 */
@Slf4j
@Controller
@RequestMapping("sales")
public class SalesController extends BaseController {
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private IOrderService orderService;

    @RequestMapping("order")
    public String order(Model model){
        List<TBCustomer> customers = customerService.getCustomers(null);
        model.addAttribute("customers",customers);
        return "sales/order";
    }
    @PostMapping(value = "submitOrder",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String,Object> submitOrder(@RequestBody  String params){
        TBOrder tbOrder = JsonUtils.jsonToPoJo(params,TBOrder.class);
        log.info("订单提交开始{}",params);
        String orderNum = OrderUtil.getOrderNoByAtomic("");
        tbOrder.setOrderNum(orderNum);
        tbOrder.setStatus(OrderStatus.INIT.value());
        tbOrder.setProcessStatus(0);
        tbOrder.setCreateUserId(ContextUtil.getUserId());
//        String parentPath = File.separator+orderNum+File.separator+"trans";
//        String path = parentPath+File.separator+orderNum+".zip";
//        tbOrder.setDownloadPath(path);


        orderService.saveOrder(tbOrder);
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
        Map<String, Object> json = new HashMap<String, Object>();
        if(file==null){
            json.put("success",false);
            return json;
        }
        String basePath = getBaseDir();
        String username = ContextUtil.getLoginUser().getUsername();
        String originalFileName = file.getOriginalFilename();
        String path = username+ File.separator+ Constants.TASKFILE+File.separator+ FileUtil.getDatePath(); //文件上传路径信息
        String uuid = orderService.getUuid();
        byte[] bytes = file.getBytes();//获取字节数组
        String filename = uuid + "." + FileUtil.getFileType(originalFileName);
        String filePath =  File.separator + path+File.separator+filename;
        String dir = basePath+File.separator+path;
        FileUtil.mkDirs(dir);
        File saveFile = new File(basePath,filePath);
        FileOutputStream fos = new FileOutputStream(saveFile);
        fos.write(bytes);
        fos.flush();
        fos.close();
        TBOrderFile orderFile = new TBOrderFile();
        orderFile.setFilename(originalFileName);
        orderFile.setFilePath(File.separator+path+File.separator+filename);
        orderFile.setUuid(uuid);
        //orderFile.setFileType(TaskFileType.TASK.value());
        orderFile.setProcessStatus(OrderFileStatus.INIT.value());
        orderFile.setUploadTime(new Date());
        orderFile.setStatus(OrderFileStatus.INIT.value());
        return jsonResult(true,orderFile);
    }
}