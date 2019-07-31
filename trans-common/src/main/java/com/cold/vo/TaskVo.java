package com.cold.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Auther: ohj
 * @Date: 2019/7/24 08:57
 * @Description:
 */
@Getter
@Setter
public class TaskVo extends BaseVo{
    private Long taskId;
    private String orderNum;
    private String title;//订单标题
    private String customer;//客户
    private String caseNo;//案号
    private String taskNo;
    private Double unitPrice;//单价
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date transTime;//翻译截止日期
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date proofTime;//校对截止日期
    private Integer taskStatus;
    private Integer taskType;//任务类型
    private Boolean canRevice;
    private Boolean isAssignTrans;
    private Boolean isAssignProof;
    private String requirement;

    public TaskVo(Long taskId,String title,String customer,String caseNo, String taskNo, Double unitPrice, Date transTime, Date proofTime, Integer taskStatus, Boolean canRevice, String requirement) {
        this.taskId = taskId;
        this.title = title;
        this.customer = customer;
        this.caseNo = caseNo;
        this.taskNo = taskNo;
        this.unitPrice = unitPrice;
        this.transTime = transTime;
        this.proofTime = proofTime;
        this.taskStatus = taskStatus;
        this.canRevice = canRevice;
        this.requirement = requirement;
    }
}