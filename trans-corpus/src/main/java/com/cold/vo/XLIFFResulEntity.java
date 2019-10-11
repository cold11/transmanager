package com.cold.vo;

import com.cold.common.MatchType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Auther: ohj
 * @Date: 2019/8/16 13:55
 * @Description:
 */
@Getter
@Setter
public class XLIFFResulEntity {
    private String transUnitId;
    private List<SegTargetUnit> segTargetUnits;
    private List<TmxEntity> tmxEntities;//匹配结果
    //private String trans;//预处理译文
}