package com.cold.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @Auther: ohj
 * @Date: 2019/6/18 14:01
 * @Description:
 */
@Getter
@Setter
public class UserVo extends BaseVo {
    private Long userId;
    private String username;
    private String password;
    private String newPassword;
    private String name;
    private Integer isLocked;;
    private Integer isDisable;
    private Date disableDate;
    private Integer isDelete;
    private Integer taskCount;//进行中的任务数
    private Long userType;
    private List<Long> roles;
    private String roleDescribe;
    public UserVo() {
    }

    public UserVo(Long userId, String username) {
        this.userId = userId;
        this.username = username;
    }
}