package com.cold.service.impl;

import com.cold.dao.IOrderFileDao;
import com.cold.dto.OrderStatus;
import com.cold.entity.TBOrder;
import com.cold.entity.TBOrderFile;
import com.cold.page.Pager;
import com.cold.service.IOrderFileService;
import com.cold.vo.OrderFileVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: ohj
 * @Date: 2019/7/17 13:20
 * @Description:
 */
@Service
public class OrderFileServiceImpl extends BaseServiceImpl<TBOrderFile> implements IOrderFileService {
    @Autowired
    private IOrderFileDao orderFileDao;
    @Override
    public  List<OrderFileVo> getOrderFiles(Integer status) {
        return orderFileDao.getOrderFiles(status);
    }

    @Override
    public void getOrderFiles(Pager pager) {
        orderFileDao.getOrderFiles(pager);
    }

    @Override
    public void updateOrderStatusNoAssigned(Long orderId) {
        if(orderFileDao.checkFileUnAssigned(orderId)){
            TBOrder tbOrder = orderFileDao.findEntityById(TBOrder.class,orderId);
            tbOrder.setStatus(OrderStatus.TRANSORPROOF.value());
            orderFileDao.update(tbOrder);
        }
    }
}