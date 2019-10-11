package com.cold.dao;
import com.cold.entity.SysUser;
import com.cold.page.Pager;
import com.cold.vo.UserVo;

import java.util.List;
import java.util.Set;

/**
 * @Auther: ohj
 * @Date: 2019/6/13 16:09
 * @Description:
 */
public interface IUserDao extends IBaseDao {
    SysUser findByLoginName(String loginName);
    Set<String> findRoles(String username);
    List<UserVo> findUserByRole(long roleId);
    void searchUserByRole(Pager pager);
    List<UserVo> findFreeUserByRole(long roleId);
    void getUserPager(Pager pager);
}
