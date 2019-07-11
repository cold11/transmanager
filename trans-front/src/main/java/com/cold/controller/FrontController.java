package com.cold.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Auther: ohj
 * @Date: 2019/6/18 09:58
 * @Description:
 */
@Controller
public class FrontController extends BaseController {
    @RequestMapping("index")
    public String index(){
        return "/index";
    }
}