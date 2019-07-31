package com.cold.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @Auther: ohj
 * @Date: 2019/6/13 09:11
 * @Description:
 */
@Getter
@Setter
@Entity
@Table(name = "sys_user_role")
public class SysUserRole implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long userRoleId;
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "userId", unique = true)
    private SysUser sysUser;
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "roleId", unique = true)
    private SysRole sysRole;
}