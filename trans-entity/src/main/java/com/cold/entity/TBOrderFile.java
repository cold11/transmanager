package com.cold.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: ohj
 * @Date: 2019/6/25 08:06
 * @Description:
 */
@Entity
@Table(name = "tborderFile")
@Getter
@Setter
public class TBOrderFile implements Serializable {
    @GenericGenerator(name = "generator", strategy = "increment")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(unique = true, nullable = false)
    private Long fileId;
    private String filename;
    private String filePath;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId", nullable = false)
    private TBOrder tbOrder;
    private Integer fileType;//文件类型(1:任务文件 2:参考文件 3:术语文件 4:语料文件)
    private Integer status;
    private Date uploadTime;
    private Integer isDelete;
    private Long sourceLanguageId;
    @Column(length = 50)
    private String sourceLanName;
    @Column(length = 50)
    private String sourceLanCode;
    private Long targetLanguageId;
    @Column(length = 50)
    private String targetLanName;
    @Column(length = 50)
    private String targetLanCode;
    @Column(length = 36)
    private String uuid;

    @Override
    public String toString() {
        return "TBOrderFile{" +
                "fileId=" + fileId +
                ", filename='" + filename + '\'' +
                ", filePath='" + filePath + '\'' +
                ", tbOrder=" + tbOrder +
                ", fileType=" + fileType +
                ", status=" + status +
                ", uploadTime=" + uploadTime +
                ", isDelete=" + isDelete +
                ", sourceLanguageId=" + sourceLanguageId +
                ", sourceLanName='" + sourceLanName + '\'' +
                ", sourceLanCode='" + sourceLanCode + '\'' +
                ", targetLanguageId=" + targetLanguageId +
                ", targetLanName='" + targetLanName + '\'' +
                ", targetLanCode='" + targetLanCode + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}