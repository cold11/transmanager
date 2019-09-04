package com.cold.controller;

import com.cold.Constants;
import com.cold.dto.FileObj;
import com.cold.dto.OrderStatus;
import com.cold.entity.*;
import com.cold.page.Pager;
import com.cold.service.IOrderFileService;
import com.cold.service.IOrderService;
import com.cold.service.ITaskService;
import com.cold.service.IUserService;
import com.cold.util.ContextUtil;
import com.cold.util.FileUtil;
import com.cold.util.Global;
import com.cold.util.ZipUtil;
import com.cold.vo.OrderFileVo;
import com.cold.vo.OrderVo;
import com.cold.vo.TaskVo;
import com.cold.vo.UserVo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Auther: ohj
 * @Date: 2019/7/17 10:40
 * @Description:
 */
@Slf4j
//@RequiresRoles("ROLE_PM")
@Controller
@RequestMapping("pm")
public class PMController extends BaseController {

    @Autowired
    private IOrderFileService orderFileService;
    @Autowired
    private IOrderService orderService;
//    @Autowired
//    private IUserService userService;
    @Autowired
    private ITaskService taskService;
    @RequestMapping("assign")
    public String assign(Model model){
//        List<UserVo> transUsers = userService.findUserByRole(Constants.USER_TRANS);
//        List<UserVo> proofUsers = userService.findUserByRole(Constants.USER_PROOF);
//
//        model.addAttribute("transUsers",transUsers);
//        model.addAttribute("proofUsers",proofUsers);
        return "pm/assign";
    }

    @RequestMapping(value = "assignOrderList",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> assignOrderList(OrderVo orderVo){
//        List<OrderVo> orderVos = orderService.getResult();
//        List<OrderFileVo> orderFileVos = orderFileService.getOrderFiles(status);
//        return jsonResult(true,orderFileVos);
        orderVo.setStatus(OrderStatus.INIT.value());
        Pager pager = new Pager();
        pager.setPageNo(orderVo.getPageNo());
        if(orderVo.getPageSize()!=null)pager.setPageSize(orderVo.getPageSize());
        pager.setCondition(orderVo);
        orderService.getResult(pager);
        return jsonResult(true,pager);
    }
    @RequestMapping(value = "orderFileList",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> orderFileList(OrderFileVo orderFileVo) {
        orderFileVo.setIsAssigned(false);
        Pager pager = new Pager();
        pager.setPageNo(orderFileVo.getPageNo());
        if(orderFileVo.getPageSize()!=null)pager.setPageSize(orderFileVo.getPageSize());
        pager.setCondition(orderFileVo);
        orderFileService.getOrderFiles(pager);
        return jsonResult(true,pager);
    }
    @RequestMapping(value = "createTask",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> createTask(TBTask task, Long orderId){
        log.info(""+orderId);
        boolean success = true;
        try {
            if (orderId != null) {
                TBOrder tbOrder = new TBOrder();
                tbOrder.setOrderId(orderId);
                task.setTbOrder(tbOrder);
            }
            Long currentUserId = ContextUtil.getUserId();
            SysUser currentUser = new SysUser();
            currentUser.setUserId(currentUserId);
            task.setSysUser(currentUser);
            List<FileObj> taskFiles = Lists.newArrayList();
            taskService.saveTask(task, taskFiles);
            String basePath = Global.getConfig("upload.path");
            String orderNum = task.getOrderNum();
            String parentPath = File.separator + orderNum + File.separator + Constants.TRANS_FILE;
            String path = parentPath + File.separator + orderNum + ".zip";
            //压缩文件
            new Thread(() -> {
                String dir = basePath + parentPath;
                FileUtil.mkDirs(dir);
                String zipFilename = basePath + path;
                try {
                    ZipUtil.zip(zipFilename, taskFiles);
                    String tasko = orderNum+"-"+String.format("%04d",task.getTaskId());
                    task.setTaskNo(tasko);
                    task.setCanRevice(true);
                    taskService.update(task);
                } catch (Exception e) {
                    log.error("{}文件压缩失败", orderNum);
                    e.printStackTrace();
                }
            }).start();
        }catch (Exception e){
            success = false;
            log.error("{}文件压缩失败",orderId);
        }
        return jsonResult(success,orderId);
    }

    /**
     * 待领取任务列表
     * @return
     */
    @RequestMapping(value = "taskList",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> taskList(TaskVo taskVo) {
        return jsonResult(true,"");
    }

    /**
     * 已被领取的任务列表
     * @return
     */
    @RequestMapping
    @ResponseBody
    public Map<String, Object> receivedList() {
        return jsonResult(true,"");
    }
}