package com.cold.service.impl;

import com.cold.dao.ITaskDao;
import com.cold.dto.TaskType;
import com.cold.entity.TBOrder;
import com.cold.entity.TBTask;
import com.cold.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Auther: ohj
 * @Date: 2019/7/10 10:08
 * @Description:
 */
@Service
public class TaskServiceImpl extends BaseServiceImpl<TBTask> implements ITaskService {
    @Autowired
    private ITaskDao taskDao;
    @Override
    public TBTask getTaskUserReceive(long userId) {
        return taskDao.getTaskUserReceive(userId);
    }

    @Override
    public TBTask getTaskUserByTaskNo(String taskNo, Integer taskType) {
        return taskDao.getTaskUserByTaskNo(taskNo,taskType);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void reviceTask(TBTask task) {
        TBOrder tbOrder = task.getTbOrder();
        tbOrder.setProcessStatus(TaskType.TRANS.value());
        taskDao.update(tbOrder);
        taskDao.save(task);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void cancelTask(TBTask task) {
        int taskType = task.getTaskType();
        TBOrder tbOrder = task.getTbOrder();
        tbOrder.setStatus(taskType);
        tbOrder.setProcessStatus(0);
        taskDao.update(tbOrder);
        delete(task);
    }
}