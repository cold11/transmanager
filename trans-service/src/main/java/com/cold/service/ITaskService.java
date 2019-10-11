package com.cold.service;

import com.cold.dto.FileObj;
import com.cold.entity.TBTask;
import com.cold.entity.TBUserTask;
import com.cold.entity.TBUserTaskFile;
import com.cold.page.Pager;
import com.cold.vo.TaskVo;
import com.cold.vo.UserTaskVo;

import java.util.List;

/**
 * @Auther: ohj
 * @Date: 2019/7/10 10:07
 * @Description:
 */
public interface ITaskService extends IBaseService<TBTask> {

    TBTask getTaskByTaskNo(String taskNo);
    /**
     * 用户领取的任务
     * @param userId
     * @return
     */
    List<TBUserTask> getTaskUserReceives(long userId,Boolean isPmAssign);
    List<UserTaskVo> getTaskUserReceiveList(long userId,Boolean isPmAssign);

    /**
     * 根据任务号任务类型查询任务
     * @param taskNo
     * @param taskType
     * @return
     */
    TBUserTask getTaskUserByTaskNo(String taskNo,Integer taskType);
    TBUserTask getTaskUserByTaskId(Long taskId);
    /**
     * 领取任务
     * @param task
     */
    void reviceTask(TBTask task,Integer taskType);

    /**
     * 取消任务
     * @param userTask
     */
    void cancelTask(TBUserTask userTask);

    /**
     * 重新分配
     * @param taskId
     */
    void updateTbTaskRedistribute(Long taskId);
    void updateTbTaskRedistribute(TBTask tbTask);
    /**
     * pm生成任务
     * @param task
     * @param taskFilePathList
     */
    void saveTask(TBTask task, List<FileObj> taskFilePathList);

    /**
     * 任务大厅
     * @param pager
     */
    void getHallPageTask(Pager pager);

    void saveUserTaskFile(TBUserTaskFile userTaskFile);

    void getUserTaskPageTask(Pager pager);

    void updateTask(TaskVo taskVo);
    void updateUserTask(UserTaskVo userTaskVo);
    TBUserTaskFile findUserTaskFileByUserTaskId(Long userTaskId,Integer taskType);
}
