package com.cold.controller;

import com.cold.Constants;
import com.cold.common.ResultBaseDto;
import com.cold.entity.SysUser;
import com.cold.service.IUserService;
import com.cold.vo.UserVo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * @Auther: ohj
 * @Date: 2019/6/17 10:31
 * @Description:
 */
@Slf4j
@Controller
public class LoginController extends BaseController {
    @Autowired
    private IUserService userService;

    @RequestMapping("login")
    public ModelAndView login(){
//        for (int i=0;i<20;i++){
//            UserVo sysUser = new UserVo();
//            sysUser.setUsername("yiyuan"+(i+1));
//            sysUser.setPassword("111111");
//            sysUser.setUserType(2l);
//            List<Long> roles = Lists.newArrayList();
//            sysUser.setRoles(roles);
//            roles.add(2l);
//            if(i%3==0)
//                roles.add(3l);
//            //sysUser.setUserId((long)(i+3));
//            userService.saveUser(sysUser);
//        }
        return new ModelAndView("/login");
    }

    @RequestMapping(value = "doLogin",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResultBaseDto doLogin(UserVo userVo, HttpSession session){
        Subject currentUser = SecurityUtils.getSubject();
        // 生成token
        UsernamePasswordToken token = new UsernamePasswordToken(userVo.getUsername(), userVo.getPassword());
        //token.setRememberMe(true);
        String message = null;
        String code = null;
        try {
            // 从自定义Realm获取安全数据进行验证
            currentUser.login(token);
            //currentUser.getSession().setAttribute(Constants.USER_SESSION_KEY, token.getUsername());
//            SysUser user = userService.findByLoginName(userVo.getUsername());
//            ConvertUtils.register(new DateConverter(null), java.util.Date.class);
//            BeanUtils.copyProperties(userVo,user);
//            session.setAttribute(Constants.USER_SESSION_KEY, user);
            code = "200";
            if(currentUser.hasRole("ROLE_YIYUAN")||currentUser.hasRole("ROLE_PROOF")){  //判断跳转地址.如果是译员到任务大厅，如果不是译员去试译页面
                message = "task/hall";
            }else if(currentUser.hasRole("ROLE_SALE")){
                message = "sales/order";
            }
            else if(currentUser.hasRole("ROLE_ADMIN")){
                message = "admin/users";
            }else if(currentUser.hasRole("ROLE_PM")){
                message = "pm/assign";
            }
            else{
                message = "task/hall";
            }
        } catch (UnknownAccountException e) {
            message ="账号密码错误";
           code = "300";
        }catch (IncorrectCredentialsException e){
            message ="账号密码错误";
            code = "300";
        }
        catch (ExcessiveAttemptsException e) {
            // TODO: handle exception
            message = "登录失败多次，账户锁定10分钟";
            code = "300";
        }catch (Exception e) {
            message = "其他错误";
            code = "400";
            log.error("登录异常",e);
        }finally {
            // 登录不成功，清除token
            if (!currentUser.isAuthenticated()) {
                token.clear();
            }
        }
        return new ResultBaseDto(code,message);
    }

    @RequestMapping(value = "/checkLoginName",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String,Object> checkLoginName(String username){
        Map<String,Object> map = Maps.newHashMap();
        SysUser tbUser = userService.findByLoginName(username);
        if(tbUser==null){
            map.put("error","登录名不存在");
        }else{
            map.put("ok","验证通过");
        }
        return map;
    }
    @RequestMapping(value = "checkUsername",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String,Object> checkUsername(String username){
        Map<String,Object> map = Maps.newHashMap();
        SysUser tbUser = userService.findByLoginName(username);
        if(tbUser==null){
            map.put("ok","验证通过");

        }else{
            map.put("error","用户名已存在");
        }
        return map;
    }
    @RequestMapping(value = "/logout")
    public String logout(HttpServletResponse response, HttpSession session) throws IOException {
        SecurityUtils.getSubject().logout();
//        response.getWriter().print("<script>top.location.replace('../');</script>");//跳转到首页
        return "redirect:/login";
    }
}