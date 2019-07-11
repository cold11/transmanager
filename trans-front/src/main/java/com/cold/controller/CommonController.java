package com.cold.controller;

import com.cold.common.ResultBaseDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Auther: ohj
 * @Date: 2019/6/14 15:19
 * @Description:
 */

    @RestController
    public class CommonController extends BaseController {

    @RequestMapping("/unauthorized")
    public ResultBaseDto unauthorized() {
        return new ResultBaseDto("401", "权限不足");
    }

    @RequestMapping("/unauthenticated")
    public ResultBaseDto unauthenticated() {
        return new ResultBaseDto("403", "未登录");
    }
    @RequestMapping("selectLanguage")
    public Map<String,Object> selectLanguage(){
        return jsonResult(true,getDefaultLanguage());
    }
}
