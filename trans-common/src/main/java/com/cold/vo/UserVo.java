package com.cold.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Auther: ohj
 * @Date: 2019/6/18 14:01
 * @Description:
 */
@Getter
@Setter
public class UserVo {
    private Long userId;
    private String username;
    private String password;
    private String name;
   private Integer isLocked;;
    private Integer isDisable;
    private Date disableDate;
    private Integer isDelete;
}