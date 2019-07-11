package com.cold.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Auther: ohj
 * @Date: 2019/7/9 10:04
 * @Description:
 */
@Getter
@Setter
public class BaseVo {
    //@DateTimeFormat
    protected Date beginTime;
    //@DateTimeFormat
    protected Date endTime;

    private Integer pageNo;
    private Integer pageSize;
    private String sort;
    private String order;
}