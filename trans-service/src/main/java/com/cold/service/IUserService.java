package com.cold.service;

import com.cold.entity.SysUser;
import com.cold.page.Pager;
import com.cold.vo.UserVo;

import java.util.List;
import java.util.Set;

/**
 * @Auther: ohj
 * @Date: 2019/6/14 09:26
 * @Description:
 */
public interface IUserService extends IBaseService {
    void saveUser(UserVo userVo);
    SysUser findByLoginName(String loginName);
    Set<String> findRoles(String username);
    List<UserVo> findUserByRole(long roleId);
    void searchUserByRole(Pager pager);
    List<UserVo> findFreeUserByRole(long roleId);//空闲用户
}