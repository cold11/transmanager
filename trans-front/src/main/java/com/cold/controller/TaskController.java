package com.cold.controller;

import com.cold.dto.TaskType;
import com.cold.entity.TBTask;
import com.cold.page.Pager;
import com.cold.service.IOrderService;
import com.cold.service.ITaskService;
import com.cold.util.ContextUtil;
import com.cold.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @Auther: ohj
 * @Date: 2019/7/8 15:44
 * @Description:
 */
@Controller
@RequestMapping("task")
public class TaskController extends BaseController {

    @Autowired
    private ITaskService taskService;
    @Autowired
    private IOrderService orderService;
    @RequestMapping("hall")
    public String hall(){
        Long userId = ContextUtil.getUserId();
        TBTask taskUserReceive = taskService.getTaskUserReceive(userId);
        if(taskUserReceive!=null){
            return "redirect:/task/unfinished";
        }
        return "task/hall";
    }

    @PostMapping(value = "hallList",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> hallList(OrderVo orderVo){
        orderVo.setProcessStatus(0);
        Pager pager = new Pager();
        pager.setPageNo(orderVo.getPageNo());
        if(orderVo.getPageSize()!=null)pager.setPageSize(orderVo.getPageSize());
        pager.setCondition(orderVo);
        orderService.getResult(pager);
        return jsonResult(true,pager);
    }

    @RequestMapping("unfinished")
    public String unfinished(Model model){
        Long userId = ContextUtil.getUserId();
        TBTask taskUserReceive = taskService.getTaskUserReceive(userId);
        if(taskUserReceive==null){
            return "redirect:/task/hall";
        }
        model.addAttribute("task",taskUserReceive);
        return "task/unfinished";
    }

    @PostMapping(value = "giveupTask",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String,Object> giveupTask(Long taskId){
        TBTask tbTask = taskService.findEntityById(TBTask.class,taskId);
        if(tbTask!=null){
            taskService.cancelTask(tbTask);
            return jsonResult(true,"task/hall");
        }else{
            return jsonResult(false,"task/hall");
        }
    }
}