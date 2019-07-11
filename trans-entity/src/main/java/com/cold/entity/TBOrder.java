package com.cold.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Entity
@Table(name = "tborder")
@Getter
@Setter
public class TBOrder implements Serializable {
    @GenericGenerator(name = "generator", strategy = "increment")
    @Id
    @GeneratedValue(generator = "generator")
    private Long orderId;
    @Column(nullable = false,length = 50)
    private String orderNum;
    private String title;
    private Date publishDate;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date expirationDate;
    @Column(length = 500)
    private String memo;
    private Integer status;
    private Integer processStatus;
    private Integer isDelete;
    private Long createUserId;
    private String customer;//客户
    private String caseNo;//案号
    private String downloadPath;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tbOrder",cascade={CascadeType.ALL},orphanRemoval = true)
    private Set<TBTask> tbTasks = new HashSet<>();
    @Transient
    private List<TBOrderFile> tbOrderFiles = Lists.newArrayList();
}