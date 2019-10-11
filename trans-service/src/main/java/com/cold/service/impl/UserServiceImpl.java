package com.cold.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.cold.dao.ITaskDao;
import com.cold.dao.IUserDao;
import com.cold.entity.SysRole;
import com.cold.entity.SysUser;
import com.cold.entity.SysUserRole;
import com.cold.page.Pager;
import com.cold.service.IUserService;
import com.cold.util.BeanUtils;
import com.cold.util.ShiroMd5Util;
import com.cold.vo.UserVo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Auther: ohj
 * @Date: 2019/6/13 16:15
 * @Description:
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<SysUser> implements IUserService {
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
        sysUser.setIsDisable(0);
        sysUser.setIsDelete(0);
        sysUser.setIsLocked(0);

        List<Long> roles = userVo.getRoles();
        if(roles!=null&&!roles.isEmpty()){
            roles.forEach(roleId->{
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setSysUser(sysUser);
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

    @Override
    public void getUserPager(Pager pager) {
        userDao.getUserPager(pager);
        List<SysUser> sysUsers = pager.getResult();
        List<UserVo> userVos = transformResult(sysUsers);
        pager.setResult(userVos);
    }

    @Override
    public void updateUserInfo(UserVo userVo) {
        SysUser sysUser = this.findEntityById(SysUser.class,userVo.getUserId());
        if(userVo.getIsDelete()!=null){
            sysUser.setIsDelete(userVo.getIsDelete());
        }
        if(userVo.getIsDisable()!=null){
            sysUser.setIsDisable(userVo.getIsDisable());
            if(userVo.getIsDisable()==1){
                sysUser.setDisableDate(new Date());
            }else sysUser.setDisableDate(null);
        }
        if(StringUtils.isNotBlank(userVo.getPassword())){
            sysUser.setPassword(ShiroMd5Util.SysMd5(sysUser.getUsername(),userVo.getPassword()));
        }
        if(userVo.getRoles()!=null&&!userVo.getRoles().isEmpty()){
            userVo.getRoles().forEach(roleId->{
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setSysUser(sysUser);
                SysRole sysRole = userDao.findEntityById(SysRole.class,roleId);
                sysUserRole.setSysRole(sysRole);
                sysUser.getSysUserRoles().add(sysUserRole);
            });
        }
    }

    //类型转换
    private List<UserVo> transformResult(List<SysUser> list){
        List<UserVo> userVoList = list.stream().map(user -> {
            UserVo userVo = new UserVo();
            //BeanUtils.copyNotNullProperties(user,userVo);
//            userVo.setUserId(user.getUserId());
//            userVo.setUsername(user.getUsername());
            BeanUtil.copyProperties(user,userVo,"password");
            Set<SysUserRole> sysUserRoles = user.getSysUserRoles();
            List<String> roleDescribes = Lists.newArrayList();
            sysUserRoles.forEach(sysUserRole -> {
                SysRole sysRole = sysUserRole.getSysRole();
                roleDescribes.add(sysRole.getRoleName());
            });
            userVo.setRoleDescribe(StringUtils.join(roleDescribes,","));
            return userVo;
        }).collect(Collectors.toList());
        return userVoList;
    }
}