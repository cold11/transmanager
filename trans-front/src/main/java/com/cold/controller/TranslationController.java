package com.cold.controller;

import com.cold.dto.TaskType;
import com.cold.entity.*;
import com.cold.service.ITaskService;
import com.cold.util.ContextUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
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
import java.util.stream.Collectors;

/**
 * @Auther: ohj
 * @Date: 2019/7/10 10:37
 * @Description: 翻译
 */
@RequiresRoles("ROLE_TRANS")
@Controller
@RequestMapping("trans")
public class TranslationController extends BaseController {
    @Autowired
    private ITaskService taskService;
    @RequestMapping(value = "receive",produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Map<String, Object> saveReceiveTrans(@RequestParam String taskNo){
        Long userId = ContextUtil.getUserId();
        Subject subject = SecurityUtils.getSubject();
//        if(!subject.hasRole("ROLE_TRANS")){
//            return jsonResult(false,"任务领取失败，没有权限！");
//        }
        TBTask tbTask = taskService.getTaskByTaskNo(taskNo);
        if(tbTask==null){
            return jsonResult(false,"任务领取失败，不存在的任务！");
        }
        List<TBUserTask> taskUserReceive = taskService.getTaskUserReceives(userId,null);
        if (!taskUserReceive.isEmpty()) {
            //公共任务
            List<TBUserTask> userReceiveList = taskUserReceive.stream().filter(t->t.getIsPmAssign()==null||t.getIsPmAssign()==false).collect(Collectors.toList());
            //指定任务
            //List<TBUserTask> pmAssignList = taskUserReceive.stream().filter(t->t.getIsPmAssign()==true).collect(Collectors.toList());
            String msg = "";
            if(!userReceiveList.isEmpty()){
                msg = "unfinished";
            }else{
                msg = "有专属任务";
            }
            return jsonResult(false,msg);
        }
        TBUserTask receivedTask = taskService.getTaskUserByTaskNo(taskNo, TaskType.TRANS.value());
        if(receivedTask!=null){
            return jsonResult(false,"任务领取失败，该任务已经被领取！");
        }
        taskService.reviceTask(tbTask,TaskType.TRANS.value());
        return jsonResult(true,"");
    }


}