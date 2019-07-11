package com.cold.dao.impl;


import com.cold.dao.IUserDao;

import com.cold.entity.SysUser;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * @Auther: ohj
 * @Date: 2019/1/2 15:34
 * @Description:
 */
@Repository
public class UserDaoImpl extends BaseDaoImpl implements IUserDao {

    @Override
    public SysUser findByLoginName(String loginName) {
        String hql = "from SysUser where username=:loginName";
        Map<String,Object> map = Maps.newHashMap();
        map.put("loginName",loginName);
        return getUniqueResult(hql,map);
    }

    @Override
    public Set<String> findRoles(String username) {
        SysUser user = this.findByLoginName(username);
        if (user == null) {
            return Collections.EMPTY_SET;
        } else {
            String hql = "select a.roleKey from SysRole a,SysUserRole b where a.roleId=b.sysRole.roleId and b.sysUser.userId = :userId";
            Map<String,Object> map = Maps.newHashMap();
            map.put("userId",user.getUserId());
            List<String> list = super.getListByHqlParamMap(hql,map);
            return Sets.newHashSet(list);
        }
    }
}