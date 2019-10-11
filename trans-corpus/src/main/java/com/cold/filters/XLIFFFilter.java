package com.cold.filters;

import com.cold.client.IMachineTranslationService;
import com.cold.client.MachineTranslationService;
import com.cold.common.MatchType;
import com.cold.mt.MTTranslate;
import com.cold.search.SearchHandler;
import com.cold.searchservice.client.ISearchService;
import com.cold.searchservice.client.SearchServiceImplService;
import com.cold.thread.XLIFFProcessThread;
import com.cold.tmx.WriteTmx;
import com.cold.util.FileUtil;
import com.cold.vo.SegTargetUnit;
import com.cold.vo.TmxEntity;
import com.cold.vo.XLIFFResulEntity;
import com.cold.xliff.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lyncode.xliff.XLIFF;
import com.lyncode.xliff.XLiffUtils;
import com.lyncode.xliff.XliffException;
import lombok.extern.slf4j.Slf4j;
import net.sf.okapi.common.Event;
import net.sf.okapi.common.ISkeleton;
import net.sf.okapi.common.LocaleId;
import net.sf.okapi.common.filters.IFilter;
import net.sf.okapi.common.filterwriter.IFilterWriter;
import net.sf.okapi.common.resource.*;
import net.sf.okapi.common.skeleton.ISkeletonWriter;
import org.dom4j.Document;
import org.dom4j.Element;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @Auther: ohj
 * @Date: 2019/8/15 08:41
 * @Description:
 */
@Slf4j
public class XLIFFFilter {

    private IMachineTranslationService machineTranslationService;
    private ISearchService searchService;

    public XLIFFFilter() {
    }

    public XLIFFFilter(IMachineTranslationService machineTranslationService, ISearchService searchService) {
        this.machineTranslationService = machineTranslationService;
        this.searchService = searchService;
    }

//    public void parse(){
//        String filename = "d:\\测试123 匹配前.docx(1).sdlxliff";
//        InputStream is = null;
//        try {
//            is = new FileInputStream(filename);
//            XLIFF x = XLiffUtils.read(is);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }catch (XliffException e){
//
//        }
//
//    }
    public Map<String,Object> parse(String filename,String outfilename){
        Map<String,Object> resMap = Maps.newHashMap();
        try {
//            String url = "http://192.168.3.113:8082/Transformer/services/MachineTranslationService?wsdl";
//            MachineTranslationService machineTranslationService1 = new MachineTranslationService(new URL(url));
//            IMachineTranslationService machineTranslationService = machineTranslationService1.getBasicHttpBindingIMachineTranslationService();
            MTTranslate mtTranslate = new MTTranslate();
//            String searchUrl = "http://114.251.8.179:88/searchServer/services/searchService?wsdl";
//            SearchServiceImplService searchServiceImplService = new SearchServiceImplService(new URL(searchUrl));
//            ISearchService searchService = searchServiceImplService.getSearchServiceImplPort();
            SearchHandler searchHandler = new SearchHandler(searchService);

            XLIFFParse xliffParse = new XLIFFParse();
            xliffParse.load(filename);
            LanguagePair languagePair = AUTODetectLanguage.detectLanguage(xliffParse);
            LocaleId srcLocaleId = LocaleId.fromString(languagePair.getSourceLanguage());
            LocaleId targetLocaleId = LocaleId.fromString(languagePair.getTargetLanguage());


            File file = new File(filename);
            IFilter filter = new net.sf.okapi.filters.xliff.XLIFFFilter();

            RawDocument rawDocument = new RawDocument(file.toURI(),"UTF-8", srcLocaleId,targetLocaleId);
            log.info(srcLocaleId.getLanguage()+">>"+targetLocaleId.getLanguage());
            filter.open(rawDocument);
            IFilterWriter filterWriter = filter.createFilterWriter();
            //ISkeletonWriter skeletonWriter =  filterWriter.getSkeletonWriter();
            filterWriter.setOutput(outfilename);
            List<TransUnit> transUnits = Lists.newArrayList();//xliff翻译集合
            List<Event> eventList = Lists.newArrayList();
            while (filter.hasNext()){
                Event event = filter.next();


                switch (event.getEventType()){
                    case TEXT_UNIT:
//                        TextUnit copyToTu = (TextUnit) event.getResource();
//                        String originalId = copyToTu.getId();
//                        System.out.println(originalId+">>"+copyToTu);
                        ITextUnit textUnit = event.getTextUnit();
                        String transId = textUnit.getId();
                        List<Segment> srcTextFragments = Lists.newArrayList();
                        List<Segment> tgtTextFragments = Lists.newArrayList();
                        TransUnit transUnit = new TransUnit(transId,srcTextFragments,tgtTextFragments,srcLocaleId.getLanguage(),targetLocaleId.getLanguage());
                        transUnits.add(transUnit);

//                        copyToTu.setId(textUnit.getId());
//                        TextContainer cont = textUnit.getTarget(LocaleId.fromString(languagePair.getTargetLanguage()));
//                        cont.append("33333333333333");
                        ISegments segments = textUnit.getSourceSegments();
                        List<Segment> srcSegmentList = segments.asList();
                        ISegments targetSegs =  textUnit.getTargetSegments(LocaleId.fromString(languagePair.getTargetLanguage()));
                        List<Segment> targetSegmentList = targetSegs.asList();
                        int index = 0;
                        for (Segment segment : srcSegmentList){
                            //TextFragment textFragment = segment.text;
                            //String srcText = textFragment.getText();
                            if(index<targetSegmentList.size()){
                                Segment targetSegment = targetSegmentList.get(index++);
                                //TextFragment targetTextFragment = targetSegment.text;
                                srcTextFragments.add(segment);
                                tgtTextFragments.add(targetSegment);
//                                String targetText = mtTranslate.translate(machineTranslationService,srcLocaleId.getLanguage(),targetLocaleId.getLanguage(),srcText);
//                                targetTextFragment.setCodedText(targetText);


                            }



                        }
                        break;
                    default:
                        break;
                }
                eventList.add(event);

            }

            List<Callable<XLIFFResulEntity>> tasks = Lists.newArrayList();

            ExecutorService executor = Executors.newFixedThreadPool(10);
            for (TransUnit transUnit : transUnits){
//                List<TextFragment> srcTextFragments = transUnit.getSrcTextFragments();
//                srcTextFragments.forEach(textFragment -> {
//                    log.info("src:"+textFragment.getText());
//                });
                tasks.add(new XLIFFProcessThread(transUnit,mtTranslate,machineTranslationService,searchHandler));
//                Future future = executor.submit(new XLIFFProcessThread(transUnit,mtTranslate,machineTranslationService,searchHandler));
//                futures.add(future);
            }
            List<Future<XLIFFResulEntity>> futures = executor.invokeAll(tasks);
            List<XLIFFResulEntity> xliffResulEntities = Lists.newArrayList();
            futures.forEach(f->{
                try {
                    xliffResulEntities.add(f.get());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
            executor.shutdown();
            eventList.forEach(event -> {
                filterWriter.handleEvent(event);
            });
            filter.close();
            log.info(">>>>>>>>>"+outfilename);
            List<TmxEntity> tmxEntities = writeSdlDefs(outfilename,xliffResulEntities);
            File outXLIFFFile = new File(outfilename);
            String tmxFile = outXLIFFFile.getParent()+File.separator+FileUtil.getFileName(outXLIFFFile.getName())+".tmx";
            if(!tmxEntities.isEmpty()){
                new WriteTmx().generateTmx(languagePair.getSourceLanguage(),languagePair.getTargetLanguage(),tmxEntities,tmxFile);
            }

            resMap.put("tmxfile",tmxFile);
            resMap.put("success",true);
        }catch (Exception e){
            log.error("解析发生异常",e);
            resMap.put("success",false);
            resMap.put("msg",e.getMessage());
        }
        return resMap;
    }


    private static List<TmxEntity> writeSdlDefs(String outFile,List<XLIFFResulEntity> xliffResulEntities){
        List<TmxEntity> tmxEntities = Lists.newArrayList();
        XLIFFParse xliffParse = new XLIFFParse();
        xliffParse.load(outFile);
        xliffResulEntities.forEach(xliffResulEntity -> {
            tmxEntities.addAll(xliffResulEntity.getTmxEntities());
            String unitId = xliffResulEntity.getTransUnitId();
            Element transUnitelement = xliffParse.getSingleElement(String.format("//%s:trans-unit[@id='%s']",xliffParse.XMLNS_PREFIX,unitId));
            if(transUnitelement!=null){
                Element segDefsElement = xliffParse.createSdlElement("seg-defs",transUnitelement);
                List<SegTargetUnit> segTargetUnits = xliffResulEntity.getSegTargetUnits();
                for (SegTargetUnit segTargetUnit : segTargetUnits){
                    String id = segTargetUnit.getId();
                    Element segElement = xliffParse.createSdlElement("seg",segDefsElement);
                    xliffParse.createSdlElementAttr(segElement,"id",id);
                    xliffParse.createSdlElementAttr(segElement,"percent",segTargetUnit.getPercent());
                    xliffParse.createSdlElementAttr(segElement,"origin-system",segTargetUnit.getProvider());
                    xliffParse.createSdlElementAttr(segElement,"origin", segTargetUnit.getMatchType().toString());
                    xliffParse.createSdlElementAttr(segElement,"conf","Draft");
                }
            }

        });
        xliffParse.save(outFile);
        return tmxEntities;
    }

    public static void main(String[] args) {
        String filename = "d:\\xliff\\tt.docx.xml";
        //filename = "d:\\xliff\\mt.xml";
        //filename = "d:\\xliff\\带公式-原文样例2.docx.xml";
        filename="d:\\xliff\\测试123.docx.xml";
        String outfilename = "d:\\xliff\\13.sdlxliff";
        new XLIFFFilter().parse(filename,outfilename);
//        //InputStream is = null;
//        try {
//            File file = new File(filename);
//            //is = new FileInputStream(filename);
////            XLIFF x = XLiffUtils.read(is);
////            String str = x.getTarget("马先生是一名金融分析师。");
////            log.info(str);
////            Collection<String> sources = x.getSources();
////            sources.forEach(s -> {
////                System.out.println(s);
////            });
//            //LocaleId locEN = LocaleId.CHINA_CHINESE;
//            IFilter filter = new net.sf.okapi.filters.xliff.XLIFFFilter();
//
//            RawDocument rawDocument = new RawDocument(file.toURI(),"UTF-8", LocaleId.AUTODETECT,LocaleId.CHINA_CHINESE);
//            log.info(rawDocument.getSourceLocale()+"");
//            filter.open(rawDocument);
//            IFilterWriter filterWriter = filter.createFilterWriter();
//            //ISkeletonWriter skeletonWriter =  filterWriter.getSkeletonWriter();
//            filterWriter.setOutput(outfilename);
//            List<Event> eventList = new ArrayList<>();
//            while (filter.hasNext()){
//                Event event = filter.next();
//                eventList.add(event);
//                switch (event.getEventType()){
//                    case TEXT_UNIT:
//                       ITextUnit textUnit = event.getTextUnit();
////                        GenericSkeleton skeleton = (GenericSkeleton) textUnit.getSkeleton();
////                        GenericSkeletonPart genericSkeletonPart = skeleton.getLastPart();
//                        //genericSkeletonPart.
//                        ISegments segments = textUnit.getSourceSegments();
//                        //TextContainer textContainer =  textUnit.createTarget(LocaleId.ENGLISH,true,1);
//
//                        ISegments targetSegs =  textUnit.getTargetSegments(LocaleId.ENGLISH);
//                        List<Segment> segmentList = segments.asList();
//                        for (Segment segment : segmentList){
//                            TextFragment textFragment = segment.text;
//
//                            log.info("source:{}",textFragment.getText());
//
//                        }
////                        List<Segment> targetSegmentList = targetSegs.asList();
////                        for (Segment segment : targetSegmentList){
////
////                            TextFragment textFragment = segment.text;
////                            textFragment.setCodedText("TTTTT",true);
////                            //filterWriter.handleEvent(event);
////                            //textFragment.setCodedText("TTTTT",true);
////                            log.info("target:{}",segment);
////                        }
//                        break;
//                     default:
//                         break;
//                }
//                //log.info(event.getEventType()+"");
//                filterWriter.handleEvent(event);
//            }
//
////            eventList.forEach(event -> {
////                filterWriter.handleEvent(event);
////            });
//            filter.close();
//        }catch (Exception e){
//            log.error("解析发生异常",e);
//        }
    }
}