package com.cold.dao;
import com.cold.entity.SysUser;

import java.util.Set;

/**
 * @Auther: ohj
 * @Date: 2019/6/13 16:09
 * @Description:
 */
public interface IUserDao extends IBaseDao {
    SysUser findByLoginName(String loginName);
    Set<String> findRoles(String username);
}
