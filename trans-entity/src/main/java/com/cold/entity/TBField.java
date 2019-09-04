package com.cold.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: ohj
 * @Date: 2019/8/2 10:49
 * @Description:
 */
@Getter
@Setter
@Entity
@Table(name = "tb_field")
public class TBField implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer fieldId;
    @Column(length = 50)
    private String fieldName;
    private Date createDate;
    private Date modifiedDate;
}