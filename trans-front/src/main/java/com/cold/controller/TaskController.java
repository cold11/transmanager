package com.cold.controller;

import com.cold.Constants;
import com.cold.dto.TaskStatus;
import com.cold.dto.TaskType;
import com.cold.entity.SysUser;
import com.cold.entity.TBTask;
import com.cold.entity.TBUserTask;
import com.cold.entity.TBUserTaskFile;
import com.cold.page.Pager;
import com.cold.service.IOrderService;
import com.cold.service.ITaskService;
import com.cold.util.ContextUtil;
import com.cold.util.FileUtil;
import com.cold.vo.OrderVo;
import com.cold.vo.TaskVo;
import com.cold.vo.UserTaskVo;
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
 * @Date: 2019/7/8 15:44
 * @Description:
 */
@RequiresRoles("ROLE_TRANS")
@Slf4j
@Controller
@RequestMapping("task")
public class TaskController extends BaseController {

    @Autowired
    private ITaskService taskService;
    @RequestMapping("hall")
    public String hall(){
        Long userId = ContextUtil.getUserId();
        List<TBUserTask> taskUserReceive = taskService.getTaskUserReceives(userId,false);
        if(!taskUserReceive.isEmpty()){
            return "redirect:/task/unfinished";
        }
        return "task/hall";
    }

    @PostMapping(value = "hallList",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> hallList(TaskVo taskVo){
//        Pager pager = new Pager();
//        pager.setPageNo(taskVo.getPageNo());
//        if(taskVo.getPageSize()!=null)pager.setPageSize(taskVo.getPageSize());
        switch (taskVo.getTaskType()){
            case 1:
                taskVo.setTaskStatus(TaskStatus.HALL.value());//翻译任务
                taskVo.setIsAssignTrans(false);
                break;
            case 2:
                taskVo.setTaskStatus(TaskStatus.TransComplete.value());//校对任务
                taskVo.setIsAssignProof(false);
                break;
             default:
                 break;
        }
        //pager.setCondition(taskVo);
        Pager pager = getPager(taskVo);
        taskService.getHallPageTask(pager);
        return jsonResult(true,pager);
    }

    @RequestMapping("assignedTask")
    public String assignedTask(Model model){

        return "mytask/pmassigned_tasks";
    }
    /**
     * 项目经理指定的任务列表
     * @param taskVo
     * @return
     */
    @PostMapping(value = "assignedList",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> assignedList(UserTaskVo taskVo){
        Long userId = ContextUtil.getUserId();
        taskVo.setUserId(userId);
        taskVo.setIsPmAssign(true);
        Pager pager = getPager(taskVo);
        taskService.getUserTaskPageTask(pager);
        return jsonResult(true,pager);
    }

    @RequestMapping("unfinished")
    public String unfinished(Model model){
        Long userId = ContextUtil.getUserId();
        List<UserTaskVo> taskUserReceive = taskService.getTaskUserReceiveList(userId,false);
        if(taskUserReceive.isEmpty()){
            return "redirect:/task/hall";
        }
        model.addAttribute("tasks",taskUserReceive);
        return "task/unfinished";
    }

    @PostMapping(value = "giveupTask",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String,Object> giveupTask(Long taskId){
        TBUserTask userTask = taskService.findEntityById(TBUserTask.class,taskId);
        if(userTask!=null){
            String url = "task/hall";
            if(userTask.getIsPmAssign()){
                url = "task/assignedTask";
            }
            taskService.cancelTask(userTask);
            return jsonResult(true,url);
        }else{
            return jsonResult(false,"task/hall");
        }
    }

    @RequestMapping("downloadTaskFile/{taskId}")
    public void downloadTaskFile(@PathVariable("taskId") Long taskId, HttpServletResponse response){
        TBTask task = taskService.findEntityById(TBTask.class,taskId);
        String downFile = getBaseDir()+task.getDownloadPath();
        File file = new File(downFile);
        if(file.exists()){
            downloadFile(response,file,file.getName());
        }else{
            outJsonString(response,"没有可下载的文件");
        }
    }
    @RequestMapping(value = "/uploadTaskFile",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> uploadTaskFile(MultipartFile resulttaskFile,String taskId, HttpServletRequest request,
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
        int taskType = userTask.getTaskType();
        String taskTypePathName = Constants.TRANS_FILE_RESULT;
        if(taskType==TaskType.PROOF.value()){
            taskTypePathName = Constants.PROOFFILE_RESULT;
        }
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

        //更新库
        //userTask.setUploadPath(filePath);
        TBUserTaskFile userTaskFile = new TBUserTaskFile();
        userTaskFile.setFilename(originalFileName);
        userTaskFile.setSysUser(sysUser);
        userTaskFile.setTaskType(taskType);
        Date now = new Date();
        userTask.setEndTime(now);//任务完成时间
        userTaskFile.setUploadTime(now);//
        userTaskFile.setUserTask(userTask);
        userTaskFile.setFilePath(filePath);
        taskService.saveUserTaskFile(userTaskFile);
        return jsonResult(true,"");
    }
}