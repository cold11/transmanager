package com.cold.controller;

import com.cold.dto.TaskType;
import com.cold.entity.SysUser;
import com.cold.entity.TBOrder;
import com.cold.entity.TBOrderFile;
import com.cold.entity.TBTask;
import com.cold.service.IOrderService;
import com.cold.service.ITaskService;
import com.cold.util.ContextUtil;
import com.google.common.collect.Lists;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Auther: ohj
 * @Date: 2019/7/10 10:37
 * @Description:
 */
@Controller
@RequestMapping("trans")
public class TranslationController extends BaseController {
    @Autowired
    private ITaskService taskService;
    @Autowired
    private IOrderService orderService;
    @RequestMapping(value = "receive",produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Map<String, Object> saveReceiveTrans(@RequestParam String orderNum){
        Long userId = ContextUtil.getUserId();
        Subject subject = SecurityUtils.getSubject();
        System.out.println(subject.getPrincipals());
        if(!subject.hasRole("ROLE_TRANS")){
            return jsonResult(false,"任务领取失败，没有权限！");
        }
        TBTask taskUserReceive = taskService.getTaskUserReceive(userId);
        if (taskUserReceive != null) {
            return jsonResult(false,"unfinished");
        }
        TBTask receivedTask = taskService.getTaskUserByTaskNo(orderNum, TaskType.TRANS.value());
        if(receivedTask!=null){
            return jsonResult(false,"任务领取失败，该任务已经被领取！");
        }
        TBOrder tbOrder = orderService.findByOrderNum(orderNum);
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        receivedTask = new TBTask();
        receivedTask.setSysUser(sysUser);
        receivedTask.setTbOrder(tbOrder);
        receivedTask.setBeginTime(new Date());
        receivedTask.setExpirationDate(tbOrder.getExpirationDate());
        receivedTask.setTaskNo(orderNum);
        receivedTask.setTaskType(TaskType.TRANS.value());
        taskService.reviceTask(receivedTask);
        return jsonResult(true,"");
    }

    @RequestMapping("downloadTaskFile/{orderId}")
    public void downloadTaskFile(@PathVariable("orderId") Long orderId, HttpServletResponse response){
        TBOrder tbOrder = orderService.findEntityById(TBOrder.class,orderId);
        String downFile = getBaseDir()+tbOrder.getDownloadPath();
        File file = new File(downFile);
        if(file.exists()){
            downloadFile(response,file,file.getName());
        }else{
            outJsonString(response,"没有可下载的文件");
        }

//        String orderNum = tbOrder.getOrderNum();
//        List<TBOrderFile> list = orderService.findTaskFileByOrderId(orderId);
//        String basePath = getBaseDir();
//        List<String> zipFiles = Lists.newArrayList();
//        list.forEach(tbOrderFile -> {
//            String filename = basePath+tbOrderFile.getFilePath();
//            zipFiles.add(filename);
//        });
//        String zipFilename = basePath+ File.separator+ContextUtil.getLoginUser().getUsername()+File.separator+"trans"+File.separator+orderNum+".zip";



    }
}