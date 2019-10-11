package com.cold.dao.impl;

import com.cold.dao.IRoleDao;
import com.cold.entity.SysRole;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Auther: ohj
 * @Date: 2019/6/13 16:15
 * @Description:
 */
@Repository
public class RoleDaoImpl extends BaseDaoImpl implements IRoleDao {
    @Override
    public List<SysRole> getRoles() {
        String hql = "from SysRole";
        return getListByHqlParamMap(hql, Maps.newHashMap());
    }
}