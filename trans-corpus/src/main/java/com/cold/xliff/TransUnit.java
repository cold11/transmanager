package com.cold.xliff;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.sf.okapi.common.resource.ISegments;
import net.sf.okapi.common.resource.Segment;
import net.sf.okapi.common.resource.TextFragment;

import java.util.List;

/**
 * @Auther: ohj
 * @Date: 2019/8/23 12:46
 * @Description: 翻译集合
 */
@Getter
@Setter
@AllArgsConstructor
public class TransUnit {
    private String id;
    private List<Segment> srcTextFragments;
    private List<Segment> tgtTextFragments;
//    private List<Segment> srcSegments;
//    private List<Segment> tgtSegments;
    private String srcLan;
    private String tgtLan;

//    public TransUnit(String id, List<Segment> srcSegments, List<Segment> tgtSegments, String srcLan, String tgtLan) {
//        this.id = id;
//        this.srcSegments = srcSegments;
//        this.tgtSegments = tgtSegments;
//        this.srcLan = srcLan;
//        this.tgtLan = tgtLan;
//    }
    //private
}