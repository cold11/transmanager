package com.cold.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

/**
 * @Auther: ohj
 * @Date: 2019/7/17 13:26
 * @Description:
 */
@Getter
@Setter
public class OrderFileVo extends BaseVo {
    private Long fileId;
    private String filename;
    private String filePath;
    private Long orderId;
    private String orderNum;
    private Integer status;
    private Integer processStatus;
    private String sourceLanId;
    private String sourceLanName;
    private String targetLanId;
    private String targetLanName;
    private Integer isDelete;
    private Integer transType;//流程
    private Boolean isAssigned;
    private Integer fileType;

    public OrderFileVo() {
    }

    public OrderFileVo(Long fileId, String filename,Integer fileType, String sourceLanName, String targetLanName) {
        this.fileId = fileId;
        this.filename = filename;
        this.fileType =  fileType;
        this.sourceLanName = sourceLanName;
        this.targetLanName = targetLanName;
    }

    public OrderFileVo(Long fileId, String filename, String filePath, Long orderId, Integer status, Integer processStatus,
                       String sourceLanId, String sourceLanName, String targetLanId, String targetLanName, Integer isDelete, Integer transType) {
        this.fileId = fileId;
        this.filename = filename;
        this.filePath = filePath;
        this.orderId = orderId;
        //this.orderNum = orderNum;
        this.status = status;
        this.processStatus = processStatus;
        this.sourceLanId = sourceLanId;
        this.sourceLanName = sourceLanName;
        this.targetLanId = targetLanId;
        this.targetLanName = targetLanName;
        this.isDelete = isDelete;
        this.transType = transType;
    }
}