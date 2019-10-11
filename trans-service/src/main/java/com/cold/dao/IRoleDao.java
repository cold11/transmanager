package com.cold.dao;

import com.cold.entity.SysRole;

import java.util.List;

/**
 * @Auther: ohj
 * @Date: 2019/6/13 16:14
 * @Description:
 */
public interface IRoleDao extends IBaseDao {
    List<SysRole> getRoles();
}
