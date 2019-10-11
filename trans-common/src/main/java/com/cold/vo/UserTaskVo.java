package com.cold.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

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
    private String username;
    private String taskNo;
    private Integer taskWords;
//    private Date beginTime;
//    private Date endTime;
    private Integer taskType;//任务类型
    private String taskTypeDescribe;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expirationDate;
    private int transFileCount;//翻译文件个数
    private int referenceFileCount;//参考文件个数
    private String filenames;
    private Boolean isPmAssign;

}