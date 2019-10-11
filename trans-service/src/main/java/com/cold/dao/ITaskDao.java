package com.cold.dao;

import com.cold.entity.TBTask;
import com.cold.entity.TBUserTask;
import com.cold.entity.TBUserTaskFile;
import com.cold.page.Pager;
import com.cold.vo.TaskVo;
import com.cold.vo.UserTaskVo;

import java.util.List;

/**
 * @Auther: ohj
 * @Date: 2019/7/10 09:00
 * @Description:
 */
public interface ITaskDao extends IBaseDao {
    TBTask getTaskByTaskNo(String taskNo);

    /**
     * 已领取的任务
     * @param userId
     * @param isPmAssign
     * @return
     */
    List<TBUserTask> getTaskUserReceives(long userId,Boolean isPmAssign);
    TBUserTask getTaskUserByTaskNo(String taskNo,Integer taskType);
    TBUserTask getTaskUserByTaskId(Long taskId);
    int getTaskUserReceivesCount(long userId);
    //任务大厅待领取任务列表
    void getHallPageTaskVo(Pager pager);
    void getHallPageTask(Pager pager);
    //我的任务列表
    void getUserTaskPageTask(Pager pager);
    void updateTask(TaskVo taskVo);

    //
    void updateUserTask(UserTaskVo userTaskVo);
    TBUserTaskFile findUserTaskFileByUserTaskId(Long userTaskId,Integer taskType);
}