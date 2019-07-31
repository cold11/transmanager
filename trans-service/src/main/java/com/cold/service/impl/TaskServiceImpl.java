package com.cold.service.impl;

import com.cold.Constants;
import com.cold.dao.ITaskDao;
import com.cold.dto.*;
import com.cold.entity.*;
import com.cold.page.Pager;
import com.cold.service.IOrderFileService;
import com.cold.service.ITaskService;
import com.cold.util.ContextUtil;
import com.cold.util.FileUtil;
import com.cold.util.Global;
import com.cold.util.ZipUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Auther: ohj
 * @Date: 2019/7/10 10:08
 * @Description:
 */
@Slf4j
@Service
public class TaskServiceImpl extends BaseServiceImpl<TBTask> implements ITaskService {
    @Autowired
    private ITaskDao taskDao;
    @Autowired
    private IOrderFileService orderFileService;

    @Override
    public TBTask getTaskByTaskNo(String taskNo) {
        return taskDao.getTaskByTaskNo(taskNo);
    }

    @Override
    public List<TBUserTask> getTaskUserReceives(long userId) {
        return taskDao.getTaskUserReceives(userId);
    }

    @Override
    public TBUserTask getTaskUserByTaskNo(String taskNo, Integer taskType) {
        return taskDao.getTaskUserByTaskNo(taskNo,taskType);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void reviceTask(TBTask task,Integer taskType) {
        Long userId = ContextUtil.getUserId();
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        TBUserTask receivedTask = new TBUserTask();
        receivedTask.setSysUser(sysUser);
        receivedTask.setTaskNo(task.getTaskNo());
        receivedTask.setExpirationDate(task.getTransTime());
        receivedTask.setOrderNum(task.getOrderNum());
        receivedTask.setBeginTime(new Date());
        receivedTask.setTbTask(task);
        receivedTask.setTaskType(taskType);
        if(taskType==TaskType.TRANS.value())
            task.setTaskStatus(TaskStatus.TransReceived.value());
        else if(taskType==TaskType.PROOF.value())
            task.setTaskStatus(TaskStatus.ProofHReceived.value());
        taskDao.save(receivedTask);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void cancelTask(TBUserTask userTask) {
        TBTask tbTask = userTask.getTbTask();
        int taskType = userTask.getTaskType();
        if(taskType==TaskType.TRANS.value()){
            tbTask.setTaskStatus(TaskStatus.HALL.value());
        }else if(taskType==TaskType.PROOF.value()){
            tbTask.setTaskStatus(TaskStatus.TransComplete.value());
        }
        taskDao.delete(userTask);
    }

    @Override
    public void saveTask(TBTask task,List<FileObj> taskFilePathList) {
        TBOrder tbOrder = task.getTbOrder();
        task.setTaskStatus(TaskStatus.HALL.value());
        task.setIsAssignTrans(false);
        task.setIsAssignProof(false);
        Long transUserId = task.getTransUserId();
        Long proofId = task.getProofUserId();
        Date now = new Date();
        task.setCreateTime(now);
        List<TBUserTask> userTasks = Lists.newArrayList();
        String basePath = Global.getConfig("upload.path");
        String orderNum = task.getOrderNum();
        String parentPath = File.separator+orderNum+ File.separator+ Constants.TRANS_FILE;
        String path = parentPath+File.separator+orderNum+".zip";
        task.setDownloadPath(path);
        if(transUserId!=null){
            SysUser sysUser = new SysUser();
            sysUser.setUserId(transUserId);
            TBUserTask tbUserTask = new TBUserTask();
            tbUserTask.setTaskType(TaskType.TRANS.value());
            tbUserTask.setTbTask(task);
            tbUserTask.setBeginTime(now);
            tbUserTask.setSysUser(sysUser);
            tbUserTask.setExpirationDate(task.getTransTime());
            tbUserTask.setOrderNum(task.getOrderNum());
            userTasks.add(tbUserTask);
            task.setIsAssignTrans(true);
        }
        if(task.getTransType()==TransType.TRANSANDPROOF.value()&&proofId!=null){
            SysUser sysUser = new SysUser();
            sysUser.setUserId(proofId);
            TBUserTask tbUserTask = new TBUserTask();
            tbUserTask.setOrderNum(task.getOrderNum());
            tbUserTask.setTaskType(TaskType.PROOF.value());
            tbUserTask.setTbTask(task);
            tbUserTask.setBeginTime(now);
            tbUserTask.setSysUser(sysUser);
            tbUserTask.setExpirationDate(task.getProofTime());
            userTasks.add(tbUserTask);
            task.setIsAssignProof(true);
        }
        task.setCanRevice(false);
        taskDao.save(task);
        userTasks.forEach(tbUserTask -> {
            String taskNo = task.getOrderNum()+"-"+String.format("%04d",task.getTaskId());
            tbUserTask.setTaskNo(taskNo);
            taskDao.save(tbUserTask);
        });

        //更新文件状态到已分配
        List<String> fileidList = Arrays.asList(StringUtils.split(task.getFileIds(),","));
        fileidList.forEach(fileid->{
            TBOrderFile orderFile = orderFileService.findEntityById(TBOrderFile.class,Long.parseLong(fileid));
            orderFile.setIsAssigned(true);
            orderFile.setStatus(OrderFileStatus.TRANS.value());
            orderFileService.update(orderFile);
            String filepath = basePath+orderFile.getFilePath();
            FileObj fileObj = new FileObj(filepath,orderFile.getFilename());
            taskFilePathList.add(fileObj);
        });
        orderFileService.updateOrderStatusNoAssigned(tbOrder.getOrderId());//全部分配,更新订单状态
    }

    @Override
    public void getHallPageTask(Pager pager) {
        taskDao.getHallPageTask(pager);
    }

    @Override
    public void saveUserTaskFile(TBUserTaskFile userTaskFile) {
        TBUserTask userTask = userTaskFile.getUserTask();
        TBTask task = userTask.getTbTask();
        int transType = task.getTransType();
        int taskType = userTask.getTaskType();
        //更新任务状态
        if(transType==TransType.TRANS.value()){
            task.setTaskStatus(TaskStatus.ProofComplete.value());
//            if(taskType==TaskType.TRANS.value()){
//                task.setTaskStatus(TaskStatus.ProofComplete.value());
//            }
        }else if(transType==TransType.TRANSANDPROOF.value()){
            if(taskType==TaskType.TRANS.value()){
                task.setTaskStatus(TaskStatus.TransComplete.value());
            }else{
                task.setTaskStatus(TaskStatus.ProofComplete.value());
            }
        }
        userTaskFile.setTbTask(task);
        taskDao.save(userTaskFile);
    }
}