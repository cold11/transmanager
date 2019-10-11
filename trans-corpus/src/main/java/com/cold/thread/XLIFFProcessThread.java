package com.cold.thread;

import com.cold.client.IMachineTranslationService;
import com.cold.common.MatchType;
import com.cold.mt.MTTranslate;
import com.cold.search.SearchHandler;
import com.cold.searchservice.client.ISearchService;
import com.cold.vo.SegTargetUnit;
import com.cold.vo.TmxEntity;
import com.cold.vo.XLIFFResulEntity;
import com.cold.xliff.TransUnit;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.okapi.common.resource.Segment;
import net.sf.okapi.common.resource.TextFragment;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * @Auther: ohj
 * @Date: 2019/8/23 13:29
 * @Description:
 */
@Slf4j
@AllArgsConstructor
public class XLIFFProcessThread implements Callable<XLIFFResulEntity> {
    private TransUnit transUnit;
    private MTTranslate mtTranslate;
    private IMachineTranslationService machineTranslationService;//机器翻译
    private SearchHandler searchHandler;//语料检索

    @Override
    public XLIFFResulEntity call(){
        XLIFFResulEntity xliffResulEntity = new XLIFFResulEntity();
        List<TmxEntity> tmxEntities = Lists.newArrayList();
        xliffResulEntity.setTmxEntities(tmxEntities);
        xliffResulEntity.setTransUnitId(transUnit.getId());
        List<SegTargetUnit> segTargetUnits = Lists.newArrayList();
        xliffResulEntity.setSegTargetUnits(segTargetUnits);
        List<Segment> srcSegments = transUnit.getSrcTextFragments();
        List<Segment> tgtSegments = transUnit.getTgtTextFragments();
        int index = 0;
        for (Segment srcSegment : srcSegments){
            try {
                String src = srcSegment.text.getText();
                String id = srcSegment.getId();
                SegTargetUnit segTargetUnit = new SegTargetUnit();
                segTargetUnit.setId(id);
                TextFragment tgtFragment = tgtSegments.get(index++).text;
                TmxEntity tmxEntity = new TmxEntity(transUnit.getSrcLan(), transUnit.getTgtLan(), src, null, 0);
                searchHandler.search(tmxEntity);
                if (tmxEntity.getPercent() > 0) {
//                    trans = tmxEntity.getTranslation();
//                    segTargetUnit.setMatchType(MatchType.TM);
//                    segTargetUnit.setPercent(Math.round(tmxEntity.getPercent() * 100) + "");
                    tmxEntities.add(tmxEntity);
                }
                String trans = mtTranslate.translate(machineTranslationService, transUnit.getSrcLan(), transUnit.getTgtLan(), src);
                segTargetUnit.setMatchType(MatchType.MT);
                segTargetUnit.setPercent("95");

                log.info(Thread.currentThread().getName() + ">>>>>" + src + ">>>" + trans);
                tgtFragment.setCodedText(trans, true);
                segTargetUnits.add(segTargetUnit);
            }catch (Exception e){
                log.error(transUnit.getId()+":",e);
            }
        }

        return xliffResulEntity;
    }
}