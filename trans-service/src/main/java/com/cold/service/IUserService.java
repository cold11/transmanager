package com.cold.service;

import com.cold.entity.SysUser;

import java.util.List;
import java.util.Set;

/**
 * @Auther: ohj
 * @Date: 2019/6/14 09:26
 * @Description:
 */
public interface IUserService {
    SysUser findByLoginName(String loginName);
    Set<String> findRoles(String username);
}