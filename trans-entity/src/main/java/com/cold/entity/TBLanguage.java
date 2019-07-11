package com.cold.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @Auther: ohj
 * @Date: 2019/6/24 13:23
 * @Description:
 */
@Entity
@Table(name = "tblanguage")
@Getter
@Setter
public class TBLanguage implements Serializable {

    // Fields
    @GenericGenerator(name = "generator", strategy = "increment")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(unique = true, nullable = false)
    private Integer languageId;
    @Column(length = 50)
    private String languageName;
    @Column(length = 50)
    private String languageCode;
    private Integer isDelete;
//    @JsonIgnore
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "language_pid", nullable = false)
//    private TBLanguage tbLanguage;
//    @JsonIgnore
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tbLanguage",cascade={CascadeType.ALL},orphanRemoval = true)
//    private Set<TBLanguage> tbLanguages = new HashSet<>();
//    @JsonIgnore
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tbLanguage",cascade={CascadeType.ALL},orphanRemoval = true)
//    private Set<TBOrder> tcspOrders = new HashSet<>();

    @Override
    public String toString() {
        return "TBLanguage{" +
                "languageId=" + languageId +
                ", languageName='" + languageName + '\'' +
                ", languageCode='" + languageCode + '\'' +
                ", isDelete=" + isDelete +
                '}';
    }
}