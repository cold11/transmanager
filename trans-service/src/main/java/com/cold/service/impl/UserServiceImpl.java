package com.cold.service.impl;

import com.cold.dao.ITaskDao;
import com.cold.dao.IUserDao;
import com.cold.entity.SysRole;
import com.cold.entity.SysUser;
import com.cold.entity.SysUserRole;
import com.cold.page.Pager;
import com.cold.service.IUserService;
import com.cold.util.ShiroMd5Util;
import com.cold.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Auther: ohj
 * @Date: 2019/6/13 16:15
 * @Description:
 */
@Service
public class UserServiceImpl extends BaseServiceImpl implements IUserService {
    @Autowired
    private IUserDao userDao;
    @Autowired
    private ITaskDao taskDao;

    @Override
    public void saveUser(UserVo userVo) {
        SysUser sysUser = new SysUser();
        sysUser.setName(userVo.getName());
        sysUser.setUsername(userVo.getUsername());
        sysUser.setPassword(ShiroMd5Util.SysMd5(sysUser.getUsername(),userVo.getPassword()));
        sysUser.setIsDelete(0);
        sysUser.setIsDisable(0);
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setSysUser(sysUser);
        List<Long> roles = userVo.getRoles();
        if(roles!=null&&!roles.isEmpty()){
            roles.forEach(roleId->{
                SysRole sysRole = userDao.findEntityById(SysRole.class,roleId);
                sysUserRole.setSysRole(sysRole);
                sysUser.getSysUserRoles().add(sysUserRole);
            });

        }

        userDao.save(sysUser);
    }

    @Override
    public SysUser findByLoginName(String loginName) {
        return userDao.findByLoginName(loginName);
    }

    @Override
    public Set<String> findRoles(String username) {
        return userDao.findRoles(username);
    }

    @Override
    public List<UserVo> findUserByRole(long roleId) {
        List<UserVo> userVoList = userDao.findUserByRole(roleId);
        userVoList.forEach(userVo -> {
            int userRecTaskCount = taskDao.getTaskUserReceivesCount(userVo.getUserId());
            userVo.setTaskCount(userRecTaskCount);
        });
        return userVoList;
    }

    @Override
    public void searchUserByRole(Pager pager) {
        userDao.searchUserByRole(pager);
        List<UserVo> list = pager.getResult();
        list.forEach(userVo -> {
            int userRecTaskCount = taskDao.getTaskUserReceivesCount(userVo.getUserId());
            userVo.setTaskCount(userRecTaskCount);
        });
    }

    @Override
    public List<UserVo> findFreeUserByRole(long roleId) {
        return userDao.findFreeUserByRole(roleId);
    }
}