package com.cold.service;

import com.cold.entity.TBTask;

/**
 * @Auther: ohj
 * @Date: 2019/7/10 10:07
 * @Description:
 */
public interface ITaskService extends IBaseService<TBTask> {
    TBTask getTaskUserReceive(long userId);
    TBTask getTaskUserByTaskNo(String taskNo,Integer taskType);
    void reviceTask(TBTask task);
    void cancelTask(TBTask task);
}
