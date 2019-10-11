package com.cold.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @Auther: ohj
 * @Date: 2019/6/13 09:07
 * @Description:
 */
@Entity
@Table(name = "sys_user")
@Getter
@Setter
public class SysUser implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GenericGenerator(name = "persistenceGenerator", strategy = "increment")
    @Column(unique = true, nullable = false)
    private Long userId;
    private String username;
    private String password;
    private String name;
    private String fieldIds;
    private Integer isLocked;
    private Integer isDisable;
    private Date disableDate;
    private Date createDate;
    private Integer isDelete;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sysUser",cascade=CascadeType.ALL,orphanRemoval = true)
    private Set<SysUserRole> sysUserRoles = new HashSet<>();
}