package com.cold.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Auther: ohj
 * @Date: 2019/7/2 10:30
 * @Description:
 */
@Getter
@Setter
public class OrderVo extends BaseVo {
    private Integer orderId;
    private String orderNum;
    private String title;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date expirationDate;
    private String memo;
    private Integer status;
    private Integer isDelete;
    private Long createUserId;
    private Long customerId;
    private String customer;//客户
    private String caseNo;//案号
    private String requirement;//要求
    private Integer processStatus;
}