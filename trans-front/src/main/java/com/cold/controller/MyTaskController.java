package com.cold.controller;

import com.cold.dto.TaskType;
import com.cold.page.Pager;
import com.cold.service.ITaskService;
import com.cold.util.ContextUtil;
import com.cold.vo.UserTaskVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @Auther: ohj
 * @Date: 2019/7/31 15:03
 * @Description:
 */
@Slf4j
@Controller
@RequestMapping("mytask")
public class MyTaskController extends BaseController {

    @Autowired
    private ITaskService taskService;
    @RequiresRoles("ROLE_TRANS")
    @RequestMapping("transTask")
    public String transTask(){
        return "mytask/transTask";
    }

    @RequiresRoles("ROLE_TRANS")
    @RequestMapping(value = "transTaskList",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String,Object> transTaskList(UserTaskVo userTaskVo){
        Long userId = ContextUtil.getUserId();
        userTaskVo.setUserId(userId);
        userTaskVo.setTaskType(TaskType.TRANS.value());
        log.info("获取翻译列表数据"+log.isDebugEnabled());
        Pager pager = new Pager();
        pager.setPageNo(userTaskVo.getPageNo());
        if(userTaskVo.getPageSize()!=null)pager.setPageSize(userTaskVo.getPageSize());
        pager.setCondition(userTaskVo);
        taskService.getUserTaskPageTask(pager);
        return jsonResult(true,pager);
    }
    @RequiresRoles("ROLE_PROOF")
    @RequestMapping("proofTask")
    public String proofTask(){
        return "mytask/proofTask";
    }

    @RequiresRoles("ROLE_PROOF")
    @RequestMapping(value = "proofTaskList",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String,Object> proofTaskList(UserTaskVo userTaskVo){
        log.info("获取审校列表数据");
        Long userId = ContextUtil.getUserId();
        userTaskVo.setUserId(userId);
        userTaskVo.setTaskType(TaskType.PROOF.value());
        Pager pager = new Pager();
        pager.setPageNo(userTaskVo.getPageNo());
        if(userTaskVo.getPageSize()!=null)pager.setPageSize(userTaskVo.getPageSize());
        pager.setCondition(userTaskVo);
        taskService.getUserTaskPageTask(pager);
        return jsonResult(true,pager);
    }
}