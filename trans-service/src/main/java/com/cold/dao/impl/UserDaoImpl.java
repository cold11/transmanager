package com.cold.dao.impl;


import com.cold.dao.IUserDao;

import com.cold.entity.SysUser;
import com.cold.page.Pager;
import com.cold.vo.UserVo;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * @Auther: ohj
 * @Date: 2019/1/2 15:34
 * @Description:
 */
@Repository
public class UserDaoImpl extends BaseDaoImpl implements IUserDao {

    @Override
    public SysUser findByLoginName(String loginName) {
        String hql = "from SysUser where username=:loginName";
        Map<String,Object> map = Maps.newHashMap();
        map.put("loginName",loginName);
        return getUniqueResult(hql,map);
    }

    @Override
    public Set<String> findRoles(String username) {
        SysUser user = this.findByLoginName(username);
        if (user == null) {
            return Collections.EMPTY_SET;
        } else {
            String hql = "select a.roleKey from SysRole a,SysUserRole b where a.roleId=b.sysRole.roleId and b.sysUser.userId = :userId";
            Map<String,Object> map = Maps.newHashMap();
            map.put("userId",user.getUserId());
            List<String> list = super.getListByHqlParamMap(hql,map);
            return Sets.newHashSet(list);
        }
    }

    @Override
    public List<UserVo> findUserByRole(long roleId) {
        String hql = "select new com.cold.vo.UserVo(a.userId,a.username) from SysUser a where exists(select 'X' from SysUserRole b where a.userId=b.sysUser.userId and b.sysRole.roleId = :roleId)";
        Map<String,Object> map = Maps.newHashMap();
        map.put("roleId",roleId);
        return super.getListByHqlParamMap(hql,map);
    }

    @Override
    public void searchUserByRole(Pager pager) {
        UserVo userVo = (UserVo) pager.getCondition();
        String preHql = "select new com.cold.vo.UserVo(a.userId,a.username) ";
        String hql = "from SysUser a where exists(select 'X' from SysUserRole b where a.userId=b.sysUser.userId and b.sysRole.roleId = :roleId)";
        Map<String,Object> map = Maps.newHashMap();
        map.put("roleId",userVo.getUserType());
        if(StringUtils.isNotBlank(userVo.getUsername())){
            hql+=" and a.username like :username";
            map.put("username","%"+userVo.getUsername()+"%");
        }
        String countHql = "select count(1) "+hql;
        List<UserVo> results = getPageListByParamMap(preHql+hql,map,pager.getPageNo(),pager.getPageSize());
        int count = getCountByHqlParamMap(countHql,map);
        pager.setResult(results);
        pager.setTotalRows(count);
    }

    @Override
    public List<UserVo> findFreeUserByRole(long roleId) {
        String hql = "select new com.cold.vo.UserVo(a.userId,a.username) from SysUser a where exists(select 'X' from SysUserRole b where a.userId=b.sysUser.userId and b.sysRole.roleId = :roleId)" +
                " and NOT EXISTS(SELECT 1 FROM TBUserTask t WHERE t.sysUser.userId=a.userId AND t.endTime is null)";
        Map<String,Object> map = Maps.newHashMap();
        map.put("roleId",roleId);
        return super.getListByHqlParamMap(hql,map);
    }
}