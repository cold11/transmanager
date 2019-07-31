package com.cold.service;

import com.cold.entity.TBCustomer;

import java.util.List;

/**
 * @Auther: ohj
 * @Date: 2019/7/16 09:16
 * @Description:
 */
public interface ICustomerService extends IBaseService<TBCustomer> {
    List<TBCustomer> getCustomers(TBCustomer customer);
    TBCustomer findCustomerByName(String name);
    //void findCustomer(TBCustomer customer);
}
