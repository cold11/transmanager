package com.cold.dao.impl;

import com.cold.dao.IOrderFileDao;
import com.cold.dto.OrderFileStatus;
import com.cold.entity.TBOrderFile;
import com.cold.page.Pager;
import com.cold.vo.OrderFileVo;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Auther: ohj
 * @Date: 2019/7/17 11:01
 * @Description:
 */
@Repository
public class OrderFileDaoImpl extends BaseDaoImpl implements IOrderFileDao {
    @Override
    public List<OrderFileVo> getOrderFiles(Integer status) {
        String hql = "select new com.cold.vo.OrderFileVo(fileId,filename,filePath,tbOrder.orderId,tbOrder.orderNum,status,processStatus,sourceLanguageId,sourceLanName,targetLanguageId,targetLanName,isDelete,transType) " +
                "from TBOrderFile where isDelete<>:isDelete and status=:status";
        Map<String,Object> paramMap = Maps.newHashMap();
        paramMap.put("isDelete", 1);
        paramMap.put("status", status);
        return getListByHqlParamMap(hql,paramMap);
    }

    @Override
    public void getOrderFiles(Pager pager) {
        //OrderFileVo vo = new OrderFileVo(1l,"filename","filePath",1l,"orderNum",null,"sourceLanId","sourceLanName","targetLanId","targetLanId");
        String hql = "select new com.cold.vo.OrderFileVo(fileId,filename,fileType,sourceLanName,targetLanName) from TBOrderFile";
        String countHql = "select count(1) from TBOrderFile";
        String whereHql = "";
        Map<String,Object> paramMap = Maps.newHashMap();
        OrderFileVo orderFileVo = (OrderFileVo) pager.getCondition();
        if(orderFileVo!=null){
            if(orderFileVo.getStatus()!=null){
                whereHql+=" and status=:status";
                paramMap.put("status",orderFileVo.getStatus());
            }
            if(orderFileVo.getIsAssigned()!=null){
                whereHql+=" and isAssigned=:isAssigned";
                paramMap.put("isAssigned",orderFileVo.getIsAssigned());
            }
            if(orderFileVo.getOrderId()!=null){
                whereHql+=" and tbOrder.orderId=:orderId";
                paramMap.put("orderId",orderFileVo.getOrderId());
            }
            if(orderFileVo.getProcessStatus()!=null){
                whereHql+=" and processStatus=:processStatus";
                paramMap.put("processStatus",orderFileVo.getProcessStatus());
            }
        }
        if(whereHql.indexOf("and")>=0)whereHql = whereHql.replaceFirst("and","where");
        hql+=whereHql;
        countHql+=whereHql;
        int count = getCountByHqlParamMap(countHql,paramMap);
        List<OrderFileVo> result = getPageListByParamMap(hql,paramMap,pager.getPageNo(),pager.getPageSize());

        pager.setResult(result);
        pager.setTotalRows(count);
    }

    @Override
    public boolean checkFileUnAssigned(Long orderId) {
        String hql = "select count(1) from TBOrderFile where tbOrder.orderId=:orderId and isAssigned=:isAssigned";
        Map<String,Object> paramMap = Maps.newHashMap();
        paramMap.put("orderId",orderId);
        paramMap.put("isAssigned",false);
        return getCountByHqlParamMap(hql,paramMap)==0;
    }
}