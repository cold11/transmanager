package com.cold.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: ohj
 * @Date: 2019/7/26 13:58
 * @Description: 用户上传的结果文件
 */
@Entity
@Table(name = "tbuserTaskFile")
@Getter
@Setter
public class TBUserTaskFile implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userTaskFileId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userTaskId")
    private TBUserTask userTask;//用户任务
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taskId")
    private TBTask tbTask;
    private Date uploadTime;
    private String filename;
    private String filePath;
    private Integer taskType;//任务类型
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private SysUser sysUser;
}