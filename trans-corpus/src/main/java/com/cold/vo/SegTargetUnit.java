package com.cold.vo;

import com.cold.common.MatchType;
import com.cold.util.Global;
import lombok.Getter;
import lombok.Setter;

/**
 * @Auther: ohj
 * @Date: 2019/8/26 08:54
 * @Description:
 */
@Getter
@Setter
public class SegTargetUnit {
    private String id;
    private String percent;
    private MatchType matchType;
    private String provider = Global.getConfig("trans.provider");
}