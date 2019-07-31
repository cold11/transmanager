package com.cold.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Auther: ohj
 * @Date: 2019/6/24 13:09
 * @Description:
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "tborder")
@Getter
@Setter
public class TBOrder implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    @Column(nullable = false,length = 50)
    private String orderNum;
    private String title;
    private Date publishDate;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date expirationDate;//到期时间
    @Column(length = 500)
    private String memo;
    private Integer status;
    private Integer processStatus;
    private Integer isDelete;
    private Long createUserId;
//    private Long customerId;
//    private String customer;//客户
//    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId", nullable = false)
    private TBCustomer customer;
    private String caseNo;//案号
    @Column(length = 500)
    private String requirement;//要求
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tbOrder",cascade={CascadeType.ALL},orphanRemoval = true)
    private Set<TBTask> tbTasks = new HashSet<>();
    @Transient
    private List<TBOrderFile> tbOrderFiles = Lists.newArrayList();

    @Override
    public String toString() {
        return "TBOrder{" +
                "orderId=" + orderId +
                ", orderNum='" + orderNum + '\'' +
                ", title='" + title + '\'' +
                ", publishDate=" + publishDate +
                ", expirationDate=" + expirationDate +
                ", memo='" + memo + '\'' +
                ", status=" + status +
                ", processStatus=" + processStatus +
                ", isDelete=" + isDelete +
                ", createUserId=" + createUserId +
                ", customer=" + customer +
                ", caseNo='" + caseNo + '\'' +
                ", requirement='" + requirement + '\'' +
                ", tbTasks=" + tbTasks +
                ", tbOrderFiles=" + tbOrderFiles +
                '}';
    }
}