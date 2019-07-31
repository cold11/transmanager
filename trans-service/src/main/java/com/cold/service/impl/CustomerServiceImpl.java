package com.cold.service.impl;

import com.cold.dao.ICustomerDao;
import com.cold.entity.TBCustomer;
import com.cold.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: ohj
 * @Date: 2019/7/16 09:16
 * @Description:
 */
@Service
public class CustomerServiceImpl extends BaseServiceImpl<TBCustomer> implements ICustomerService {
    @Autowired
    private ICustomerDao customerDao;
    @Override
    public List<TBCustomer> getCustomers(TBCustomer customer) {
        return customerDao.getCustomers(customer);
    }

    @Override
    public TBCustomer findCustomerByName(String name) {
        return customerDao.findCustomerByName(name);
    }
}