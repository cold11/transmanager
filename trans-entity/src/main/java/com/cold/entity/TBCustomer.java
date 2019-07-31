package com.cold.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Auther: ohj
 * @Date: 2019/7/16 08:23
 * @Description: 客户
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table
@Getter
@Setter
public class TBCustomer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long customerId;
    @Column(length = 100)
    private String name;
    @Column(length = 500)
    private String requirement;
    //private String

    @Override
    public String toString() {
        return "TBCustomer{" +
                "customerId=" + customerId +
                ", name='" + name + '\'' +
                ", requirement='" + requirement + '\'' +
                '}';
    }
}