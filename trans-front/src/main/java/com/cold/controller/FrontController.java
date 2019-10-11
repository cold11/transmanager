package com.cold.controller;

import com.cold.Constants;
import com.cold.dto.TaskStatus;
import com.cold.entity.TBCustomer;
import com.cold.page.Pager;
import com.cold.service.ICustomerService;
import com.cold.service.IUserService;
import com.cold.vo.UserVo;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @Auther: ohj
 * @Date: 2019/6/18 09:58
 * @Description:
 */
@Controller
public class FrontController extends BaseController {
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private IUserService userService;
    @RequestMapping("index")
    public String index(){
        return "/index";
    }

    @ResponseBody
    @RequestMapping("saveCustomer")
    public Map<String,Object> saveCustomer(TBCustomer customer){
        customerService.save(customer);
        return jsonResult(true,"");
    }
    @ResponseBody
    @RequestMapping("findCustomer")
    public Map<String,Object> findCustomer(TBCustomer customer){
        List<TBCustomer> customers = customerService.getCustomers(customer);
        if(customer==null){
            return jsonResult(true, customers);
        }else{
            return jsonResult(true, customers.get(0));
        }

//        if(customers.isEmpty()){
//            return jsonResult(false,"");
//        }else {
//            return jsonResult(true, customers.get(0));
//        }
    }
    @ResponseBody
    @RequestMapping("findUsers")
    public Map<String,Object> findUsers(UserVo userVo){
        Pager pager = new Pager();
        if(userVo.getPageNo()==null)userVo.setPageNo(1);
        pager.setPageNo(userVo.getPageNo());
        if(userVo.getPageSize()!=null)pager.setPageSize(userVo.getPageSize());
        else pager.setPageSize(5);
        pager.setCondition(userVo);
       if(userVo.getUserType()==Constants.USER_TRANS||userVo.getUserType()==Constants.USER_PROOF){
           userService.searchUserByRole(pager);
       }
        return jsonResult(true, pager);
    }

    @RequestMapping(value = "getTaskStatus",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String getTaskStatus(){
        TaskStatus[] taskStatuses = TaskStatus.values();
        JsonArray jsonArray = new JsonArray();
        for (TaskStatus taskStatus : taskStatuses){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("value",taskStatus.value());
            jsonObject.addProperty("text",taskStatus.description());
            jsonArray.add(jsonObject);
        }
        String json = jsonArray.toString();
        return json;
    }
}