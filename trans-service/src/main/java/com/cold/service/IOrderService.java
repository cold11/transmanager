package com.cold.service;

import com.cold.entity.TBOrder;
import com.cold.entity.TBOrderFile;
import com.cold.page.Pager;

import java.util.List;

/**
 * @Auther: ohj
 * @Date: 2019/7/2 08:32
 * @Description:
 */
public interface IOrderService extends IBaseService<TBOrder> {
    void saveOrder(TBOrder tbOrder);
    void getResult(Pager pager);
    TBOrder findByOrderNum(String orderNum);
    List<TBOrderFile> findTaskFileByOrderId(Long orderId);
}
