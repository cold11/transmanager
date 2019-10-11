package com.cold.service.impl;

import com.cold.dao.ICustomerDao;
import com.cold.dao.IOrderDao;
import com.cold.dao.IOrderFileDao;
import com.cold.dto.OrderFileStatus;
import com.cold.dto.TaskFileType;
import com.cold.dto.TaskType;
import com.cold.entity.TBCustomer;
import com.cold.entity.TBOrder;
import com.cold.entity.TBOrderFile;
import com.cold.entity.TBTask;
import com.cold.page.Pager;
import com.cold.service.IOrderService;
import com.cold.vo.OrderVo;
import com.google.common.collect.Sets;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Auther: ohj
 * @Date: 2019/7/2 08:33
 * @Description:
 */
@Service
public class OrderServiceImpl extends BaseServiceImpl<TBOrder> implements IOrderService {

    @Autowired
    private IOrderDao orderDao;
    @Autowired
    private IOrderFileDao orderFileDao;
    @Autowired
    private ICustomerDao customerDao;
    @Override
    //@Transactional
    public void saveOrder(TBOrder tbOrder) {
        Date now = new Date();
        tbOrder.setIsDelete(0);
        tbOrder.setPublishDate(now);
        TBCustomer customer = tbOrder.getCustomer();
        if(customer.getCustomerId()!=null&&customerDao.findEntityById(TBCustomer.class,customer.getCustomerId())==null){
            customer.setCustomerId(null);
            customerDao.save(customer);
        }
        orderDao.save(tbOrder);
        List<TBOrderFile> orderFiles = tbOrder.getTbOrderFiles();
        for (TBOrderFile orderFile:orderFiles) {
            orderFile.setTbOrder(tbOrder);
            orderFile.setStatus(OrderFileStatus.INIT.value());
            orderFile.setIsAssigned(false);
            orderDao.save(orderFile);
        }

    }

    @Override
    public void getResult(Pager pager) {
        orderDao.getResult(pager);
        List<TBOrder> orderList = pager.getResult();
        pager.setResult(transformResult(orderList));
    }

    @Override
    public TBOrder findByOrderNum(String orderNum) {
        return orderDao.findByOrderNum(orderNum);
    }

    @Override
    public List<TBOrderFile> findTaskFileByOrderId(Long orderId) {
        return orderDao.findTaskFileByOrderId(orderId);
    }

    //类型转换
    private List<OrderVo> transformResult(List<TBOrder> list){
        List<OrderVo> orderVos = list.stream().map(tbOrder -> {
            OrderVo orderVo = new OrderVo();
            try {
                BeanUtils.copyProperties(orderVo,tbOrder);
                int words = 0;
                Set<String> languageSet = Sets.newHashSet();
                List<TBOrderFile> orderFiles = orderFileDao.getOrderFilesByOrderId(tbOrder.getOrderId(), TaskFileType.TASK.value());
                for (TBOrderFile orderFile : orderFiles){
                    Integer fileWords = orderFile.getWords();
                    fileWords = fileWords==null?0:fileWords;
                    words+=fileWords;
                    languageSet.add(orderFile.getSourceLanName()+"-"+orderFile.getTargetLanName());
                }
                orderVo.setOrderWords(words);
                orderVo.setCaseFiles(orderFiles.size());
                orderVo.setLanguage(StringUtils.join(languageSet,"/"));
                TBCustomer customer = tbOrder.getCustomer();
                orderVo.setCustomer(customer.getCode());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return orderVo;
        }).collect(Collectors.toList());
        return orderVos;
    }
}