package com.cold.controller;

import com.cold.dto.TaskType;
import com.cold.entity.TBTask;
import com.cold.entity.TBUserTask;
import com.cold.service.ITaskService;
import com.cold.util.ContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @Auther: ohj
 * @Date: 2019/7/30 08:44
 * @Description:审校
 */
@RequiresRoles("ROLE_PROOF")
@Slf4j
@Controller
@RequestMapping("proof")
public class ProofreadingController extends BaseController {
    @Autowired
    private ITaskService taskService;
    @RequestMapping(value = "receive",produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Map<String, Object> saveReceiveTrans(@RequestParam String taskNo){
        Long userId = ContextUtil.getUserId();
        Subject subject = SecurityUtils.getSubject();
//        if(!subject.hasRole("ROLE_PROOF")){
//            return jsonResult(false,"任务领取失败，没有权限！");
//        }
        TBTask tbTask = taskService.getTaskByTaskNo(taskNo);
        if(tbTask==null){
            return jsonResult(false,"任务领取失败，不存在的任务！");
        }
        List<TBUserTask> taskUserReceive = taskService.getTaskUserReceives(userId,false);
        if (!taskUserReceive.isEmpty()) {
            return jsonResult(false,"unfinished");
        }
        TBUserTask receivedTask = taskService.getTaskUserByTaskNo(taskNo, TaskType.PROOF.value());
        if(receivedTask!=null){
            return jsonResult(false,"任务领取失败，该任务已经被领取！");
        }
        taskService.reviceTask(tbTask,TaskType.PROOF.value());
        return jsonResult(true,"");
    }
}