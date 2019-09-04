package com.cold.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: ohj
 * @Date: 2019/7/19 08:21
 * @Description:用户领取的任务
 */
@Entity
@Table(name = "tbuserTask")
@Getter
@Setter
public class TBUserTask implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userTaskId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private SysUser sysUser;//领取人
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taskId")
    private TBTask tbTask;
    private Date beginTime;
    private Date endTime;
    private String orderNum;
    private String taskNo;
    private Integer taskWords;//任务字数
    private Integer taskType;//任务类型
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date expirationDate;//到期时间
    @Column(columnDefinition = "bit(1) default false",nullable = false)
    private Boolean isPmAssign;//是否项目经理指定任务
//    @Column(length = 200)
//    private String uploadPath;
}