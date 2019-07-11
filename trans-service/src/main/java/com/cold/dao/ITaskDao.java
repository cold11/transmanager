package com.cold.dao;

import com.cold.entity.TBTask;

/**
 * @Auther: ohj
 * @Date: 2019/7/10 09:00
 * @Description:
 */
public interface ITaskDao extends IBaseDao {
    TBTask getTaskUserReceive(long userId);
    TBTask getTaskUserByTaskNo(String taskNo,Integer taskType);
}