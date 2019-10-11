package com.cold.service;

import com.cold.entity.SysRole;

import java.util.List;

/**
 * @Auther: ohj
 * @Date: 2019/9/9 08:45
 * @Description:
 */
public interface IRoleService extends IBaseService {
    List<SysRole> getRoles();
}