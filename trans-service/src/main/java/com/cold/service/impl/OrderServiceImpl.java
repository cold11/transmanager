package com.cold.service.impl;

import com.cold.dao.IOrderDao;
import com.cold.entity.TBOrder;
import com.cold.entity.TBOrderFile;
import com.cold.page.Pager;
import com.cold.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * @Auther: ohj
 * @Date: 2019/7/2 08:33
 * @Description:
 */
@Service
public class OrderServiceImpl extends BaseServiceImpl<TBOrder> implements IOrderService {

    @Autowired
    private IOrderDao orderDao;
    @Override
    //@Transactional
    public void saveOrder(TBOrder tbOrder) {
        Date now = new Date();
        tbOrder.setIsDelete(0);
        tbOrder.setPublishDate(now);
        orderDao.save(tbOrder);
        List<TBOrderFile> orderFiles = tbOrder.getTbOrderFiles();
        for (TBOrderFile orderFile:orderFiles) {
            orderFile.setTbOrder(tbOrder);
            orderFile.setStatus(0);
            orderDao.save(orderFile);
        }

    }

    @Override
    public void getResult(Pager pager) {
        orderDao.getResult(pager);
    }

    @Override
    public TBOrder findByOrderNum(String orderNum) {
        return orderDao.findByOrderNum(orderNum);
    }

    @Override
    public List<TBOrderFile> findTaskFileByOrderId(Long orderId) {
        return orderDao.findTaskFileByOrderId(orderId);
    }
}