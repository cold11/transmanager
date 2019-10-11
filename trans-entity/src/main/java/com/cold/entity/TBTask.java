package com.cold.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: ohj
 * @Date: 2019/7/10 08:38
 * @Description:任务
 */
@Entity
@Table(name = "tbtask")
@Getter
@Setter
public class TBTask implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private SysUser sysUser;//分配人
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId", nullable = false)
    private TBOrder tbOrder;//订单id
    private String orderNum;
    private String taskTitle;//任务标题
    private String fileIds;//文件id
    private Date createTime;//分配时间
    private Integer transType;//流程
    private Double unitPrice;//单价
    private Integer taskWords;//任务字数
    @Column(length = 300)
    private String languages;//任务语种
    private Integer transFileCount;//案件数
    private Integer referenceFileCount;//参考文件数
    @Column(length = 500)
    private String filenames;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date transTime;//翻译截止日期
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date proofTime;//校对截止日期
    private Long transUserId;//指定翻译
    private Long proofUserId;//指定审校
    private String taskNo;//任务号
    private Integer taskStatus;
    @Column(columnDefinition = "bit(1) default false",nullable = false)
    private Boolean canRevice;//是否能领取  分配任务时文件处理完成后状态重置为true
    @Column(columnDefinition = "bit(1) default false",nullable = false)
    private Boolean isAssignTrans;//是否是指定翻译
    @Column(columnDefinition = "bit(1) default false",nullable = false)
    private Boolean isAssignProof;//是否是指定校对
    @Column(length = 500)
    private String requirement;
    @Column(length = 100)
    private String downloadPath;

}