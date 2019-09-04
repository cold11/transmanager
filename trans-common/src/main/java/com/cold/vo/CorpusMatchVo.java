package com.cold.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Auther: ohj
 * @Date: 2019/8/14 15:52
 * @Description:
 */
@Getter
@Setter
public class CorpusMatchVo extends BaseVo {
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
    private String taskNo;
    private Long userTaskId;
}