package com.cold.dao;

import com.cold.entity.TBTask;
import com.cold.entity.TBUserTask;
import com.cold.page.Pager;

import java.util.List;

/**
 * @Auther: ohj
 * @Date: 2019/7/10 09:00
 * @Description:
 */
public interface ITaskDao extends IBaseDao {
    TBTask getTaskByTaskNo(String taskNo);
    List<TBUserTask> getTaskUserReceives(long userId,Boolean isPmAssign);
    TBUserTask getTaskUserByTaskNo(String taskNo,Integer taskType);
    int getTaskUserReceivesCount(long userId);
    //任务大厅待领取任务列表
    void getHallPageTask(Pager pager);
    //我的任务列表
    void getUserTaskPageTask(Pager pager);
}