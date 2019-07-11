package com.cold.util;

import com.cold.Constants;
import com.cold.vo.UserVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * @Auther: ohj
 * @Date: 2019/7/4 14:58
 * @Description:
 */
public class ContextUtil {

    public static UserVo getLoginUser(){
        Subject currentUser = SecurityUtils.getSubject();
        if(null!=currentUser){
            UserVo userVo = (UserVo) currentUser.getSession().getAttribute(Constants.USER_SESSION_KEY);
            return userVo;
        }
        return null;
    }

    public static Long getUserId(){
        UserVo userVo = getLoginUser();
        return userVo!=null?userVo.getUserId():null;
    }
}