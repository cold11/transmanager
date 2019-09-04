package com.cold.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @Auther: ohj
 * @Date: 2019/8/12 13:30
 * @Description:
 */
@Getter
@Setter
@AllArgsConstructor
public class TmxEntity {
    private String srcLan;
    private String tgtLan;
    private String source;
    private String translation;
    private float percent;

}