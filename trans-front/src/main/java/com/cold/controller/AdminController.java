package com.cold.controller;

import com.cold.entity.SysRole;
import com.cold.page.Pager;
import com.cold.service.IRoleService;
import com.cold.service.IUserService;
import com.cold.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @Auther: ohj
 * @Date: 2019/9/6 16:14
 * @Description:
 */
@RequestMapping("admin")
@Slf4j
@Controller
@RequiresRoles("ROLE_ADMIN")
public class AdminController extends BaseController {

    @Autowired
    private IRoleService roleService;
    @Autowired
    private IUserService userService;
    @RequestMapping("users")
    public String users(Model model){
        List<SysRole> roles = roleService.getRoles();
        model.addAttribute("roles",roles);
        return "admin/users";
    }

    @RequestMapping(value = "userListData",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> userListData(UserVo userVo){
        Pager pager = getPager(userVo);
        userService.getUserPager(pager);
        return jsonResult(true,pager);
    }
    @RequestMapping(value = "updateUserInfo",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> updateUserInfo(UserVo userVo){
        userService.updateUserInfo(userVo);
        return jsonResult(true,"");
    }

    @RequestMapping(value = "addUser",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> addUser(UserVo userVo){
        if(userService.findByLoginName(userVo.getUsername())!=null){
            return jsonResult(false,"用户已存在");
        }
        userService.saveUser(userVo);
        return jsonResult(true,"");
    }

    @RequestMapping(value = "deleteUser",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> deleteUser(UserVo userVo){
        userVo.setIsDelete(1);
        userService.updateUserInfo(userVo);
        return jsonResult(true,"");
    }

    @RequestMapping(value = "resetUserPwd",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> resetUserPwd(UserVo userVo){
        userVo.setPassword("111111");
        userService.updateUserInfo(userVo);
        return jsonResult(true,"");
    }
}