package com.cold.dao;

import com.cold.entity.TBOrderFile;
import com.cold.page.Pager;
import com.cold.vo.OrderFileVo;

import java.util.List;

/**
 * @Auther: ohj
 * @Date: 2019/7/17 10:56
 * @Description:
 */
public interface IOrderFileDao extends IBaseDao {
    List<OrderFileVo> getOrderFiles(Integer status);
    void getOrderFiles(Pager pager);
    boolean checkFileUnAssigned(Long orderId);//未分配的任务
    List<TBOrderFile> getOrderFilesByOrderId(Long orderId,Integer fileType);
}
