package com.cold.service.impl;

import com.cold.dao.IRoleDao;
import com.cold.entity.SysRole;
import com.cold.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: ohj
 * @Date: 2019/9/9 08:46
 * @Description:
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl implements IRoleService {
    @Autowired
    private IRoleDao roleDao;
    @Override
    public List<SysRole> getRoles() {
        return roleDao.getRoles();
    }
}