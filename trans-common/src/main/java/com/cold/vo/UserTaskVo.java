package com.cold.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @Auther: ohj
 * @Date: 2019/7/31 08:47
 * @Description:
 */
@Getter
@Setter
public class UserTaskVo extends BaseVo {
    private Long userTaskId;
    private Long taskId;
    private Long userId;
    private String taskNo;
//    private Date beginTime;
//    private Date endTime;
    private Integer taskType;//任务类型
    private String taskTypeDescribe;
    private Date expirationDate;
    private int transFileCount;//翻译文件个数
    private int referenceFileCount;//参考文件个数
    private String filenames;
    private Boolean isPmAssign;

}