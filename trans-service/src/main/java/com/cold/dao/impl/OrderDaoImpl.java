package com.cold.dao.impl;

import com.cold.dao.IOrderDao;
import com.cold.entity.TBCustomer;
import com.cold.entity.TBOrder;
import com.cold.entity.TBOrderFile;
import com.cold.page.Pager;
import com.cold.vo.OrderVo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * @Auther: ohj
 * @Date: 2019/7/2 08:30
 * @Description:
 */
@Repository
public class OrderDaoImpl extends BaseDaoImpl implements IOrderDao {
    @Override
    public void getResult(Pager pager) {
        String hql = "from TBOrder";
        StringBuilder builder = new StringBuilder(hql);
        Map<String,Object> paramMap = Maps.newHashMap();
        OrderVo orderVo = (OrderVo) pager.getCondition();
        if(orderVo!=null){
            if(orderVo.getStatus()!=null){
                builder.append(" and status=:status");
                paramMap.put("status",orderVo.getStatus());
            }
            if(orderVo.getProcessStatus()!=null){
                builder.append(" and processStatus=:processStatus");
                paramMap.put("processStatus",orderVo.getProcessStatus());
            }
            if(orderVo.getIsDelete()!=null){
                builder.append(" and isDelete=:isDelete");
                paramMap.put("isDelete",orderVo.getIsDelete());
            }

        }
        hql = builder.toString();
        if(hql.indexOf("and")>0){
            hql = hql.replaceFirst("and","where");
        }
        String countHql = "select count(1) "+hql;
        if (StringUtils.isNotBlank(orderVo.getSort())) {
            hql += " order by " + orderVo.getSort();
            if (StringUtils.isNotBlank(orderVo.getOrder())) {
                hql += " " + orderVo.getOrder();
            }
        } else {
            hql += " order by expirationDate asc";
        }
        List<TBOrder> result = getPageListByParamMap(hql,paramMap,pager.getPageNo(),pager.getPageSize());
//        List<OrderVo> orderVos = Lists.newArrayList();
//        result.forEach(tbOrder -> {
//            OrderVo orderVo1 = new OrderVo();
//            try {
//                BeanUtils.copyProperties(orderVo1,tbOrder);
//                TBCustomer customer = tbOrder.getCustomer();
//                orderVo1.setCustomer(customer.getName());
//                orderVos.add(orderVo1);
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            }
//        });
        int count = getCountByHqlParamMap(countHql,paramMap);
        pager.setResult(result);
        pager.setTotalRows(count);
    }

    @Override
    public TBOrder findByOrderNum(String orderNum) {
        //language=HQL
        String hql = "from TBOrder where orderNum=:orderNum";
        Map<String,Object> paramMap = Maps.newHashMap();
        paramMap.put("orderNum",orderNum);
        return getUniqueResult(hql,paramMap);
    }

    @Override
    public List<TBOrderFile> findTaskFileByOrderId(Long orderId) {
        String hql = "from TBOrderFile where tbOrder.orderId=:orderId";
        Map<String,Object> paramMap = Maps.newHashMap();
        paramMap.put("orderId",orderId);
        return getListByHqlParamMap(hql,paramMap);
    }
}