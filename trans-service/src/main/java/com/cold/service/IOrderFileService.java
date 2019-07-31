package com.cold.service;

import com.cold.entity.TBOrderFile;
import com.cold.page.Pager;
import com.cold.vo.OrderFileVo;

import java.util.List;

/**
 * @Auther: ohj
 * @Date: 2019/7/17 13:17
 * @Description:
 */
public interface IOrderFileService extends IBaseService<TBOrderFile> {
    List<OrderFileVo> getOrderFiles(Integer status);
    void getOrderFiles(Pager pager);
    void updateOrderStatusNoAssigned(Long orderId);//没有未分配的任务,更新状态
}
