package com.cold.dao.impl;

import com.cold.dao.ITaskDao;
import com.cold.dto.TaskStatus;
import com.cold.entity.TBTask;
import com.cold.entity.TBUserTask;
import com.cold.page.Pager;
import com.cold.vo.TaskVo;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Auther: ohj
 * @Date: 2019/7/10 09:00
 * @Description:
 */
@Repository
public class TaskDaoImol extends BaseDaoImpl implements ITaskDao {

    @Override
    public TBTask getTaskByTaskNo(String taskNo) {
        String hql = " from TBTask where taskNo=:taskNo";
        Map<String,Object> paramMap = Maps.newHashMap();
        paramMap.put("taskNo",taskNo);
        return getUniqueResult(hql,paramMap);
    }

    @Override
    public List<TBUserTask> getTaskUserReceives(long userId) {
        String hql = " from TBUserTask where sysUser.userId=:userId and endTime is null";
        Map<String,Object> paramMap = Maps.newHashMap();
        paramMap.put("userId",userId);
        return getListByHqlParamMap(hql,paramMap);
    }

    @Override
    public TBUserTask getTaskUserByTaskNo(String taskNo, Integer taskType) {
        String hql = " from TBUserTask where taskNo=:taskNo";
        Map<String,Object> paramMap = Maps.newHashMap();
        if(taskType!=null){
            hql+=" and taskType=:taskType";
            paramMap.put("taskType",taskType);
        }
        paramMap.put("taskNo",taskNo);
        List<TBUserTask> list = getListByHqlParamMap(hql,paramMap);
        if(!list.isEmpty()){
            return list.get(0);
        }
        return null;
    }

    @Override
    public int getTaskUserReceivesCount(long userId) {
        String hql = "select count(1) from TBUserTask where sysUser.userId=:userId and endTime is null";
        Map<String,Object> paramMap = Maps.newHashMap();
        paramMap.put("userId",userId);
        return getCountByHqlParamMap(hql,paramMap);
    }

    @Override
    public void getHallPageTask(Pager pager) {
        TaskVo taskVo = (TaskVo) pager.getCondition();
        String preHql = "select new com.cold.vo.TaskVo(taskId,tbOrder.title,tbOrder.customer.name,tbOrder.caseNo,taskNo,unitPrice,transTime,proofTime,taskStatus,canRevice,requirement)";
        String hql = " from TBTask where canRevice=:canRevice";
        Map<String,Object> paramMap = Maps.newHashMap();
        paramMap.put("canRevice",true);
        if(taskVo!=null){
            if(taskVo.getTaskStatus()!=null){
                hql+=" and taskStatus=:taskStatus";
                paramMap.put("taskStatus", taskVo.getTaskStatus());
            }
            if(taskVo.getIsAssignTrans()!=null){
                hql+=" and isAssignTrans=:isAssignTrans";
                paramMap.put("isAssignTrans", taskVo.getIsAssignTrans());
            }
            if(taskVo.getIsAssignProof()!=null){
                hql+=" and isAssignProof=:isAssignProof";
                paramMap.put("isAssignProof", taskVo.getIsAssignProof());
            }
        }
        String countHql = "select count(1)"+hql;
        if (StringUtils.isNotBlank(taskVo.getSort())) {
            hql += " order by " + taskVo.getSort();
            if (StringUtils.isNotBlank(taskVo.getOrder())) {
                hql += " " + taskVo.getOrder();
            }
        } else {
            hql += " order by createTime asc";
        }

        List<TaskVo> list = getPageListByParamMap(preHql+hql,paramMap,pager.getPageNo(),pager.getPageSize());
        int count = getCountByHqlParamMap(countHql,paramMap);
        pager.setResult(list);
        pager.setTotalRows(count);
    }
}