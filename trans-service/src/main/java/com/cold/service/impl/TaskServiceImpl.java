package com.cold.service.impl;

import com.cold.Constants;
import com.cold.dao.ITaskDao;
import com.cold.dto.*;
import com.cold.entity.*;
import com.cold.page.Pager;
import com.cold.service.IOrderFileService;
import com.cold.service.ITaskService;
import com.cold.util.BeanUtils;
import com.cold.util.ContextUtil;
import com.cold.util.Global;
import com.cold.vo.UserTaskVo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<TBUserTask> getTaskUserReceives(long userId,Boolean isPmAssign) {
        return taskDao.getTaskUserReceives(userId,isPmAssign);
    }

    @Override
    public List<UserTaskVo> getTaskUserReceiveList(long userId,Boolean isPmAssign) {
        List<TBUserTask> list = taskDao.getTaskUserReceives(userId,isPmAssign);
        List<UserTaskVo> userTaskVos = transformResult(list);
        return userTaskVos;
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
            if(userTask.getIsPmAssign()){//项目经理分配的任务,放弃后需重新分配
                TBOrder tbOrder = tbTask.getTbOrder();
                tbOrder.setStatus(OrderStatus.INIT.value());
                String[] fileids = StringUtils.split(tbTask.getFileIds(),",");
                for (String fileId : fileids) {
                    TBOrderFile orderFile = taskDao.findEntityById(TBOrderFile.class, Long.parseLong(fileId));
                    orderFile.setIsAssigned(false);
                    orderFile.setStatus(OrderFileStatus.INIT.value());
                    orderFile.setProcessStatus(OrderFileStatus.INIT.value());
                }
                taskDao.delete(userTask);
            }else{
                tbTask.setTaskStatus(TaskStatus.HALL.value());
            }
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
            tbUserTask.setIsPmAssign(true);//项目经理指定
            userTasks.add(tbUserTask);
            task.setIsAssignTrans(true);
            task.setTaskStatus(TaskStatus.TransReceived.value());
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
            tbUserTask.setIsPmAssign(true);
            userTasks.add(tbUserTask);
            task.setIsAssignProof(true);
            //task.setTaskStatus(TaskStatus.TransReceived.value());
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

    @Override
    public void getUserTaskPageTask(Pager pager) {
        taskDao.getUserTaskPageTask(pager);
        List<TBUserTask> userTasks = pager.getResult();
        List<UserTaskVo> userTaskVos = transformResult(userTasks);
        pager.setResult(userTaskVos);
    }

    //类型转换
    private List<UserTaskVo> transformResult(List<TBUserTask> list){
        List<UserTaskVo> userTaskVos = list.stream().map(userTask -> {
            UserTaskVo userTaskVo = new UserTaskVo();
            BeanUtils.copyNotNullProperties(userTask,userTaskVo);
            userTaskVo.setUserId(userTask.getSysUser().getUserId());
//            userTaskVo.setUserTaskId(userTask.getUserTaskId());
//            userTaskVo.setTaskType(userTask.getTaskType());
//            userTaskVo.setTaskNo(userTask.getTaskNo());
            userTaskVo.setTaskTypeDescribe(TaskType.fromValue(userTask.getTaskType()).description());
//            userTaskVo.setBeginTime(userTask.getBeginTime());
//            userTaskVo.setExpirationDate(userTask.getExpirationDate());
            TBTask task = userTask.getTbTask();
            userTaskVo.setTaskId(task.getTaskId());
            String[] fileids = StringUtils.split(task.getFileIds(), ",");
            List<String> filenameList = Lists.newArrayList();
            int transFileCount = 0;
            int referenceFileCount = 0;
            for (String fileId : fileids) {
                TBOrderFile orderFile = taskDao.findEntityById(TBOrderFile.class, Long.parseLong(fileId));
                Integer fileType = orderFile.getFileType();
                if (fileType == TaskFileType.TASK.value()) {
                    filenameList.add(orderFile.getFilename());
                    transFileCount++;
                } else referenceFileCount++;
            }
            userTaskVo.setFilenames(StringUtils.join(filenameList, ","));
            userTaskVo.setTransFileCount(transFileCount);
            userTaskVo.setReferenceFileCount(referenceFileCount);
            return userTaskVo;
        }).collect(Collectors.toList());
        return userTaskVos;
    }
}