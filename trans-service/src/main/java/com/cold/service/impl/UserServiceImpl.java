package com.cold.service.impl;

import com.cold.dao.IUserDao;
import com.cold.entity.SysUser;
import com.cold.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @Auther: ohj
 * @Date: 2019/6/13 16:15
 * @Description:
 */
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private IUserDao userDao;

    @Override
    public SysUser findByLoginName(String loginName) {
        return userDao.findByLoginName(loginName);
    }

    @Override
    public Set<String> findRoles(String username) {
        return userDao.findRoles(username);
    }
}