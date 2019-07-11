package com.cold.dao.impl;

import com.cold.dao.ILanguageDao;
import com.cold.entity.TBLanguage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: ohj
 * @Date: 2018/8/8 15:15
 * @Description:
 */
@Repository("languageDao")
public class LanguageDaoImpl extends BaseDaoImpl implements ILanguageDao {
    /*@CacheEvict(value="lan_cache", key="#lanId",beforeInvocation=true)*/
    @Cacheable(value="lan_cache", key="#lanId")
    @Override
    public List<TBLanguage> getLanguages(String lanId) {
        Map<String,Object> paramMap = new HashMap<>();
        String hql = "from TBLanguage";
        if(StringUtils.isNotBlank(lanId)){
            hql = " where languageId=:languageId";
            paramMap.put("languageId",lanId);
        }
        return getListByHqlParamMap(hql,paramMap);
    }
}