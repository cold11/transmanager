package com.cold.dao.impl;

import com.cold.dao.ITaskDao;
import com.cold.entity.TBTask;
import com.google.common.collect.Maps;
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
    public TBTask getTaskUserReceive(long userId) {
        String hql = " from TBTask where sysUser.userId=:userId and endTime is null";
        Map<String,Object> paramMap = Maps.newHashMap();
        paramMap.put("userId",userId);
        return getUniqueResult(hql,paramMap);
    }

    @Override
    public TBTask getTaskUserByTaskNo(String taskNo, Integer taskType) {
        String hql = " from TBTask where taskNo=:taskNo";
        Map<String,Object> paramMap = Maps.newHashMap();
        if(taskType!=null){
            hql+=" and taskType=:taskType";
            paramMap.put("taskType",taskType);
        }
        paramMap.put("taskNo",taskNo);
        List<TBTask> list = getListByHqlParamMap(hql,paramMap);
        if(!list.isEmpty()){
            return list.get(0);
        }
        return null;
    }
}