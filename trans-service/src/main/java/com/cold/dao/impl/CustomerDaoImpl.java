package com.cold.dao.impl;

import com.cold.dao.ICustomerDao;
import com.cold.entity.TBCustomer;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Auther: ohj
 * @Date: 2019/7/16 09:10
 * @Description:
 */
@Repository
public class CustomerDaoImpl extends BaseDaoImpl implements ICustomerDao  {
    @Override
    public List<TBCustomer> getCustomers(TBCustomer customer) {
        String hql = "from TBCustomer";
        Map<String,Object> paramMap = Maps.newHashMap();
        if(customer!=null){
            if(StringUtils.isNotBlank(customer.getName())){
                hql += " and name=:name";
                paramMap.put("name",customer.getName());
            }
            if(customer.getCustomerId()!=null){
                hql += " and customerId=:customerId";
                paramMap.put("customerId",customer.getCustomerId());
            }
        }
        if(hql.indexOf("and")>0)hql = hql.replaceFirst("and","where");
        return getListByHqlParamMap(hql,paramMap);
    }

    @Override
    public TBCustomer findCustomerByName(String name) {
        String hql = "from TBCustomer where name=:name";
        Map<String,Object> paramMap = Maps.newHashMap();
        paramMap.put("name",name);
        return getUniqueResult(hql,paramMap);
    }

}