package com.cold.controller;

import com.cold.entity.SysUser;
import com.cold.service.IUserService;
import com.cold.util.ContextUtil;
import com.cold.util.ShiroMd5Util;
import com.cold.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @Auther: ohj
 * @Date: 2019/9/18 08:23
 * @Description:
 */
@Slf4j
@RequiresUser
@Controller
@RequestMapping("user")
public class UserController extends BaseController {
    @Autowired
    private IUserService userService;

    @RequestMapping("modifyPwd")
    public String modifyPwd(){
        return "users/modify_pwd";
    }

    @RequestMapping(value = "updatePwd",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> updatePwd(UserVo userVo){
        boolean success = true;
        String msg = "";
        String password = userVo.getPassword();
        String newPassword = userVo.getNewPassword();
        if(StringUtils.isBlank(password)||StringUtils.isBlank(newPassword)){
            success = false;
            msg = "请输入密码";
        }else{
            Long userId = ContextUtil.getUserId();
            SysUser sysUser = userService.findEntityById(SysUser.class,userId);
            String oldPassword = sysUser.getPassword();
            String md5Psd = ShiroMd5Util.SysMd5(sysUser.getUsername(),password);
            if(!StringUtils.equals(oldPassword,md5Psd)){
                success = false;
                msg = "原密码输入错误";
            }else{
                sysUser.setPassword(ShiroMd5Util.SysMd5(sysUser.getUsername(),newPassword));
                userService.update(sysUser);
            }
        }

        return jsonResult(success,msg);
    }
}