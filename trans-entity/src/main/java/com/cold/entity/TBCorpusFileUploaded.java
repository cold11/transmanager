package com.cold.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * @Auther: ohj
 * @Date: 2019/8/14 09:46
 * @Description:
 */
@Entity
@Table(name = "tbCorpusFileUploaded")
@Getter
@Setter
public class TBCorpusFileUploaded {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;
    private String filePath;
    private String filename;
    private String rsFilename;
    private String resFilePath;
    private Date uploadTime;
    private Date finishedTime;
    private String processMsg;
    private Integer status;
    private Long createUserId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userTaskId")
    private TBUserTask userTask;//用户任务
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "taskId")
//    private TBTask tbTask;//任务
    private String taskNo;
}