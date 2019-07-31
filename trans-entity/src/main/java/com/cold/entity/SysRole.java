package com.cold.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @Auther: ohj
 * @Date: 2019/6/13 09:12
 * @Description:
 */
@Getter
@Setter
@Entity
@Table(name = "sys_role")
public class SysRole implements Serializable {
//    @GenericGenerator(name = "generator", strategy = "identity")
//    @Id
//    @GeneratedValue(generator = "generator")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long roleId;
    private String roleName;
    private String roleKey;
    private Integer roleOrder;
    private String roleDesc;
    private Long createId;
    private Date createTime;
    private Long modifyId;
    private Date modifyTime;
    private Integer isDisable;
    private Integer isDelete;
    @OneToMany(mappedBy="sysRole",cascade=CascadeType.ALL,orphanRemoval = true)
    private Set<SysUserRole> sysUserRoles = new HashSet<>();
}