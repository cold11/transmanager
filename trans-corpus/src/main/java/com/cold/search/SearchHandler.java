package com.cold.search;

import com.cold.Constants;
import com.cold.searchservice.client.*;
import com.cold.util.Base64Utils;
import com.cold.util.SpringContextHolder;
import com.cold.util.StringUtil;
import com.cold.vo.TmxEntity;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Auther: ohj
 * @Date: 2019/8/12 15:47
 * @Description:
 */
public class SearchHandler {
    private ISearchService searchService;

    public SearchHandler() {
        searchService = SpringContextHolder.getBean(ISearchService.class);
    }

    public SearchHandler(ISearchService searchService) {
        this.searchService = searchService;
    }

    public void search(TmxEntity tmxEntity){
        //long start = System.currentTimeMillis();
        SearcherParameter searcherParameter = new SearcherParameter();
        LanguageOptions languageOptions = new LanguageOptions();
        languageOptions.setSource(LanguageOption.fromValue(tmxEntity.getSrcLan()));
        languageOptions.setTarget(LanguageOption.fromValue(tmxEntity.getTgtLan()));
        searcherParameter.setHighlight(false);
        searcherParameter.setLanguageOptions(languageOptions);
        searcherParameter.setSearchCount(10);//返回结果数量
        searcherParameter.setOriginalText(Base64Utils.encode(tmxEntity.getSource()));
        SearchResult searchResult = searchService.search(searcherParameter);
        List<Result> list = searchResult.getResults();//结果集列表
//        list.stream().map(result -> {
//            TmxEntity tmxEntity = new TmxEntity();
//
//            return tmxEntity;
//        }).collect(Collectors.toList());
        findSim(list,tmxEntity);
    }

    private void findSim(List<Result> list,TmxEntity tmxEntity){
        String source = tmxEntity.getSource();
        AtomicReference<Float> f = new AtomicReference<>(0f);
        for (Result result : list) {
            String src = result.getSrc();
            float score = StringUtil.similarityScore(source, src);
            if (score >= Constants.SIMSCORE && score > f.get()) {
                f.set(score);
                tmxEntity.setPercent(score);
                tmxEntity.setSource(src);
                tmxEntity.setTranslation(result.getTgt());
            }

        }
    }
}