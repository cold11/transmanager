package com.cold.service.impl;

import com.cold.dao.ILanguageDao;
import com.cold.entity.TBLanguage;
import com.cold.service.ILanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: ohj
 * @Date: 2019/7/1 14:33
 * @Description:
 */
@Service
public class LanguageServiceImpl extends BaseServiceImpl<TBLanguage> implements ILanguageService {
    @Autowired
    private ILanguageDao languageDao;
    @Override
    public List<TBLanguage> getLanguages(String lanId) {
        return languageDao.getLanguages(lanId);
    }
}