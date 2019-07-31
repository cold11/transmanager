package com.cold.dao;

import com.cold.entity.TBCustomer;

import java.util.List;

/**
 * @Auther: ohj
 * @Date: 2019/7/16 09:09
 * @Description:
 */
public interface ICustomerDao extends IBaseDao {
    List<TBCustomer> getCustomers(TBCustomer customer);
    TBCustomer findCustomerByName(String name);
    //void findCustomer(TBCustomer customer);
}
