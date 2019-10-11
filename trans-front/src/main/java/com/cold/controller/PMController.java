package com.cold.controller;

import com.cold.Constants;
import com.cold.dto.FileObj;
import com.cold.dto.OrderStatus;
import com.cold.dto.TaskType;
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
import com.cold.vo.*;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

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

    @RequestMapping("createdTasks")
    public String createdTasks(){
        return "pm/created_tasks";
    }

    /**
     * 任务列表
     * @return
     */
    @RequestMapping(value = "taskList",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> taskList(TaskVo taskVo) {
        Pager pager = getPager(taskVo);
        taskService.getHallPageTask(pager);
        return jsonResult(true,pager);
    }


    @RequestMapping("userTasks")
    public String userTasks(){
        return "pm/usertasks";
    }
    /**
     * 已被领取的任务列表
     * @return
     */
    @RequestMapping(value = "receivedList",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> receivedList(UserTaskVo taskVo) {
        Pager pager = getPager(taskVo);
        taskService.getUserTaskPageTask(pager);
        return jsonResult(true,pager);
    }

    @RequestMapping(value = "updateTask",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> updateTask(TaskVo taskVo){
        taskService.updateTask(taskVo);
        return jsonResult(true,"");
    }

    @RequestMapping(value = "updateUserTask",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> updateUserTask(UserTaskVo userTaskVo){
        userTaskVo.setIsPmAssign(true);
        taskService.updateUserTask(userTaskVo);
        return jsonResult(true,"");
    }

    /**
     * 重新分配
     * @param taskId
     * @return
     */
    @RequestMapping(value = "redistribute",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> redistribute(Long taskId){
        TBUserTask userTask = taskService.getTaskUserByTaskId(taskId);
        if(userTask!=null){
            return jsonResult(false,"任务已被领取,不能重新分配");
        }
        taskService.updateTbTaskRedistribute(taskId);
        return jsonResult(true,"");
    }

    @RequestMapping("downloadTaskFile")
    public void downloadTaskFile(UserTaskVo userTaskVo, HttpServletResponse response){
        TBUserTaskFile userTaskFile = taskService.findUserTaskFileByUserTaskId(userTaskVo.getUserTaskId(),userTaskVo.getTaskType());
        if(userTaskFile==null){
            outJsonString(response,"没有可下载的文件");
        }else{
            String downFile = getBaseDir()+userTaskFile.getFilePath();
            File file = new File(downFile);
            if(file.exists()){
                downloadFile(response,file,userTaskFile.getFilename());
            }else{
                outJsonString(response,"没有可下载的文件");
            }
        }

    }
    @RequestMapping(value = "/uploadTaskFile",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> uploadTaskFile(MultipartFile resulttaskFile, String taskId, HttpServletRequest request,
                                             HttpServletResponse response) throws IOException {
        log.info("任务文件上传{}",taskId);
        Map<String, Object> json = new HashMap<String, Object>();
        if(resulttaskFile==null){
            json.put("success",false);
            return json;
        }
        Long userId = ContextUtil.getUserId();
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        String basePath = getBaseDir();
        TBUserTask userTask = taskService.findEntityById(TBUserTask.class,Long.parseLong(taskId));
        String taskTypePathName = Constants.PMFILE_RESULT;
        String taskNo = userTask.getTaskNo();
        String orderNum = userTask.getOrderNum();
        String username = ContextUtil.getLoginUser().getUsername();
        String path = orderNum+File.separator+taskNo+File.separator+username+ File.separator+ taskTypePathName; //文件上传路径信息
        String dir = basePath+File.separator+path;
        FileUtil.mkDirs(dir);
        String uuid = taskService.getUuid();
        byte[] bytes = resulttaskFile.getBytes();
        String originalFileName = resulttaskFile.getOriginalFilename();
        String filename = uuid + "." + FileUtil.getFileType(originalFileName);
        String filePath =  File.separator + path+File.separator+filename;
        File saveFile = new File(basePath,filePath);
        FileOutputStream fos = new FileOutputStream(saveFile);
        fos.write(bytes);
        fos.flush();
        fos.close();
        TBUserTaskFile userTaskFile = new TBUserTaskFile();
        userTaskFile.setFilename(originalFileName);
        userTaskFile.setSysUser(sysUser);
        userTaskFile.setTaskType(TaskType.PM.value());
        userTaskFile.setUploadTime(new Date());//
        userTaskFile.setUserTask(userTask);
        userTaskFile.setFilePath(filePath);
        taskService.saveUserTaskFile(userTaskFile);
        return jsonResult(true,"");
    }
}