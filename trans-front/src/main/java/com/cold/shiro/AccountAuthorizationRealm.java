package com.cold.shiro;

import com.cold.Constants;
import com.cold.entity.SysUser;
import com.cold.service.IUserService;
import com.cold.util.serializable.ByteSourceUtils;
import com.cold.vo.UserVo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;

/**
 * @Auther: ohj
 * @Date: 2019/6/13 14:41
 * @Description:
 */
public class AccountAuthorizationRealm extends AuthorizingRealm {
    @Autowired
    private IUserService userService;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String)principals.getPrimaryPrincipal();
        //从缓存中获取权限认证信息
        Cache<Object, AuthorizationInfo> AuthorizationInfoCache = getAuthorizationCache();
        if (null != AuthorizationInfoCache) {
            AuthorizationInfo AuthorizationInfoResult = AuthorizationInfoCache.get(username);
            if (null != AuthorizationInfoResult) {
                return AuthorizationInfoResult;
            }
        }

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(userService.findRoles(username));
        //authorizationInfo.setStringPermissions(userBiz.findPermissions(username));
        if (null != AuthorizationInfoCache)AuthorizationInfoCache.put(username, authorizationInfo);
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        Subject currentUser = SecurityUtils.getSubject();
        // 判断是否已经登录
        if(!currentUser.isAuthenticated()) {
            UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
            String accountName = token.getUsername();
            if(StringUtils.isNoneEmpty(accountName)){
                //从缓存中获取登录验证信息
                Cache<Object, AuthenticationInfo> AuthenTicationInfoCache = this.getAuthenticationCache();
                if (null != AuthenTicationInfoCache) {
                    AuthenticationInfo AuthenticationInfoResult = AuthenTicationInfoCache.get(accountName);
                    if (null != AuthenticationInfoResult) {
                        return AuthenTicationInfoCache.get(accountName);
                    }
                }
                SysUser sysUser = userService.findByLoginName(token.getUsername());
                if (sysUser==null) {
                    throw new UnknownAccountException();//用户不存在
                }
                else if(sysUser.getIsDisable()!=null&&sysUser.getIsDisable()==1){
                    throw new DisabledAccountException();
                }
                else if(sysUser.getIsLocked()!=null&&sysUser.getIsLocked()==1){
                    throw new LockedAccountException();
                }else{
                    String realmName = getName();
                    ByteSource credentialsSalt = ByteSourceUtils.bytes(sysUser.getUsername());
                    SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                            sysUser.getUsername(),
                            sysUser.getPassword(),
                            credentialsSalt,
                            realmName  //realm name
                    );
                    UserVo userVo = new UserVo();
                    try {
                        ConvertUtils.register(new DateConverter(null), java.util.Date.class);
                        BeanUtils.copyProperties(userVo,sysUser);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    //将登录验证信息放入缓存
                    if (null != AuthenTicationInfoCache) {
                        AuthenTicationInfoCache.put(accountName, authenticationInfo);
                    }
                    currentUser.getSession().setAttribute(Constants.USER_SESSION_KEY, userVo);
                    return authenticationInfo;
                }

            }

        }

        return null;
    }

//    @Override
//    protected void clearCache(PrincipalCollection principals) {
//        super.clearCache(principals);
//    }

    /**
     *
     * @Description: 权限修改生效后，立即刷新清空缓存，则可以实现用户不退出生效新的权限
     */ public void clearCache() {
         PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
         super.clearCache(principals);
     }
}