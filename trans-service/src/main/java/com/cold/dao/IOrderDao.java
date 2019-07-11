package com.cold.dao;

import com.cold.entity.TBOrder;
import com.cold.entity.TBOrderFile;
import com.cold.page.Pager;

import java.util.List;

/**
 * @Auther: ohj
 * @Date: 2019/7/2 08:30
 * @Description:
 */
public interface IOrderDao extends IBaseDao {
    void getResult(Pager pager);
    TBOrder findByOrderNum(String orderNum);
    List<TBOrderFile> findTaskFileByOrderId(Long orderId);
}